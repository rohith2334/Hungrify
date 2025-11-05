package com.app.hungrify.main.controller;

// package com.app.hungrify.main.controller;

import com.app.hungrify.main.dto.user.AddToCartRequest;
import com.app.hungrify.main.dto.user.CartDto;
import com.app.hungrify.main.dto.user.OkResponse;
import com.app.hungrify.main.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart", description = "Persisted cart operations stored in users.profile_json.cart")
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Add item to persisted cart (stored in users.profile_json.cart)")
    @PostMapping("/add")
    public ResponseEntity<OkResponse<CartDto>> addToCart(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestBody AddToCartRequest req,
            @RequestParam(value = "forceReplace", defaultValue = "false") boolean forceReplace) {
        CartDto cart = cartService.addToCart(userId, req, forceReplace);
        return ResponseEntity.ok(OkResponse.<CartDto>builder().status("ok").payload(cart).build());
    }

    @Operation(summary = "Get persisted cart")
    @GetMapping
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal(expression = "id") Long userId) {
        CartDto cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "Remove/decrement an item from persisted cart")
    @PostMapping("/remove")
    public ResponseEntity<CartDto> removeFromCart(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestParam("itemId") Long itemId,
            @RequestParam(value = "decrementBy", defaultValue = "1") int decrementBy) {
        CartDto cart = cartService.removeFromCart(userId, itemId, decrementBy);
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "Clear persisted cart")
    @PostMapping("/clear")
    public ResponseEntity<OkResponse<String>> clearCart(@AuthenticationPrincipal(expression = "id") Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(OkResponse.<String>builder().status("ok").payload("cleared").build());
    }
}
