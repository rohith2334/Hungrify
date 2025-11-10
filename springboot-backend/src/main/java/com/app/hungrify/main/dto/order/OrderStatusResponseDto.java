package com.app.hungrify.main.dto.order;

import lombok.*;
import java.time.Instant;
import java.util.List;

/**
 * Response for order status + timestamps.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusResponseDto {
    private Long orderId;
    private String currentStatus;
    private List<StatusHistoryDto> history;
    private Instant lastUpdated;
}