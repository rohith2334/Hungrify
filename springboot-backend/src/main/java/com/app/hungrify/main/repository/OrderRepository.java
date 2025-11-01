package com.app.hungrify.main.repository;

import com.app.hungrify.common.models.Users;
import com.app.hungrify.main.models.Order;
import com.app.hungrify.main.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(Users user);

    List<Order> findByRestaurant(Restaurant restaurant);

    List<Order> findByStatus(Order.OrderStatus status);

    List<Order> findByUserAndStatus(Users user, Order.OrderStatus status);
}
