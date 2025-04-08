package com.yipintsoi.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception ที่ใช้เมื่อไม่พบทรัพยากรที่ร้องขอ
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * สร้าง ResourceNotFoundException ด้วยข้อความที่กำหนด
     * @param message ข้อความแสดงข้อผิดพลาด
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * สร้าง ResourceNotFoundException สำหรับทรัพยากรที่ระบุ
     * @param resourceName ชื่อทรัพยากร
     * @param fieldName ชื่อฟิลด์
     * @param fieldValue ค่าของฟิลด์
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}