package com.app.hungrify.main.dto.search;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Lightweight food item for search results.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchFoodItemDto {
    private Long itemId;
    private String displayName;
    private String shortDescription;
    private BigDecimal price;
    private List<String> imageUrls;
    private BigDecimal rating;
    private String categoryName;
}