package com.app.hungrify.main.dto.user;

import com.app.hungrify.main.models.FoodItemProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Food item DTO for API responses.
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Food item (menu) DTO")
public class FoodItemDto {
    private Long itemId;
    private Long restaurantId;
    private String displayName;
    private String shortDescription;
    private BigDecimal price;
    private Integer prepTimeMinutes;
    private List<String> imageUrls;
    private Boolean isAvailable;
    private FoodItemProfile profile; // nutrition/tags
    private Double score; // search score if applicable
}

