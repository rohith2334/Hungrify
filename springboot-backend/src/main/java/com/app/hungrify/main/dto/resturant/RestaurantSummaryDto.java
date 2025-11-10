package com.app.hungrify.main.dto.resturant;


import lombok.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * Lightweight restaurant summary used on discovery/home.
 * Fields follow the exact datatypes from shared schema.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSummaryDto {
    @NotNull
    private Long restaurantId;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 100)
    private String cuisine;

    @Size(max = 100)
    private String city;

    /**
     * Meta includes banner_image, delivery_time_range, price_band, thumbnail etc.
     * Matches restaurants.restaurant_meta (JSON).
     */
    private Map<String, Object> restaurantMeta;

    /**
     * average rating (scale matches schema BigDecimal but exposed as BigDecimal)
     */
    private BigDecimal rating;

    private Boolean isActive;
}