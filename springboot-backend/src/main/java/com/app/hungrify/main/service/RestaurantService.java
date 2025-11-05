package com.app.hungrify.main.service;

// package com.app.hungrify.main.service;

import com.app.hungrify.main.dto.user.RestaurantSummaryDto;
import com.app.hungrify.main.models.Restaurant;
import com.app.hungrify.main.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Basic restaurant listing logic. Keeps controllers thin.
 */
@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantsRepository;

    /**
     * Returns a paginated/randomized list of restaurants for discovery.
     * If city is provided, we restrict to that city. For "random" sample,
     * the repository can use ORDER BY RAND() limited by page size,
     * or a deterministic shuffle using offset.
     */
    public List<RestaurantSummaryDto> listRestaurants(String city, int page, int limit) {
        int offset = (page - 1) * limit;
        // NOTE: assumes repository has a method findByCityWithPagination or a custom query
        List<Restaurant> restaurants = (city == null || city.isEmpty())
                ? restaurantsRepository.findRandomSample(limit, offset)
                : restaurantsRepository.findByCityWithPagination(city, limit, offset);

        return restaurants.stream().map(this::toDto).collect(Collectors.toList());
    }

    private RestaurantSummaryDto toDto(Restaurant r) {
        return RestaurantSummaryDto.builder()
                .restaurantId(r.getRestaurantId())
                .name(r.getName())
                .cuisine(r.getCuisine())
                .city(r.getCity())
                .rating(r.getRestaurantMeta() != null && r.getRestaurantMeta().get("average_rating") != null
                        ? new java.math.BigDecimal(r.getRestaurantMeta().get("average_rating").toString()) : null)
                .restaurantMeta(r.getRestaurantMeta())
                .isActive(r.getIsActive())
                .build();
    }
}
