package com.app.hungrify.main.dto.menu;

import lombok.*;
import javax.validation.constraints.*;
import java.util.List;

/**
 * Simplified ingredient DTO for item detail/edit screens.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientSimpleDto {
    private Long ingredientId;
    @NotNull
    @Size(max = 200)
    private String name;
    @Size(max = 200)
    private String displayName;
    private Boolean removable;
    private List<String> allergens;
}