package com.app.hungrify.main.dto.resturant;


import lombok.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

/**
 * Update profile request body for PUT /restaurants/{restaurantId}/profile
 * Fields mirror Restaurant.restaurantMeta and base restaurant fields.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequestDto {
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String address;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Size(max = 30)
    private String postalCode;

    private Boolean isActive;

    /**
     * restaurantMeta JSON object (open_hours, delivery_time_range, banner_image, supports_takeaway etc.)
     * Use the exact JSON structure used in DB.
     */
    private Map<String, Object> restaurantMeta;
}
