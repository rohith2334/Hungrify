package com.app.hungrify.main.dto.resturant;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Popular dish item for dashboard.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularDishDto {
    private Long itemId;
    private String displayName;
    private Integer timesOrdered;
    private BigDecimal revenue;
    private List<String> imageUrls;
}
