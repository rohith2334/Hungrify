package com.app.hungrify.main.repository;

import com.app.hungrify.main.models.FoodItem;
import com.app.hungrify.main.models.FoodItemProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FoodItemProfileRepository extends JpaRepository<FoodItemProfile, Long> {
    Optional<FoodItemProfile> findByItem_ItemId(Long itemId);
}

