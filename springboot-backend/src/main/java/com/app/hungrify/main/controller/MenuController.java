package com.app.hungrify.main.controller;

import com.app.hungrify.main.dto.menu.*;
import com.app.hungrify.main.service.MenuService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.parameters.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * MenuController
 * Path: /api/v1/restaurants/{restaurantId}/menu
 */
@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/menu")
@Validated
@RequiredArgsConstructor
@Tag(name = "Menu", description = "Menu CRUD, low-stock, item details, parse-ingredients")
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "Get grouped menu for restaurant")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "OK") })
    @GetMapping
    public ResponseEntity<GroupedMenuResponseDto> getMenu(
            @Parameter(description = "Restaurant id") @PathVariable("restaurantId") Long restaurantId) {
        return ResponseEntity.ok(menuService.getGroupedMenu(restaurantId));
    }

    @Operation(summary = "Get single item detail")
    @GetMapping("/items/{itemId}")
    public ResponseEntity<FoodItemDetailDto> getItem(
            @Parameter(description = "Restaurant id") @PathVariable("restaurantId") Long restaurantId,
            @Parameter(description = "Item id") @PathVariable("itemId") Long itemId) {
        return ResponseEntity.ok(menuService.getItemDetail(restaurantId, itemId));
    }

    @Operation(summary = "Create a menu item")
    @PostMapping("/items")
    public ResponseEntity<FoodItemDetailDto> createItem(
            @Parameter(description = "Restaurant id") @PathVariable("restaurantId") Long restaurantId,
            @Valid @RequestBody CreateFoodItemRequestDto request) {
        FoodItemDetailDto created = menuService.createItem(restaurantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Edit (replace) a menu item")
    @PutMapping("/items/{itemId}")
    public ResponseEntity<FoodItemDetailDto> updateItem(
            @Parameter(description = "Restaurant id") @PathVariable("restaurantId") Long restaurantId,
            @Parameter(description = "Item id") @PathVariable("itemId") Long itemId,
            @Valid @RequestBody UpdateFoodItemRequestDto request) {
        FoodItemDetailDto updated = menuService.updateItem(restaurantId, itemId, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Inline update item (price/qty/availability)")
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<FoodItemDetailDto> patchItem(
            @Parameter(description = "Restaurant id") @PathVariable("restaurantId") Long restaurantId,
            @Parameter(description = "Item id") @PathVariable("itemId") Long itemId,
            @Valid @RequestBody PatchFoodItemRequestDto request) {
        FoodItemDetailDto updated = menuService.patchItem(restaurantId, itemId, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Soft delete item")
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @Parameter(description = "Restaurant id") @PathVariable("restaurantId") Long restaurantId,
            @Parameter(description = "Item id") @PathVariable("itemId") Long itemId) {
        menuService.softDeleteItem(restaurantId, itemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get low stock menu items")
    @GetMapping("/low-stock")
    public ResponseEntity<List<LowStockDto>> lowStock(
            @Parameter(description = "Restaurant id") @PathVariable("restaurantId") Long restaurantId) {
        return ResponseEntity.ok(menuService.getLowStock(restaurantId));
    }

    @Operation(summary = "Parse ingredients (mocked AI)")
    @PostMapping("/parse-ingredients")
    public ResponseEntity<ParseIngredientsResponseDto> parseIngredients(
            @Parameter(description = "Restaurant id") @PathVariable("restaurantId") Long restaurantId,
            @Valid @RequestBody ParseIngredientsRequestDto request) {
        return ResponseEntity.ok(menuService.parseIngredients(restaurantId, request));
    }
}