package com.app.hungrify.common.service;

import com.app.hungrify.common.models.ERole;
import com.app.hungrify.common.models.Users;
import com.app.hungrify.common.payload.request.LoginRequest;
import com.app.hungrify.common.payload.request.SignupRequest;
import com.app.hungrify.common.payload.response.MessageResponse;
import com.app.hungrify.common.payload.response.UserInfoResponse;
import com.app.hungrify.common.repository.UserRepository;
import com.app.hungrify.common.security.jwt.JwtUtils;
import com.app.hungrify.common.security.services.UserDetailsImpl;
import com.app.hungrify.common.security.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
//    private final UserRepository userRepository;
    //    private final RoleRepository roleRepository;
    private final UserRepository customerRepository;
    private final PasswordEncoder encoder;

    private final UserService userService;

    AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository customerRepository, PasswordEncoder encoder, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.customerRepository = customerRepository;
//        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {


        if (customerRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (customerRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Users customer = new Users(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = Set.of(signUpRequest.getRole());
        AtomicReference<ERole> final_role = new AtomicReference<>(ERole.ROLE_USER);

        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    final_role.set(ERole.ROLE_ADMIN);

                    break;
                case "delivery":
                    final_role.set(ERole.ROLE_DELIVERY_AGENT);

                case "restaurant":
                    final_role.set(ERole.ROLE_RESTAURANT);
                    break;

                default:
                    final_role.set(ERole.ROLE_USER);
            }
        });

        customer.setRoles(final_role.get());
//        var savedUser = userRepository.save(user);
        //TODO : Create Profile
        userService.createProfile(signUpRequest,  customer);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));


    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Users customer = customerRepository.findByEmail(loginRequest.getEmail()).get();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        System.out.println(jwtCookie);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.ok().header("X-JWT-Token", jwtCookie.toString())
                .header("Access-Control-Expose-Headers", "X-JWT-Token")
                .body(new UserInfoResponse(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles.get(0),customer.isVerified()));
    }
}
