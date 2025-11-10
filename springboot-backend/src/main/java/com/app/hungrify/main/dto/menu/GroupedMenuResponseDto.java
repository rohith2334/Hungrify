package com.app.hungrify.main.dto.menu;


import lombok.*;
import java.util.List;

/**
 * Response for GET /restaurants/{id}/menu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupedMenuResponseDto {
    private Long restaurantId;
    private List<MenuGroupDto> groups;
}
