package com.yipintsoi.userservice.security;

import com.yipintsoi.userservice.client.AuthServiceClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter สำหรับตรวจสอบ JWT Token จาก Auth Service
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthServiceClient authServiceClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            String token = extractTokenFromRequest(request);
            
            if (StringUtils.hasText(token) && authServiceClient.validateToken(token)) {
                // Token ถูกต้อง
                // อาจเพิ่มข้อมูลจาก token ไว้ใน request attributes เพื่อให้ controller ใช้งานได้
                AuthServiceClient.UserInfo userInfo = authServiceClient.getUserInfoFromToken(token);
                if (userInfo != null) {
                    request.setAttribute("userInfo", userInfo);
                }
            } else {
                log.debug("Invalid token or no token provided");
            }
        } catch (Exception e) {
            log.error("Could not validate token", e);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}