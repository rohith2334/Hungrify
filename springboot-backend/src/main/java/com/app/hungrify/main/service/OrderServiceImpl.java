package com.app.hungrify.main.service;


import com.app.hungrify.common.models.Users;
import com.app.hungrify.common.repository.UserRepository;
import com.app.hungrify.main.dto.order.*;
import com.app.hungrify.main.exception.NotFoundException;
import com.app.hungrify.main.models.*;
import com.app.hungrify.main.repository.DeliveryRepository;
import com.app.hungrify.main.repository.FoodItemRepository;
import com.app.hungrify.main.repository.OrderRepository;
import com.app.hungrify.main.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final UserRepository userRepository;
    private final FoodItemRepository foodItemRepository;


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

        Map<String, Instant> timestampsMap = Optional.ofNullable(order.getOrderMeta())
                .map(meta -> {
                    Object rawObj = meta.get("status_timestamps");
                    if (rawObj instanceof Map<?, ?> rawMap) {
                        return rawMap.entrySet().stream()
                                .collect(Collectors.toMap(
                                        e -> e.getKey().toString(),
                                        e -> safeParseInstant(e.getValue().toString())
                                ));
                    } else if (rawObj instanceof List<?> rawList && !rawList.isEmpty() && rawList.get(0) instanceof Map<?, ?> firstMap) {
                        return firstMap.entrySet().stream()
                                .collect(Collectors.toMap(
                                        e -> e.getKey().toString(),
                                        e -> safeParseInstant(e.getValue().toString())
                                ));
                    }
                    return new HashMap<String, Instant>();
                })
                .orElse(new HashMap<>());

        Map<String, String> timestamps = timestampsMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() != null ? e.getValue().toString() : null
                ));

        Optional<Delivery> deliveryOpt = deliveryRepository.findByOrder_OrderId(orderId);
        String note = order.getOrderMeta() != null ? (String) order.getOrderMeta().get("last_note") : null;

        Long deliveryPartnerUserId = null;
        String deliveryPartnerUsername = null;
        if (deliveryOpt.isPresent() && deliveryOpt.get().getPartnerUser() != null) {
            deliveryPartnerUserId = deliveryOpt.get().getPartnerUser().getUserId();
            deliveryPartnerUsername = deliveryOpt.get().getPartnerUser().getUsername();
        }

        return OrderStatusResponseDto.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus().name())
                .paymentStatus(order.getPaymentStatus().name())
                .restaurantId(order.getRestaurant().getRestaurantId())
                .deliveryId(deliveryOpt.map(Delivery::getDeliveryId).orElse(null))
                .statusTimestamps(timestamps)
                .updatedAt(order.getUpdatedAt())
                .note(note)
                .deliveryPartnerUserId(deliveryPartnerUserId)
                .deliveryPartnerUsername(deliveryPartnerUsername)
                .build();
    }

    // Helper method
    private Instant safeParseInstant(String value) {
        if (value.contains(".")) {
            int zIdx = value.indexOf('Z');
            String beforeZ = zIdx != -1 ? value.substring(0, zIdx) : value;
            int dotIdx = beforeZ.indexOf('.');
            String beforeDot = beforeZ.substring(0, dotIdx);
            String afterDot = beforeZ.substring(dotIdx + 1);
            String fraction = afterDot.length() > 9 ? afterDot.substring(0, 9)
                    : String.format("%-9s", afterDot).replace(' ', '0');
            String rebuilt = beforeDot + "." + fraction + "Z";
            value = rebuilt;
        } else if (!value.endsWith("Z")) {
            value = value + "Z";
        }
        return Instant.parse(value);
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
        Object rawTimestamps = orderMeta.get("status_timestamps");
        Map<String, String> timestamps;
        if (rawTimestamps instanceof Map<?, ?> rawMap) {
            timestamps = new HashMap<>();
            rawMap.forEach((k, v) -> timestamps.put(k.toString(), v != null ? v.toString() : null));
        } else if (rawTimestamps instanceof List<?> rawList && !rawList.isEmpty() && rawList.get(0) instanceof Map<?, ?> firstMap) {
            timestamps = new HashMap<>();
            ((Map<?, ?>) firstMap).forEach((k, v) -> timestamps.put(k.toString(), v != null ? v.toString() : null));
        } else {
            timestamps = new HashMap<>();
        }
        timestamps.put(newStatus.name(), Instant.now().toString());
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

        // if new status is preparing assign a delivery partner who is available
        if (newStatus == Order.OrderStatus.preparing) {
            List<Users> deliveryPartner = userRepository.findAvailableDeliveryUsers();
            Delivery delivery = new Delivery();
            delivery.setOrder(order);
            delivery.setStatus(Delivery.DeliveryStatus.assigned);
            if (!deliveryPartner.isEmpty()) {
                delivery.setPartnerUser(deliveryPartner.get(0)); // Assign first available partner
            }
            delivery.setPartnerVehicleType(Delivery.VehicleType.other); // Default vehicle type
            delivery.setEstimatedTimeMinutes(30); // Default estimated time
            delivery.setCreatedAt(Instant.now());
            deliveryRepository.save(delivery);
            deliveryOpt = Optional.of(delivery);
        }

        Long deliveryPartnerUserId = null;
        String deliveryPartnerUsername = null;
        if (deliveryOpt.isPresent() && deliveryOpt.get().getPartnerUser() != null) {
            deliveryPartnerUserId = deliveryOpt.get().getPartnerUser().getUserId();
            deliveryPartnerUsername = deliveryOpt.get().getPartnerUser().getUsername();
        }

        String note = order.getOrderMeta() != null ? (String) order.getOrderMeta().get("last_note") : null;

        return OrderStatusResponseDto.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus().name())
                .paymentStatus(order.getPaymentStatus().name())
                .restaurantId(order.getRestaurant().getRestaurantId())
                .deliveryId(deliveryOpt.map(Delivery::getDeliveryId).orElse(null))
                .statusTimestamps(timestamps)
                .updatedAt(order.getUpdatedAt())
                .note(note)
                .deliveryPartnerUserId(deliveryPartnerUserId)
                .deliveryPartnerUsername(deliveryPartnerUsername)
                .build();
    }

    @Override
    @Transactional
    public void submitRating(RatingRequestDto request) {
        Long orderId= request.getOrderId();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        Map<String, Object> meta = order.getOrderMeta() != null ? order.getOrderMeta() : new HashMap<>();
        meta.put("rating", Map.of("stars", request.getRating(), "review", request.getReview()));
        order.setOrderMeta(meta);
        orderRepository.save(order);
        // update food item ratings
        // After saving order meta
//        for (OrderItem item : order.getItems()) {
//            Long itemId = Long.valueOf(item.getItemSnapshot().get("item_id").toString());
//            FoodItem foodItem = foodItemRepository.findById(itemId)
//                    .orElseThrow(() -> new NotFoundException("Food item not found"));
//
//            BigDecimal ratingCount = foodItem.getRating() != null ? foodItem.getRating() : 0;
//            double currentRating = foodItem.getRating() != null ? foodItem.getRating().doubleValue() : 0.0;
//            int newRating = request.getRating();
//
//            double newAverage = ((currentRating * ratingCount) + newRating) / (ratingCount + 1);
//            foodItem.setRating(BigDecimal.valueOf(newAverage));
//            foodItem.setRating(BigDecimal.valueOf(ratingCount + 1));
//
//            foodItemRepository.save(foodItem);
//        }
    }
}
