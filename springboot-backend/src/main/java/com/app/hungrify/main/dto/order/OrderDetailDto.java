package com.app.hungrify.main.dto.order;


import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Full order details for order view.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {
    private Long orderId;
    private Long userId;
    private Long restaurantId;
    private String restaurantName;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentTransactionRef;
    private Map<String, Object> paymentMeta;
    private String deliveryAddress;
    private String status;
    private List<OrderItemDto> items;
    private List<StatusHistoryDto> statusHistory;
    private Instant createdAt;
}
