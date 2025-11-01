package com.app.hungrify.main.repository;


import com.app.hungrify.main.models.Order;
import com.app.hungrify.main.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
}
