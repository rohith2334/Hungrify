package com.app.demo.common.security.services;


import com.app.demo.common.models.Users;
import com.app.demo.common.payload.request.SignupRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
   ResponseEntity<?> createProfile(SignupRequest signUpRequest, Users customer);


}
