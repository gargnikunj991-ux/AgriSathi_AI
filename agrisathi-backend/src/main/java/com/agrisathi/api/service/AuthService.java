package com.agrisathi.api.service;

import com.agrisathi.api.dto.request.LoginRequest;
import com.agrisathi.api.dto.request.RegisterRequest;
import com.agrisathi.api.dto.response.AuthTokenResponse;
import com.agrisathi.api.model.entity.User;

public interface AuthService {
    void registerUser(RegisterRequest registerRequest);
    AuthTokenResponse loginUser(LoginRequest loginRequest);
    User getCurrentUser(Long userId);
}
