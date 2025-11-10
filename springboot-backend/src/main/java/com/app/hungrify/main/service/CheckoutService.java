package com.app.hungrify.main.service;

import com.app.hungrify.main.dto.order.PlaceOrderRequestDto;
import com.app.hungrify.main.dto.order.OrderDetailDto;

/**
 * Service for validating and creating orders atomically.
 */
public interface CheckoutService {
    OrderDetailDto placeOrder(PlaceOrderRequestDto request);
}
