package com.app.hungrify.main.service;


import com.app.hungrify.main.dto.order.*;
import java.util.List;

/**
 * Service for fetching and managing orders.
 */
public interface OrderService {
    List<OrderSummaryDto> listUserOrders(Long userId);
    OrderDetailDto getOrderDetail(Long orderId);
    OrderStatusResponseDto getOrderStatus(Long orderId);
    OrderStatusResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateRequestDto request);
    void submitRating(Long orderId, RatingRequestDto request);
}
