package com.yipintsoi.authservice.service;

/**
 * Service สำหรับการส่งอีเมล
 */
public interface EmailService {
    
    /**
     * ส่งอีเมลรีเซ็ตรหัสผ่าน
     * @param to อีเมลปลายทาง
     * @param resetLink ลิงก์สำหรับรีเซ็ตรหัสผ่าน
     */
    void sendPasswordResetEmail(String to, String resetLink);
    
    /**
     * ส่งอีเมลยืนยันการลงทะเบียน
     * @param to อีเมลปลายทาง
     * @param verificationLink ลิงก์สำหรับยืนยันการลงทะเบียน
     */
    void sendRegistrationVerificationEmail(String to, String verificationLink);
}