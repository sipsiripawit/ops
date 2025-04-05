package com.yipintsoi.authservice.service;

import com.yipintsoi.authservice.domain.dto.LoginRequest;
import com.yipintsoi.authservice.domain.dto.LoginResponse;
import com.yipintsoi.authservice.domain.dto.RefreshTokenRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse refreshToken(RefreshTokenRequest request);
}
