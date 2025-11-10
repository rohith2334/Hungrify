package com.app.hungrify.main.repository;


import com.app.hungrify.common.models.Users;
import com.app.hungrify.main.models.Delivery;
import com.app.hungrify.main.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    @Query("SELECT d FROM Delivery d WHERE d.order.restaurant.restaurantId = :restaurantId AND d.status IN :statuses")
    List<Delivery> findByRestaurantAndStatuses(@Param("restaurantId") Long restaurantId, @Param("statuses") List<Delivery.DeliveryStatus> statuses);

    // pending pickups count
    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.order.restaurant.restaurantId = :restaurantId AND d.status = com.app.hungrify.main.models.Delivery$DeliveryStatus.assigned")
    Long countAssignedByRestaurant(@Param("restaurantId") Long restaurantId);

    List<Delivery> findByPartnerUser_UserIdAndStatusIn(Long userId, List<Delivery.DeliveryStatus> statuses);
    List<Delivery> findByPartnerUser_UserIdAndStatus(Long userId, Delivery.DeliveryStatus status);
}
