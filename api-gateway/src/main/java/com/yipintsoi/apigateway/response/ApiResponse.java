package com.yipintsoi.apigateway.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private String status;
    private T data;
    private String message;
}
