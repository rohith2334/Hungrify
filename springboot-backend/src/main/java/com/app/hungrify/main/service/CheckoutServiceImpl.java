package com.app.hungrify.main.service;


import com.app.hungrify.common.models.Users;
import com.app.hungrify.common.repository.UserRepository;
import com.app.hungrify.main.dto.cart.CartItemDto;
import com.app.hungrify.main.dto.cart.CartResponseDto;
import com.app.hungrify.main.dto.order.*;
import com.app.hungrify.main.exception.BadRequestException;
import com.app.hungrify.main.models.*;
import com.app.hungrify.main.repository.*;
import com.app.hungrify.main.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Atomic order creation and validation.
 */
@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodItemRepository foodItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CommonUtils commonUtils;
    private final CartService cartService;

    @Override
    @Transactional
    public List<OrderDetailDto> placeOrder(PlaceOrderRequestDto request) {
        Users user = userRepository.findById(getLoggedInUserId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        List<CartResponseDto> carts = cartService.getCart();

        if (carts.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        List<OrderDetailDto> orders = new ArrayList<>();

        for (CartResponseDto cart : carts) {
            // Get all item IDs for this restaurant's cart
            List<Long> itemIds = cart.getItems().stream()
                    .map(CartItemDto::getItemId)
                    .collect(Collectors.toList());

            List<FoodItem> dbItems = foodItemRepository.findAllById(itemIds);

            if (dbItems.size() != itemIds.size()) {
                throw new BadRequestException("Some items no longer exist in restaurant " + cart.getRestaurantId());
            }

            // Validate availability
            for (FoodItem fi : dbItems) {
                if (Boolean.FALSE.equals(fi.getIsAvailable())) {
                    throw new BadRequestException(fi.getDisplayName() + " is unavailable");
                }
            }

            // Calculate total for this restaurant
            BigDecimal calcTotal = cart.getItems().stream()
                    .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Restaurant rest = restaurantRepository.findById(cart.getRestaurantId())
                    .orElseThrow(() -> new BadRequestException("Restaurant not found"));

            // Create order for this restaurant
            Order order = new Order();
            order.setUser(user);
            order.setRestaurant(rest);
            order.setTotalAmount(calcTotal);
            order.setPaymentMethod(Order.PaymentMethod.valueOf(request.getPaymentMethod()));
            order.setPaymentStatus(Order.PaymentStatus.pending);
            order.setStatus(Order.OrderStatus.pending);
            order.setDeliveryAddress(request.getDeliveryAddress());
            order.setDeliveryLat(request.getDeliveryLat());
            order.setDeliveryLon(request.getDeliveryLon());
            order.setOrderMeta(Map.of("status_timestamps", List.of(Map.of("placed", LocalDateTime.now().toString()))));

            Order saved = orderRepository.save(order);

            // Add items for this order
            List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
                OrderItem oi = new OrderItem();
                oi.setOrder(saved);
                oi.setItem(dbItems.stream()
                        .filter(f -> f.getItemId().equals(cartItem.getItemId()))
                        .findFirst()
                        .orElse(null));
                oi.setQuantity(cartItem.getQuantity());
                oi.setUnitPrice(cartItem.getUnitPrice());
                oi.setItemSnapshot(Map.of(
                        "display_name", cartItem.getDisplayName(),
                        "price", cartItem.getUnitPrice()
                ));
                return oi;
            }).collect(Collectors.toList());

            orderItemRepository.saveAll(orderItems);

            // Build order detail DTO
            orders.add(OrderDetailDto.builder()
                    .orderId(saved.getOrderId())
                    .userId(user.getUserId())
                    .restaurantId(rest.getRestaurantId())
                    .restaurantName(rest.getName())
                    .totalAmount(saved.getTotalAmount())
                    .status(saved.getStatus().name())
                    .createdAt(saved.getCreatedAt())
                    .items(orderItems.stream().map(oi ->
                            OrderItemDto.builder()
                                    .orderItemId(oi.getOrderItemId())
                                    .displayName((String) oi.getItemSnapshot().get("display_name"))
                                    .quantity(oi.getQuantity())
                                    .unitPrice(oi.getUnitPrice())
                                    .itemSnapshot(oi.getItemSnapshot())
                                    .build()
                    ).toList())
                    .build());
        }

        cartService.clearCart();

        return orders;
    }

    public Long getLoggedInUserId() {
        return commonUtils.getUserId();
    }
}