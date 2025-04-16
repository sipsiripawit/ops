package com.yipintsoi.authservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yipintsoi.authservice.common.Constants;
import com.yipintsoi.authservice.response.ApiResponse;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimiterRegistry rateLimiterRegistry;
    private final ObjectMapper objectMapper;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // เลือก rate limiter ตาม endpoint
        RateLimiter rateLimiter = null;
        if (path.equals("/api/auth/token")) {
            rateLimiter = rateLimiterRegistry.rateLimiter(Constants.RATE_LIMITER_LOGIN);
        } else if (path.equals("/api/auth/refresh")) {
            rateLimiter = rateLimiterRegistry.rateLimiter(Constants.RATE_LIMITER_REFRESH);
        }
        
        // ถ้าไม่มี rate limiter ที่เกี่ยวข้อง ให้ผ่านไปเลย
        if (rateLimiter == null) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // ทดลองขอ permission จาก rate limiter
        boolean permission = rateLimiter.acquirePermission();
        if (permission) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("Rate limit exceeded for path: {}, IP: {}", path, request.getRemoteAddr());
            
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            
            ApiResponse<Void> errorResponse = ApiResponse.error("Too many requests. Please try again later.");
            
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}