package com.app.hungrify.main.dto.search.ingredient;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientSuggestionListDto {
    private String q;
    private List<IngredientSuggestionDto> items;
}
