package com.app.hungrify.main.dto.resturant;


import lombok.*;

import javax.validation.constraints.*;

/**
 * Query parameters wrapper (not required as object in controller but helpful).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscoveryRequestDto {
    private String city;
    private String cuisine;
    @Min(1)
    private Integer page = 1;
    @Min(1)
    private Integer limit = 12;
}