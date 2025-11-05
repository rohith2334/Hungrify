package com.app.hungrify.main.controller;

// package com.app.hungrify.main.controller;

import com.app.hungrify.main.models.Order;
import com.app.hungrify.main.service.CartService;
import com.app.hungrify.main.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Orders", description = "Order placement and tracking")
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;

    @Operation(summary = "Place order using persisted cart (atomic). Server computes totals.")
    @PostMapping
    public ResponseEntity<Map<String, Object>> placeOrder(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestBody PlaceOrderRequest req) {
        // Create order from the server-persisted cart
        Order order = orderService.createOrderFromCart(userId, req.getDeliveryAddress(),
                req.getDeliveryLat(), req.getDeliveryLon(), req.getPaymentMethod(),
                req.getOrderMeta());
        return ResponseEntity.status(201).body(Map.of(
                "order_id", order.getOrderId(),
                "status", order.getStatus(),
                "payment_status", order.getPaymentStatus(),
                "total_amount", order.getTotalAmount(),
                "created_at", order.getCreatedAt()
        ));
    }

    @Operation(summary = "Get order status for tracking")
    @GetMapping("/{orderId}/status")
    public ResponseEntity<Map<String, Object>> orderStatus(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable Long orderId) {
        Map<String, Object> resp = orderService.getOrderStatus(userId, orderId);
        return ResponseEntity.ok(resp);
    }

    // DTO for place order
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PlaceOrderRequest {
        private String deliveryAddress;
        private Double deliveryLat;
        private Double deliveryLon;
        private String paymentMethod; // credit_card, debit_card, upi, cod
        private Map<String, Object> orderMeta;
    }
}
