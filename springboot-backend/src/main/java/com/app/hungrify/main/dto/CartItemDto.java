package com.app.hungrify.main.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Cart item stored in user's profile_json.cart
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Cart item")
public class CartItemDto {
    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private List<Map<String, Object>> customizationSelected;
    private Map<String, Object> itemSnapshot;
}