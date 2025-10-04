package com.app.hungrify.common.security.services;


import com.app.hungrify.common.models.Users;
import com.app.hungrify.common.payload.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
   ResponseEntity<?> createProfile(SignupRequest signUpRequest, Users customer);


}
