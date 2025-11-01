package com.app.hungrify.main.repository;


import com.app.hungrify.common.models.Users;
import com.app.hungrify.main.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByCityIgnoreCase(String city);

    List<Restaurant> findByIsActiveTrue();

    List<Restaurant> findByOwner(Users owner);
}
