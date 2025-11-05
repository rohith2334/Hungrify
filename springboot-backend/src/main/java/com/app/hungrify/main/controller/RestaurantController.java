package com.app.hungrify.main.controller;

// package com.app.hungrify.main.controller;

import com.app.hungrify.main.dto.RestaurantSummaryDto;
import com.app.hungrify.main.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Restaurants", description = "Restaurant discovery APIs")
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Operation(summary = "List restaurants for Home/Discovery")
    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantSummaryDto>> listRestaurants(
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "12") int limit) {
        List<RestaurantSummaryDto> list = restaurantService.listRestaurants(city, page, limit);
        return ResponseEntity.ok(list);
    }

    // Menu endpoint omitted here (assumes you already have controller for menu).
}
