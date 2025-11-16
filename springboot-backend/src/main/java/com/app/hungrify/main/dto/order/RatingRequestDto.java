package com.app.hungrify.main.dto.order;

import lombok.*;
import jakarta.validation.constraints.*;

/**
 * User rating submission for an order.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequestDto {
    @NotNull
    private Long itemId;

    @Min(1)
    @Max(5)
    private Integer rating;
}