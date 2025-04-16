package com.yipintsoi.authservice.security;

import java.time.Instant;

public interface TokenStore {
    /**
     * บันทึก token ในระบบจัดเก็บ
     * @param token token ที่ต้องการเก็บ
     * @param username ชื่อผู้ใช้
     * @param expiryTime เวลาหมดอายุของ token
     */
    void saveToken(String token, String username, Instant expiryTime);
    
    /**
     * ดึงชื่อผู้ใช้จาก token
     * @param token token ที่ต้องการค้นหา
     * @return ชื่อผู้ใช้หรือ null ถ้าไม่พบ
     */
    String getUsernameFromToken(String token);
    
    /**
     * ตรวจสอบความถูกต้องของ token
     * @param token token ที่ต้องการตรวจสอบ
     * @return true ถ้า token ถูกต้อง
     */
    boolean validateToken(String token);
    
    /**
     * ยกเลิก token
     * @param token token ที่ต้องการยกเลิก
     */
    void removeToken(String token);
    
    /**
     * ยกเลิกทุก token ของผู้ใช้
     * @param username ชื่อผู้ใช้
     */
    void removeAllUserTokens(String username);

    /**
     * บังคับให้ผู้ใช้ออกจากระบบ (ใช้โดย admin)
     * @param username ชื่อผู้ใช้ที่ต้องการบังคับออกจากระบบ
     */
    void forceLogout(String username);

    /**
     * ตรวจสอบว่าผู้ใช้มี active session หรือไม่
     * @param username ชื่อผู้ใช้ที่ต้องการตรวจสอบ
     * @return true ถ้ามี active session
     */
    boolean hasActiveSession(String username);
}