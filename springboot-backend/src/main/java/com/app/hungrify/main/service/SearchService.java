package com.app.hungrify.main.service;

// package com.app.hungrify.main.service;

import com.app.hungrify.main.dto.FoodItemDto;
import com.app.hungrify.main.dto.RestaurantWithFoodsDto;
import com.app.hungrify.main.dto.RestaurantSummaryDto;
import com.app.hungrify.main.models.FoodItem;
import com.app.hungrify.main.models.Restaurant;
import com.app.hungrify.main.repository.FoodItemRepository;
import com.app.hungrify.main.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Search service: full-text search on food_items and grouping by restaurant.
 */
@Service
@RequiredArgsConstructor
public class SearchService {

    private final FoodItemRepository foodItemsRepository;
    private final RestaurantRepository restaurantsRepository;

    /**
     * Search foods using FULLTEXT and return grouped RestaurantWithFoodsDto structure.
     * If q is null/empty and no filters, return a random sample (used on Home).
     */
    public List<RestaurantWithFoodsDto> searchFoodsGrouped(String q, String city, int page, int limit) {
        int offset = (page - 1) * limit;

        List<FoodItem> matched;
        if (q == null || q.trim().isEmpty()) {
            // fallback to random sample of foods (or restaurants)
            matched = foodItemsRepository.findRandomSample(limit, offset);
        } else {
            matched = foodItemsRepository.searchFullText(q, city, limit, offset);
        }

        // group by restaurant id
        Map<Long, List<FoodItem>> byRestaurant = matched.stream()
                .collect(Collectors.groupingBy(foodItem -> foodItem.getRestaurant().getRestaurantId(), LinkedHashMap::new, Collectors.toList()));

        List<Long> restaurantIds = new ArrayList<>(byRestaurant.keySet());
        Map<Long, Restaurant> restaurants =
                StreamSupport.stream(restaurantsRepository.findAllById(restaurantIds).spliterator(), false)
                        .collect(Collectors.toMap(Restaurant::getRestaurantId, Function.identity()));

        List<RestaurantWithFoodsDto> results = new ArrayList<>();
        for (Map.Entry<Long, List<FoodItem>> entry : byRestaurant.entrySet()) {
            Long rid = entry.getKey();
            Restaurant rest = restaurants.get(rid);
            RestaurantSummaryDto restDto = RestaurantSummaryDto.builder()
                    .restaurantId(rest.getRestaurantId())
                    .name(rest.getName())
                    .cuisine(rest.getCuisine())
                    .city(rest.getCity())
                    .rating(rest.getRestaurantMeta() != null && rest.getRestaurantMeta().get("average_rating") != null
                            ? new java.math.BigDecimal(rest.getRestaurantMeta().get("average_rating").toString()) : null)
                    .restaurantMeta(rest.getRestaurantMeta())
                    .isActive(rest.getIsActive())
                    .build();

            List<FoodItemDto> foods = entry.getValue().stream().map(fi -> FoodItemDto.builder()
                    .itemId(fi.getItemId())
                    .restaurantId(fi.getRestaurant().getRestaurantId())
                    .displayName(fi.getDisplayName())
                    .shortDescription(fi.getShortDescription())
                    .price(fi.getPrice())
                    .imageUrls(fi.getImageUrls())
                    .isAvailable(fi.getIsAvailable())
                    .prepTimeMinutes(fi.getPrepTimeMinutes())
                    .profile(null) // assumes profile mapped to Map //TODO: remove null
                    .build()).collect(Collectors.toList());

            results.add(RestaurantWithFoodsDto.builder().restaurant(restDto).foods(foods).build());
        }
        return results;
    }
}
