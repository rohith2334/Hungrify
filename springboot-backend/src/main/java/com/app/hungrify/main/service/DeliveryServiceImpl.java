package com.app.hungrify.main.service;


import com.app.hungrify.common.repository.UserRepository;
import com.app.hungrify.main.dto.delivery.*;
import com.app.hungrify.main.exception.BadRequestException;
import com.app.hungrify.main.exception.NotFoundException;
import com.app.hungrify.main.models.*;
import com.app.hungrify.main.repository.*;
import com.app.hungrify.main.util.CommonUtils;
import lombok.RequiredArgsConstructor;
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
        Long partnerUserId = getLoggedInDeliveryPartnerId();
        List<Delivery> list = deliveryRepository.findByPartnerUser_UserIdAndStatusIn(
                partnerUserId,
                List.of(Delivery.DeliveryStatus.assigned, Delivery.DeliveryStatus.picked_up)
        );
        return list.stream().map(this::toSummary).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DeliveryActionResponseDto acceptOrDecline(Long deliveryId, Long partnerUserId, AcceptDeliveryRequestDto request) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NotFoundException("Delivery not found"));
        if (!Objects.equals(delivery.getPartnerUser().getUserId(), partnerUserId))
            throw new BadRequestException("Unauthorized delivery access");

        if (request.getAccept()) {
            delivery.setStatus(Delivery.DeliveryStatus.assigned);
            delivery.setDeliveryMeta(updateMeta(delivery, "accepted_at", Instant.now()));
            deliveryRepository.save(delivery);
            return new DeliveryActionResponseDto(deliveryId, "assigned", "Delivery accepted successfully");
        } else {
            delivery.setStatus(Delivery.DeliveryStatus.cancelled);
            deliveryRepository.save(delivery);
            return new DeliveryActionResponseDto(deliveryId, "cancelled", "Delivery declined");
        }
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
        Long partnerUserId= getLoggedInDeliveryPartnerId();
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
    public List<DeliverySummaryDto> getHistory(Long partnerUserId) {
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