package com.app.hungrify.main.dto.delivery;

import lombok.*;
import java.math.BigDecimal;

/**
 * Total earnings summary for delivery partner.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EarningsSummaryDto {
    private Long partnerUserId;
    private Integer totalDeliveries;
    private BigDecimal totalEarnings;
    private BigDecimal avgPerDelivery;
}