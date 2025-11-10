package com.app.hungrify.main.dto.resturant;


import lombok.*;

/**
 * Simple quick action counts.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuickActionsDto {
    private Integer unconfirmedOrders;
    private Integer itemsOutOfStock;
    private Integer pendingPickups;
}
