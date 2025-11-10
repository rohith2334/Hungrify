package com.app.hungrify.main.dto.menu;


import lombok.*;
import java.util.List;

/**
 * Menu group (category) containing items.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuGroupDto {
    private String categoryCode;
    private String categoryName;
    private List<FoodItemSummaryDto> items;
}
