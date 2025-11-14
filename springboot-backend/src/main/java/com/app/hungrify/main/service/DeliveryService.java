package com.app.hungrify.main.service;

import com.app.hungrify.main.dto.delivery.*;
import java.util.List;

/**
 * Service interface for delivery partner actions.
 */
public interface DeliveryService {
    List<DeliverySummaryDto> getAssignedDeliveries();
    DeliveryActionResponseDto acceptOrDecline(Long deliveryId, Long partnerUserId, AcceptDeliveryRequestDto request);
    DeliveryActionResponseDto confirmPickup(Long deliveryId);
    DeliveryActionResponseDto markDelivered(Long deliveryId);
    List<DeliverySummaryDto> getHistory();
    EarningsSummaryDto getEarnings(Long partnerUserId);
    DeliveryDetailDto getDeliveryDetail(Long deliveryId);
}