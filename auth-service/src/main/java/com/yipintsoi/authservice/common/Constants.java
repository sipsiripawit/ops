package com.yipintsoi.authservice.common;

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
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGOUT_SUCCESS = "Logged out successfully";
    public static final String TOKEN_REFRESH_SUCCESS = "Token refreshed successfully";
    public static final String USER_PROFILE_SUCCESS = "User profile retrieved successfully";

    // Error Messages
    public static final String USER_NOT_FOUND = "User not found";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String ACCOUNT_DISABLED = "Account is disabled";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String SESSION_NOT_FOUND = "Session not found";

    // Topics
    public static final String TOPIC_AUTH_EVENTS = "auth-events";

    // Event Types
    public static final String EVENT_LOGIN = "LOGIN";
    public static final String EVENT_LOGOUT = "LOGOUT";
    public static final String EVENT_FORCE_LOGOUT = "FORCE_LOGOUT";
    public static final String EVENT_PERMISSION_CHANGED = "PERMISSION_CHANGED";

    // Rate Limiting
    public static final String RATE_LIMITER_LOGIN = "loginRateLimiter";
    public static final String RATE_LIMITER_REFRESH = "refreshTokenRateLimiter";

    private Constants() {
        // ป้องกันการสร้าง instance
    }
}