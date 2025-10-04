package com.app.hungrify.common.service;

import com.app.hungrify.common.payload.request.LoginRequest;
import com.app.hungrify.common.payload.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> registerUser(SignupRequest signUpRequest) ;

    ResponseEntity<?> authenticateUser( LoginRequest loginRequest);
}
