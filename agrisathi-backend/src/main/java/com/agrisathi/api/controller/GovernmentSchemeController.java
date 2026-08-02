package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.GovernmentSchemeRequest;
import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.GovernmentSchemeResponse;
import com.agrisathi.api.dto.response.SchemeRecommendationResponse;
import com.agrisathi.api.model.entity.GovernmentScheme;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.GovernmentSchemeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/government-schemes")
@RequiredArgsConstructor
public class GovernmentSchemeController {

    private final GovernmentSchemeService schemeService;

    /**
     * List and Filter Schemes by State and/or Crop
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<GovernmentSchemeResponse>>> getSchemes(
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String crop) {
        List<GovernmentSchemeResponse> schemes = schemeService.getSchemes(state, crop).stream()
                .map(GovernmentSchemeResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Government schemes retrieved successfully", schemes));
    }

    /**
     * Get Single Scheme by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GovernmentSchemeResponse>> getSchemeById(@PathVariable Long id) {
        GovernmentScheme scheme = schemeService.getSchemeById(id);
        return ResponseEntity.ok(ApiResponse.success("Government scheme retrieved successfully", GovernmentSchemeResponse.fromEntity(scheme)));
    }

    /**
     * Recommend Schemes tailored to Farmer Profile / Context
     */
    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponse<List<SchemeRecommendationResponse>>> recommendSchemes(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String crop,
            @RequestParam(required = false) Double farmSize) {
        Long userId = (currentUser != null) ? currentUser.getId() : null;
        List<SchemeRecommendationResponse> recommendations = schemeService.recommendSchemes(userId, state, crop, farmSize);
        return ResponseEntity.ok(ApiResponse.success("Government scheme recommendations retrieved successfully", recommendations));
    }

    /**
     * Admin: Create New Government Scheme
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GovernmentSchemeResponse>> createScheme(@Valid @RequestBody GovernmentSchemeRequest request) {
        GovernmentScheme created = schemeService.createScheme(request);
        return new ResponseEntity<>(ApiResponse.success("Government scheme created successfully", GovernmentSchemeResponse.fromEntity(created)), HttpStatus.CREATED);
    }

    /**
     * Admin: Update Government Scheme
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GovernmentSchemeResponse>> updateScheme(
            @PathVariable Long id,
            @Valid @RequestBody GovernmentSchemeRequest request) {
        GovernmentScheme updated = schemeService.updateScheme(id, request);
        return ResponseEntity.ok(ApiResponse.success("Government scheme updated successfully", GovernmentSchemeResponse.fromEntity(updated)));
    }

    /**
     * Admin: Soft Delete / Deactivate Scheme
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteScheme(@PathVariable Long id) {
        schemeService.deleteScheme(id);
        return ResponseEntity.ok(ApiResponse.success("Government scheme deactivated successfully"));
    }
}
