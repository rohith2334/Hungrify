package com.app.hungrify.main.repository;


import com.app.hungrify.common.models.Users;
import com.app.hungrify.main.models.Delivery;
import com.app.hungrify.main.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByOrder(Order order);

    List<Delivery> findByPartnerUser(Users partner);

    List<Delivery> findByStatus(Delivery.DeliveryStatus status);

    Optional<Delivery> findByOrderOrderId(Long orderId);
}
