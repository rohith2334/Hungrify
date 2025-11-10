package com.app.hungrify.main.dto.menu;


import lombok.*;
import java.math.BigDecimal;

/**
 * Inline updates (PATCH) for price/qty/availability.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchFoodItemRequestDto {
    private Boolean isAvailable;
    private Integer quantity;
    private BigDecimal price;
}
