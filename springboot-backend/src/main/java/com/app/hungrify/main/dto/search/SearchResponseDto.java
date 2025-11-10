package com.app.hungrify.main.dto.search;

import lombok.*;
import java.util.List;

/**
 * Final grouped response for /api/v1/search
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDto {
    private String query;
    private String city;
    private Integer totalRestaurants;
    private Integer totalFoodMatches;
    private List<SearchRestaurantDto> restaurants;
}
