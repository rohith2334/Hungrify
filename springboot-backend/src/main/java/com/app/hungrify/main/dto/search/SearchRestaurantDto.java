package com.app.hungrify.main.dto.search;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Restaurant with matching food items for search results.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRestaurantDto {
    private Long restaurantId;
    private String name;
    private String cuisine;
    private String address;
    private String city;
    private Boolean isActive;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<SearchFoodItemDto> foods;
}
