package com.app.hungrify.main.dto.menu;

import lombok.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Full item details for edit view.
 * Matches FoodItem and FoodItemProfile fields where applicable.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodItemDetailDto {
    @NotNull
    private Long itemId;

    @NotNull
    private Long restaurantId;

    @NotNull
    @Size(max = 250)
    private String canonicalName;

    @NotNull
    @Size(max = 250)
    private String displayName;

    @Size(max = 512)
    private String shortDescription;

    private String longDescription;

    @NotNull
    private BigDecimal price;

    private Integer quantity;

    private Boolean isAvailable;

    private Integer prepTimeMinutes;

    private List<String> imageUrls;

    private BigDecimal rating;

    private Instant createdAt;
    private Instant updatedAt;

    // Profile fields (FoodItemProfile)
    private String categoryCode;
    private String categoryName;
    private Integer caloriesKcal;
    private BigDecimal carbsG;
    private BigDecimal proteinG;
    private BigDecimal fatsG;
    private Integer spiceScore;
    private String spiceLevel; // enum -> string
    private List<String> allergens;
    private List<String> tags;
    private Map<String, Object> tasteProfile;

    // ingredients represented as simple objects (name, removable)
    private List<IngredientSimpleDto> ingredients;
}
