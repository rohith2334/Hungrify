package com.app.demo.common.repository;

import com.app.demo.common.models.ERole;
import com.app.demo.common.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
