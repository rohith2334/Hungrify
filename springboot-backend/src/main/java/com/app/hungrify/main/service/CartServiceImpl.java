package com.app.hungrify.main.service;

import com.app.hungrify.common.models.Users;
import com.app.hungrify.common.repository.UserRepository;
import com.app.hungrify.main.dto.cart.*;
import com.app.hungrify.main.exception.BadRequestException;
import com.app.hungrify.main.exception.NotFoundException;
import com.app.hungrify.main.repository.RestaurantRepository;
import com.app.hungrify.main.util.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Handles CRUD operations on user's cart inside profile_json.
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CommonUtils commonUtils;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CartResponseDto> getCart() {
        Long userId = getUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Map<String, Object> profile = user.getProfileJson();
        if (profile == null || !profile.containsKey("carts")) {
            return new ArrayList<>();
        }

        try {
            List<CartResponseDto> carts = objectMapper.convertValue(profile.get("carts"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CartResponseDto.class));

            // Populate restaurant names
            for (CartResponseDto cart : carts) {
                if (cart.getRestaurantId() != null) {
                    // Fetch restaurant name from repository
                    // Assuming you have a RestaurantRepository
                    restaurantRepository.findById(cart.getRestaurantId())
                            .ifPresent(restaurant -> cart.setRestaurantName(restaurant.getName()));
                }
            }

            return carts;
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public List<CartResponseDto> addToCart(CartAddRequestDto request) {
        Long userId = getUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Map<String, Object> profile = user.getProfileJson() != null ? user.getProfileJson() : new HashMap<>();

        // Get existing carts list
       List<CartResponseDto> carts;
                try {
                    carts = profile.containsKey("carts")
                            ? objectMapper.convertValue(profile.get("carts"),
                                objectMapper.getTypeFactory().constructCollectionType(List.class, CartResponseDto.class))
                            : new ArrayList<>();
                } catch (IllegalArgumentException e) {
                    // If deserialization fails (e.g., old cart structure exists), create fresh list
                    carts = new ArrayList<>();
                    profile.remove("cart"); // Remove old single cart if exists
                    profile.remove("carts"); // Remove corrupted carts if exists
                }

        // Find existing cart for this restaurant
        Optional<CartResponseDto> existingCart = carts.stream()
                .filter(c -> c.getRestaurantId().equals(request.getRestaurantId()))
                .findFirst();

        CartResponseDto cart;
        if (existingCart.isPresent()) {
            // Add to existing restaurant cart
            cart = existingCart.get();
        } else {
            // Create new cart for this restaurant
            cart = emptyCart();
            cart.setRestaurantId(request.getRestaurantId());
// Fetch and set restaurant name
            restaurantRepository.findById(request.getRestaurantId())
                    .ifPresent(restaurant -> cart.setRestaurantName(restaurant.getName()));
            carts.add(cart);
        }

        // Add or update item in cart
        List<CartItemDto> items = cart.getItems() != null ? new ArrayList<>(cart.getItems()) : new ArrayList<>();
        Optional<CartItemDto> existingItem = items.stream()
                .filter(i -> i.getItemId().equals(request.getItemId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItemDto item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            items.add(CartItemDto.builder()
                    .itemId(request.getItemId())
                    .displayName(request.getDisplayName())
                    .quantity(request.getQuantity())
                    .unitPrice(request.getUnitPrice())
                    .imageUrl(request.getImageUrl())
                    .build());
        }

        cart.setItems(items);
        updateTotals(cart);

        profile.put("carts", carts);
        user.setProfileJson(profile);
        userRepository.save(user);
        return carts;
    }

    @Override
    @Transactional
    public List<CartResponseDto> removeFromCart(CartRemoveRequestDto request) {
        Long userId = getUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Map<String, Object> profile = user.getProfileJson();
        if (profile == null || !profile.containsKey("carts")) {
            throw new BadRequestException("Cart is empty");
        }

        List<CartResponseDto> carts;
        try {
            carts = objectMapper.convertValue(profile.get("carts"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CartResponseDto.class));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Cart is empty");
        }

        // Find the cart for the specified restaurant
        Optional<CartResponseDto> targetCart = carts.stream()
                .filter(c -> c.getRestaurantId().equals(request.getRestaurantId()))
                .findFirst();

        if (targetCart.isEmpty()) {
            throw new BadRequestException("No cart found for this restaurant");
        }

        CartResponseDto cart = targetCart.get();
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        // Remove the item or decrease quantity
        List<CartItemDto> updated = new ArrayList<>();
        boolean itemFound = false;
        for (CartItemDto item : cart.getItems()) {
            if (item.getItemId().equals(request.getItemId())) {
                itemFound = true;
                int newQty = item.getQuantity() - 1;
                if (newQty > 0) {
                    item.setQuantity(newQty);
                    updated.add(item);
                }
                // If newQty is 0, item is not added to updated list (effectively removed)
            } else {
                updated.add(item);
            }
        }

        if (!itemFound) {
            throw new BadRequestException("Item not found in cart");
        }

        cart.setItems(updated);
        updateTotals(cart);

        // If cart is now empty, remove it from the carts list
        if (cart.getItems().isEmpty()) {
            carts.remove(cart);
        }

        profile.put("carts", carts);
        user.setProfileJson(profile);
        userRepository.save(user);
        return carts;
    }

    @Override
    @Transactional
    public void clearCart() {
        Long userId = getUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Map<String, Object> profile = user.getProfileJson() != null ? user.getProfileJson() : new HashMap<>();
        profile.remove("carts");
        user.setProfileJson(profile);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public List<CartResponseDto> saveCart() {
        // For now, just return current cart
        return getCart();
    }

    private void updateTotals(CartResponseDto cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            cart.setSubtotal(BigDecimal.ZERO);
            cart.setTotalItems(0);
            return;
        }
        BigDecimal subtotal = cart.getItems().stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setSubtotal(subtotal);
        cart.setTotalItems(cart.getItems().stream().mapToInt(CartItemDto::getQuantity).sum());
    }

    private CartResponseDto emptyCart() {
        return CartResponseDto.builder()
                .restaurantId(null)
                .items(new ArrayList<>())
                .subtotal(BigDecimal.ZERO)
                .totalItems(0)
                .build();
    }

    private Long getUserId(){
        return commonUtils.getUserId();
    }
}