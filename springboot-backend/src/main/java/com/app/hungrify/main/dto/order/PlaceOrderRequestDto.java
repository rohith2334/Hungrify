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
    private String paymentMethod;

//    @NotBlank
    private String deliveryAddress;

    private boolean pickup;

    private BigDecimal deliveryLat;
    private BigDecimal deliveryLon;
}