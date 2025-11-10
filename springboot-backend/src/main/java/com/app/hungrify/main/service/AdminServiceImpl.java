package com.app.hungrify.main.service;

import com.app.hungrify.common.models.ERole;
import com.app.hungrify.common.models.Users;
import com.app.hungrify.common.repository.UserRepository;
import com.app.hungrify.main.dto.admin.*;
import com.app.hungrify.main.exception.NotFoundException;
import com.app.hungrify.main.models.*;
import com.app.hungrify.main.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of admin management and analytics.
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponseDto getDashboard(String dateFrom, String dateTo) {
        LocalDate from = LocalDate.parse(dateFrom);
        LocalDate to = LocalDate.parse(dateTo);

        List<Order> all = orderRepository.findAll();
        BigDecimal totalRevenue = all.stream()
                .filter(o -> o.getCreatedAt().isAfter(from.atStartOfDay().toInstant(ZoneOffset.UTC)))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<DashboardResponseDto.OrdersOverTime> daily = all.stream()
                .collect(Collectors.groupingBy(o -> o.getCreatedAt().atZone(ZoneOffset.UTC).toLocalDate()))
                .entrySet().stream()
                .map(e -> DashboardResponseDto.OrdersOverTime.builder()
                        .date(e.getKey())
                        .orders((long) e.getValue().size())
                        .revenue(e.getValue().stream().map(Order::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
                        .build())
                .collect(Collectors.toList());

        return DashboardResponseDto.builder()
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .kpis(new DashboardResponseDto.KPI(
                        (long) daily.stream().mapToLong(DashboardResponseDto.OrdersOverTime::getOrders).sum(),
                        totalRevenue,
                        userRepository.count(),
                        32))
                .ordersOverTime(daily)
                .topRestaurants(Collections.emptyList())
                .topDeliveryStaff(Collections.emptyList())
                .recentAlerts(Collections.emptyList())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserSummaryDto> listUsers(String role, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        return userRepository.findAll(pageable).stream()
                .filter(u -> role == null ||
                        (u.getRoles() != null && u.getRoles().name().equalsIgnoreCase(role))).map(u -> AdminUserSummaryDto.builder()
                        .userId(u.getUserId())
                        .username(u.getUsername())
                        .fullName(u.getFullName())
                        .roles(u.getRoles().toString())
                        .email(u.getEmail())
                        .phone(u.getPhone())
                        .active(u.getActive())
                        .verified(u.getVerified())
                        .createdAt(u.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserDetailDto getUserDetail(Long userId) {
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Restaurant linked = restaurantRepository.findByOwner_UserId(u.getUserId()).orElse(null);

        return AdminUserDetailDto.builder()
                .userId(u.getUserId())
                .username(u.getUsername())
                .fullName(u.getFullName())
                .roles(u.getRoles().toString())
                .email(u.getEmail())
                .phone(u.getPhone())
                .verified(u.getVerified())
                .active(u.getActive())
                .profileImage(u.getProfileImage())
                .profileJson(u.getProfileJson())
                .linkedRestaurantName(linked != null ? linked.getName() : null)
                .lastLogin(u.getLastLogin())
                .createdAt(u.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public AdminUserDetailDto updateUser(Long userId, AdminUpdateUserRequestDto request) {
        Users u = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (request.getActive() != null) u.setActive(request.getActive());
        if (request.getVerified() != null) u.setVerified(request.getVerified());
        if (request.getRoles() != null) u.setRoles(ERole.valueOf(request.getRoles()));
        userRepository.save(u);
        return getUserDetail(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PendingRestaurantDto> getPendingRestaurants() {
        return restaurantRepository.findByIsActiveFalse().stream()
                .map(r -> PendingRestaurantDto.builder()
                        .restaurantId(r.getRestaurantId())
                        .name(r.getName())
                        .ownerUserId(r.getOwner().getUserId())
                        .ownerName(r.getOwner().getFullName())
                        .phone(r.getOwner().getPhone())
                        .restaurantMeta(r.getRestaurantMeta())
                        .submittedAt(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void approveRestaurant(Long restaurantId, String note) {
        Restaurant r = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        r.setIsActive(true);
        Map<String, Object> meta = r.getRestaurantMeta() != null ? new HashMap<>(r.getRestaurantMeta()) : new HashMap<>();
        meta.put("admin_note", note);
        r.setRestaurantMeta(meta);
        restaurantRepository.save(r);
    }

    @Override
    @Transactional
    public void rejectRestaurant(Long restaurantId, String reason) {
        Restaurant r = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        Map<String, Object> meta = r.getRestaurantMeta() != null ? new HashMap<>(r.getRestaurantMeta()) : new HashMap<>();
        meta.put("rejected_reason", reason);
        r.setRestaurantMeta(meta);
        restaurantRepository.save(r);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminOrderSummaryDto> listOrders(String status, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        return orderRepository.findAll(pageable).stream()
                .filter(o -> status == null || o.getStatus().name().equalsIgnoreCase(status))
                .map(o -> AdminOrderSummaryDto.builder()
                        .orderId(o.getOrderId())
                        .restaurantName(o.getRestaurant().getName())
                        .customerMaskedPhone(maskPhone(o.getUser().getPhone()))
                        .deliveryStaffName(o.getOrderId().toString())
                        .status(o.getStatus().name())
                        .amount(o.getTotalAmount())
                        .createdAt(o.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminOrderDetailDto getOrderDetail(Long orderId) {
        Order o = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return AdminOrderDetailDto.builder()
                .orderId(o.getOrderId())
                .userId(o.getUser().getUserId())
                .restaurantId(o.getRestaurant().getRestaurantId())
                .items(o.getItems() != null ? o.getItems().stream()
                        .map(i -> Map.<String, Object>ofEntries(
                                Map.entry("display_name", i.getItem().getDisplayName()),
                                Map.entry("qty", i.getQuantity())
                        ))
                        .collect(Collectors.toList()) : Collections.emptyList())
                .totals(Map.of("total", o.getTotalAmount()))
                .status(o.getStatus().name())
                .paymentMethod(o.getPaymentMethod().name())
                .paymentStatus(o.getPaymentStatus().name())
                .paymentTransactionRef(o.getPaymentTransactionRef())
                .paymentMeta(o.getPaymentMeta())
                .createdAt(o.getCreatedAt())
                .build();
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 3) return "****";
        return phone.charAt(0) + "*****" + phone.substring(phone.length() - 2);
    }
}
