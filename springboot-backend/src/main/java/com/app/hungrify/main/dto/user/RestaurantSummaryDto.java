package com.app.hungrify.main.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Simple restaurant summary DTO used by discovery endpoints.
 */
@Data
@NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Restaurant summary for listing")
public class RestaurantSummaryDto {
    private Long restaurantId;
    private String name;
    private String cuisine;
    private String city;
    private BigDecimal rating;
    private Map<String, Object> restaurantMeta; // use Map to hold arbitrary JSON
    private Boolean isActive;
}