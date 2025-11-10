package com.app.hungrify.main.dto.user;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Request to add an address to profile_json.addresses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddAddressRequestDto {
    @NotBlank
    private String label;
    @NotBlank
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private BigDecimal lat;
    private BigDecimal lon;
    private String phone;
}
