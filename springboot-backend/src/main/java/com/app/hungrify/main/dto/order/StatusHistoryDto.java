package com.app.hungrify.main.dto.order;

import lombok.*;
import java.time.Instant;

/**
 * Order status timeline record.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusHistoryDto {
    private String status;
    private Instant at;
}
