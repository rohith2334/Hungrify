package com.app.hungrify.main.controller;

import com.app.hungrify.main.dto.order.*;
import com.app.hungrify.main.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Place, view and rate orders")
public class OrderController {

    private final OrderService orderService;
    private final CheckoutService checkoutService;

    @Operation(summary = "Place a new order atomically")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or unavailable items")
    })
    @PostMapping
    public ResponseEntity<OrderDetailDto> placeOrder(@Valid @RequestBody PlaceOrderRequestDto request) {
        return ResponseEntity.ok(checkoutService.placeOrder(request));
    }

    @Operation(summary = "List all orders for a user")
    @GetMapping
    public ResponseEntity<List<OrderSummaryDto>> listUserOrders(@RequestParam Long userId) {
        return ResponseEntity.ok(orderService.listUserOrders(userId));
    }

    @Operation(summary = "Fetch full order details")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailDto> getOrderDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderId));
    }

    @Operation(summary = "Fetch order status timeline")
    @GetMapping("/{orderId}/status")
    public ResponseEntity<OrderStatusResponseDto> getOrderStatus(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderStatus(orderId));
    }

    @Operation(summary = "Submit order rating")
    @PostMapping("/{orderId}/ratings")
    public ResponseEntity<Void> submitRating(
            @PathVariable Long orderId,
            @Valid @RequestBody RatingRequestDto request) {
        orderService.submitRating(orderId, request);
        return ResponseEntity.ok().build();
    }
}