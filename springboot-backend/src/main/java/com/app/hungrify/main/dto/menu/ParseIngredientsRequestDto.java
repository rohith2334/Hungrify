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
    @NotNull
    @Size(min = 1)
    private String text;

    private String itemHint;
}
