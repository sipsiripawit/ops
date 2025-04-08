package com.yipintsoi.authservice.common;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

/**
 * คลาสที่เก็บฟังก์ชันช่วยเหลือต่างๆ ที่ใช้งานในหลายส่วนของแอปพลิเคชัน
 */
public class Utils {

    /**
     * ดึง Bearer token จาก HTTP request
     * @param request HTTP request
     * @return Bearer token หรือ null ถ้าไม่มี
     */
    public static String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.HEADER_STRING);
        if (bearerToken != null && bearerToken.startsWith(Constants.TOKEN_PREFIX)) {
            return bearerToken.substring(Constants.TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * ดึง Bearer token จาก Authorization header
     * @param authorizationHeader Authorization header
     * @return Bearer token หรือ null ถ้าไม่มี
     */
    public static String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(Constants.TOKEN_PREFIX)) {
            return authorizationHeader.substring(Constants.TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * แปลง Instant เป็น LocalDateTime โดยใช้ system default timezone
     * @param instant Instant ที่ต้องการแปลง
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        return instant != null ? LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) : null;
    }

    /**
     * แปลง LocalDateTime เป็น Instant
     * @param localDateTime LocalDateTime ที่ต้องการแปลง
     * @return Instant
     */
    public static Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atZone(ZoneId.systemDefault()).toInstant() : null;
    }

    /**
     * สร้าง UUID สำหรับใช้เป็น token หรือ identifier
     * @return UUID string
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * ตรวจสอบว่า string เป็น null หรือว่างเปล่า
     * @param str string ที่ต้องการตรวจสอบ
     * @return true ถ้า string เป็น null หรือว่างเปล่า
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private Utils() {
        // ป้องกันการสร้าง instance
    }
}