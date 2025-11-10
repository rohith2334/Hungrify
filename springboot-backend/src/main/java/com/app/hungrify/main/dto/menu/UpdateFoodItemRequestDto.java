package com.app.hungrify.main.dto.menu;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Full update (PUT) request - mirrors Create but fields optional for partials allowed by client.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFoodItemRequestDto {
    private String canonicalName;
    private String displayName;
    private String shortDescription;
    private String longDescription;
    private BigDecimal price;
    private Integer quantity;
    private Boolean isAvailable;
    private Integer prepTimeMinutes;
    private List<String> imageUrls;
    private FoodItemProfileDto profile;
    private List<CreateIngredientDto> ingredients;
}
