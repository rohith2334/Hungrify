package com.app.hungrify.main.dto.transaction;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * Full transaction log detail for admin view.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailDto {
    private String transactionId;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String status;
    private String gateway;
    private String type;
    private Map<String, Object> rawResponse; // from payment_meta or logs
    private Instant createdAt;
}