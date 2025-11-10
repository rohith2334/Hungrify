package com.app.hungrify.main.service;

import com.app.hungrify.main.dto.cart.*;

/**
 * Cart management service.
 */
public interface CartService {
    CartResponseDto getCart(Long userId);
    CartResponseDto addToCart(Long userId, CartAddRequestDto request);
    CartResponseDto removeFromCart(Long userId, CartRemoveRequestDto request);
    void clearCart(Long userId);
    CartResponseDto saveCart(Long userId);
}
