package com.app.hungrify.main.dto.menu;


import lombok.*;
import java.util.List;

/**
 * Suggested ingredient row from the parser.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestedIngredientDto {
    private Long ingredientId; // if matched to existing ingredient
    private String name;
    private String displayName;
    private List<String> allergens;
}