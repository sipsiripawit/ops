package com.yipintsoi.authservice.service.impl;

import com.yipintsoi.authservice.common.Constants;
import com.yipintsoi.authservice.dto.auth.LoginRequest;
import com.yipintsoi.authservice.dto.auth.LoginResponse;
import com.yipintsoi.authservice.dto.auth.RefreshTokenRequest;
import com.yipintsoi.authservice.dto.user.UserDTO;
import com.yipintsoi.authservice.entity.User;
import com.yipintsoi.authservice.entity.UserSession;
import com.yipintsoi.authservice.exception.AuthException;
import com.yipintsoi.authservice.exception.ResourceNotFoundException;
import com.yipintsoi.authservice.mapper.UserMapper;
import com.yipintsoi.authservice.repository.UserRepository;
import com.yipintsoi.authservice.repository.UserSessionRepository;
import com.yipintsoi.authservice.service.AuthService;
import com.yipintsoi.authservice.service.EmailService;
import com.yipintsoi.authservice.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Implementation ของ AuthService
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

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            log.debug("Authenticating user {}", request.getUsername());
            
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

            if (!user.getActive()) {
                throw new AuthException(Constants.ACCOUNT_DISABLED);
            }

            // Invalidate previous active sessions for Single Active Session policy
            List<UserSession> activeSessions = userSessionRepository.findByUserAndActive(user, true);
            for (UserSession session : activeSessions) {
                log.debug("Invalidating previous session for user {}", user.getUsername());
                session.setActive(false);
                session.setUpdatedBy(Constants.SYSTEM_USER);
                session.setUpdatedDate(Instant.now());
                userSessionRepository.save(session);
            }

            // Generate new tokens
            String accessToken = jwtTokenProvider.generateToken(user.getUsername());
            String refreshToken = null;
            
            // Only generate refresh token if rememberMe is true
            if (request.isRememberMe()) {
                refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
                
                // Save new session
                UserSession newSession = UserSession.builder()
                        .user(user)
                        .refreshToken(refreshToken)
                        .ipAddress("N/A") // In real system, we would get this from the request
                        .active(true)
                        .expiresAt(Instant.now().plusMillis(jwtTokenProvider.getRefreshExpirationMs()))
                        .createdBy(Constants.SYSTEM_USER)
                        .createdDate(Instant.now())
                        .build();
                userSessionRepository.save(newSession);
                
                log.debug("Created new session with refresh token for user {}", user.getUsername());
            }

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .build();
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user {}: {}", request.getUsername(), e.getMessage());
            throw new AuthException(Constants.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout(String token) {
        if (!validateToken(token)) {
            throw new AuthException(Constants.INVALID_TOKEN);
        }

        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        log.debug("Logging out user {}", username);

        // Invalidate all active sessions
        List<UserSession> activeSessions = userSessionRepository.findByUserAndActive(user, true);
        for (UserSession session : activeSessions) {
            session.setActive(false);
            session.setUpdatedBy(username);
            session.setUpdatedDate(Instant.now());
            userSessionRepository.save(session);
        }
        
        log.debug("Successfully logged out user {}", username);
    }

    /**
     * {@inheritDoc}
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

        UserSession session = userSessionRepository.findByRefreshTokenAndActive(refreshToken, true)
                .orElseThrow(() -> new AuthException(Constants.SESSION_NOT_FOUND));

        if (session.getExpiresAt().isBefore(Instant.now())) {
            session.setActive(false);
            userSessionRepository.save(session);
            throw new AuthException(Constants.TOKEN_EXPIRED);
        }

        log.debug("Refreshing token for user {}", username);

        // Generate new tokens
        String accessToken = jwtTokenProvider.generateToken(username);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

        // Invalidate old session
        session.setActive(false);
        session.setUpdatedBy(username);
        session.setUpdatedDate(Instant.now());
        userSessionRepository.save(session);

        // Create new session
        UserSession newSession = UserSession.builder()
                .user(user)
                .refreshToken(newRefreshToken)
                .ipAddress(session.getIpAddress())
                .active(true)
                .expiresAt(Instant.now().plusMillis(jwtTokenProvider.getRefreshExpirationMs()))
                .createdBy(username)
                .createdDate(Instant.now())
                .build();
        userSessionRepository.save(newSession);
        
        log.debug("Successfully refreshed token for user {}", username);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public void initiatePasswordReset(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        if (!user.getActive()) {
            throw new AuthException(Constants.ACCOUNT_DISABLED);
        }

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        
        // In real world application, we would save this token to database
        // and associate it with the user, along with an expiration time
        
        // For demonstration, we'll just log it
        log.debug("Generated reset token for user {}: {}", username, resetToken);

        // Send reset email
        String resetLink = "https://your-app.com/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
        
        log.info("Password reset initiated for user: {}", username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetPassword(String token, String newPassword) {
        // In real world application, we would:
        // 1. Validate the token
        // 2. Check if it's not expired
        // 3. Find the user associated with this token
        
        // For demonstration, we'll just log it
        log.debug("Resetting password with token: {}", token);
        
        // Instead of looking up a real user, we'll create a mock one
        User user = new User(); // This would be replaced with actual user lookup
        
        // Set the new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        log.info("Password reset completed successfully");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
}