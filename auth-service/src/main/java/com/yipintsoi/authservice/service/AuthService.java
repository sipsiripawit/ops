package com.yipintsoi.authservice.service;


import com.yipintsoi.authservice.domain.dto.LoginRequest;
import com.yipintsoi.authservice.domain.dto.LoginResponse;
import com.yipintsoi.authservice.domain.dto.RefreshTokenRequest;
import com.yipintsoi.authservice.domain.dto.UserDTO;

/**
 * Service สำหรับจัดการการตรวจสอบตัวตนและการอนุญาต
 */
public interface AuthService {
    
    /**
     * ทำการเข้าสู่ระบบสำหรับผู้ใช้
     * @param request ข้อมูลการล็อกอิน ประกอบด้วย username, password และ rememberMe flag
     * @return ข้อมูลการเข้าสู่ระบบที่ประกอบด้วย access token และ refresh token
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * ทำการออกจากระบบสำหรับผู้ใช้
     * @param token JWT token ที่ใช้อยู่
     */
    void logout(String token);
    
    /**
     * รีเฟรช JWT token
     * @param request คำขอที่มี refresh token
     * @return ชุดใหม่ของ token (access token และ refresh token)
     */
    LoginResponse refreshToken(RefreshTokenRequest request);
    
    /**
     * ดึงข้อมูลโปรไฟล์ผู้ใช้จาก token
     * @param token JWT token
     * @return ข้อมูลโปรไฟล์ผู้ใช้
     */
    UserDTO getUserProfileFromToken(String token);
    
    /**
     * เริ่มกระบวนการรีเซ็ตรหัสผ่าน
     * @param username ชื่อผู้ใช้ที่ต้องการรีเซ็ตรหัสผ่าน
     */
    void initiatePasswordReset(String username);
    
    /**
     * รีเซ็ตรหัสผ่าน
     * @param token token สำหรับรีเซ็ตรหัสผ่าน
     * @param newPassword รหัสผ่านใหม่
     */
    void resetPassword(String token, String newPassword);
    
    /**
     * ตรวจสอบความถูกต้องของ token
     * @param token token ที่ต้องการตรวจสอบ
     * @return true ถ้า token ถูกต้อง
     */
    boolean validateToken(String token);
}