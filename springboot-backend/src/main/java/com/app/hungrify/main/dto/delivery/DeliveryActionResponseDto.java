package com.app.hungrify.main.dto.delivery;


import lombok.*;

/**
 * Simple response after partner performs an action.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryActionResponseDto {
    private Long deliveryId;
    private String status;
    private String message;
}
