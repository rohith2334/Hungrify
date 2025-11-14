package com.app.hungrify.main.service;

import com.app.hungrify.main.dto.admin.*;
import java.util.List;

public interface AdminService {
    DashboardResponseDto getDashboard(String dateFrom, String dateTo);
    List<AdminUserSummaryDto> listUsers(String role, int page, int limit);
    AdminUserDetailDto getUserDetail(Long userId);
    AdminUserDetailDto updateUser(Long userId, AdminUpdateUserRequestDto request);

    List<PendingRestaurantDto> getPendingRestaurants();
    void approveRestaurant(Long restaurantId, String note);
    void rejectRestaurant(Long restaurantId, String reason);

    List<AdminOrderSummaryDto> listOrders(String status, int page, int limit);
    AdminOrderDetailDto getOrderDetail(Long orderId);

    List<AdminTransactionDto> listTransactions(String status, int page, int limit);
}