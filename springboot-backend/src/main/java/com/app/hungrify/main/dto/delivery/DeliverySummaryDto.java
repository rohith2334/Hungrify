package com.app.hungrify.main.dto.delivery;


import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Compact summary for assigned/history list.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliverySummaryDto {
    private Long deliveryId;
    private Long orderId;
    private String restaurantName;
    private String customerName;
    private String deliveryAddress;
    private String status;
    private BigDecimal orderTotal;
    private Instant createdAt;
    private Instant updatedAt;
    private String batchId;
    private List<Long> batchOrderIds;
    private Boolean isPartOfBatch;
    private Integer totalOrdersInBatch;
}
