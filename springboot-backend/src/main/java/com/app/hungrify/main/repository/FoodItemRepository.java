package com.app.hungrify.main.repository;


import com.app.hungrify.main.models.FoodItem;
import com.app.hungrify.main.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    List<FoodItem> findByRestaurant(Restaurant restaurant);

    List<FoodItem> findByIsAvailableTrue();

    List<FoodItem> findByCanonicalNameContainingIgnoreCase(String keyword);

    List<FoodItem> findByPriceBetween(Double min, Double max);
}

