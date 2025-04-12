package com.yipintsoi.authservice.repository;

import com.yipintsoi.authservice.domain.entity.User;
import com.yipintsoi.authservice.domain.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    
    /**
     * ค้นหา session ตาม user และสถานะ active
     */
    List<UserSession> findByUserAndActive(User user, Boolean active);
    
    /**
     * ค้นหา session ตาม refresh token
     */
    Optional<UserSession> findByRefreshToken(String refreshToken);
    
    /**
     * เรียกใช้ function ในฐานข้อมูลเพื่อตรวจสอบและอัปเดต session ที่ไม่มีกิจกรรม
     */
    @Modifying
    @Transactional
    @Query(value = "SELECT check_inactive_sessions()", nativeQuery = true)
    void checkInactiveSessions();
    
    /**
     * อัปเดตสถานะของ session ให้เป็น inactive
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserSession s SET s.active = false, s.updatedBy = ?2, s.updatedDate = CURRENT_TIMESTAMP WHERE s.user.id = ?1 AND s.active = true")
    void deactivateAllUserSessions(Long userId, String updatedBy);
    
    /**
     * ค้นหา session ที่ active ทั้งหมดยกเว้น session ปัจจุบัน
     */
    @Query("SELECT s FROM UserSession s WHERE s.user.id = ?1 AND s.active = true AND s.refreshToken != ?2")
    List<UserSession> findActiveSessionsExceptCurrent(Long userId, String currentRefreshToken);
}