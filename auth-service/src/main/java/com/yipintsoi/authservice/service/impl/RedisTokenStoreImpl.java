package com.yipintsoi.authservice.service.impl;

import com.yipintsoi.authservice.security.TokenStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisTokenStoreImpl implements TokenStore {

    private final StringRedisTemplate redisTemplate;
    
    @Value("${app.force-single-session:true}")
    private boolean forceSingleSession;
    
    private static final String TOKEN_PREFIX = "auth:token:";
    private static final String USER_TOKENS_PREFIX = "auth:user:tokens:";
    private static final String USER_ACTIVE_SESSION_PREFIX = "auth:user:active-session:";

    @Override
    public void saveToken(String token, String username, Instant expiryTime) {
        String tokenKey = TOKEN_PREFIX + token;
        String userTokensKey = USER_TOKENS_PREFIX + username;
        String userActiveSessionKey = USER_ACTIVE_SESSION_PREFIX + username;
        
        log.debug("Saving token for user: {}", username);
        
        if (forceSingleSession) {
            // ตรวจสอบว่ามี active session หรือไม่
            String existingToken = redisTemplate.opsForValue().get(userActiveSessionKey);
            
            if (existingToken != null) {
                log.debug("Found existing active session for user: {}. Invalidating old session.", username);
                
                // ลบ token เก่า
                redisTemplate.delete(TOKEN_PREFIX + existingToken);
                redisTemplate.opsForSet().remove(userTokensKey, existingToken);
            }
            
            // บันทึก active session ใหม่
            redisTemplate.opsForValue().set(userActiveSessionKey, token);
            redisTemplate.expire(userActiveSessionKey, Duration.between(Instant.now(), expiryTime));
        }
        
        // เก็บความสัมพันธ์ token -> username
        redisTemplate.opsForValue().set(tokenKey, username);
        long timeToLive = Duration.between(Instant.now(), expiryTime).toMillis();
        redisTemplate.expire(tokenKey, Duration.ofMillis(timeToLive));
        
        // เก็บความสัมพันธ์ username -> tokens
        redisTemplate.opsForSet().add(userTokensKey, token);
    }

    @Override
    public String getUsernameFromToken(String token) {
        return redisTemplate.opsForValue().get(TOKEN_PREFIX + token);
    }

    @Override
    public boolean validateToken(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(TOKEN_PREFIX + token));
    }

    @Override
    public void removeToken(String token) {
        String tokenKey = TOKEN_PREFIX + token;
        String username = redisTemplate.opsForValue().get(tokenKey);
        
        if (username != null) {
            log.debug("Removing token for user: {}", username);
            
            // ลบความสัมพันธ์ username -> token
            redisTemplate.opsForSet().remove(USER_TOKENS_PREFIX + username, token);
            
            // ลบความสัมพันธ์ token -> username
            redisTemplate.delete(tokenKey);
            
            // ถ้าเป็น active session ให้ลบด้วย
            String activeToken = redisTemplate.opsForValue().get(USER_ACTIVE_SESSION_PREFIX + username);
            if (token.equals(activeToken)) {
                redisTemplate.delete(USER_ACTIVE_SESSION_PREFIX + username);
            }
        }
    }

    @Override
    public void removeAllUserTokens(String username) {
        log.debug("Removing all tokens for user: {}", username);
        
        String userTokensKey = USER_TOKENS_PREFIX + username;
        String userActiveSessionKey = USER_ACTIVE_SESSION_PREFIX + username;
        
        // ดึงรายการ tokens ทั้งหมดของ user
        Set<String> tokens = redisTemplate.opsForSet().members(userTokensKey);
        
        if (tokens != null) {
            // ลบแต่ละ token
            for (String token : tokens) {
                redisTemplate.delete(TOKEN_PREFIX + token);
            }
            
            // ลบรายการ tokens ของ user
            redisTemplate.delete(userTokensKey);
            
            // ลบ active session
            redisTemplate.delete(userActiveSessionKey);
        }
    }
    
    @Override
    public void forceLogout(String username) {
        log.debug("Force logout user: {}", username);
        removeAllUserTokens(username);
    }
    
    @Override
    public boolean hasActiveSession(String username) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(USER_ACTIVE_SESSION_PREFIX + username));
    }
}