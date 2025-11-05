package com.app.hungrify.main.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Combined search result: restaurant + its matching foods
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Search result grouping for a restaurant with matching foods")
public class RestaurantWithFoodsDto {
    private RestaurantSummaryDto restaurant;
    private List<FoodItemDto> foods;
}
