package com.agrisathi.api.controller;

import com.agrisathi.api.dto.request.LoginRequest;
import com.agrisathi.api.dto.request.RegisterRequest;
import com.agrisathi.api.dto.response.ApiResponse;
import com.agrisathi.api.dto.response.AuthTokenResponse;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.security.UserPrincipal;
import com.agrisathi.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.registerUser(registerRequest);
        return new ResponseEntity<>(
                ApiResponse.success("User registered successfully"),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthTokenResponse tokenResponse = authService.loginUser(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successful", tokenResponse));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
        User user = authService.getCurrentUser(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("User details fetched successfully", user));
    }
}
