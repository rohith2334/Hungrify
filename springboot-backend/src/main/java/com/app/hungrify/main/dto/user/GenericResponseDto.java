package com.app.hungrify.main.dto.user;

import lombok.*;

/**
 * Simple ok response for endpoints that only need to return status.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponseDto {
    private String status;
    private String message;
}
