package com.app.hungrify.main.service;


import com.app.hungrify.main.dto.menu.*;

import java.util.List;

/**
 * Service interface for menu CRUD and parse-ingredients.
 */
public interface MenuService {

    /**
     * Get grouped menu for a restaurant.
     * @param restaurantId
     */
    GroupedMenuResponseDto getGroupedMenu(Long restaurantId);

    /**
     * Get item details (for edit).
     */
    FoodItemDetailDto getItemDetail(Long itemId);

    /**
     * Create a new food item (and optional profile/ingredients).
     * Returns created item id/details.
     */
    FoodItemDetailDto createItem(CreateFoodItemRequestDto request);

    /**
     * Replace full item record (PUT).
     */
    FoodItemDetailDto updateItem(Long itemId, UpdateFoodItemRequestDto request);

    /**
     * Inline updates (PATCH) to fields such as availability, qty, price.
     */
    FoodItemDetailDto patchItem(Long itemId, PatchFoodItemRequestDto request);

    /**
     * Soft-delete item (set isAvailable=false and archive).
     */
    void softDeleteItem(Long itemId);

    /**
     * Get low-stock items for a restaurant.
     */
    List<LowStockDto> getLowStock(Long restaurantId);

    /**
     * Mock parse-ingredients; returns suggested normalized ingredient objects and allergens.
     */
    CreateFoodItemRequestDto parseIngredients(ParseIngredientsRequestDto request);
}