package com.app.hungrify.main.repository;


import com.app.hungrify.main.models.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // discovery: find by city and optional cuisine; when no filters we can rely on random sample via ORDER BY RAND() in native query
    @Query("SELECT r FROM Restaurant r WHERE (:city IS NULL OR r.city = :city) AND (:cuisine IS NULL OR r.cuisine = :cuisine)")
    Page<Restaurant> findByCityAndCuisine(@Param("city") String city, @Param("cuisine") String cuisine, Pageable pageable);

    // randomized sample when no filters - use native query for RAND (MySQL) or native dialect - I'll provide a native fallback
    @Query(value = "SELECT * FROM restaurants WHERE is_active = true ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Restaurant> findRandomActiveRestaurants(@Param("limit") int limit);

    List<Restaurant> findByCityIgnoreCase(String city);

    Optional<Restaurant> findByOwner_UserId(Long userId);

    /**
     * Find all restaurants that are inactive (pending approval).
     *
     * @return List of restaurants where isActive = false.
     */
    List<Restaurant> findByIsActiveFalse();

    /**
     * Find all active restaurants for discovery/search.
     *
     * @return List of restaurants where isActive = true.
     */
    List<Restaurant> findByIsActiveTrue();

    /**
     * Check if restaurant name exists for uniqueness enforcement.
     */
    boolean existsByNameIgnoreCase(String name);

}