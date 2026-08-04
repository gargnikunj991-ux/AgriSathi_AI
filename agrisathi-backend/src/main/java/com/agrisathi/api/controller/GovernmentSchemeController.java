package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.GovernmentSchemeRequest;
import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.GovernmentSchemeResponse;
import com.agrisathi.api.dto.response.SchemeRecommendationResponse;
import com.agrisathi.api.model.entity.GovernmentScheme;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.GovernmentSchemeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Government Schemes APIs", description = "Endpoints for discovering state & national agricultural subsidies, financial support programs, and AI scheme matching")
public class GovernmentSchemeController {

    private final GovernmentSchemeService schemeService;

    @GetMapping
    @Operation(summary = "Get & Filter Government Schemes", description = "Retrieves active government agricultural schemes filtered by state and/or target crop.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Government schemes retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<List<GovernmentSchemeResponse>>> getSchemes(
            @Parameter(description = "Filter by state (e.g. Uttarakhand, Punjab, All India)", example = "Uttarakhand") @RequestParam(required = false) String state,
            @Parameter(description = "Filter by target crop (e.g. Rice, Wheat, Horticulture)", example = "Rice") @RequestParam(required = false) String crop) {
        List<GovernmentSchemeResponse> schemes = schemeService.getSchemes(state, crop).stream()
                .map(GovernmentSchemeResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Government schemes retrieved successfully", schemes));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Scheme by ID", description = "Retrieves full eligibility criteria, benefit amounts, and application links for a specific scheme.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Government scheme retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Scheme not found")
    })
    public ResponseEntity<ApiResponse<GovernmentSchemeResponse>> getSchemeById(
            @Parameter(description = "Scheme ID", example = "1") @PathVariable Long id) {
        GovernmentScheme scheme = schemeService.getSchemeById(id);
        return ResponseEntity.ok(ApiResponse.success("Government scheme retrieved successfully", GovernmentSchemeResponse.fromEntity(scheme)));
    }

    @GetMapping("/recommendations")
    @Operation(summary = "Recommend Relevant Schemes for Farmer", description = "Calculates match scores (0-100) and eligibility rationale for government schemes based on farmer profile and location.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Government scheme recommendations retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<SchemeRecommendationResponse>>> recommendSchemes(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Parameter(description = "State location override", example = "Uttarakhand") @RequestParam(required = false) String state,
            @Parameter(description = "Target crop override", example = "Rice") @RequestParam(required = false) String crop,
            @Parameter(description = "Farm size in acres override", example = "2.5") @RequestParam(required = false) Double farmSize) {
        Long userId = (currentUser != null) ? currentUser.getId() : null;
        List<SchemeRecommendationResponse> recommendations = schemeService.recommendSchemes(userId, state, crop, farmSize);
        return ResponseEntity.ok(ApiResponse.success("Government scheme recommendations retrieved successfully", recommendations));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: Create Government Scheme", description = "Adds a new government subsidy or insurance scheme to the platform catalog (Admin only).",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Government scheme created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    })
    public ResponseEntity<ApiResponse<GovernmentSchemeResponse>> createScheme(@Valid @RequestBody GovernmentSchemeRequest request) {
        GovernmentScheme created = schemeService.createScheme(request);
        return new ResponseEntity<>(ApiResponse.success("Government scheme created successfully", GovernmentSchemeResponse.fromEntity(created)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: Update Government Scheme", description = "Updates details of an existing scheme catalog entry (Admin only).",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Government scheme updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Scheme not found")
    })
    public ResponseEntity<ApiResponse<GovernmentSchemeResponse>> updateScheme(
            @Parameter(description = "Scheme ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody GovernmentSchemeRequest request) {
        GovernmentScheme updated = schemeService.updateScheme(id, request);
        return ResponseEntity.ok(ApiResponse.success("Government scheme updated successfully", GovernmentSchemeResponse.fromEntity(updated)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin: Deactivate Scheme", description = "Deactivates/soft deletes a scheme from the public catalog (Admin only).",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Government scheme deactivated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Scheme not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteScheme(
            @Parameter(description = "Scheme ID", example = "1") @PathVariable Long id) {
        schemeService.deleteScheme(id);
        return ResponseEntity.ok(ApiResponse.success("Government scheme deactivated successfully"));
    }
}
