package com.yipintsoi.authservice.service;

import com.yipintsoi.authservice.domain.dto.TokenResponse;
import org.springframework.security.core.Authentication;

public interface TokenService {
    /**
     * สร้าง token ใหม่
     * @param authentication ข้อมูลการยืนยันตัวตน
     * @param rememberMe ตัวเลือกจดจำผู้ใช้
     * @return token response ที่มี access token และ refresh token
     */
    TokenResponse createToken(Authentication authentication, boolean rememberMe);
    
    /**
     * refresh token
     * @param refreshToken refresh token
     * @return token response ใหม่
     */
    TokenResponse refreshToken(String refreshToken);
    
    /**
     * ยกเลิก token
     * @param refreshToken refresh token ที่ต้องการยกเลิก
     */
    void revokeToken(String refreshToken);
    
    /**
     * ยกเลิกทุก token ของผู้ใช้
     * @param accessToken access token ปัจจุบัน
     */
    void revokeAllUserTokens(String accessToken);
    
    /**
     * ตรวจสอบความถูกต้องของ token
     * @param token token ที่ต้องการตรวจสอบ
     * @return true ถ้า token ถูกต้อง
     */
    boolean validateToken(String token);
    
    /**
     * ดึงข้อมูลการยืนยันตัวตนจาก token
     * @param token access token
     * @return ข้อมูลการยืนยันตัวตน
     */
    Authentication getAuthentication(String token);

    /**
     * บังคับให้ผู้ใช้ออกจากระบบ (ใช้โดย admin)
     * @param username ชื่อผู้ใช้ที่ต้องการบังคับออกจากระบบ
     */
    void forceLogout(String username);
}