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

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Manage user cart stored in profile_json")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Fetch saved cart for logged-in user")
    @ApiResponse(responseCode = "200", description = "Cart fetched successfully")
    @GetMapping
    public ResponseEntity<List<CartResponseDto>> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @Operation(summary = "Add food to cart (enforces single restaurant constraint)")
    @PostMapping("/add")
    public ResponseEntity<List<CartResponseDto>> addToCart(
            
            @Valid @RequestBody CartAddRequestDto request) {
        return ResponseEntity.ok(cartService.addToCart( request));
    }

    @Operation(summary = "Remove/decrement food from cart")
    @PostMapping("/remove")
    public ResponseEntity<List<CartResponseDto>> removeFromCart(
            
            @Valid @RequestBody CartRemoveRequestDto request) {
        return ResponseEntity.ok(cartService.removeFromCart( request));
    }

    @Operation(summary = "Clear all items from cart")
    @PostMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Persist cart (optional explicit save)")
    @PostMapping("/save")
    public ResponseEntity<List<CartResponseDto>> saveCart() {
        return ResponseEntity.ok(cartService.saveCart());
    }
}