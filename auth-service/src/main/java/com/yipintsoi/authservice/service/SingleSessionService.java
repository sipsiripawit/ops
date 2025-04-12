package com.yipintsoi.authservice.service;

import com.yipintsoi.authservice.domain.entity.User;
import com.yipintsoi.authservice.domain.entity.UserSession;
import com.yipintsoi.authservice.exception.AuthException;
import com.yipintsoi.authservice.repository.UserSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service ที่จัดการเกี่ยวกับการ Login แบบ Single Session
 * จะไม่อนุญาตให้มีการ Login ซ้อนหากผู้ใช้ยังคงใช้งานอยู่
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SingleSessionService {

    private final UserSessionRepository userSessionRepository;

    /**
     * ตรวจสอบว่าผู้ใช้มี session ที่ active อยู่หรือไม่
     * ถ้ามีจะทำการยกเลิก session เก่าทั้งหมดก่อนสร้าง session ใหม่
     *
     * @param user ผู้ใช้ที่ต้องการตรวจสอบ
     * @param request ข้อมูล HTTP request
     * @return true ถ้าสามารถสร้าง session ใหม่ได้
     */
    public boolean enforceActiveSingleSession(User user, HttpServletRequest request) {
        try {
            // ค้นหา session ที่ active ของผู้ใช้
            List<UserSession> activeSessions = userSessionRepository.findByUserAndActive(user, true);
            
            // ถ้ามี session ที่ active อยู่ ให้ทำการ invalidate session เก่าทั้งหมด
            if (!activeSessions.isEmpty()) {
                log.info("พบ {} session ที่ active ของผู้ใช้ {}, ทำการยกเลิก session เก่า", 
                         activeSessions.size(), user.getUsername());
                
                for (UserSession session : activeSessions) {
                    session.setActive(false);
                    session.setUpdatedBy("system");
                    session.setUpdatedDate(Instant.now());
                    userSessionRepository.save(session);
                }
            }
            
            return true;
        } catch (Exception e) {
            log.error("เกิดข้อผิดพลาดในการตรวจสอบ single session: {}", e.getMessage());
            return false;
        }
    }

    /**
     * ดึงข้อมูลอุปกรณ์และเบราว์เซอร์จาก User-Agent
     *
     * @param request HTTP request
     * @return ข้อมูลอุปกรณ์และเบราว์เซอร์
     */
    public String[] extractDeviceAndBrowserInfo(HttpServletRequest request) {
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

    /**
     * ดึงข้อมูล IP Address จาก request
     *
     * @param request HTTP request
     * @return IP Address
     */
    public String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    /**
     * สร้าง session ใหม่สำหรับผู้ใช้
     *
     * @param user ผู้ใช้
     * @param refreshToken refresh token
     * @param request HTTP request
     * @return user session ที่สร้างขึ้น
     */
    public UserSession createSession(User user, String refreshToken, HttpServletRequest request) {
        // ตรวจสอบว่าสามารถสร้าง session ใหม่ได้หรือไม่ (single session)
        if (!enforceActiveSingleSession(user, request)) {
            throw new AuthException("ไม่สามารถสร้าง session ใหม่ได้", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        String ipAddress = getClientIpAddress(request);
        String[] deviceInfo = extractDeviceAndBrowserInfo(request);
        
        UserSession session = UserSession.builder()
                .user(user)
                .refreshToken(refreshToken)
                .ipAddress(ipAddress)
                .deviceInfo(deviceInfo[0])
                .browserInfo(deviceInfo[1])
                .active(true)
                .expiresAt(Instant.now().plusMillis(604800000)) // 7 วัน (ควรดึงค่าจาก config)
                .createdBy(user.getUsername())
                .createdDate(Instant.now())
                .lastActivityTimestamp(Instant.now())
                .build();
        
        return userSessionRepository.save(session);
    }

    /**
     * อัปเดตเวลาการใช้งานล่าสุดของ session
     *
     * @param refreshToken refresh token
     */
    public void updateLastActivityTimestamp(String refreshToken) {
        Optional<UserSession> sessionOpt = userSessionRepository.findByRefreshToken(refreshToken);
        if (sessionOpt.isPresent() && sessionOpt.get().getActive()) {
            UserSession session = sessionOpt.get();
            session.setLastActivityTimestamp(Instant.now());
            userSessionRepository.save(session);
        }
    }

    /**
     * ตรวจสอบและยกเลิก session ที่หมดเวลา (ไม่มีกิจกรรมเกิน 30 นาที)
     */
    public void checkAndInvalidateInactiveSessions() {
        log.debug("ตรวจสอบ session ที่ไม่มีกิจกรรม");
        try {
            // เรียกใช้ฟังก์ชันใน DB ที่สร้างไว้
            userSessionRepository.checkInactiveSessions();
            log.debug("ตรวจสอบ session ที่ไม่มีกิจกรรมเสร็จสิ้น");
        } catch (Exception e) {
            log.error("เกิดข้อผิดพลาดในการตรวจสอบ session ที่ไม่มีกิจกรรม: {}", e.getMessage());
        }
    }
}