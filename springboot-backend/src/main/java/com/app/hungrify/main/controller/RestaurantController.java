package com.app.hungrify.main.controller;


import com.app.hungrify.main.dto.resturant.*;
import com.app.hungrify.main.service.RestaurantService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.parameters.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * RestaurantController
 * Path: /api/v1/restaurants
 */
@RestController
@RequestMapping("/api/v1/restaurants")
@Validated
@RequiredArgsConstructor
@Tag(name = "Restaurant", description = "Restaurant profile, dashboard & alerts")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Operation(summary = "Discovery (paginated, randomized when no filters)",
            description = "GET /api/v1/restaurants — List restaurants. If no city/cuisine filters provided returns randomized sample.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping
    public ResponseEntity<PagedRestaurantsResponseDto> discover(
            @Parameter(description = "City") @RequestParam(value = "city", required = false) String city,
            @Parameter(description = "Cuisine") @RequestParam(value = "cuisine", required = false) String cuisine,
            @Parameter(description = "Page") @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "Limit") @RequestParam(value = "limit", defaultValue = "12") int limit) {
        PagedRestaurantsResponseDto resp = restaurantService.discoverRestaurants(city, cuisine, page, limit);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Restaurant details",
            description = "GET /api/v1/restaurants/{restaurantId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDetailDto> details(
            @Parameter(description = "Restaurant id") @PathVariable("restaurantId") Long restaurantId) {
        RestaurantDetailDto dto = restaurantService.getRestaurantDetails(restaurantId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Restaurant dashboard",
            description = "GET /api/v1/restaurants/{restaurantId}/dashboard — dateFrom/to format yyyy-MM-dd")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "OK") })
    @GetMapping("/{restaurantId}/dashboard")
    public ResponseEntity<RestaurantDashboardResponseDto> dashboard(
            @Parameter(description = "Restaurant id") @PathVariable("restaurantId") Long restaurantId,
            @Parameter(description = "Date from yyyy-MM-dd", example = "2025-11-01") @RequestParam(value = "date_from", required = false) String dateFrom,
            @Parameter(description = "Date to yyyy-MM-dd", example = "2025-11-04") @RequestParam(value = "date_to", required = false) String dateTo,
            @Parameter(description = "limit popular dishes") @RequestParam(value = "limit_popular", defaultValue = "5") int limitPopular) {

        RestaurantDashboardResponseDto resp = restaurantService.getDashboard(restaurantId, dateFrom, dateTo, limitPopular);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Recent alerts for restaurant")
    @GetMapping("/{restaurantId}/alerts")
    public ResponseEntity<List<AlertDto>> alerts(
            @Parameter(description = "Restaurant id") @PathVariable("restaurantId") Long restaurantId) {
        List<AlertDto> alerts = restaurantService.getAlerts(restaurantId);
        return ResponseEntity.ok(alerts);
    }

    @Operation(summary = "Update restaurant profile",
            description = "PUT /api/v1/restaurants/{restaurantId}/profile — updates open hours and meta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PutMapping("/{restaurantId}/profile")
    public ResponseEntity<RestaurantDetailDto> updateProfile(
            @Parameter(description = "Restaurant id") @PathVariable("restaurantId") Long restaurantId,
            @Valid @RequestBody UpdateProfileRequestDto request) {
        RestaurantDetailDto updated = restaurantService.updateProfile(restaurantId, request);
        return ResponseEntity.ok(updated);
    }
}