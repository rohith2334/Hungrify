package com.app.hungrify.main.dto.admin;

import lombok.*;
import java.time.Instant;

/**
 * Compact view for user management table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserSummaryDto {
    private Long userId;
    private String username;
    private String fullName;
    private String roles;
    private String email;
    private String phone;
    private Boolean active;
    private Boolean verified;
    private Instant createdAt;
}