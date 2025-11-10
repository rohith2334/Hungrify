package com.app.hungrify.main.dto.admin;

import lombok.*;
import jakarta.validation.constraints.*;

/**
 * Request payload for PATCH /api/v1/admin/users/{id}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateUserRequestDto {
    private Boolean active;
    private Boolean verified;
    @Size(max = 255)
    private String roles;
}