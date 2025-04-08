package com.yipintsoi.authservice.controller;

import com.yipintsoi.authservice.common.Constants;
import com.yipintsoi.authservice.common.Utils;
import com.yipintsoi.authservice.domain.dto.LoginRequest;
import com.yipintsoi.authservice.domain.dto.LoginResponse;
import com.yipintsoi.authservice.domain.dto.RefreshTokenRequest;
import com.yipintsoi.authservice.domain.dto.UserDTO;
import com.yipintsoi.authservice.domain.dto.ForgotPasswordRequest;
import com.yipintsoi.authservice.domain.dto.ResetPasswordRequest;
import com.yipintsoi.authservice.response.ApiResponse;
import com.yipintsoi.authservice.service.AuthService;
import com.yipintsoi.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller สำหรับจัดการการตรวจสอบตัวตนและการอนุญาต
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    /**
     * Endpoint สำหรับเข้าสู่ระบบ
     * @param request ข้อมูลการล็อกอิน
     * @return ข้อมูลการเข้าสู่ระบบที่ประกอบด้วย access token, refresh token และข้อมูลผู้ใช้
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());

        LoginResponse authResponse = authService.login(request);

        // ดึงข้อมูลผู้ใช้
        UserDTO user = userService.getUserByUsername(request.getUsername());

        // สร้างข้อมูลตอบกลับ
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("accessToken", authResponse.getAccessToken());
        responseData.put("refreshToken", authResponse.getRefreshToken());
        responseData.put("user", user);

        return ResponseEntity.ok(ApiResponse.success(Constants.LOGIN_SUCCESS, responseData));
    }

    /**
     * Endpoint สำหรับออกจากระบบ
     * @param authorizationHeader JWT token
     * @return ข้อความยืนยันการออกจากระบบ
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = Utils.extractTokenFromHeader(authorizationHeader);
        log.info("Logout request received");

        authService.logout(token);

        return ResponseEntity.ok(ApiResponse.success(Constants.LOGOUT_SUCCESS));
    }

    /**
     * Endpoint สำหรับรีเฟรช token
     * @param request คำขอที่มี refresh token
     * @return ชุดใหม่ของ token (access token และ refresh token)
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<Map<String, Object>>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        log.info("Token refresh request received");

        LoginResponse authResponse = authService.refreshToken(request);

        // สร้างข้อมูลตอบกลับ
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("accessToken", authResponse.getAccessToken());
        responseData.put("refreshToken", authResponse.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success(Constants.TOKEN_REFRESH_SUCCESS, responseData));
    }

    /**
     * Endpoint สำหรับดึงข้อมูลโปรไฟล์ผู้ใช้
     * @param authorizationHeader JWT token
     * @return ข้อมูลโปรไฟล์ผู้ใช้
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> getProfile(
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = Utils.extractTokenFromHeader(authorizationHeader);
        log.info("Profile request received");

        UserDTO user = authService.getUserProfileFromToken(token);

        return ResponseEntity.ok(ApiResponse.success(Constants.USER_PROFILE_SUCCESS, user));
    }

    /**
     * Endpoint สำหรับขอรีเซ็ตรหัสผ่าน
     * @param request คำขอที่มีชื่อผู้ใช้
     * @return ข้อความยืนยันการส่งอีเมล
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("Password reset request for username: {}", request.getUsername());

        authService.initiatePasswordReset(request.getUsername());

        return ResponseEntity.ok(ApiResponse.success(Constants.PASSWORD_RESET_EMAIL_SENT));
    }

    /**
     * Endpoint สำหรับรีเซ็ตรหัสผ่าน
     * @param request คำขอที่มี token และรหัสผ่านใหม่
     * @return ข้อความยืนยันการรีเซ็ตรหัสผ่าน
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("Password reset execution request received");

        authService.resetPassword(request.getToken(), request.getNewPassword());

        return ResponseEntity.ok(ApiResponse.success(Constants.PASSWORD_RESET_SUCCESS));
    }
}