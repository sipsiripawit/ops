package com.yipintsoi.userservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Client สำหรับติดต่อกับ Auth Service
 */
@Slf4j
@Component
public class AuthServiceClient {

    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public AuthServiceClient(RestTemplate restTemplate, @Value("${services.auth-service.url:http://auth-service:8081}") String authServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }

    /**
     * ตรวจสอบความถูกต้องของ token
     * @param token JWT token ที่ต้องการตรวจสอบ
     * @return true ถ้า token ถูกต้อง
     */
    public boolean validateToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Void> response = restTemplate.exchange(
                authServiceUrl + "/api/auth/validate-token",
                HttpMethod.GET,
                entity,
                Void.class
            );
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Error validating token with auth service", e);
            return false;
        }
    }
    
    /**
     * ดึงข้อมูลผู้ใช้จาก token
     * @param token JWT token
     * @return ข้อมูลผู้ใช้หรือ null ถ้าไม่สามารถดึงข้อมูลได้
     */
    public UserInfo getUserInfoFromToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            ResponseEntity<UserInfo> response = restTemplate.exchange(
                authServiceUrl + "/api/auth/user-info",
                HttpMethod.GET,
                entity,
                UserInfo.class
            );
            
            return response.getBody();
        } catch (Exception e) {
            log.error("Error getting user info from token", e);
            return null;
        }
    }
    
    /**
     * DTO สำหรับข้อมูลผู้ใช้จาก Auth Service
     */
    public static class UserInfo {
        private Integer id;
        private String username;
        private String email;
        
        // Getters และ Setters
        public Integer getId() {
            return id;
        }
        
        public void setId(Integer id) {
            this.id = id;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
    }
}