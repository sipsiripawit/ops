package com.yipintsoi.authservice.common;

/**
 * คลาสสำหรับเก็บค่าคงที่ที่ใช้ในแอปพลิเคชัน
 */
public class Constants {

    // Security Constants
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    
    // User Status
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_PENDING = "PENDING";
    
    // Role Names
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    
    // Audit Log Constants
    public static final String SYSTEM_USER = "system";
    
    // Response Messages
    public static final String LOGIN_SUCCESS = "ล็อกอินสำเร็จ";
    public static final String LOGOUT_SUCCESS = "ออกจากระบบสำเร็จ";
    public static final String TOKEN_REFRESH_SUCCESS = "รีเฟรชโทเค็นสำเร็จ";
    public static final String USER_PROFILE_SUCCESS = "ดึงข้อมูลผู้ใช้สำเร็จ";
    public static final String PASSWORD_RESET_EMAIL_SENT = "ส่งลิงก์รีเซ็ตรหัสผ่านไปยังอีเมลเรียบร้อยแล้ว";
    public static final String PASSWORD_RESET_SUCCESS = "รีเซ็ตรหัสผ่านสำเร็จ";
    
    // Error Messages
    public static final String USER_NOT_FOUND = "ไม่พบผู้ใช้";
    public static final String INVALID_CREDENTIALS = "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง";
    public static final String ACCOUNT_DISABLED = "บัญชีผู้ใช้ถูกระงับการใช้งาน";
    public static final String INVALID_TOKEN = "โทเค็นไม่ถูกต้อง";
    public static final String TOKEN_EXPIRED = "โทเค็นหมดอายุ";
    public static final String SESSION_NOT_FOUND = "ไม่พบเซสชัน";
    
    private Constants() {
        // ป้องกันการสร้าง instance
    }
}