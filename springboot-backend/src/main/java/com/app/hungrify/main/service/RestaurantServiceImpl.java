package com.app.hungrify.main.service;


import com.app.hungrify.main.dto.resturant.*;
import com.app.hungrify.main.exception.NotFoundException;
import com.app.hungrify.main.models.*;
import com.app.hungrify.main.repository.*;
import com.app.hungrify.main.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;
import java.util.*;
import java.util.stream.*;
import java.time.*;
import java.math.BigDecimal;

/**
 * Implementation using repositories only (no other services).
 */
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final FoodItemRepository foodItemRepository;
    private final DeliveryRepository deliveryRepository;
    private final CommonUtils commonUtils;

    @Override
    public PagedRestaurantsResponseDto discoverRestaurants(String city, String cuisine, int page, int limit) {
        if ((city == null || city.isEmpty()) && (cuisine == null || cuisine.isEmpty())) {
            // randomized sample
            List<Restaurant> sample = restaurantRepository.findRandomActiveRestaurants(limit);
            List<RestaurantSummaryDto> items = sample.stream().map(this::toSummary).collect(Collectors.toList());
            return PagedRestaurantsResponseDto.builder().page(1).limit(limit).totalEstimate((long) items.size()).items(items).build();
        } else {
            Pageable pageable = PageRequest.of(Math.max(0, page-1), limit);
            Page<Restaurant> p = restaurantRepository.findByCityAndCuisine(city, cuisine, pageable);
            List<RestaurantSummaryDto> items = p.getContent().stream().map(this::toSummary).collect(Collectors.toList());
            return PagedRestaurantsResponseDto.builder().page(page).limit(limit).totalEstimate(p.getTotalElements()).items(items).build();
        }
    }

    @Override
    public RestaurantDetailDto getRestaurantDetails(Long restaurantId) {
        Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException("Restaurant not found: " + restaurantId));
        return toDetail(r);
    }

    @Override
    public RestaurantDashboardResponseDto getDashboard(String dateFrom, String dateTo, int limitPopular) {
        // parse dates or set defaults
        LocalDate fromDate = (dateFrom == null) ? LocalDate.now().minusDays(7) : LocalDate.parse(dateFrom);
        LocalDate toDate = (dateTo == null) ? LocalDate.now() : LocalDate.parse(dateTo);
        Instant from = fromDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant to = toDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Long restaurantId= getRestaurantByUserId().getRestaurantId();
        // KPIs
        Long todayOrders = orderRepository.countByRestaurantAndCreatedAtBetween(restaurantId, from, to);
        BigDecimal periodRevenue = orderRepository.sumTotalAmountByRestaurantAndCreatedAtBetween(restaurantId, from, to);
        if (periodRevenue == null) periodRevenue = BigDecimal.ZERO;

        // average order value (safe)
        BigDecimal avgOrderValue = BigDecimal.ZERO;
        if (todayOrders != null && todayOrders > 0 && periodRevenue != null) {
            avgOrderValue = periodRevenue.divide(BigDecimal.valueOf(Math.max(1L, todayOrders)), 2, BigDecimal.ROUND_HALF_UP);
        }

        // pending orders (status = pending or confirmed)
        Long pending = orderRepository.countByRestaurantAndStatusBetween(restaurantId, Order.OrderStatus.pending, from, to);

        // cancellations
        Long cancellations = orderRepository.countCancellationsSince(restaurantId, from);

        // average prep time and on time rate - simplistic (mocked): fetch some deliveries
        List<Delivery> deliveries = deliveryRepository.findByRestaurantAndStatuses(restaurantId, Arrays.asList(Delivery.DeliveryStatus.assigned, Delivery.DeliveryStatus.picked_up, Delivery.DeliveryStatus.delivered));
        double avgPrep = deliveries.stream().mapToInt(d -> d.getEstimatedTimeMinutes() == null ? 0 : d.getEstimatedTimeMinutes()).average().orElse(0.0);
        double onTimePct = 90.0; // placeholder: computing requires delivery timestamps - set to a reasonable default; can be refined.

        DashboardKpiDto kpi = DashboardKpiDto.builder()
                .todayRevenue(periodRevenue)
                .periodRevenue(periodRevenue)
                .todayOrders(todayOrders != null ? todayOrders.intValue() : 0)
                .periodOrders(todayOrders != null ? todayOrders.intValue() : 0)
                .avgOrderValue(avgOrderValue)
                .pendingOrdersCount(pending != null ? pending.intValue() : 0)
                .cancellationsCount(cancellations != null ? cancellations.intValue() : 0)
                .averagePrepTimeMinutes(avgPrep)
                .onTimeRatePct(onTimePct)
                .build();

        // current orders (take top 5)
        Page<Order> recentOrdersPage = orderRepository.findRecentByRestaurant(restaurantId, PageRequest.of(0,5));
        List<DashboardCurrentOrderDto> currentOrders = recentOrdersPage.getContent().stream().map(o ->
                DashboardCurrentOrderDto.builder()
                        .orderId(o.getOrderId())
                        .status(o.getStatus().name())
                        .createdAt(o.getCreatedAt())
                        .estimatedReadyInMinutes( (o.getCreatedAt() == null) ? null : 20 ) // placeholder
                        .orderItemsCount(o.getItems() == null ? 0 : o.getItems().size())
                        .totalAmount(o.getTotalAmount())
                        .customer(Map.of("user_id", o.getUser().getUserId(), "masked_phone", maskPhone(o.getUser().getPhone()), "last_order_count", 1))
                        .build()).collect(Collectors.toList());

        // popular dishes
        List<java.util.Map<String, Object>> popularRaw = orderItemRepository.findPopularDishesByRestaurant(restaurantId, PageRequest.of(0, limitPopular));
        List<PopularDishDto> popularDishes = popularRaw.stream().map(map -> PopularDishDto.builder()
                .itemId(((Number)map.get("itemId")).longValue())
                .displayName((String)map.get("displayName"))
                .timesOrdered(((Number)map.get("timesOrdered")).intValue())
                .revenue((BigDecimal) map.getOrDefault("revenue", BigDecimal.ZERO))
                .imageUrls(Collections.emptyList())
                .build()).collect(Collectors.toList());

        // inventory low: fetch available items and check thresholds locally
        List<FoodItem> available = foodItemRepository.findAvailableByRestaurant(restaurantId);
        List<InventoryLowDto> inventoryLow = available.stream()
                .filter(fi -> {
                    Integer q = fi.getQuantity() == null ? 0 : fi.getQuantity();
                    Integer threshold =  (fi.getPrepTimeMinutes() == null) ? 0 : 0; // placeholder; schema uses threshold in profile? We check quantity <= 5 as fallback
                    return q <= 5;
                })
                .map(fi -> InventoryLowDto.builder()
                        .itemId(fi.getItemId())
                        .displayName(fi.getDisplayName())
                        .quantity(fi.getQuantity())
                        .threshold(5)
                        .isTracked(Boolean.TRUE)
                        .build()).collect(Collectors.toList());

        // hourly trend: compute rough buckets from order items
        List<OrderItem> oiList = orderItemRepository.findAllByRestaurantAndCreatedAtBetween(restaurantId, from, to);
        Map<Integer, HourlyTrendDto> hourBucket = new TreeMap<>();
        for (OrderItem oi : oiList) {
            Instant ts = oi.getOrder().getCreatedAt();
            int hour = ts.atZone(ZoneOffset.UTC).getHour();
            String hourStr = String.format("%02d:00", hour);
            HourlyTrendDto h = hourBucket.computeIfAbsent(hour, k -> new HourlyTrendDto(hourStr, 0, BigDecimal.ZERO));
            h.setOrders(h.getOrders() + oi.getQuantity());
            BigDecimal add = oi.getUnitPrice() == null ? BigDecimal.ZERO : oi.getUnitPrice().multiply(BigDecimal.valueOf(oi.getQuantity()));
            h.setRevenue(h.getRevenue().add(add));
        }
        List<HourlyTrendDto> hourlyTrend = new ArrayList<>(hourBucket.values());

        // top modifiers - cannot easily compute without modifiers table; return empty or placeholders
        List<TopModifierDto> topModifiers = Collections.emptyList();

        QuickActionsDto quickActions = QuickActionsDto.builder()
                .unconfirmedOrders(kpi.getPendingOrdersCount())
                .itemsOutOfStock(inventoryLow.size())
                .pendingPickups(deliveryRepository.countAssignedByRestaurant(restaurantId).intValue())
                .build();

        return RestaurantDashboardResponseDto.builder()
                .restaurantId(restaurantId)
                .dateFrom(fromDate.toString())
                .dateTo(toDate.toString())
                .kpis(kpi)
                .currentOrders(currentOrders)
                .recentOrders(currentOrders)
                .popularDishes(popularDishes)
                .inventoryLow(inventoryLow)
                .hourlyTrend(hourlyTrend)
                .topModifiers(topModifiers)
                .quickActions(quickActions)
                .build();
    }

    @Override
    public List<AlertDto> getAlerts() {
        Long restaurantId = getRestaurantByUserId().getRestaurantId();
        List<AlertDto> alerts = new ArrayList<>();

        // low stock alerts
        List<FoodItem> available = foodItemRepository.findAvailableByRestaurant(restaurantId);
        for (FoodItem f : available) {
            Integer q = f.getQuantity() == null ? 0 : f.getQuantity();
            if (q <= 5) {
                alerts.add(AlertDto.builder().type("OUT_OF_STOCK").message(f.getDisplayName() + " low stock: " + q).createdAt(Instant.now()).build());
            }
        }
        // cancellation spike: last hour
        Instant since = Instant.now().minus(Duration.ofHours(1));
        Long cancelled = orderRepository.countCancellationsSince(restaurantId, since);
        if (cancelled != null && cancelled > 5) {
            alerts.add(AlertDto.builder().type("HIGH_CANCELLATION").message("Cancellation spike in last hour").createdAt(Instant.now()).build());
        }
        return alerts;
    }

    @Override
    @Transactional
    public RestaurantDetailDto updateProfile(UpdateProfileRequestDto update) {
        Long restaurantId = getRestaurantByUserId().getRestaurantId();
        Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException("Restaurant not found: " + restaurantId));
        if (update.getName() != null) r.setName(update.getName());
        if (update.getAddress() != null) r.setAddress(update.getAddress());
        if (update.getCity() != null) r.setCity(update.getCity());
        if (update.getState() != null) r.setState(update.getState());
        if (update.getPostalCode() != null) r.setPostalCode(update.getPostalCode());
        if (update.getIsActive() != null) r.setIsActive(update.getIsActive());
        if (update.getRestaurantMeta() != null) r.setRestaurantMeta(update.getRestaurantMeta());
        Restaurant saved = restaurantRepository.save(r);
        return toDetail(saved);
    }

    // helper mappers
    private RestaurantSummaryDto toSummary(Restaurant r) {
        return RestaurantSummaryDto.builder()
                .restaurantId(r.getRestaurantId())
                .name(r.getName())
                .cuisine(r.getCuisine())
                .city(r.getCity())
                .restaurantMeta(r.getRestaurantMeta())
                .rating(r.getRestaurantMeta() != null && r.getRestaurantMeta().containsKey("average_rating") ? new BigDecimal(r.getRestaurantMeta().get("average_rating").toString()) : BigDecimal.ZERO)
                .isActive(r.getIsActive())
                .build();
    }

    private RestaurantDetailDto toDetail(Restaurant r) {
        return RestaurantDetailDto.builder()
                .restaurantId(r.getRestaurantId())
                .name(r.getName())
                .ownerUserId(r.getOwner() == null ? null : r.getOwner().getUserId())
                .cuisine(r.getCuisine())
                .address(r.getAddress())
                .city(r.getCity())
                .state(r.getState())
                .postalCode(r.getPostalCode())
                .latitude(r.getLatitude())
                .longitude(r.getLongitude())
                .restaurantMeta(r.getRestaurantMeta())
                .isActive(r.getIsActive())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }

    private String maskPhone(String phone) {
        if (phone == null) return null;
        if (phone.length() <= 4) return "****";
        return phone.charAt(0) + "*****" + phone.substring(Math.max(1, phone.length()-2));
    }

    private Restaurant getRestaurantByUserId() {
        Long userId = commonUtils.getUserId();
        return restaurantRepository.findByOwner_UserId(userId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found for user: " + userId));
    }
}
