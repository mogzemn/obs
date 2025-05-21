package com.example.obs.business.service;

import com.example.obs.core.security.dto.request.LoginRequest;
import com.example.obs.core.security.dto.request.RefreshTokenRequest;
import com.example.obs.core.security.dto.response.AuthResponse;

public interface AuthService {
    
    AuthResponse authenticate(LoginRequest request);
    
    AuthResponse refreshToken(RefreshTokenRequest request);
} 