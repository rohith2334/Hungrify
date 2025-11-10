package com.app.hungrify.main.service;

import com.app.hungrify.main.dto.delivery.*;
import java.util.List;

/**
 * Service interface for delivery partner actions.
 */
public interface DeliveryService {
    List<DeliverySummaryDto> getAssignedDeliveries(Long partnerUserId);
    DeliveryActionResponseDto acceptOrDecline(Long deliveryId, Long partnerUserId, AcceptDeliveryRequestDto request);
    DeliveryActionResponseDto confirmPickup(Long deliveryId, Long partnerUserId);
    DeliveryActionResponseDto markDelivered(Long deliveryId, Long partnerUserId);
    List<DeliverySummaryDto> getHistory(Long partnerUserId);
    EarningsSummaryDto getEarnings(Long partnerUserId);
    DeliveryDetailDto getDeliveryDetail(Long deliveryId);
}