package com.app.hungrify.main.controller;


import com.app.hungrify.common.models.ERole;
import com.app.hungrify.main.dto.admin.*;
import com.app.hungrify.main.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * AdminController — dashboard, user management, restaurant approvals, and order monitoring.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin dashboard and management APIs")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Fetch admin dashboard analytics")
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponseDto> getDashboard(
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo) {
        return ResponseEntity.ok(adminService.getDashboard(dateFrom, dateTo));
    }

    @Operation(summary = "List all users (with role filter optional)")
    @GetMapping("/users")
    public ResponseEntity<List<AdminUserSummaryDto>> listUsers(
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(adminService.listUsers(role, page, limit));
    }

    @Operation(summary = "Get user detail by ID")
    @GetMapping("/users/{id}")
    public ResponseEntity<AdminUserDetailDto> getUserDetail(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserDetail(id));
    }

    @Operation(summary = "Update user (active, verified, roles)")
    @PatchMapping("/users/{id}")
    public ResponseEntity<AdminUserDetailDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateUserRequestDto request) {
        return ResponseEntity.ok(adminService.updateUser(id, request));
    }

    @Operation(summary = "Get list of pending restaurants")
    @GetMapping("/restaurants/pending")
    public ResponseEntity<List<PendingRestaurantDto>> getPendingRestaurants() {
        return ResponseEntity.ok(adminService.getPendingRestaurants());
    }

    @Operation(summary = "Approve a restaurant registration")
    @PostMapping("/restaurants/{id}/approve")
    public ResponseEntity<String> approveRestaurant(
            @PathVariable Long id,
            @RequestParam(required = false) String note) {
        adminService.approveRestaurant(id, note);
        return ResponseEntity.ok("Restaurant approved");
    }

    @Operation(summary = "Reject a restaurant registration")
    @PostMapping("/restaurants/{id}/reject")
    public ResponseEntity<String> rejectRestaurant(
            @PathVariable Long id,
            @RequestParam String reason) {
        adminService.rejectRestaurant(id, reason);
        return ResponseEntity.ok("Restaurant rejected");
    }

    @Operation(summary = "List all orders for admin")
    @GetMapping("/orders")
    public ResponseEntity<List<AdminOrderSummaryDto>> listOrders(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(adminService.listOrders(status, page, limit));
    }

    @Operation(summary = "Get full order details for admin")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<AdminOrderDetailDto> getOrderDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok(adminService.getOrderDetail(orderId));
    }

    // GET /api/v1/admin/delivery-staff
    @Operation(summary = "List all delivery staff")
    @GetMapping("/delivery-staff")
    public ResponseEntity<List<AdminUserSummaryDto>> listDeliveryStaff(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(adminService.listUsers(ERole.ROLE_DELIVERY_AGENT.name(), page, limit));
    }

//    // Request: GET /api/v1/admin/transactions?status=success&page=1&limit=20
//    @Operation(summary = "List all transactions with optional status filter")
//    @GetMapping("/transactions")
//    public ResponseEntity<List<AdminTransactionDto>> listTransactions(
//            @RequestParam(required = false) String status,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "20") int limit) {
//        return ResponseEntity.ok(adminService.listTransactions(status, page, limit));
//    }
}