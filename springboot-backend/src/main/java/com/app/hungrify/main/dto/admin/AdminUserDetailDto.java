package com.app.hungrify.main.dto.admin;

import lombok.*;
import java.time.Instant;
import java.util.Map;

/**
 * Detailed user profile for admin.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDetailDto {
    private Long userId;
    private String username;
    private String fullName;
    private String roles;
    private String email;
    private String phone;
    private Boolean verified;
    private Boolean active;
    private String profileImage;
    private Map<String, Object> profileJson;
    private String linkedRestaurantName;
    private Instant lastLogin;
    private Instant createdAt;
}