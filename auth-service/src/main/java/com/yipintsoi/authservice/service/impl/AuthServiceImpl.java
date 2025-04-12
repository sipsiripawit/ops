package com.yipintsoi.authservice.service.impl;

import com.yipintsoi.authservice.common.Constants;
import com.yipintsoi.authservice.domain.dto.LoginRequest;
import com.yipintsoi.authservice.domain.dto.LoginResponse;
import com.yipintsoi.authservice.domain.dto.RefreshTokenRequest;
import com.yipintsoi.authservice.domain.dto.UserDTO;
import com.yipintsoi.authservice.domain.entity.User;
import com.yipintsoi.authservice.domain.entity.UserSession;
import com.yipintsoi.authservice.domain.mapper.UserMapper;
import com.yipintsoi.authservice.exception.AuthException;
import com.yipintsoi.authservice.exception.ResourceNotFoundException;
import com.yipintsoi.authservice.repository.UserRepository;
import com.yipintsoi.authservice.repository.UserSessionRepository;
import com.yipintsoi.authservice.service.AuthService;
import com.yipintsoi.authservice.service.EmailService;
import com.yipintsoi.authservice.service.JwtTokenProvider;
import com.yipintsoi.authservice.service.SingleSessionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation ของ AuthService ที่รองรับ Single Session Login
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final SingleSessionService singleSessionService;

    @Value("${app.force-single-session:true}")
    private boolean forceSingleSession;

    /**
     * ทำการเข้าสู่ระบบสำหรับผู้ใช้
     * รองรับการบังคับใช้ Single Session ถ้าตั้งค่าเป็น true
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            log.debug("กำลังตรวจสอบผู้ใช้ {}", request.getUsername());

            // ตรวจสอบสิทธิ์ผู้ใช้
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

            if (!user.getActive()) {
                throw new AuthException(Constants.ACCOUNT_DISABLED);
            }

            HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            // สร้าง token ใหม่
            String accessToken = jwtTokenProvider.generateToken(user.getUsername());
            String refreshToken = null;

            // สร้าง refresh token ถ้าผู้ใช้เลือก "จำฉันไว้"
            if (request.isRememberMe()) {
                refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
                
                // สร้าง session ใหม่ (จะทำการยกเลิก session เก่าถ้าเปิดใช้ single session)
                singleSessionService.createSession(user, refreshToken, httpRequest);
                
                log.debug("สร้าง session ใหม่พร้อม refresh token สำหรับผู้ใช้ {}", user.getUsername());
            } else if (forceSingleSession) {
                // ถ้าไม่ได้เลือก "จำฉันไว้" แต่เปิดใช้ single session ให้ยกเลิก session เก่าทั้งหมด
                singleSessionService.enforceActiveSingleSession(user, httpRequest);
            }

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .build();
            
        } catch (AuthenticationException e) {
            log.error("การตรวจสอบสิทธิ์ผู้ใช้ {} ล้มเหลว: {}", request.getUsername(), e.getMessage());
            throw new AuthException(Constants.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * ทำการออกจากระบบสำหรับผู้ใช้
     * ยกเลิก session ทั้งหมดของผู้ใช้
     */
    @Override
    public void logout(String token) {
        if (!validateToken(token)) {
            throw new AuthException(Constants.INVALID_TOKEN);
        }

        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        log.debug("กำลังออกจากระบบผู้ใช้ {}", username);

        // ยกเลิก session ทั้งหมดของผู้ใช้
        List<UserSession> activeSessions = userSessionRepository.findByUserAndActive(user, true);
        for (UserSession session : activeSessions) {
            session.setActive(false);
            session.setUpdatedBy(username);
            session.setUpdatedDate(Instant.now());
            userSessionRepository.save(session);
        }

        log.debug("ออกจากระบบผู้ใช้ {} สำเร็จ", username);
    }

    /**
     * รีเฟรช JWT token
     * อัปเดตเวลาใช้งานล่าสุดของ session เพื่อรักษาสถานะ active
     */
    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new AuthException(Constants.INVALID_TOKEN);
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        Optional<UserSession> sessionOpt = userSessionRepository.findByRefreshToken(refreshToken);
        if (sessionOpt.isEmpty() || !sessionOpt.get().getActive()) {
            throw new AuthException(Constants.SESSION_NOT_FOUND);
        }

        UserSession session = sessionOpt.get();

        if (session.getExpiresAt().isBefore(Instant.now())) {
            session.setActive(false);
            userSessionRepository.save(session);
            throw new AuthException(Constants.TOKEN_EXPIRED);
        }

        log.debug("กำลังรีเฟรช token สำหรับผู้ใช้ {}", username);

        // อัปเดตเวลาใช้งานล่าสุด
        singleSessionService.updateLastActivityTimestamp(refreshToken);

        // สร้าง token ใหม่
        String accessToken = jwtTokenProvider.generateToken(username);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

        // ยกเลิก session เก่า
        session.setActive(false);
        session.setUpdatedBy(username);
        session.setUpdatedDate(Instant.now());
        userSessionRepository.save(session);

        // สร้าง session ใหม่
        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        singleSessionService.createSession(user, newRefreshToken, httpRequest);

        log.debug("รีเฟรช token สำหรับผู้ใช้ {} สำเร็จ", username);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();
    }

    /**
     * ดึงข้อมูลโปรไฟล์ผู้ใช้จาก token
     */
    @Override
    public UserDTO getUserProfileFromToken(String token) {
        if (!validateToken(token)) {
            throw new AuthException(Constants.INVALID_TOKEN);
        }

        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        return userMapper.userToUserDTO(user);
    }

    /**
     * เริ่มกระบวนการรีเซ็ตรหัสผ่าน
     */
    @Override
    public void initiatePasswordReset(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        if (!user.getActive()) {
            throw new AuthException(Constants.ACCOUNT_DISABLED);
        }

        // สร้าง token สำหรับรีเซ็ตรหัสผ่าน
        String resetToken = UUID.randomUUID().toString();

        // ในระบบจริงควรบันทึก token นี้ลงฐานข้อมูลพร้อมกำหนดเวลาหมดอายุ
        log.debug("สร้าง token สำหรับรีเซ็ตรหัสผ่านให้ผู้ใช้ {}: {}", username, resetToken);

        // ส่งอีเมลรีเซ็ตรหัสผ่าน
        String resetLink = "https://smartops.yipintsoi.com/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

        log.info("เริ่มกระบวนการรีเซ็ตรหัสผ่านสำหรับผู้ใช้: {}", username);
    }

    /**
     * รีเซ็ตรหัสผ่าน
     */
    @Override
    public void resetPassword(String token, String newPassword) {
        // ในระบบจริงควรตรวจสอบ token จากฐานข้อมูล
        log.debug("กำลังรีเซ็ตรหัสผ่านด้วย token: {}", token);

        // จำลองการค้นหาผู้ใช้จาก token
        User mockUser = new User();
        mockUser.setUsername("mock.user");
        mockUser.setEmail("mock@example.com");
        mockUser.setPassword("oldPassword");

        // ตั้งรหัสผ่านใหม่
        mockUser.setPassword(passwordEncoder.encode(newPassword));
        // userRepository.save(mockUser); // ในระบบจริงต้อง save

        log.info("รีเซ็ตรหัสผ่านสำเร็จสำหรับผู้ใช้จำลอง");
    }

    /**
     * ตรวจสอบความถูกต้องของ token
     */
    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
}