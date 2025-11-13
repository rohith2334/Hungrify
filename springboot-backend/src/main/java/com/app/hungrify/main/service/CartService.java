package com.app.hungrify.main.service;

import com.app.hungrify.main.dto.cart.*;

/**
 * Cart management service.
 */
public interface CartService {
    CartResponseDto getCart();
    CartResponseDto addToCart(CartAddRequestDto request);
    CartResponseDto removeFromCart(CartRemoveRequestDto request);
    void clearCart();
    CartResponseDto saveCart();
}
