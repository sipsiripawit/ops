package com.yipintsoi.authservice.common;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class Utils {
    public static String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.HEADER_STRING);
        if (bearerToken != null && bearerToken.startsWith(Constants.TOKEN_PREFIX)) {
            return bearerToken.substring(Constants.TOKEN_PREFIX.length());
        }
        return null;
    }

    public static String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(Constants.TOKEN_PREFIX)) {
            return authorizationHeader.substring(Constants.TOKEN_PREFIX.length());
        }
        return null;
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        return instant != null ? LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) : null;
    }

    public static Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atZone(ZoneId.systemDefault()).toInstant() : null;
    }

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public static String[] extractDeviceAndBrowserInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String deviceInfo = "Unknown Device";
        String browserInfo = "Unknown Browser";

        if (userAgent != null) {
            // ตรวจสอบอุปกรณ์
            if (userAgent.contains("Mobile") || userAgent.contains("Android") || userAgent.contains("iPhone")) {
                deviceInfo = "Mobile Phone";
            } else if (userAgent.contains("iPad") || userAgent.contains("Tablet")) {
                deviceInfo = "Tablet";
            } else if (userAgent.contains("Windows") || userAgent.contains("Macintosh") || userAgent.contains("Linux")) {
                deviceInfo = "Desktop Computer";
            }

            // ตรวจสอบเบราว์เซอร์
            if (userAgent.contains("Chrome") && !userAgent.contains("Edg")) {
                browserInfo = "Chrome";
            } else if (userAgent.contains("Firefox")) {
                browserInfo = "Firefox";
            } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
                browserInfo = "Safari";
            } else if (userAgent.contains("Edg")) {
                browserInfo = "Edge";
            } else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                browserInfo = "Internet Explorer";
            }
        }

        return new String[]{deviceInfo, browserInfo};
    }

    private Utils() {
        // ป้องกันการสร้าง instance
    }
}