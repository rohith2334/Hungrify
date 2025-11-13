package com.app.hungrify.common.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupRequest {

    //common

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private String phoneNumber;

    private String firstName;
    private String lastName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private String role;

    private String profileImage;

    private String address;

    private RestaurantData restaurantData;

    private DeliveryData deliveryData;


}
