package com.app.hungrify.main.service;

// package com.app.hungrify.main.service;

import com.app.hungrify.common.models.Users;
import com.app.hungrify.common.repository.UserRepository;
import com.app.hungrify.main.models.Delivery;
import com.app.hungrify.main.models.FoodItem;
import com.app.hungrify.main.models.Order;
import com.app.hungrify.main.models.Restaurant;
import com.app.hungrify.main.models.OrderItem;
import com.app.hungrify.main.repository.*;
import com.app.hungrify.main.util.JsonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

/**
 * Order creation service. Performs atomic transaction:
 * 1. Validate cart and items
 * 2. Create orders row
 * 3. Insert order_items rows (with item_snapshot)
 * 4. Insert deliveries row
 *
 * Note: This method uses repository.save(...) calls. Your entities must be configured
 * to cascade/flush ids correctly. Adjust if your repositories require saveAndFlush.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository usersRepository;
    private final OrderRepository ordersRepository;
    private final OrderItemRepository orderItemsRepository;
    private final DeliveryRepository deliveriesRepository;
    private final FoodItemRepository foodItemsRepository;
    private final RestaurantRepository restaurantsRepository;

    @Transactional
    public Order createOrderFromCart(Long userId, String deliveryAddress, Double deliveryLat, Double deliveryLon, String paymentMethod, Map<String, Object> orderMeta) {
        // 1) Load user and cart
        Users user = usersRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        ObjectNode profileJson = user.getProfileJson() == null ? JsonUtils.M.createObjectNode() : (ObjectNode) JsonUtils.M.valueToTree(user.getProfileJson());
        ObjectNode cart = JsonUtils.ensureCart(profileJson);

        // Validate cart not empty
        var itemsArray = cart.withArray("items");
        if (itemsArray == null || itemsArray.size() == 0) {
            throw new IllegalStateException("Cart is empty");
        }

        Long restaurantId = cart.hasNonNull("restaurant_id") && !cart.get("restaurant_id").isNull() ? cart.get("restaurant_id").asLong() : null;
        if (restaurantId == null) throw new IllegalStateException("Cart has no restaurant_id");

        // Compute total server-side from item prices (defensive)
        BigDecimal computedTotal = BigDecimal.ZERO;
        List<OrderItem> orderItemEntities = new ArrayList<>();
        for (var ci : itemsArray) {
            long itemId = ci.get("item_id").asLong();
            int qty = ci.get("quantity").asInt();
            FoodItem fi = foodItemsRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));
            if (!Boolean.TRUE.equals(fi.getIsAvailable())) throw new IllegalStateException("Item not available: " + fi.getDisplayName());
            BigDecimal unitPrice = fi.getPrice();
            BigDecimal line = unitPrice.multiply(BigDecimal.valueOf(qty));
            computedTotal = computedTotal.add(line);

            // Create OrderItem entity (item_snapshot as Map)
            OrderItem oi = new OrderItem();
            oi.setOrderItemId(itemId);
            oi.setQuantity(qty);
            oi.setUnitPrice(unitPrice);
            // convert item snapshot: minimal fields
            Map<String, Object> snapshot = new HashMap<>();
            snapshot.put("item_id", fi.getItemId());
            snapshot.put("display_name", fi.getDisplayName());
            snapshot.put("price", fi.getPrice());
            snapshot.put("image_urls", fi.getImageUrls());
            oi.setItemSnapshot(snapshot);
            orderItemEntities.add(oi);
        }

        // Add delivery fee & taxes if present in cart
        BigDecimal deliveryFee = cart.hasNonNull("delivery_fee") ? BigDecimal.valueOf(cart.get("delivery_fee").asDouble()) : BigDecimal.ZERO;
        BigDecimal taxes = cart.hasNonNull("taxes") ? BigDecimal.valueOf(cart.get("taxes").asDouble()) : BigDecimal.ZERO;
        BigDecimal finalTotal = computedTotal.add(deliveryFee).add(taxes);

        // Create Orders entity (use entity associations - ManyToOne mappings)
        Order order = new Order();

        // load restaurant entity and set associations
        com.app.hungrify.main.models.Restaurant restaurant = restaurantsRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurantId));

        order.setUser(user);                 // set ManyToOne user association
        order.setRestaurant(restaurant);     // set ManyToOne restaurant association

        order.setTotalAmount(finalTotal);
        // paymentMethod may be an enum or string in your Order entity; keep current behavior if it's a String
        try {
            // attempt to set as String if setter accepts it
            order.setPaymentMethod(Order.PaymentMethod.credit_card);
        } catch (Exception ex) {
            // if paymentMethod is an enum, try to convert safely
            try {
                if (paymentMethod != null) {
                    Order.PaymentMethod pm = Order.PaymentMethod.valueOf(paymentMethod);
                    order.setPaymentMethod(pm);
                }
            } catch (Exception ignore) {
                // fallback: leave null or rely on default
            }
        }
        order.setPaymentStatus(Order.PaymentStatus.pending);
        order.setDeliveryAddress(deliveryAddress);
        order.setDeliveryLat(deliveryLat == null ? null : BigDecimal.valueOf(deliveryLat));
        order.setDeliveryLon(deliveryLon == null ? null : BigDecimal.valueOf(deliveryLon));
        order.setOrderMeta(orderMeta == null ? Map.of() : orderMeta);
        ordersRepository.save(order); // generates order_id

        // Insert order_items
        for (OrderItem oi : orderItemEntities) {
            oi.setOrder(order); // set owning relation
            orderItemsRepository.save(oi);
        }

        // Create delivery row (1:1)
        Delivery d = new Delivery();
        d.setOrder(order);
        d.setStatus(Delivery.DeliveryStatus.assigned);
        // partner_user_id null until assignment
        deliveriesRepository.save(d);

        // Commit transaction, then clear user's cart
        // Clear cart
        cart.putNull("restaurant_id");
        cart.set("items", JsonUtils.M.createArrayNode());
        cart.put("subtotal", 0.0);
        cart.put("delivery_fee", 0.0);
        cart.put("taxes", 0.0);
        cart.put("total", 0.0);
        cart.put("saved_at", OffsetDateTime.now().toString());
        user.setProfileJson(JsonUtils.M.convertValue(profileJson, Map.class));
        usersRepository.save(user);

        return order;
    }

    /**
     * Returns status summary (order + delivery) used by tracking UI
     */
    public Map<String, Object> getOrderStatus(Long userId, Long orderId) {
        Order order = ordersRepository.findById(orderId).orElseThrow();
        // basic check: owner or admin
        if (order.getUser() == null || !order.getUser().getUserId().equals(userId)) {
            throw new SecurityException("Not allowed");
        }
        Delivery d = deliveriesRepository.findByOrderOrderId(orderId).orElse(null);
        Map<String, Object> resp = new HashMap<>();
        resp.put("order_id", orderId);
        resp.put("status", order.getStatus());
        Map<String, Object> timestamps = (Map<String, Object>) order.getOrderMeta().getOrDefault("status_timestamps", Map.of());
        resp.put("status_timestamps", timestamps);
        if (d != null) {
            Map<String, Object> del = new HashMap<>();
            del.put("delivery_id", d.getDeliveryId());
            del.put("partner_user_id", d.getPartnerUser().getUserId());
            del.put("status", d.getStatus());
            del.put("estimated_time_minutes", d.getEstimatedTimeMinutes());
            del.put("actual_delivery_time", d.getActualDeliveryTime());
            resp.put("delivery", del);
        }
        return resp;
    }
}
