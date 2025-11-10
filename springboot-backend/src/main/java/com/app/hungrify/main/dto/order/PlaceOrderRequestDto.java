package com.app.hungrify.main.dto.order;


import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Request DTO for placing an order.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequestDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long restaurantId;

    @NotEmpty
    private List<OrderItemRequestDto> items;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal totalAmount;

    @NotNull
    private String paymentMethod;

    @NotBlank
    private String deliveryAddress;

    private BigDecimal deliveryLat;
    private BigDecimal deliveryLon;
}