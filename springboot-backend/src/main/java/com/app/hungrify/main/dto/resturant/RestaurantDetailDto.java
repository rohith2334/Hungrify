package com.app.hungrify.main.dto.resturant;

import jakarta.persistence.Column;
import lombok.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * Full restaurant details for /api/v1/restaurants/{restaurantId}
 * Mirrors the Restaurant entity fields exactly in types.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDetailDto {
    @NotNull
    private Long restaurantId;

    @NotNull
    @Size(max = 255)
    private String name;

    private Long ownerUserId;

    @Size(max = 100)
    private String cuisine;

    private String address;

    @Size(max = 100)
    private String city;

    @Column(nullable = true)
    private String state;

    @Size(max = 30)
    private String postalCode;

    private BigDecimal latitude;
    private BigDecimal longitude;

    /**
     * restaurantMeta JSON exactly as stored in DB
     */
    private Map<String, Object> restaurantMeta;

    private Boolean isActive;

    private Instant createdAt;
    private Instant updatedAt;
}
