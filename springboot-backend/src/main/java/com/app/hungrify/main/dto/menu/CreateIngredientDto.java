package com.app.hungrify.main.dto.menu;

import lombok.*;
import javax.validation.constraints.*;
import java.util.List;

/**
 * Create ingredient entry used during create/edit flows.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateIngredientDto {
    private Long ingredientId; // optional - if provided, link existing ingredient
    @NotNull
    @Size(max = 200)
    private String name;
    private Boolean removable = false;
    private List<String> allergens;
}