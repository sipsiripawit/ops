package com.yipintsoi.authservice.controller;

import com.yipintsoi.authservice.common.Utils;
import com.yipintsoi.authservice.response.ApiResponse;
import com.yipintsoi.authservice.security.oauth.JwtToUserConverter;
import com.yipintsoi.authservice.service.TokenService;
import com.yipintsoi.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * API สำหรับตรวจสอบความถูกต้องของ token และดึงข้อมูลผู้ใช้
 * เพื่อให้ microservices อื่นๆ เรียกใช้
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Token Validation", description = "Token Validation API for microservices")
public class TokenValidationController {

    private final TokenService tokenService;
    private final UserService userService;

    @GetMapping("/validate-token")
    @Operation(summary = "Validate token", description = "Check if the provided token is valid")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = Utils.extractTokenFromHeader(authorizationHeader);
        
        boolean isValid = false;
        if (token != null) {
            isValid = tokenService.validateToken(token);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Token validation result", isValid));
    }

    @GetMapping("/user-info")
    @Operation(summary = "Get user info from token", description = "Extract user information from the provided token")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        String token = Utils.extractTokenFromHeader(authorizationHeader);
        
        Map<String, Object> userInfo = new HashMap<>();
        
        if (token != null && tokenService.validateToken(token)) {
            Authentication authentication = tokenService.getAuthentication(token);
            String username = authentication.getName();
            
            // ดึงข้อมูลผู้ใช้เพิ่มเติม
            try {
                var user = userService.getUserByUsername(username);
                userInfo.put("id", user.getId());
                userInfo.put("username", user.getUsername());
                userInfo.put("email", user.getEmail());
                userInfo.put("roles", user.getRoles());
                userInfo.put("status", user.getStatus());
            } catch (Exception e) {
                log.error("Error retrieving user details for {}", username, e);
            }
        }
        
        return ResponseEntity.ok(ApiResponse.success("User information", userInfo));
    }
}