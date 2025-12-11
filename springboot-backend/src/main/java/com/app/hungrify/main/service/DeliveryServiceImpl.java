package com.app.hungrify.main.service;


import com.app.hungrify.common.models.Users;
import com.app.hungrify.common.repository.UserRepository;
import com.app.hungrify.main.dto.delivery.*;
import com.app.hungrify.main.dto.user.UserProfileDto;
import com.app.hungrify.main.exception.BadRequestException;
import com.app.hungrify.main.exception.NotFoundException;
import com.app.hungrify.main.models.*;
import com.app.hungrify.main.repository.*;
import com.app.hungrify.main.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of delivery partner actions using repositories only.
 */
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CommonUtils commonUtils;

    @Override
    @Transactional(readOnly = true)
    public List<DeliverySummaryDto> getAssignedDeliveries() {
        Long deliveryPartnerId = getLoggedInDeliveryPartnerId();

        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> {
                    if ("PICKUP_ORDER_BY_CUSTOMER".equals(order.getDeliveryAddress())) {
                        return false;
                    }

                    // Check order status
                    Order.OrderStatus status = order.getStatus();
                    if (status != Order.OrderStatus.confirmed &&
                            status != Order.OrderStatus.preparing &&
                            status != Order.OrderStatus.ready_to_pickup) {
                        return false;
                    }

                    // Check deliveryPartnerAssigned in orderMeta
                    Map<String, Object> orderMeta = order.getOrderMeta();
                    if (orderMeta == null || orderMeta.isEmpty()) {
                        return true;
                    }

                    Object deliveryPartnerAssignedObj = orderMeta.get("deliveryPartnerAssigned");
                    if (deliveryPartnerAssignedObj != null) {
                        Boolean deliveryPartnerAssigned = (Boolean) deliveryPartnerAssignedObj;
                        if (deliveryPartnerAssigned) {
                            return false;
                        }
                    }

                    // Check if logged-in user is in rejectedUsers list
                    if (orderMeta.containsKey("rejectedUsers")) {
                        Object rejectedUsersObj = orderMeta.get("rejectedUsers");
                        if (rejectedUsersObj instanceof List) {
                            List<Long> rejectedUsers = (List<Long>) rejectedUsersObj;
                            if (rejectedUsers != null && !rejectedUsers.isEmpty() &&
                                    rejectedUsers.contains(deliveryPartnerId)) {
                                return false;
                            }
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());

        return orders.stream()
                .map(order -> {
                    return DeliverySummaryDto.builder()
                            .orderId(order.getOrderId())
                            .restaurantName(order.getRestaurant() != null ? order.getRestaurant().getName() : null)
                            .customerName(order.getUser() != null ? order.getUser().getFirstName() : null)
                            .deliveryAddress(order.getDeliveryAddress())
                            .status(order.getStatus() != null ? order.getStatus().name() : null)
                            .orderTotal(order.getTotalAmount())
                            .createdAt(order.getCreatedAt())
                            .updatedAt(order.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DeliveryActionResponseDto accept(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        Long deliveryPartnerId = getLoggedInDeliveryPartnerId();
        Users partnerUser = userRepository.findById(deliveryPartnerId)
                .orElseThrow(() -> new NotFoundException("Delivery partner user not found"));

        Order.OrderStatus orderStatus = order.getStatus();

        // Create new delivery entity
        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setPartnerUser(partnerUser);
        delivery.setStatus(Delivery.DeliveryStatus.assigned);
        delivery.setEstimatedTimeMinutes(30);

        Map<String, Object> deliveryMeta = new HashMap<>();
        deliveryMeta.put("accepted_at", Instant.now().toString());
        delivery.setDeliveryMeta(deliveryMeta);

        Delivery savedDelivery = deliveryRepository.save(delivery);

        // Update order status if ready_to_pickup
        if (orderStatus == Order.OrderStatus.ready_to_pickup) {
            order.setStatus(Order.OrderStatus.out_for_delivery);
        }

        // Update order meta
        Map<String, Object> orderMeta = order.getOrderMeta() != null ?
                new HashMap<>(order.getOrderMeta()) : new HashMap<>();
        orderMeta.put("deliveryPartnerAssigned", true);
        order.setOrderMeta(orderMeta);
        orderRepository.save(order);

        return new DeliveryActionResponseDto(savedDelivery.getDeliveryId(), "assigned", "Delivery accepted successfully");
    }

    @Override
    @Transactional
    public DeliveryActionResponseDto reject(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        Long deliveryPartnerId = getLoggedInDeliveryPartnerId();

        // Update order meta with rejected user
        Map<String, Object> orderMeta = order.getOrderMeta() != null ?
                new HashMap<>(order.getOrderMeta()) : new HashMap<>();

        List<Long> rejectedUsers;
        if (orderMeta.containsKey("rejectedUsers")) {
            rejectedUsers = (List<Long>) orderMeta.get("rejectedUsers");
            if (!rejectedUsers.contains(deliveryPartnerId)) {
                rejectedUsers.add(deliveryPartnerId);
            }
        } else {
            rejectedUsers = new ArrayList<>();
            rejectedUsers.add(deliveryPartnerId);
        }

        orderMeta.put("rejectedUsers", rejectedUsers);
        order.setOrderMeta(orderMeta);
        orderRepository.save(order);

        return new DeliveryActionResponseDto(null, "rejected", "Delivery rejected successfully");
    }

    @Override
    @Transactional
    public DeliveryActionResponseDto confirmPickup(Long deliveryId) {
        Long partnerUserId = getLoggedInDeliveryPartnerId();
        Delivery d = validatePartnerAccess(deliveryId, partnerUserId);
        d.setStatus(Delivery.DeliveryStatus.picked_up);
        d.setDeliveryMeta(updateMeta(d, "picked_up_at", Instant.now()));
        deliveryRepository.save(d);
        // update order status
        Order order = d.getOrder();
        order.setStatus(Order.OrderStatus.out_for_delivery);
        //set picked up time in order meta
        Map<String, Object> orderMeta = order.getOrderMeta() != null ? new HashMap<>(order.getOrderMeta()) : new HashMap<>();
        Map<String, String> status_timestamps = orderMeta.containsKey("status_timestamps") ?
                (Map<String, String>) orderMeta.get("status_timestamps") : new HashMap<>();
        status_timestamps.put("picked_up_at", Instant.now().toString());
        orderMeta.put("status_timestamps", status_timestamps);
        order.setOrderMeta(orderMeta); // keep this if other meta updates are needed
        orderRepository.save(order);

        return new DeliveryActionResponseDto(d.getDeliveryId(), "picked_up", "Pickup confirmed");

    }

    @Override
    @Transactional
    public DeliveryActionResponseDto markDelivered(Long deliveryId) {
        Long partnerUserId = getLoggedInDeliveryPartnerId();
        Delivery d = validatePartnerAccess(deliveryId, partnerUserId);
        d.setStatus(Delivery.DeliveryStatus.delivered);
        d.setActualDeliveryTime(Instant.now());
        d.setDeliveryMeta(updateMeta(d, "delivered_at", d.getActualDeliveryTime()));
        deliveryRepository.save(d);

        // update order status
        Order order = d.getOrder();
        order.setStatus(Order.OrderStatus.delivered);

        Map<String, Object> orderMeta = order.getOrderMeta() != null ? new HashMap<>(order.getOrderMeta()) : new HashMap<>();
        Map<String, String> status_timestamps = orderMeta.containsKey("status_timestamps") ?
                (Map<String, String>) orderMeta.get("status_timestamps") : new HashMap<>();
        status_timestamps.put("delivered_at", Instant.now().toString());
        orderMeta.put("status_timestamps", status_timestamps);
        order.setOrderMeta(orderMeta); // keep this if other meta updates are needed
        orderRepository.save(order);


        return new DeliveryActionResponseDto(d.getDeliveryId(), "delivered", "Delivery completed successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliverySummaryDto> getHistory() {
        Long partnerUserId = getLoggedInDeliveryPartnerId();
        List<Delivery> list = deliveryRepository.findByPartnerUser_UserIdAndStatusIn(
                partnerUserId,
                List.of(Delivery.DeliveryStatus.delivered, Delivery.DeliveryStatus.cancelled)
        );
        return list.stream().map(this::toSummary).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EarningsSummaryDto getEarnings(Long partnerUserId) {
        List<Delivery> delivered = deliveryRepository.findByPartnerUser_UserIdAndStatus(
                partnerUserId,
                Delivery.DeliveryStatus.delivered
        );
        BigDecimal total = delivered.stream()
                .map(d -> d.getOrder().getTotalAmount().multiply(BigDecimal.valueOf(0.1))) // 10% commission
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return EarningsSummaryDto.builder()
                .partnerUserId(partnerUserId)
                .totalDeliveries(delivered.size())
                .totalEarnings(total)
                .avgPerDelivery(delivered.isEmpty() ? BigDecimal.ZERO : total.divide(BigDecimal.valueOf(delivered.size())))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryDetailDto getDeliveryDetail(Long deliveryId) {
        Delivery d = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NotFoundException("Delivery not found"));
        Order o = d.getOrder();
        Restaurant r = o.getRestaurant();

        return DeliveryDetailDto.builder()
                .deliveryId(d.getDeliveryId())
                .orderId(o.getOrderId())
                .partnerUserId(d.getPartnerUser() != null ? d.getPartnerUser().getUserId() : null)
                .partnerName(d.getPartnerUser() != null ? d.getPartnerUser().getUsername() : null)
                .partnerVehicleType(d.getPartnerVehicleType() != null ? d.getPartnerVehicleType().name() : null)
                .status(d.getStatus().name())
                .estimatedTimeMinutes(d.getEstimatedTimeMinutes())
                .actualDeliveryTime(d.getActualDeliveryTime())
                .deliveryMeta(d.getDeliveryMeta())
                .restaurantName(r.getName())
                .restaurantAddress(r.getAddress())
                .orderAmount(o.getTotalAmount())
                .deliveryAddress(o.getDeliveryAddress())
                .createdAt(d.getCreatedAt())
                .build();
    }

    @Override
    public List<DeliverySummaryDto> getAssignedDeliveriesForPartner() {
      List<Delivery> list = deliveryRepository.findByPartnerUser_UserIdAndStatusIn(
                getLoggedInDeliveryPartnerId(),
                List.of(Delivery.DeliveryStatus.assigned, Delivery.DeliveryStatus.picked_up)
        );
        List<DeliverySummaryDto> deliverySummaryDtos = list.stream().map(this::toSummary).collect(Collectors.toList());
        // assign orderstatus from associated orders
        for (DeliverySummaryDto dto : deliverySummaryDtos) {
            Order order = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new NotFoundException("Order not found"));
            dto.setStatus(order.getStatus().name());

        }
        return deliverySummaryDtos;
    }

    @Override
    public UserProfileDto updatePartnerStatus() {
        Long partnerUserId = getLoggedInDeliveryPartnerId();
        Users user = userRepository.findById(partnerUserId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        //toggle isActive status
        user.setActive(!user.getActive());
        userRepository.save(user);
        return UserProfileDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // Helper functions
    private Delivery validatePartnerAccess(Long deliveryId, Long partnerUserId) {
        Delivery d = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NotFoundException("Delivery not found"));
        if (d.getPartnerUser() == null || !Objects.equals(d.getPartnerUser().getUserId(), partnerUserId))
            throw new BadRequestException("Unauthorized");
        return d;
    }

    private Map<String, Object> updateMeta(Delivery d, String key, Instant val) {
        Map<String, Object> meta = d.getDeliveryMeta() != null ? new HashMap<>(d.getDeliveryMeta()) : new HashMap<>();
        meta.put(key, val.toString());
        return meta;
    }

    private DeliverySummaryDto toSummary(Delivery d) {
        Order o = d.getOrder();
        return DeliverySummaryDto.builder()
                .deliveryId(d.getDeliveryId())
                .orderId(o.getOrderId())
                .restaurantName(o.getRestaurant().getName())
                .customerName(o.getUser().getFirstName())
                .deliveryAddress(o.getDeliveryAddress())
                .status(d.getStatus().name())
                .orderTotal(o.getTotalAmount())
                .createdAt(d.getCreatedAt())
                .updatedAt(o.getUpdatedAt())
                .build();
    }

    public Long getLoggedInDeliveryPartnerId() {
        // This is a placeholder. In a real application, this would fetch the authenticated user's ID.
        return commonUtils.getUserId();
    }
}