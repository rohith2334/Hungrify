package com.app.hungrify.main.dto.order;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Lightweight summary for order listing.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDto {
    private Long orderId;
    private Long restaurantId;
    private String restaurantName;
    private BigDecimal totalAmount;
    private String status;
    private Instant createdAt;
}