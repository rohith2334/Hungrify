package com.app.hungrify.main.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Address object stored inside profile_json.addresses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    @NotBlank
    private String id;           // e.g., "addr_1" client-generated or server-generated
    @NotBlank
    private String label;        // Home / Work
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