package com.app.hungrify.main.service;

import com.app.hungrify.main.dto.cart.*;

import java.util.List;

/**
 * Cart management service.
 */
public interface CartService {
    List<CartResponseDto> getCart();
    List<CartResponseDto> addToCart(CartAddRequestDto request);
    List<CartResponseDto> removeFromCart(CartRemoveRequestDto request);
    void clearCart();
    List<CartResponseDto> saveCart();
}
