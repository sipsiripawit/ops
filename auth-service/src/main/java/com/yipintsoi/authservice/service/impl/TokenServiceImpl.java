package com.yipintsoi.authservice.service.impl;

import com.yipintsoi.authservice.common.Constants;
import com.yipintsoi.authservice.common.Utils;
import com.yipintsoi.authservice.domain.dto.TokenResponse;
import com.yipintsoi.authservice.event.publisher.AuthEventPublisher;
import com.yipintsoi.authservice.exception.AuthException;
import com.yipintsoi.authservice.exception.InvalidTokenException;
import com.yipintsoi.authservice.security.TokenStore;
import com.yipintsoi.authservice.security.oauth.JwtToUserConverter;
import com.yipintsoi.authservice.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final TokenStore tokenStore;
    private final UserDetailsService userDetailsService;
    private final AuthEventPublisher eventPublisher;
    private final JwtToUserConverter jwtToUserConverter;
    
    @Value("${app.jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;
    
    @Value("${app.jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    @Override
    public TokenResponse createToken(Authentication authentication, boolean rememberMe) {
        String username = authentication.getName();

        log.debug("Creating token for user: {}", username);

        // สร้าง access token
        Instant now = Instant.now();
        String accessToken = generateAccessToken(username, now);

        // สร้าง refresh token ถ้าผู้ใช้เลือก "จำฉันไว้"
        String refreshToken = null;
        if (rememberMe) {
            refreshToken = UUID.randomUUID().toString();
            Instant expiryTime = now.plusMillis(refreshTokenExpirationMs);
            tokenStore.saveToken(refreshToken, username, expiryTime);
        }

        // ส่ง event การ login
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ipAddress = Utils.getClientIpAddress(request);
        String[] deviceInfo = Utils.extractDeviceAndBrowserInfo(request);
        eventPublisher.publishLoginEvent(username, deviceInfo[0], ipAddress);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        // ตรวจสอบความถูกต้องของ refresh token
        String username = tokenStore.getUsernameFromToken(refreshToken);
        
        if (username == null) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }
        
        log.debug("Refreshing token for user: {}", username);

        // สร้าง token ใหม่
        Instant now = Instant.now();
        String accessToken = generateAccessToken(username, now);
        String newRefreshToken = UUID.randomUUID().toString();

        // บันทึก refresh token ใหม่
        Instant expiryTime = now.plusMillis(refreshTokenExpirationMs);
        tokenStore.saveToken(newRefreshToken, username, expiryTime);

        // ยกเลิก refresh token เก่า
        tokenStore.removeToken(refreshToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public void revokeToken(String refreshToken) {
        String username = tokenStore.getUsernameFromToken(refreshToken);

        if (username != null) {
            log.debug("Revoking token for user: {}", username);

            // ยกเลิก token
            tokenStore.removeToken(refreshToken);

            // ส่ง event การ logout
            eventPublisher.publishLogoutEvent(username);
        }
    }

    @Override
    public void revokeAllUserTokens(String accessToken) {
        if (validateToken(accessToken)) {
            String username = getUsernameFromToken(accessToken);

            log.debug("Revoking all tokens for user: {}", username);

            // ยกเลิกทุก token ของผู้ใช้
            tokenStore.removeAllUserTokens(username);

            // ส่ง event การ logout
            eventPublisher.publishLogoutEvent(username);
        } else {
            throw new InvalidTokenException("Invalid access token");
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (Exception e) {
            log.debug("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Authentication getAuthentication(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwtToUserConverter.convert(jwt);
    }

    @Override
    public void forceLogout(String username) {
        log.debug("Force logout for user: {}", username);

        // บังคับออกจากระบบ
        tokenStore.forceLogout(username);

        // ส่ง event การ logout
        eventPublisher.publishLogoutEvent(username);
    }

    private String generateAccessToken(String username, Instant now) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusMillis(accessTokenExpirationMs))
                .subject(username)
                .claim("roles", roles)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String getUsernameFromToken(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject();
    }
}