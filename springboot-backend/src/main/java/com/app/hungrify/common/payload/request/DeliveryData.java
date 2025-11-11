package com.app.hungrify.common.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DeliveryData {

    //delivery
    private String vehicleType;       // "bike", "car", etc.
    private String serviceAreaCity;
}
