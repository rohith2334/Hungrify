package com.app.hungrify.main.repository;


import com.app.hungrify.main.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}

