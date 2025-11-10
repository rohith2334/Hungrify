package com.app.hungrify.main.dto.cart;


import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Single item inside the cart.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    @NotNull
    private Long itemId;

    @NotBlank
    private String displayName;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal unitPrice;

    private String imageUrl;
}
