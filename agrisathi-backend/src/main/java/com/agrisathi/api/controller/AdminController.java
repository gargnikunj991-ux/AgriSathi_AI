package com.agrisathi.api.controller;

import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.model.enums.Role;
import com.agrisathi.api.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin / Owner Management Controller (Restricted to ROLE_ADMIN / OWNER)
 * Provides master platform oversight and metrics while preserving user personal information privacy.
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin & Owner Management APIs", description = "Privileged system oversight endpoints for platform Administrators and Owners")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    @Operation(summary = "Admin: List Registered System Accounts", description = "Retrieves high-level overview of registered platform accounts.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    })
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("All users retrieved successfully", users));
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Admin: Platform System Dashboard Metrics", description = "Retrieves master system statistics and platform status metrics.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "System metrics retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAdminDashboard() {
        long totalUsers = userRepository.count();
        long farmerCount = userRepository.findAll().stream().filter(u -> u.getRole() == Role.ROLE_FARMER).count();
        long buyerCount = userRepository.findAll().stream().filter(u -> u.getRole() == Role.ROLE_BUYER).count();
        long adminCount = userRepository.findAll().stream().filter(u -> u.getRole() == Role.ROLE_ADMIN).count();

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalUsers", totalUsers);
        metrics.put("farmersCount", farmerCount);
        metrics.put("buyersCount", buyerCount);
        metrics.put("adminsCount", adminCount);
        metrics.put("status", "HEALTHY");

        return ResponseEntity.ok(ApiResponse.success("Admin dashboard metrics retrieved successfully", metrics));
    }
}
