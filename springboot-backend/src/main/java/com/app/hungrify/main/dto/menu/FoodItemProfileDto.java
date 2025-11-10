package com.app.hungrify.main.dto.menu;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Sub-object for profile data during create/edit.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodItemProfileDto {
    private String categoryCode;
    private String categoryName;
    private Integer caloriesKcal;
    private BigDecimal carbsG;
    private BigDecimal proteinG;
    private BigDecimal fatsG;
    private Integer spiceScore;
    private String spiceLevel;
    private List<String> allergens;
    private List<String> tags;
    private Map<String, Object> tasteProfile;
}
