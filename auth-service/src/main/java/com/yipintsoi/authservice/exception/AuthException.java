package com.yipintsoi.authservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception ที่ใช้สำหรับข้อผิดพลาดที่เกี่ยวกับ authentication และ authorization
 */
@Getter
public class AuthException extends RuntimeException {
    
    private final HttpStatus status;
    
    /**
     * สร้าง AuthException ด้วยข้อความและสถานะ HTTP ที่กำหนด
     * @param message ข้อความแสดงข้อผิดพลาด
     * @param status สถานะ HTTP ที่จะส่งกลับ
     */
    public AuthException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    
    /**
     * สร้าง AuthException ด้วยข้อความ โดยสถานะเป็น UNAUTHORIZED
     * @param message ข้อความแสดงข้อผิดพลาด
     */
    public AuthException(String message) {
        this(message, HttpStatus.UNAUTHORIZED);
    }
}