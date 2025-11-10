package com.app.hungrify.main.repository;


import com.app.hungrify.main.models.FoodItem;
import com.app.hungrify.main.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {


    @Query("SELECT f FROM FoodItem f WHERE f.restaurant.restaurantId = :restaurantId")
    List<FoodItem> findByRestaurant(@Param("restaurantId") Long restaurantId);

    // low stock items
//    @Query("SELECT f FROM FoodItem f WHERE f.restaurant.restaurantId = :restaurantId AND f.isAvailable = true AND f.quantity <= f.restaurant.restaurantMeta['threshold']") // cannot access JSON like this in JPQL - fallback below
//    List<FoodItem> findLowStockByRestaurant(@Param("restaurantId") Long restaurantId);

    // Fallback: fetch tracked items and check thresholds in service
    @Query("SELECT f FROM FoodItem f WHERE f.restaurant.restaurantId = :restaurantId AND f.isAvailable = true")
    List<FoodItem> findAvailableByRestaurant(@Param("restaurantId") Long restaurantId);

    // list items for a restaurant, optionally filter by category
    @Query("SELECT f FROM FoodItem f JOIN FETCH f.profile p WHERE f.restaurant.restaurantId = :restaurantId ORDER BY p.categoryName, f.displayName")
    List<FoodItem> findAllByRestaurantWithProfile(@Param("restaurantId") Long restaurantId);

    Optional<FoodItem> findByItemIdAndRestaurant_RestaurantId(Long itemId, Long restaurantId);

    // for checking duplicates
    boolean existsByCanonicalNameAndRestaurant_RestaurantId(String canonicalName, Long restaurantId);

    /**
     * Fulltext search on food items with optional city filter.
     * Uses MySQL MATCH AGAINST on fulltext index fields.
     */
    @Query(value = """
        SELECT f.* FROM food_items f
        JOIN restaurants r ON f.restaurant_id = r.restaurant_id
        WHERE MATCH(f.display_name, f.canonical_name, f.short_description, f.long_description)
              AGAINST (:query IN NATURAL LANGUAGE MODE)
          AND (:city IS NULL OR LOWER(r.city) = LOWER(:city))
          AND f.is_available = TRUE
        """, nativeQuery = true)
    List<FoodItem> searchFullText(@Param("query") String query, @Param("city") String city);

}

