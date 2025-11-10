package com.app.hungrify.main.dto.cart;


import lombok.*;
import jakarta.validation.constraints.*;

/**
 * Add food item to cart request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartAddRequestDto {
    @NotNull
    private Long restaurantId;

    @NotNull
    private Long itemId;

    @NotBlank
    private String displayName;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private java.math.BigDecimal unitPrice;

    private String imageUrl;
}
