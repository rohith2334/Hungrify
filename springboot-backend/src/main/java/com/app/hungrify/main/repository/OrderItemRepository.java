package com.app.hungrify.main.repository;


import com.app.hungrify.main.models.Order;
import com.app.hungrify.main.models.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // popular dishes aggregated by item_id
    @Query("SELECT new map(oi.item.itemId as itemId, oi.item.displayName as displayName, SUM(oi.quantity) as timesOrdered, SUM(oi.unitPrice * oi.quantity) as revenue) " +
            "FROM OrderItem oi WHERE oi.item.restaurant.restaurantId = :restaurantId GROUP BY oi.item.itemId, oi.item.displayName ORDER BY timesOrdered DESC")
    List<java.util.Map<String, Object>> findPopularDishesByRestaurant(@Param("restaurantId") Long restaurantId, Pageable pageable);

    // hourly trend - we will compute in service by grouping results from a simple query
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.restaurant.restaurantId = :restaurantId AND oi.order.createdAt BETWEEN :from AND :to")
    List<OrderItem> findAllByRestaurantAndCreatedAtBetween(@Param("restaurantId") Long restaurantId, @Param("from") java.time.Instant from, @Param("to") java.time.Instant to);}
