package com.app.hungrify.main.service;


import com.app.hungrify.main.dto.order.*;
import com.app.hungrify.main.exception.NotFoundException;
import com.app.hungrify.main.models.*;
import com.app.hungrify.main.repository.DeliveryRepository;
import com.app.hungrify.main.repository.OrderRepository;
import com.app.hungrify.main.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final DeliveryRepository deliveryRepository;


    @Override
    public List<OrderSummaryDto> listUserOrders(Long userId) {
        return orderRepository.findByUser_UserIdOrderByCreatedAtDesc(userId)
                .stream().map(o -> OrderSummaryDto.builder()
                        .orderId(o.getOrderId())
                        .restaurantId(o.getRestaurant().getRestaurantId())
                        .restaurantName(o.getRestaurant().getName())
                        .totalAmount(o.getTotalAmount())
                        .status(o.getStatus().name())
                        .createdAt(o.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailDto getOrderDetail(Long orderId) {
        Order o = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return OrderDetailDto.builder()
                .orderId(o.getOrderId())
                .userId(o.getUser().getUserId())
                .restaurantId(o.getRestaurant().getRestaurantId())
                .restaurantName(o.getRestaurant().getName())
                .totalAmount(o.getTotalAmount())
                .paymentMethod(o.getPaymentMethod().name())
                .paymentStatus(o.getPaymentStatus().name())
                .status(o.getStatus().name())
                .createdAt(o.getCreatedAt())
                .items(o.getItems().stream().map(i ->
                        OrderItemDto.builder()
                                .orderItemId(i.getOrderItemId())
                                .displayName((String) i.getItemSnapshot().get("display_name"))
                                .quantity(i.getQuantity())
                                .unitPrice(i.getUnitPrice())
                                .itemSnapshot(i.getItemSnapshot())
                                .build()
                ).toList())
                .build();
    }

    @Override
    public OrderStatusResponseDto getOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        Map<String, Instant> timestamps = Optional.ofNullable(order.getOrderMeta())
                .map(meta -> (Map<String, Instant>) meta.getOrDefault("status_timestamps", new HashMap<>()))
                .orElse(new HashMap<>());

        Optional<Delivery> deliveryOpt = deliveryRepository.findByOrder_OrderId(orderId);

        return OrderStatusResponseDto.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus().name())
                .paymentStatus(order.getPaymentStatus().name())
                .restaurantId(order.getRestaurant().getRestaurantId())
                .deliveryId(deliveryOpt.map(Delivery::getDeliveryId).orElse(null))
                .statusTimestamps(timestamps)
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public OrderStatusResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateRequestDto request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        String newStatusStr = request.getStatus().toLowerCase(Locale.ROOT);
        Order.OrderStatus newStatus;
        try {
            newStatus = Order.OrderStatus.valueOf(newStatusStr);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid order status: " + newStatusStr);
        }

        // Update order status
        order.setStatus(newStatus);
        order.setUpdatedAt(Instant.now());

        // Maintain status_timestamps in order_meta
        Map<String, Object> orderMeta = order.getOrderMeta() != null
                ? new HashMap<>(order.getOrderMeta())
                : new HashMap<>();
        Map<String, Instant> timestamps = (Map<String, Instant>) orderMeta.getOrDefault("status_timestamps", new HashMap<>());
        timestamps.put(newStatus.name(), Instant.now());
        orderMeta.put("status_timestamps", timestamps);

        if (request.getNote() != null)
            orderMeta.put("last_note", request.getNote());
        order.setOrderMeta(orderMeta);

        orderRepository.save(order);

        // Sync Delivery status if applicable
        Optional<Delivery> deliveryOpt = deliveryRepository.findByOrder_OrderId(orderId);
        deliveryOpt.ifPresent(delivery -> {
            switch (newStatus) {
                case out_for_delivery -> delivery.setStatus(Delivery.DeliveryStatus.assigned);
                case delivered -> delivery.setStatus(Delivery.DeliveryStatus.delivered);
                case cancelled -> delivery.setStatus(Delivery.DeliveryStatus.cancelled);
                default -> {
                }
            }
            deliveryRepository.save(delivery);
        });

        return OrderStatusResponseDto.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus().name())
                .paymentStatus(order.getPaymentStatus().name())
                .restaurantId(order.getRestaurant().getRestaurantId())
                .deliveryId(deliveryOpt.map(Delivery::getDeliveryId).orElse(null))
                .statusTimestamps(timestamps)
                .updatedAt(order.getUpdatedAt())
                .note(request.getNote())
                .build();
    }

    @Override
    @Transactional
    public void submitRating(Long orderId, RatingRequestDto request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        Map<String, Object> meta = order.getOrderMeta() != null ? order.getOrderMeta() : new HashMap<>();
        meta.put("rating", Map.of("stars", request.getRating(), "review", request.getReview()));
        order.setOrderMeta(meta);
    }
}
