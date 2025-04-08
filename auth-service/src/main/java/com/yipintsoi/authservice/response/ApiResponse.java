package com.yipintsoi.authservice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * คลาสสำหรับรูปแบบการตอบกลับมาตรฐานของ API
 * @param <T> ประเภทข้อมูลที่จะส่งกลับ
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    
    /**
     * สร้าง response สำเร็จพร้อมข้อมูล
     * @param message ข้อความ
     * @param data ข้อมูล
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
    
    /**
     * สร้าง response สำเร็จพร้อมข้อความ
     * @param message ข้อความ
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }
    
    /**
     * สร้าง response ไม่สำเร็จพร้อมข้อความ
     * @param message ข้อความ
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
    
    /**
     * สร้าง response ไม่สำเร็จพร้อมข้อความและข้อมูล
     * @param message ข้อความ
     * @param data ข้อมูล
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data);
    }
}