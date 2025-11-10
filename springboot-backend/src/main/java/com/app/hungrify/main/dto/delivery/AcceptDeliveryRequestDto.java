package com.app.hungrify.main.dto.delivery;

import lombok.*;
import jakarta.validation.constraints.*;

/**
 * Accept/decline request payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptDeliveryRequestDto {
    @NotNull
    private Boolean accept;
}
