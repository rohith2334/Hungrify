package com.app.hungrify.main.service;


import com.app.hungrify.main.dto.resturant.*;

import java.util.List;

/**
 * Restaurant service interface.
 */
public interface RestaurantService {

    /**
     * Discover restaurants paginated (randomized sample if no filters).
     * @param city optional city
     * @param cuisine optional cuisine
     * @param page page number (1-based)
     * @param limit page size
     * @return paged restaurants
     */
    PagedRestaurantsResponseDto discoverRestaurants(String city, String cuisine, int page, int limit);

    /**
     * Get restaurant details.
     * @param restaurantId id
     * @return RestaurantDetailDto
     * @throws com.example.delivery.service.restaurant.exception.NotFoundException when not found
     */
    RestaurantDetailDto getRestaurantDetails(Long restaurantId);

    /**
     * Build dashboard for restaurant (KPIs, current orders, popular dishes, inventory low).
     * @param restaurantId id
     * @param dateFrom ISO date string (yyyy-MM-dd) optional
     * @param dateTo ISO date string optional
     * @param limitPopular number of popular dishes to return
     */
    RestaurantDashboardResponseDto getDashboard(String dateFrom, String dateTo, int limitPopular);

    /**
     * Compute alerts list for restaurant.
     */
    List<AlertDto> getAlerts();

    /**
     * Update restaurant profile.
     */
    RestaurantDetailDto updateProfile( UpdateProfileRequestDto update);
}