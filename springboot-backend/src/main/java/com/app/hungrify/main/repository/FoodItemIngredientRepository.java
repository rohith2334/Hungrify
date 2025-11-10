package com.app.hungrify.main.repository;

import com.app.hungrify.main.models.FoodItem;
import com.app.hungrify.main.models.FoodItemIngredient;
import com.app.hungrify.main.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoodItemIngredientRepository extends JpaRepository<FoodItemIngredient, Long> {

//    List<FoodItemIngredient> findByFoodItem(FoodItem item);
//
//    List<FoodItemIngredient> findByIngredient(Ingredient ingredient);

    List<FoodItemIngredient> findByFoodItem_ItemId(Long itemId);
    void deleteByFoodItem_ItemId(Long itemId);
}

