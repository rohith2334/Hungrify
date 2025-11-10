package com.app.hungrify.main.dto.order;


import lombok.*;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Order item detail inside OrderDetailDto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long orderItemId;
    private String displayName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Map<String, Object> customizationSelected;
    private Map<String, Object> itemSnapshot;
}
