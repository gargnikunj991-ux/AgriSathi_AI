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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email is already registered!");
        }

        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                .phone(registerRequest.getPhone())
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthTokenResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        String token = tokenProvider.generateToken(authentication);
        long expiresInSeconds = tokenProvider.getJwtExpirationInMs() / 1000;

        return AuthTokenResponse.builder()
                .accessToken(token)
                .expiresIn(expiresInSeconds)
                .tokenType("Bearer")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
}
