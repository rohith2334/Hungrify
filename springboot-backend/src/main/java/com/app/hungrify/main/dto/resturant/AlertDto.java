package com.app.hungrify.main.dto.resturant;

import lombok.*;
import java.time.Instant;

/**
 * Simple computed alerts output.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertDto {
    private String type; // e.g., OUT_OF_STOCK, HIGH_CANCELLATION
    private String message;
    private Instant createdAt;
}
