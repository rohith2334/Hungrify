package com.app.hungrify.main.dto.cart;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Cart summary response for UI.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {
    private Long restaurantId;
    private String restaurantName;
    private List<CartItemDto> items;
    private BigDecimal subtotal;
    private Integer totalItems;
}