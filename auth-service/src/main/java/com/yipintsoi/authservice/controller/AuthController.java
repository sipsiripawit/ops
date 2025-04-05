package com.yipintsoi.authservice.controller;

import com.yipintsoi.authservice.domain.dto.LoginRequest;
import com.yipintsoi.authservice.domain.dto.LoginResponse;
import com.yipintsoi.authservice.domain.dto.RefreshTokenRequest;
import com.yipintsoi.authservice.response.ApiResponse;
import com.yipintsoi.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        ApiResponse<LoginResponse> apiResponse = ApiResponse.<LoginResponse>builder()
                .status("success")
                .data(response)
                .message("Login successful")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refreshToken(request);
        ApiResponse<LoginResponse> apiResponse = ApiResponse.<LoginResponse>builder()
                .status("success")
                .data(response)
                .message("Token refreshed successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
