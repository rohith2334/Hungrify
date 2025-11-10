package com.app.hungrify.main.dto.admin;


import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Admin order summary table row.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderSummaryDto {
    private Long orderId;
    private String restaurantName;
    private String customerMaskedPhone;
    private String deliveryStaffName;
    private String status;
    private BigDecimal amount;
    private Instant createdAt;
}