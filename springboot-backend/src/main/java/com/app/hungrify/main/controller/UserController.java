package com.app.hungrify.main.controller;

import com.app.hungrify.common.security.services.UserService;
import com.app.hungrify.main.dto.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Map;

/**
 * UserController - profile CRUD, addresses, preferences.
 *
 * Assumption: if a query param 'userId' is supplied it will be used (helpful for local testing).
 * Otherwise the controller attempts to resolve the authenticated user from SecurityContext.
 */
@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
@Tag(name = "Users", description = "Profile, addresses and preferences")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get current authenticated user's profile")
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getMe(@RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(userService.getCurrentUserProfile(userId));
    }

    @Operation(summary = "Update current authenticated user's profile")
    @PutMapping("/me")
    public ResponseEntity<UserProfileDto> updateMe(@RequestParam(required = false) Long userId,
                                                   @Valid @RequestBody UpdateUserRequestDto request) {
        return ResponseEntity.ok(userService.updateCurrentUser(userId, request));
    }

    @Operation(summary = "Add address to current user's profile")
    @PostMapping("/me/addresses")
    public ResponseEntity<Map<String,String>> addAddress(@RequestParam(required = false) Long userId,
                                                         @Valid @RequestBody AddAddressRequestDto request) {
        String id = userService.addAddress(userId, request);
        return ResponseEntity.ok(Map.of("status", "ok", "address_id", id));
    }

    @Operation(summary = "Delete address by id")
    @DeleteMapping("/me/addresses/{addressId}")
    public ResponseEntity<GenericResponseDto> deleteAddress(@RequestParam(required = false) Long userId,
                                                            @PathVariable String addressId) {
        userService.deleteAddress(userId, addressId);
        return ResponseEntity.ok(GenericResponseDto.builder().status("ok").message("address deleted").build());
    }
}