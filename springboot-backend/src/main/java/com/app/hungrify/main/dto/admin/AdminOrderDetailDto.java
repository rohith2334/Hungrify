package com.app.hungrify.main.dto.admin;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Full order detail view.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderDetailDto {
    private Long orderId;
    private Long userId;
    private Long restaurantId;
    private List<Map<String,Object>> items;
    private Map<String, Object> totals;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentTransactionRef;
    private Map<String, Object> paymentMeta;
    private Map<String, Object> delivery;
    private Instant createdAt;
}
