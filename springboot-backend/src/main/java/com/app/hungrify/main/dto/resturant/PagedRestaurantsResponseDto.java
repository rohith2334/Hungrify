package com.app.hungrify.main.dto.resturant;

import lombok.*;
import javax.validation.constraints.*;
import java.util.List;

/**
 * Generic paged response for restaurants discovery.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedRestaurantsResponseDto {
    private Integer page;
    private Integer limit;
    private Long totalEstimate;
    private List<RestaurantSummaryDto> items;
}
