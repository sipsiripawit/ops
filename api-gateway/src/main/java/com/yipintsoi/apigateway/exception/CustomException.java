package com.yipintsoi.apigateway.exception;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
