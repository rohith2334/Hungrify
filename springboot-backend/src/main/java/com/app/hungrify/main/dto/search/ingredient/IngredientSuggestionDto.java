package com.app.hungrify.main.dto.search.ingredient;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientSuggestionDto {
    private Long ingredientId;
    private String name;
    private String displayName;
    private List<String> allergens;
    private Double score;
}