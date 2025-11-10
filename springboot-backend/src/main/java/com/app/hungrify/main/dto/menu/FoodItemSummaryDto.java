package com.app.hungrify.main.dto.menu;

import lombok.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Lightweight food item used in grouped menu lists.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodItemSummaryDto {
    @NotNull
    private Long itemId;

    @NotNull
    @Size(max = 250)
    private String canonicalName;

    @NotNull
    @Size(max = 250)
    private String displayName;

    @Size(max = 512)
    private String shortDescription;

    @NotNull
    private BigDecimal price;

    private Integer quantity;

    private Boolean isAvailable;

    private Integer prepTimeMinutes;

    private List<String> imageUrls;

    private BigDecimal rating;

    private Instant lastUpdated;
}
