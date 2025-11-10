package com.app.hungrify.main.service;


import com.app.hungrify.main.dto.order.*;
import com.app.hungrify.main.exception.NotFoundException;
import com.app.hungrify.main.models.*;
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
        Order o = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        List<Map<String, Object>> timestamps = (List<Map<String, Object>>) o.getOrderMeta().get("status_timestamps");
        List<StatusHistoryDto> history = new ArrayList<>();
        if (timestamps != null) {
            for (Map<String, Object> map : timestamps) {
                map.forEach((status, at) ->
                        history.add(new StatusHistoryDto(status, Instant.parse(at.toString()))));
            }
        }

        return OrderStatusResponseDto.builder()
                .orderId(o.getOrderId())
                .currentStatus(o.getStatus().name())
                .history(history)
                .lastUpdated(o.getUpdatedAt())
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
