package com.app.hungrify.main.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request to update order status")
public class OrderStatusUpdateRequestDto {

    @NotBlank
    @Schema(example = "preparing", description = "New order status (confirmed, preparing, out_for_delivery, delivered, cancelled)")
    private String status;

    @Schema(example = "Kitchen started preparation", description = "Optional remarks or note about the update")
    private String note;
}