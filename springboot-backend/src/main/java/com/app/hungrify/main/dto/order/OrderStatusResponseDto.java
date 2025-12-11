package com.app.hungrify.main.dto.order;

import com.app.hungrify.main.models.FoodItem;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Response for order status + timestamps.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusResponseDto {
    private Long orderId;
    private String status;
    private String paymentStatus;
    private Long restaurantId;
    private String restaurantName;
    private BigDecimal totalAmount;
    private Long deliveryId;
    private Map<String, String> statusTimestamps;
    private Instant updatedAt;
    private String note;
    private Long deliveryPartnerUserId;
    private String deliveryPartnerUsername;
    private List<OrderItemDto> items;
    private Boolean isPickupOrder;

}