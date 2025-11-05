package com.app.hungrify.main.repository;


import com.app.hungrify.main.models.FoodItem;
import com.app.hungrify.main.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    List<FoodItem> findByRestaurant(Restaurant restaurant);

    List<FoodItem> findByIsAvailableTrue();

    List<FoodItem> findByCanonicalNameContainingIgnoreCase(String keyword);

    List<FoodItem> findByPriceBetween(Double min, Double max);


    @Query(value = """
        SELECT * FROM food_items
        WHERE is_available = TRUE
        ORDER BY RAND()
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<FoodItem> findRandomSample(@Param("limit") int limit, @Param("offset") int offset);


    /**
     * Fulltext search for food items by keyword and city.
     * Uses MySQL FULLTEXT index on (display_name, canonical_name, short_description, long_description).
     * Optionally filters by restaurant city.
     */
    @Query(value = """
        SELECT fi.* FROM food_items fi
        JOIN restaurants r ON fi.restaurant_id = r.restaurant_id
        WHERE MATCH(fi.display_name, fi.canonical_name, fi.short_description, fi.long_description)
              AGAINST(:query IN NATURAL LANGUAGE MODE)
          AND r.is_active = TRUE
          AND (:city IS NULL OR r.city = :city)
        ORDER BY fi.rating DESC
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<FoodItem> searchFullText(@Param("query") String query,
                                  @Param("city") String city,
                                  @Param("limit") int limit,
                                  @Param("offset") int offset);
}

