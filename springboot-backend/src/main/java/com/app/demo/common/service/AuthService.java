package com.app.demo.common.service;

import com.app.demo.common.payload.request.LoginRequest;
import com.app.demo.common.payload.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> registerUser(SignupRequest signUpRequest) ;

    ResponseEntity<?> authenticateUser( LoginRequest loginRequest);
}
