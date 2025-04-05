package com.yipintsoi.authservice.service.impl;

import com.yipintsoi.authservice.domain.dto.LoginRequest;
import com.yipintsoi.authservice.domain.dto.LoginResponse;
import com.yipintsoi.authservice.domain.dto.RefreshTokenRequest;
import com.yipintsoi.authservice.domain.entity.User;
import com.yipintsoi.authservice.domain.entity.UserSession;
import com.yipintsoi.authservice.exception.CustomException;
import com.yipintsoi.authservice.repository.UserRepository;
import com.yipintsoi.authservice.repository.UserSessionRepository;
import com.yipintsoi.authservice.service.AuthService;
import com.yipintsoi.authservice.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException("User not found"));

        // Invalidate previous active sessions (Single Active Session)
        List<UserSession> activeSessions = userSessionRepository.findByUserAndActive(user, true);
        for (UserSession session : activeSessions) {
            session.setActive(false);
            session.setUpdatedBy("system");
            session.setUpdatedDate(Instant.now());
            userSessionRepository.save(session);
        }

        // Generate new tokens
        String accessToken = jwtTokenProvider.generateToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        // Save new session
        UserSession newSession = UserSession.builder()
                .user(user)
                .refreshToken(refreshToken)
                .ipAddress("N/A")
                .active(true)
                .expiresAt(Instant.now().plusMillis(jwtTokenProvider.getRefreshExpirationMs()))
                .createdBy("system")
                .createdDate(Instant.now())
                .build();
        userSessionRepository.save(newSession);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            throw new CustomException("Invalid refresh token");
        }
        UserSession session = userSessionRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new CustomException("Session not found"));

        if (!session.getActive() || session.getExpiresAt().isBefore(Instant.now())) {
            throw new CustomException("Session expired or inactive");
        }

        String username = jwtTokenProvider.getUsernameFromToken(request.getRefreshToken());
        String newAccessToken = jwtTokenProvider.generateToken(username);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

        session.setActive(false);
        session.setUpdatedBy("system");
        session.setUpdatedDate(Instant.now());
        userSessionRepository.save(session);

        User user = session.getUser();
        UserSession newSession = UserSession.builder()
                .user(user)
                .refreshToken(newRefreshToken)
                .ipAddress("N/A")
                .active(true)
                .expiresAt(Instant.now().plusMillis(jwtTokenProvider.getRefreshExpirationMs()))
                .createdBy("system")
                .createdDate(Instant.now())
                .build();
        userSessionRepository.save(newSession);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();
    }
}
