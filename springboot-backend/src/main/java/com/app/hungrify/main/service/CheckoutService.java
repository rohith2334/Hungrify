package com.app.hungrify.main.service;

import com.app.hungrify.main.dto.order.PlaceOrderRequestDto;
import com.app.hungrify.main.dto.order.OrderDetailDto;

import java.util.List;

/**
 * Service for validating and creating orders atomically.
 */
public interface CheckoutService {
    List<OrderDetailDto> placeOrder(PlaceOrderRequestDto request);
}
