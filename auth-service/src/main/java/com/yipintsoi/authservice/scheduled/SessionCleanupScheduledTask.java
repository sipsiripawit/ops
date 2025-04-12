package com.yipintsoi.authservice.scheduled;

import com.yipintsoi.authservice.service.SingleSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled Task สำหรับการตรวจสอบและทำความสะอาด Session ที่ไม่มีการใช้งานตามระยะเวลาที่กำหนด
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SessionCleanupScheduledTask {

    private final SingleSessionService singleSessionService;

    /**
     * ทำการตรวจสอบและยกเลิก Session ที่ไม่มีการใช้งานทุก 5 นาที
     */
    @Scheduled(fixedRate = 300000) // 5 นาที = 300,000 มิลลิวินาที
    public void checkInactiveSessions() {
        log.debug("เริ่มต้นการตรวจสอบ session ที่ไม่มีการใช้งานตามกำหนดเวลา");
        singleSessionService.checkAndInvalidateInactiveSessions();
    }
}