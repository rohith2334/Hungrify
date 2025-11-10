package com.app.hungrify.main.dto.menu;


import lombok.*;
import java.util.List;

/**
 * Mocked parse response with suggested ingredients and detected allergens.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParseIngredientsResponseDto {
    private String input;
    private List<SuggestedIngredientDto> suggestedIngredients;
    private List<String> allergens;
    private String notes;
}