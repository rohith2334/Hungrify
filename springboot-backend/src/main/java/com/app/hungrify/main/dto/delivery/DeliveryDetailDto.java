package com.app.hungrify.main.dto.delivery;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * Full delivery detail.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDetailDto {
    private Long deliveryId;
    private Long orderId;
    private Long partnerUserId;
    private String partnerName;
    private String partnerVehicleType;
    private String status;
    private Integer estimatedTimeMinutes;
    private Instant actualDeliveryTime;
    private Map<String, Object> deliveryMeta;

    private String restaurantName;
    private String restaurantAddress;
    private BigDecimal orderAmount;
    private String deliveryAddress;
    private Instant createdAt;
}