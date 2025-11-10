package com.app.hungrify.main.dto.transaction;


import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Lightweight transaction summary for list view.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryDto {
    private String transactionId;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String status;
    private String gateway;
    private String type; // e.g., "order_payment", "refund"
    private Instant createdAt;
}