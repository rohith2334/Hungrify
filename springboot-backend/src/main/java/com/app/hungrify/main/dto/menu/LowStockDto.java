package com.app.hungrify.main.dto.menu;

import lombok.*;

/**
 * Low stock entry returned from low-stock endpoint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LowStockDto {
    private Long itemId;
    private String displayName;
    private Integer quantity;
    private Integer threshold;
    private Boolean isTracked;
}