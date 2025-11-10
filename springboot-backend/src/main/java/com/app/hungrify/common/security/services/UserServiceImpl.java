package com.app.hungrify.common.security.services;

import com.app.hungrify.common.models.Users;
import com.app.hungrify.common.payload.request.SignupRequest;
import com.app.hungrify.common.repository.UserRepository;
import com.app.hungrify.main.dto.user.AddAddressRequestDto;
import com.app.hungrify.main.dto.user.UpdateUserRequestDto;
import com.app.hungrify.main.dto.user.UserProfileDto;
import com.app.hungrify.main.exception.BadRequestException;
import com.app.hungrify.main.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service

public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;



    public UserServiceImpl(UserRepository customerRepository) {
        this.userRepository = customerRepository;

    }


    @Override
    public ResponseEntity<?> createProfile(SignupRequest signUpRequest, Users customer) {
        try {

            customer.setUsername(signUpRequest.getUsername());
            customer.setEmail(signUpRequest.getEmail());
            customer.setProfileImage(signUpRequest.getProfileImage());

            Users _customer = userRepository.save(customer);

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

    /**
     * Try to resolve the effective user id:
     * - If maybeUserIdFromParam != null => use it (for tests)
     * - Else try to resolve from SecurityContextHolder (assumption)
     */
    private Long resolveUserId(Long maybeUserIdFromParam) {
        if (maybeUserIdFromParam != null) return maybeUserIdFromParam;
        // Attempt to read from SecurityContext (assumption about principal storing user id).
        try {
            Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                // by username we find user and return id
                String username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
                Users u = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Authenticated user not found"));
                return u.getUserId();
            } else if (principal instanceof String) {
                String username = (String) principal;
                Users u = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Authenticated user not found"));
                return u.getUserId();
            }
        } catch (Exception ex) {
            // fall through
        }
        throw new BadRequestException("User not authenticated and no userId provided");
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUserProfile(Long maybeUserIdFromParam) {
        Long userId = resolveUserId(maybeUserIdFromParam);
        Users u = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return mapToDto(u);
    }

    @Override
    @Transactional
    public UserProfileDto updateCurrentUser(Long maybeUserIdFromParam, UpdateUserRequestDto request) {
        Long userId = resolveUserId(maybeUserIdFromParam);
        Users u = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        if (request.getFirstName() != null) u.setFirstName(request.getFirstName());
        if (request.getLastName() != null) u.setLastName(request.getLastName());
        if (request.getEmail() != null) u.setEmail(request.getEmail());
        if (request.getPhone() != null) u.setPhone(request.getPhone());
        if (request.getProfileImage() != null) u.setProfileImage(request.getProfileImage());

        // Merge or replace profileJson depending on request; here we'll merge top-level keys.
        Map<String, Object> existing = u.getProfileJson() != null ? new HashMap<>(u.getProfileJson()) : new HashMap<>();
        if (request.getProfileJson() != null) {
            // shallow merge: request values replace existing keys
            existing.putAll(request.getProfileJson());
            u.setProfileJson(existing);
        }

        Users saved = userRepository.save(u);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public String addAddress(Long maybeUserIdFromParam, AddAddressRequestDto request) {
        Long userId = resolveUserId(maybeUserIdFromParam);
        Users u = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        Map<String, Object> profile = u.getProfileJson() != null ? new HashMap<>(u.getProfileJson()) : new HashMap<>();
        List<Map<String, Object>> addresses = profile.containsKey("addresses") ?
                new ArrayList<>((List<Map<String,Object>>) profile.get("addresses")) : new ArrayList<>();

        // generate id
        String id = "addr_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Map<String, Object> addr = new HashMap<>();
        addr.put("id", id);
        addr.put("label", request.getLabel());
        addr.put("line1", request.getLine1());
        addr.put("line2", request.getLine2());
        addr.put("city", request.getCity());
        addr.put("state", request.getState());
        addr.put("postalCode", request.getPostalCode());
        addr.put("country", request.getCountry());
        addr.put("lat", request.getLat());
        addr.put("lon", request.getLon());
        addr.put("phone", request.getPhone());

        addresses.add(addr);
        profile.put("addresses", addresses);
        u.setProfileJson(profile);
        userRepository.save(u);
        return id;
    }

    @Override
    @Transactional
    public void deleteAddress(Long maybeUserIdFromParam, String addressId) {
        Long userId = resolveUserId(maybeUserIdFromParam);
        Users u = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Map<String, Object> profile = u.getProfileJson() != null ? new HashMap<>(u.getProfileJson()) : new HashMap<>();
        if (!profile.containsKey("addresses")) {
            throw new NotFoundException("Address not found");
        }
        List<Map<String, Object>> addresses = new ArrayList<>((List<Map<String,Object>>) profile.get("addresses"));
        boolean removed = addresses.removeIf(a -> addressId.equals(String.valueOf(a.get("id"))));
        if (!removed) throw new NotFoundException("Address not found");
        profile.put("addresses", addresses);
        u.setProfileJson(profile);
        userRepository.save(u);
    }

    private UserProfileDto mapToDto(Users u) {
        return UserProfileDto.builder()
                .userId(u.getUserId())
                .username(u.getUsername())
                .email(u.getEmail())
                .phone(u.getPhone())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .fullName(u.getFullName())
                .verified(u.getVerified())
                .active(u.getActive())
                .profileImage(u.getProfileImage())
                .profileJson(u.getProfileJson())
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .build();
    }

}
