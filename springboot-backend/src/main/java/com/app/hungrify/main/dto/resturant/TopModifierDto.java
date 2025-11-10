package com.app.hungrify.main.dto.resturant;

import lombok.*;

/**
 * Top modifiers (like extra_cheese).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopModifierDto {
    private String modifier;
    private Integer timesSelected;
}
