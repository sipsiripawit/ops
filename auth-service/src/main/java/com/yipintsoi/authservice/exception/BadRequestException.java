package com.yipintsoi.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception ที่ใช้เมื่อคำขอไม่ถูกต้อง
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    
    /**
     * สร้าง BadRequestException ด้วยข้อความที่กำหนด
     * @param message ข้อความแสดงข้อผิดพลาด
     */
    public BadRequestException(String message) {
        super(message);
    }
    
    /**
     * สร้าง BadRequestException ด้วยข้อความและสาเหตุที่กำหนด
     * @param message ข้อความแสดงข้อผิดพลาด
     * @param cause สาเหตุของข้อผิดพลาด
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}