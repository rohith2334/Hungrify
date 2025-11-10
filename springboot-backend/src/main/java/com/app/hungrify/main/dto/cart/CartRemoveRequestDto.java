package com.app.hungrify.main.dto.cart;

import lombok.*;
import jakarta.validation.constraints.NotNull;

/**
 * Remove/decrement food item from cart.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartRemoveRequestDto {
    @NotNull
    private Long itemId;
}