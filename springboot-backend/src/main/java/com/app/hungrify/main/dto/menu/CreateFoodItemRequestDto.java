package com.app.hungrify.main.dto.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Request body for creating a food item.
 * Fields mirror the schema you provided.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFoodItemRequestDto {
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
    @DecimalMin("0.00")
    private BigDecimal price;

    private Integer quantity = 1;

    private Boolean isAvailable = true;

    private Integer prepTimeMinutes;

    private List<String> imageUrls;

    private FoodItemProfileDto profile;

    private List<CreateIngredientDto> ingredients;

    public static CreateFoodItemRequestDto fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, CreateFoodItemRequestDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }
}
