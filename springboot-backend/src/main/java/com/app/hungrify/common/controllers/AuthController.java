package com.app.hungrify.common.controllers;


import com.app.hungrify.common.payload.request.LoginRequest;
import com.app.hungrify.common.payload.request.SignupRequest;
import com.app.hungrify.common.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

  private  final AuthService authService;

  AuthController(AuthService authService){
    this.authService=authService;
  }


  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    return authService.authenticateUser(loginRequest);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    return  authService.registerUser(signUpRequest);
  }
}
