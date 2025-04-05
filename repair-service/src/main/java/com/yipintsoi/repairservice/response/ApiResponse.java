package com.yipintsoi.repairservice.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private String status;   // "success" เป็นต้น
    private T data;
    private String message;
}
