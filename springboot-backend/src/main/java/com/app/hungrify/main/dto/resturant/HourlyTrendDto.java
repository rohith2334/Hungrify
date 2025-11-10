package com.app.hungrify.main.dto.resturant;


import lombok.*;
import java.math.BigDecimal;

/**
 * Hourly trend for dashboard.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HourlyTrendDto {
    private String hour; // e.g., "10:00"
    private Integer orders;
    private BigDecimal revenue;
}
