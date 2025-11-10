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
    FoodItemDetailDto getItemDetail(Long restaurantId, Long itemId);

    /**
     * Create a new food item (and optional profile/ingredients).
     * Returns created item id/details.
     */
    FoodItemDetailDto createItem(Long restaurantId, CreateFoodItemRequestDto request);

    /**
     * Replace full item record (PUT).
     */
    FoodItemDetailDto updateItem(Long restaurantId, Long itemId, UpdateFoodItemRequestDto request);

    /**
     * Inline updates (PATCH) to fields such as availability, qty, price.
     */
    FoodItemDetailDto patchItem(Long restaurantId, Long itemId, PatchFoodItemRequestDto request);

    /**
     * Soft-delete item (set isAvailable=false and archive).
     */
    void softDeleteItem(Long restaurantId, Long itemId);

    /**
     * Get low-stock items for a restaurant.
     */
    List<LowStockDto> getLowStock(Long restaurantId);

    /**
     * Mock parse-ingredients; returns suggested normalized ingredient objects and allergens.
     */
    ParseIngredientsResponseDto parseIngredients(Long restaurantId, ParseIngredientsRequestDto request);
}