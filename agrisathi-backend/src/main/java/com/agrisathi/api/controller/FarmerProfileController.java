package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.FarmerProfileRequest;
import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.FarmerProfileResponse;
import com.agrisathi.api.model.entity.FarmerProfile;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.FarmerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/farmer/profile")
@RequiredArgsConstructor
@Tag(name = "Farmer Profile APIs", description = "Endpoints for managing farmer profile metadata (location, land size, soil type, primary crop)")
@SecurityRequirement(name = "bearerAuth")
public class FarmerProfileController {

    private final FarmerProfileService farmerProfileService;

    @GetMapping
    @Operation(summary = "Get Farmer Profile", description = "Fetches demographic and farming profile details for the authenticated farmer.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Farmer profile fetched successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Farmer profile not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<ApiResponse<FarmerProfileResponse>> getProfile(@AuthenticationPrincipal UserPrincipal currentUser) {
        FarmerProfile profile = farmerProfileService.getProfileByUserId(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", FarmerProfileResponse.fromEntity(profile)));
    }

    @PostMapping
    @Operation(summary = "Create Farmer Profile", description = "Creates a new farming profile for the authenticated user.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Profile created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failure or invalid input")
    })
    public ResponseEntity<ApiResponse<FarmerProfileResponse>> createProfile(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody FarmerProfileRequest request) {
        FarmerProfile profile = farmerProfileService.createOrUpdateProfile(currentUser.getId(), request);
        return new ResponseEntity<>(ApiResponse.success("Profile Created Successfully", FarmerProfileResponse.fromEntity(profile)), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Update Farmer Profile", description = "Updates state, district, village, farm size, soil type, or main crop for the authenticated farmer.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failure")
    })
    public ResponseEntity<ApiResponse<FarmerProfileResponse>> updateProfile(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody FarmerProfileRequest request) {
        FarmerProfile profile = farmerProfileService.createOrUpdateProfile(currentUser.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile Updated Successfully", FarmerProfileResponse.fromEntity(profile)));
    }

    @DeleteMapping
    @Operation(summary = "Delete Farmer Profile", description = "Deletes the authenticated user's farmer profile.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteProfile(@AuthenticationPrincipal UserPrincipal currentUser) {
        farmerProfileService.deleteProfileByUserId(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Profile Deleted Successfully"));
    }
}
