package com.app.hungrify.main.repository;

import com.app.hungrify.common.models.Users;
import com.app.hungrify.main.models.Order;
import com.app.hungrify.main.models.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // counts and sums for KPIs by restaurant and date range
    @Query("SELECT COUNT(o) FROM Order o WHERE o.restaurant.restaurantId = :restaurantId AND o.createdAt BETWEEN :from AND :to")
    Long countByRestaurantAndCreatedAtBetween(@Param("restaurantId") Long restaurantId, @Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.restaurant.restaurantId = :restaurantId AND o.createdAt BETWEEN :from AND :to")
    BigDecimal sumTotalAmountByRestaurantAndCreatedAtBetween(@Param("restaurantId") Long restaurantId, @Param("from") Instant from, @Param("to") Instant to);

    // pending orders count
    @Query("SELECT COUNT(o) FROM Order o WHERE o.restaurant.restaurantId = :restaurantId AND o.status = :status AND o.createdAt BETWEEN :from AND :to")
    Long countByRestaurantAndStatusBetween(@Param("restaurantId") Long restaurantId, @Param("status") Order.OrderStatus status, @Param("from") Instant from, @Param("to") Instant to);

    // cancellation spike count (last X minutes)
    @Query("SELECT COUNT(o) FROM Order o WHERE o.restaurant.restaurantId = :restaurantId AND o.status = com.app.hungrify.main.models.Order$OrderStatus.cancelled AND o.updatedAt >= :since")
    Long countCancellationsSince(@Param("restaurantId") Long restaurantId, @Param("since") Instant since);

    // recent orders small list
    @Query("SELECT o FROM Order o WHERE o.restaurant.restaurantId = :restaurantId ORDER BY o.createdAt DESC")
    Page<Order> findRecentByRestaurant(@Param("restaurantId") Long restaurantId, Pageable pageable);
}
