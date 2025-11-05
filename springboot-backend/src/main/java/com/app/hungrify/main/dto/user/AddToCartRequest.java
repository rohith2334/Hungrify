package com.app.hungrify.main.dto.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Add-to-cart request (POST /api/v1/cart/add)
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Request to add item to cart")
public class AddToCartRequest {
    private Long restaurantId;
    private Long itemId;
    private Integer quantity;
    private List<Map<String, Object>> customizationSelected;
}
