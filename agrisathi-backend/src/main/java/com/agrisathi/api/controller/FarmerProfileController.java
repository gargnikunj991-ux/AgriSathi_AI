package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.FarmerProfileRequest;
import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.FarmerProfileResponse;
import com.agrisathi.api.model.entity.FarmerProfile;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.FarmerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/farmer/profile")
@RequiredArgsConstructor
public class FarmerProfileController {

    private final FarmerProfileService farmerProfileService;

    @GetMapping
    public ResponseEntity<ApiResponse<FarmerProfileResponse>> getProfile(@AuthenticationPrincipal UserPrincipal currentUser) {
        FarmerProfile profile = farmerProfileService.getProfileByUserId(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", FarmerProfileResponse.fromEntity(profile)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FarmerProfileResponse>> createProfile(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody FarmerProfileRequest request) {
        FarmerProfile profile = farmerProfileService.createOrUpdateProfile(currentUser.getId(), request);
        return new ResponseEntity<>(ApiResponse.success("Profile Created Successfully", FarmerProfileResponse.fromEntity(profile)), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<FarmerProfileResponse>> updateProfile(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody FarmerProfileRequest request) {
        FarmerProfile profile = farmerProfileService.createOrUpdateProfile(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile Updated Successfully", FarmerProfileResponse.fromEntity(profile)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteProfile(@AuthenticationPrincipal UserPrincipal currentUser) {
        farmerProfileService.deleteProfileByUserId(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Profile Deleted Successfully"));
    }
}
