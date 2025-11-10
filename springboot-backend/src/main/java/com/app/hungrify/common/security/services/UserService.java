package com.app.hungrify.common.security.services;


import com.app.hungrify.common.models.Users;
import com.app.hungrify.common.payload.request.SignupRequest;
import com.app.hungrify.main.dto.user.AddAddressRequestDto;
import com.app.hungrify.main.dto.user.UpdateUserRequestDto;
import com.app.hungrify.main.dto.user.UserProfileDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
   ResponseEntity<?> createProfile(SignupRequest signUpRequest, Users customer);

   UserProfileDto getCurrentUserProfile(Long maybeUserIdFromParam);
   UserProfileDto updateCurrentUser(Long maybeUserIdFromParam, UpdateUserRequestDto request);
   String addAddress(Long maybeUserIdFromParam, AddAddressRequestDto request);
   void deleteAddress(Long maybeUserIdFromParam, String addressId);

}
