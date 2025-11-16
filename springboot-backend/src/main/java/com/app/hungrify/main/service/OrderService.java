package com.app.hungrify.main.service;


import com.app.hungrify.main.dto.order.*;
import java.util.List;

/**
 * Service for fetching and managing orders.
 */
public interface OrderService {
    List<OrderSummaryDto> listUserOrders();
    OrderDetailDto getOrderDetail(Long orderId);
    OrderStatusResponseDto getOrderStatus(Long orderId);
    OrderStatusResponseDto updateOrderStatus(Long orderId, OrderStatusUpdateRequestDto request);
    void submitRating(List<RatingRequestDto> request);
}
