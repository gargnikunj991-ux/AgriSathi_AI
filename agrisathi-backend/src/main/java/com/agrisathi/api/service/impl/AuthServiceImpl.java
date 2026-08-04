package com.agrisathi.api.service.impl;

import com.agrisathi.api.dto.request.LoginRequest;
import com.agrisathi.api.dto.request.RegisterRequest;
import com.agrisathi.api.dto.response.AuthTokenResponse;
import com.agrisathi.api.exception.BadRequestException;
import com.agrisathi.api.exception.ResourceNotFoundException;
import com.agrisathi.api.model.entity.User;
import com.agrisathi.api.model.enums.Role;
import com.agrisathi.api.repository.UserRepository;
import com.agrisathi.api.security.JwtTokenProvider;
import com.agrisathi.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public void registerUser(RegisterRequest registerRequest) {
        log.info("[AUTH_REGISTER_ATTEMPT] Registration attempt for email: {}, role requested: '{}'",
                registerRequest.getEmail(), registerRequest.getRole());

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("[AUTH_REGISTER_FAILED] Duplicate email registration attempt: {}", registerRequest.getEmail());
            throw new BadRequestException("Email is already registered!");
        }

        Role assignedRole = determineRole(registerRequest.getRole());

        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                .phone(registerRequest.getPhone())
                .role(assignedRole)
                .build();

        User saved = userRepository.save(user);
        log.info("[AUTH_REGISTER_SUCCESS] User registered successfully - UserID: {}, Email: {}, Role: {}",
                saved.getId(), saved.getEmail(), saved.getRole());
    }

    private Role determineRole(String requestedRole) {
        if (requestedRole == null || requestedRole.isBlank()) {
            log.warn("[AUTH_REGISTER_FAILED] Missing role in registration request");
            throw new BadRequestException("Role selection is required. Please choose either FARMER or BUYER.");
        }
        String normalized = requestedRole.trim().toUpperCase();
        if (normalized.equals("ADMIN") || normalized.equals("ROLE_ADMIN") || normalized.equals("OWNER")) {
            return Role.ROLE_ADMIN;
        } else if (normalized.equals("BUYER") || normalized.equals("ROLE_BUYER")) {
            return Role.ROLE_BUYER;
        } else if (normalized.equals("FARMER") || normalized.equals("ROLE_FARMER")) {
            return Role.ROLE_FARMER;
        }
        log.warn("[AUTH_REGISTER_FAILED] Invalid role provided: '{}'", requestedRole);
        throw new BadRequestException("Invalid role selected. Role must be either FARMER or BUYER.");
    }

    @Override
    @Transactional(readOnly = true)
    public AuthTokenResponse loginUser(LoginRequest loginRequest) {
        log.info("[AUTH_LOGIN_ATTEMPT] Login attempt for email: {}", loginRequest.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            String token = tokenProvider.generateToken(authentication);
            long expiresInSeconds = tokenProvider.getJwtExpirationInMs() / 1000;

            log.info("[AUTH_LOGIN_SUCCESS] Authentication successful for email: {}", loginRequest.getEmail());

            return AuthTokenResponse.builder()
                    .accessToken(token)
                    .expiresIn(expiresInSeconds)
                    .tokenType("Bearer")
                    .build();
        } catch (BadCredentialsException ex) {
            log.warn("[AUTH_LOGIN_FAILED] Invalid credentials for email: {}", loginRequest.getEmail());
            throw ex;
        } catch (Exception ex) {
            log.error("[AUTH_LOGIN_ERROR] Error during authentication for email: {}: {}", loginRequest.getEmail(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser(Long userId) {
        log.debug("[AUTH_ME_FETCH] User details requested for UserID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("[AUTH_ME_FAILED] User not found for UserID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });
    }
}
