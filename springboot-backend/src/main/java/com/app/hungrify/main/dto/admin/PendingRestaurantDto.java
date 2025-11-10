package com.app.hungrify.main.dto.admin;


import lombok.*;
import java.time.Instant;
import java.util.Map;

/**
 * Pending restaurant awaiting approval.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingRestaurantDto {
    private Long restaurantId;
    private String name;
    private Long ownerUserId;
    private String ownerName;
    private String phone;
    private Map<String, Object> restaurantMeta;
    private Instant submittedAt;
}
