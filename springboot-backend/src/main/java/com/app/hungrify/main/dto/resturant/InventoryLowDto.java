package com.app.hungrify.main.dto.resturant;

import lombok.*;

/**
 * Low inventory alert row.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryLowDto {
    private Long itemId;
    private String displayName;
    private Integer quantity;
    private Integer threshold;
    private Boolean isTracked;
}