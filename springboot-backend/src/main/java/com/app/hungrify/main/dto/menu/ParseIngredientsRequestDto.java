package com.app.hungrify.main.dto.menu;


import lombok.*;
import javax.validation.constraints.*;

/**
 * Request body for parse-ingredients endpoint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParseIngredientsRequestDto {

    private String shortDescription;

    @NotNull
    private String itemName;

    private String category;
}
