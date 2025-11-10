package com.app.hungrify.main.dto.admin;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;

/**
 * Response for GET /api/v1/admin/dashboard
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDto {
    private String dateFrom;
    private String dateTo;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class KPI {
        private Long todayOrders;
        private BigDecimal totalRevenue;
        private Long activeUsers;
        private Integer avgDeliveryTimeMinutes;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class OrdersOverTime {
        private LocalDate date;
        private Long orders;
        private BigDecimal revenue;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TopRestaurant {
        private Long restaurantId;
        private String name;
        private BigDecimal sales;
        private Long orders;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TopDeliveryStaff {
        private Long userId;
        private String name;
        private Double rating;
        private Long deliveries;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Alert {
        private String type;
        private String message;
        private String createdAt;
    }

    private KPI kpis;
    private List<OrdersOverTime> ordersOverTime;
    private List<TopRestaurant> topRestaurants;
    private List<TopDeliveryStaff> topDeliveryStaff;
    private List<Alert> recentAlerts;
}