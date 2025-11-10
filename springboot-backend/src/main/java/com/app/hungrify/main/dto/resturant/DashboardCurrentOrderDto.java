package com.app.hungrify.main.dto.resturant;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * Current order summary used in the restaurant dashboard.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardCurrentOrderDto {
    private Long orderId;
    private String status; // matches Order.OrderStatus enum
    private Instant createdAt;
    private Integer estimatedReadyInMinutes;
    private Integer orderItemsCount;
    private BigDecimal totalAmount;
    private Map<String, Object> customer; // {"user_id":..., "masked_phone":"", ...}
}
