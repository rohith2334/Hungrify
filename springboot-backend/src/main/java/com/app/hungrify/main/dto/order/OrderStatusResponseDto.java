package com.app.hungrify.main.dto.order;

import lombok.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Response for order status + timestamps.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusResponseDto {
    private Long orderId;
    private String status;
    private String paymentStatus;
    private Long restaurantId;
    private Long deliveryId;
    private Map<String, String> statusTimestamps;
    private Instant updatedAt;
    private String note;
}