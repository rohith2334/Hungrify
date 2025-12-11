package com.app.hungrify.main.service;

import com.app.hungrify.main.dto.delivery.*;
import com.app.hungrify.main.dto.user.UserProfileDto;

import java.util.List;

/**
 * Service interface for delivery partner actions.
 */
public interface DeliveryService {
    List<DeliverySummaryDto> getAssignedDeliveries();
    DeliveryActionResponseDto accept(Long orderId);
    DeliveryActionResponseDto reject(Long orderId);
    DeliveryActionResponseDto confirmPickup(Long deliveryId);
    DeliveryActionResponseDto markDelivered(Long deliveryId);
    List<DeliverySummaryDto> getHistory();
    EarningsSummaryDto getEarnings(Long partnerUserId);
    DeliveryDetailDto getDeliveryDetail(Long deliveryId);

    List<DeliverySummaryDto> getAssignedDeliveriesForPartner();

    UserProfileDto updatePartnerStatus();
}