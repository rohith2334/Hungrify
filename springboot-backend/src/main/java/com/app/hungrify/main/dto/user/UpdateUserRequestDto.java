package com.app.hungrify.main.dto.user;

import jakarta.validation.constraints.Email;
import lombok.*;
import javax.validation.constraints.*;
import java.util.Map;

/**
 * PUT /api/v1/users/me
 * Partial update allowed; only safe fields (not password) are exposed here.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 255)
    @Email
    private String email;

    @Size(max = 32)
    private String phone;

    @Size(max = 255)
    private String profileImage;

    /**
     * profileJson patch - replace or merge at service level.
     * Example keys: addresses (List), payments (List), preferences (Map)
     */
    private Map<String, Object> profileJson;
}