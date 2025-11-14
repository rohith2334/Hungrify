package com.app.hungrify.main.dto.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static List<CreateIngredientDto> listFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<CreateIngredientDto>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }
}