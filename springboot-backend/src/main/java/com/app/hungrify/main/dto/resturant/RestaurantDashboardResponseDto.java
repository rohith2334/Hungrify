package com.app.hungrify.main.dto.resturant;


import lombok.*;
import java.util.List;
import java.util.Map;

/**
 * Full dashboard response payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDashboardResponseDto {
    private Long restaurantId;
    private String dateFrom;
    private String dateTo;
    private DashboardKpiDto kpis;
    private List<DashboardCurrentOrderDto> currentOrders;
    private List<DashboardCurrentOrderDto> recentOrders;
    private List<PopularDishDto> popularDishes;
    private List<InventoryLowDto> inventoryLow;
    private List<HourlyTrendDto> hourlyTrend;
    private List<TopModifierDto> topModifiers;
    private QuickActionsDto quickActions;
}
