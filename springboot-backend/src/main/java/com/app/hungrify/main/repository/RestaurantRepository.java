package com.app.hungrify.main.repository;


import com.app.hungrify.main.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    /**
     * Random sample of active restaurants.
     * Used for Home/Discover when no filters are applied.
     * MySQL RAND() is sufficient here for small samples.
     */
    @Query(value = """
        SELECT * FROM restaurants
        WHERE is_active = TRUE
        ORDER BY RAND()
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<Restaurant> findRandomSample(@Param("limit") int limit, @Param("offset") int offset);


    /**
     * Paginated restaurants by city, ordered by name (or rating if you add it later).
     */
    @Query(value = """
        SELECT * FROM restaurants
        WHERE (:city IS NULL OR city = :city)
          AND is_active = TRUE
        ORDER BY name ASC
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<Restaurant> findByCityWithPagination(@Param("city") String city,
                                              @Param("limit") int limit,
                                              @Param("offset") int offset);
}