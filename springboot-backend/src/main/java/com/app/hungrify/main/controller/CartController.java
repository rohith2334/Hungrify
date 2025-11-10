package com.app.hungrify.main.controller;


import com.app.hungrify.main.dto.cart.*;
import com.app.hungrify.main.service.CartService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Manage user cart stored in profile_json")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Fetch saved cart for logged-in user")
    @ApiResponse(responseCode = "200", description = "Cart fetched successfully")
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@RequestParam Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @Operation(summary = "Add food to cart (enforces single restaurant constraint)")
    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addToCart(
            @RequestParam Long userId,
            @Valid @RequestBody CartAddRequestDto request) {
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    @Operation(summary = "Remove/decrement food from cart")
    @PostMapping("/remove")
    public ResponseEntity<CartResponseDto> removeFromCart(
            @RequestParam Long userId,
            @Valid @RequestBody CartRemoveRequestDto request) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, request));
    }

    @Operation(summary = "Clear all items from cart")
    @PostMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Persist cart (optional explicit save)")
    @PostMapping("/save")
    public ResponseEntity<CartResponseDto> saveCart(@RequestParam Long userId) {
        return ResponseEntity.ok(cartService.saveCart(userId));
    }
}