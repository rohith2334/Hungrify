package com.app.hungrify.common.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RestaurantData {
    private String restaurantName;
    private String cuisine;
    private String city;
    private String state;
    private String postalCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String openHours;
    //ai filters
    private Boolean isPureVeg;
    private Boolean isVeganFriendly;
    private Boolean isGlutenFreeFriendly;
    private Boolean isHalalCertified;
    private Boolean isCloudKitchen;
    private Boolean isOrganicIngredients;
    private Boolean isNutFreeFriendly;
}
