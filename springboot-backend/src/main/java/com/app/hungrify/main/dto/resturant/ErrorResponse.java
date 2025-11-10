package com.app.hungrify.main.dto.resturant;

import lombok.*;
import java.time.Instant;

/**
 * Standard error response returned by controller advice.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private Instant timestamp;
}
