package com.app.hungrify.main.service;


import com.app.hungrify.main.dto.search.*;
import com.app.hungrify.main.models.*;
import com.app.hungrify.main.repository.*;
import com.app.hungrify.main.template.PromptTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SearchService implementation that uses MySQL full-text and tag filters.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

    private final FoodItemRepository foodItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final PromptTemplate    promptTemplate;

    @Override
    public SearchResponseDto search(String query, String city, boolean AIFlag) {
        if (query == null || query.trim().isEmpty()) {
            return SearchResponseDto.builder()
                    .query(query)
                    .city(city)
                    .totalRestaurants(0)
                    .totalFoodMatches(0)
                    .restaurants(Collections.emptyList())
                    .build();
        }

        // handle AIFlag if needed (currently not implemented)
        if(AIFlag) {
            // Placeholder for AI-based search logic
            try {
                return promptTemplate.searchFood(query);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Fetch restaurants matching city (if provided)
        List<Restaurant> restaurants = (city != null && !city.isBlank())
                ? restaurantRepository.findByCityIgnoreCase(city)
                : restaurantRepository.findAll();

        if (restaurants.isEmpty()) {
            return SearchResponseDto.builder()
                    .query(query)
                    .city(city)
                    .totalRestaurants(0)
                    .totalFoodMatches(0)
                    .restaurants(Collections.emptyList())
                    .build();
        }

        // Gather all matching food items across restaurants
        List<FoodItem> foodMatches = foodItemRepository.searchFullText(query.toLowerCase(), city);

        // Group by restaurant
        Map<Long, List<FoodItem>> grouped = foodMatches.stream()
                .collect(Collectors.groupingBy(fi -> fi.getRestaurant().getRestaurantId()));

        // Build final DTO
        List<SearchRestaurantDto> groupedRestaurants = new ArrayList<>();
        for (Restaurant r : restaurants) {
            List<FoodItem> matches = grouped.get(r.getRestaurantId());
            if (matches == null || matches.isEmpty()) continue;

            List<SearchFoodItemDto> foodDtos = matches.stream()
                    .map(fi -> SearchFoodItemDto.builder()
                            .itemId(fi.getItemId())
                            .displayName(fi.getDisplayName())
                            .shortDescription(fi.getShortDescription())
                            .price(fi.getPrice())
                            .imageUrls(fi.getImageUrls())
                            .rating(fi.getRating())
                            .categoryName(fi.getProfile() != null ? fi.getProfile().getCategoryName() : null)
                            .build())
                    .collect(Collectors.toList());

            groupedRestaurants.add(SearchRestaurantDto.builder()
                    .restaurantId(r.getRestaurantId())
                    .name(r.getName())
                    .cuisine(r.getCuisine())
                    .address(r.getAddress())
                    .city(r.getCity())
                    .isActive(r.getIsActive())
                    .latitude(r.getLatitude())
                    .longitude(r.getLongitude())
                    .foods(foodDtos)
                    .build());
        }

        int totalFoods = foodMatches.size();

        return SearchResponseDto.builder()
                .query(query)
                .city(city)
                .totalRestaurants(groupedRestaurants.size())
                .totalFoodMatches(totalFoods)
                .restaurants(groupedRestaurants)
                .build();
    }
}