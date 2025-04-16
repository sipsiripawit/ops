package com.yipintsoi.authservice.controller;

import com.yipintsoi.authservice.common.Constants;
import com.yipintsoi.authservice.common.Utils;
import com.yipintsoi.authservice.domain.dto.*;
import com.yipintsoi.authservice.exception.AuthException;
import com.yipintsoi.authservice.response.ApiResponse;
import com.yipintsoi.authservice.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/token")
    @Operation(summary = "Login to the system", description = "Authenticate user and get JWT token")
    public ResponseEntity<ApiResponse<TokenResponse>> getToken(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            log.info("Login attempt for user: {}", loginRequest.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            TokenResponse tokenResponse = tokenService.createToken(authentication, loginRequest.isRememberMe());

            return ResponseEntity.ok(
                    ApiResponse.success(Constants.LOGIN_SUCCESS, tokenResponse)
            );
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {}", loginRequest.getUsername());
            throw new AuthException(Constants.INVALID_CREDENTIALS);
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT token", description = "Get new JWT token using refresh token")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Token refresh request received");

        TokenResponse tokenResponse = tokenService.refreshToken(request.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success(Constants.TOKEN_REFRESH_SUCCESS, tokenResponse));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout from the system", description = "Invalidate current refresh token")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody LogoutRequest request) {
        log.info("Logout request received");

        tokenService.revokeToken(request.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success(Constants.LOGOUT_SUCCESS));
    }

    @PostMapping("/logout-all")
    @Operation(summary = "Logout from all devices", description = "Invalidate all refresh tokens for the user")
    public ResponseEntity<ApiResponse<Void>> logoutAll(@RequestHeader("Authorization") String authorizationHeader) {
        String token = Utils.extractTokenFromHeader(authorizationHeader);
        log.info("Logout all request received");

        if (token != null) {
            tokenService.revokeAllUserTokens(token);
            return ResponseEntity.ok(ApiResponse.success("Logged out from all devices successfully"));
        } else {
            throw new AuthException(Constants.INVALID_TOKEN);
        }
    }

    @PostMapping("/force-logout")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Force logout a user", description = "Invalidate all sessions for a specific user")
    public ResponseEntity<ApiResponse<Void>> forceLogout(@Valid @RequestBody ForceLogoutRequest request) {
        log.info("Admin force logout request for user: {}", request.getUsername());

        tokenService.forceLogout(request.getUsername());

        return ResponseEntity.ok(ApiResponse.success("User has been forcefully logged out"));
    }
}