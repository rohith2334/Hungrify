package com.app.hungrify.common.security.services;

import com.app.hungrify.common.models.Users;
import com.app.hungrify.common.payload.request.SignupRequest;
import com.app.hungrify.common.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service

public class UserServiceImpl implements UserService{

    private final UserRepository customerRepository;


    public UserServiceImpl(UserRepository customerRepository) {
        this.customerRepository = customerRepository;

    }


    @Override
    public ResponseEntity<?> createProfile(SignupRequest signUpRequest, Users customer) {
        try {

            customer.setUsername(signUpRequest.getUsername());
            customer.setEmail(signUpRequest.getEmail());
            customer.setProfileImage(signUpRequest.getProfileImage());

            Users _customer = customerRepository.save(customer);

            if (signUpRequest.getRole().equals("moderator")) {
                //implement
            }

            if (signUpRequest.getRole().equals("admin")) {
                //implement
            }
            return ResponseEntity.ok("Profile created successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error creating profile:");
        }
    }


}
