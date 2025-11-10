package com.app.hungrify.main.dto.order;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Represents each item being ordered.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {
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

    private Map<String, Object> customizationSelected;
}