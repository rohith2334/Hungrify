package com.app.hungrify.main.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Cart object stored under users.profile_json.cart
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Cart DTO persisted in user.profile_json")
public class CartDto {
    private Long restaurantId;
    private List<CartItemDto> items;
    private BigDecimal subtotal;
    private BigDecimal deliveryFee;
    private BigDecimal taxes;
    private BigDecimal total;
    private String savedAt; // ISO timestamp
}
