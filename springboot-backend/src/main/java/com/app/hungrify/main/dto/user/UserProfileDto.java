package com.app.hungrify.main.dto.user;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Response DTO for GET /api/v1/users/me
 * Mirrors allowed public user fields and full profile_json.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private Long userId;

    @NotBlank
    private String username;

    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String fullName;
    private Boolean verified;
    private Boolean active;
    private String profileImage;
    private Map<String, Object> profileJson; // addresses, payments, preferences, cart
    private Instant createdAt;
    private Instant updatedAt;
}
