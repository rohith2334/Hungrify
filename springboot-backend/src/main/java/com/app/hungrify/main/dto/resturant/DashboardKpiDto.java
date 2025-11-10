package com.app.hungrify.main.dto.resturant;

import lombok.*;
import java.math.BigDecimal;

/**
 * KPIs for the restaurant dashboard.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardKpiDto {
    private BigDecimal todayRevenue;
    private BigDecimal periodRevenue;
    private Integer todayOrders;
    private Integer periodOrders;
    private BigDecimal avgOrderValue;
    private Integer pendingOrdersCount;
    private Integer cancellationsCount;
    private Double averagePrepTimeMinutes;
    private Double onTimeRatePct;
}