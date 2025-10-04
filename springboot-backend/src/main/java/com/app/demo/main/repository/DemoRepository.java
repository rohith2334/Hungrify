package com.app.demo.main.repository;


import com.app.demo.common.models.ERole;
import com.app.demo.common.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DemoRepository extends MongoRepository<Role, String> {
        Optional<Role> findByName(ERole name);
        }

