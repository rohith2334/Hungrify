package com.app.hungrify.main.service;

// package com.app.hungrify.main.service;

import com.app.hungrify.common.models.Users;
import com.app.hungrify.common.repository.UserRepository;
import com.app.hungrify.main.models.FoodItem;
import com.app.hungrify.main.repository.FoodItemRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.app.hungrify.main.dto.AddToCartRequest;
import com.app.hungrify.main.dto.CartDto;
import com.app.hungrify.main.dto.CartItemDto;
import com.app.hungrify.main.models.FoodItem;
import com.app.hungrify.main.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cart is persisted inside users.profile_json.cart. This service updates that JSON safely.
 */
@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository usersRepository;
    private final FoodItemRepository foodItemsRepository;

    /**
     * Adds item to user's persisted cart in profile_json.
     * Validations:
     * - item must exist and is_available
     * - If cart exists and restaurant_id differs -> throw conflict
     */
    @Transactional
    public CartDto addToCart(Long userId, AddToCartRequest req, boolean forceReplace) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        FoodItem item = foodItemsRepository.findById(req.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        if (!Boolean.TRUE.equals(item.getIsAvailable())) {
            throw new IllegalStateException("Item not available");
        }

        // Convert user's profile_json to JsonNode (ObjectNode)
        ObjectNode profileJson = (ObjectNode) (user.getProfileJson() == null ? JsonUtils.M.createObjectNode() : JsonUtils.M.valueToTree(user.getProfileJson()));

        ObjectNode cartNode = JsonUtils.ensureCart(profileJson);

        Long cartRestaurantId = cartNode.hasNonNull("restaurant_id") && !cartNode.get("restaurant_id").isNull()
                ? cartNode.get("restaurant_id").asLong() : null;

        if (cartRestaurantId != null && !cartRestaurantId.equals(req.getRestaurantId()) && !forceReplace) {
            // Conflict - different restaurant
            throw new IllegalStateException("Cart contains items from a different restaurant");
        }

        // If forceReplace, clear items and set restaurant id to new one
        if (cartRestaurantId == null || forceReplace) {
            cartNode.put("restaurant_id", req.getRestaurantId());
            cartNode.set("items", JsonUtils.M.createArrayNode());
        }

        // Build cart item snapshot
        ObjectNode itemSnapshot = JsonUtils.M.createObjectNode();
        itemSnapshot.put("display_name", item.getDisplayName());
        itemSnapshot.put("price", item.getPrice().doubleValue());
        itemSnapshot.set("image_urls", JsonUtils.M.valueToTree(item.getImageUrls()));
        itemSnapshot.put("prep_time_minutes", item.getPrepTimeMinutes());

        ObjectNode newCartItem = JsonUtils.M.createObjectNode();
        newCartItem.put("item_id", req.getItemId());
        newCartItem.put("quantity", req.getQuantity() == null ? 1 : req.getQuantity());
        newCartItem.put("unit_price", item.getPrice().doubleValue());
        newCartItem.set("customization_selected", req.getCustomizationSelected() == null ? JsonUtils.M.createArrayNode() : JsonUtils.M.valueToTree(req.getCustomizationSelected()));
        newCartItem.set("item_snapshot", itemSnapshot);

        // Append to items array
        ((ObjectNode) cartNode).withArray("items").add(newCartItem);

        // Recalculate subtotal
        double subtotal = 0.0;
        for (JsonNode ci : cartNode.withArray("items")) {
            subtotal += ci.get("unit_price").asDouble() * ci.get("quantity").asInt();
        }
        double deliveryFee = 20.0; // simplistic default. In real app compute from restaurant/meta/distance
        double taxes = Math.round(subtotal * 0.05 * 100.0) / 100.0; // 5% tax example
        double total = Math.round((subtotal + deliveryFee + taxes) * 100.0) / 100.0;

        cartNode.put("subtotal", subtotal);
        cartNode.put("delivery_fee", deliveryFee);
        cartNode.put("taxes", taxes);
        cartNode.put("total", total);
        cartNode.put("saved_at", OffsetDateTime.now().toString());

        // Save back to user's profile_json (persist)
        user.setProfileJson(JsonUtils.M.convertValue(profileJson, java.util.Map.class));
        usersRepository.save(user);

        // Convert to CartDto and return
        CartDto cartDto = JsonUtils.fromJsonNode(cartNode, CartDto.class);
        return cartDto;
    }

    /**
     * Return current persisted cart or empty cart DTO.
     */
    public CartDto getCart(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow();
        ObjectNode profileJson = user.getProfileJson() == null ? JsonUtils.M.createObjectNode() : (ObjectNode) JsonUtils.M.valueToTree(user.getProfileJson());
        ObjectNode cartNode = JsonUtils.ensureCart(profileJson);
        return JsonUtils.fromJsonNode(cartNode, CartDto.class);
    }

    /**
     * Remove item or decrement quantity
     */
    @Transactional
    public CartDto removeFromCart(Long userId, Long itemId, int decrementBy) {
        Users user = usersRepository.findById(userId).orElseThrow();
        ObjectNode profileJson = user.getProfileJson() == null ? JsonUtils.M.createObjectNode() : (ObjectNode) JsonUtils.M.valueToTree(user.getProfileJson());
        ObjectNode cartNode = JsonUtils.ensureCart(profileJson);
        ArrayNode arr = (ArrayNode) cartNode.withArray("items");
        ArrayNode newArr = JsonUtils.M.createArrayNode();
        for (JsonNode node : arr) {
            long id = node.get("item_id").asLong();
            int qty = node.get("quantity").asInt();
            if (id == itemId) {
                int newQty = qty - decrementBy;
                if (newQty > 0) {
                    ((ObjectNode) node).put("quantity", newQty);
                    newArr.add(node);
                } else {
                    // skip to remove
                }
            } else {
                newArr.add(node);
            }
        }
        cartNode.set("items", newArr);

        // recalc totals (same logic)
        double subtotal = 0.0;
        for (JsonNode ci : cartNode.withArray("items")) {
            subtotal += ci.get("unit_price").asDouble() * ci.get("quantity").asInt();
        }
        double deliveryFee = newArr.size() == 0 ? 0.0 : 20.0;
        double taxes = Math.round(subtotal * 0.05 * 100.0) / 100.0;
        double total = Math.round((subtotal + deliveryFee + taxes) * 100.0) / 100.0;
        cartNode.put("subtotal", subtotal);
        cartNode.put("delivery_fee", deliveryFee);
        cartNode.put("taxes", taxes);
        cartNode.put("total", total);
        cartNode.put("saved_at", OffsetDateTime.now().toString());

        user.setProfileJson(JsonUtils.M.convertValue(profileJson, java.util.Map.class));
        usersRepository.save(user);
        return JsonUtils.fromJsonNode(cartNode, CartDto.class);
    }

    @Transactional
    public void clearCart(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow();
        ObjectNode profileJson = user.getProfileJson() == null ? JsonUtils.M.createObjectNode() : (ObjectNode) JsonUtils.M.valueToTree(user.getProfileJson());
        ObjectNode cartNode = JsonUtils.ensureCart(profileJson);
        cartNode.putNull("restaurant_id");
        cartNode.set("items", JsonUtils.M.createArrayNode());
        cartNode.put("subtotal", 0.0);
        cartNode.put("delivery_fee", 0.0);
        cartNode.put("taxes", 0.0);
        cartNode.put("total", 0.0);
        cartNode.put("saved_at", OffsetDateTime.now().toString());
        user.setProfileJson(JsonUtils.M.convertValue(profileJson, java.util.Map.class));
        usersRepository.save(user);
    }
}
