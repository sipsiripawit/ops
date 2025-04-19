package com.yipintsoi.userservice.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yipintsoi.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Listener สำหรับรับ events จาก Auth Service
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthEventListener {

    private final ObjectMapper objectMapper;
    private final UserProfileService userProfileService;

    /**
     * รับ events จาก Kafka topic "auth-events"
     */
    @KafkaListener(topics = "${app.topic.auth-events:auth-events}", groupId = "user-service-group")
    public void handleAuthEvent(String message) {
        try {
            log.debug("Received auth event: {}", message);
            JsonNode eventNode = objectMapper.readTree(message);

            String eventType = eventNode.get("eventType").asText();
            String username = eventNode.get("username").asText();

            switch (eventType) {
                case "LOGIN":
                    handleLoginEvent(eventNode);
                    break;
                case "LOGOUT":
                    handleLogoutEvent(eventNode);
                    break;
                case "FORCE_LOGOUT":
                    handleForceLogoutEvent(eventNode);
                    break;
                case "PERMISSION_CHANGED":
                    handlePermissionChangedEvent(eventNode);
                    break;
                case "USER_CREATED":
                    handleUserCreatedEvent(eventNode);
                    break;
                case "USER_UPDATED":
                    handleUserUpdatedEvent(eventNode);
                    break;
                default:
                    log.warn("Unknown auth event type: {}", eventType);
            }
        } catch (Exception e) {
            log.error("Error processing auth event", e);
        }
    }

    private void handleLoginEvent(JsonNode eventNode) {
        log.debug("Processing login event");
        // อัปเดตเวลาล็อกอินล่าสุดของผู้ใช้ หรือทำการบันทึกข้อมูล session (ถ้าต้องการ)
        String username = eventNode.get("username").asText();
        // อาจจะมีการเรียกใช้ userProfileService เพื่ออัปเดตข้อมูล
    }

    private void handleLogoutEvent(JsonNode eventNode) {
        log.debug("Processing logout event");
        String username = eventNode.get("username").asText();
        // อาจจะมีการอัปเดตสถานะหรือเวลาล็อกเอาท์ล่าสุด
    }

    private void handleForceLogoutEvent(JsonNode eventNode) {
        log.debug("Processing force logout event");
        String username = eventNode.get("username").asText();
        String adminUsername = eventNode.path("metadata").path("adminUsername").asText();
        log.info("User {} was forcefully logged out by admin {}", username, adminUsername);
    }

    private void handlePermissionChangedEvent(JsonNode eventNode) {
        log.debug("Processing permission changed event");
        // อาจจะมีการอัปเดตข้อมูลสิทธิ์ในฐานข้อมูลของ User Service
    }

    private void handleUserCreatedEvent(JsonNode eventNode) {
        log.debug("Processing user created event");
        try {
            String username = eventNode.get("username").asText();
            Integer authUserId = eventNode.get("userId").asInt();
            String email = eventNode.get("email").asText();

            // ตรวจสอบว่ามีโปรไฟล์ผู้ใช้นี้อยู่แล้วหรือไม่
            if (!userProfileService.existsByAuthUserId(authUserId)) {
                // สร้างโปรไฟล์ผู้ใช้ใหม่
                userProfileService.createUserProfileFromAuthUser(authUserId, username, email);
                log.info("Created new user profile for auth user ID: {}", authUserId);
            }
        } catch (Exception e) {
            log.error("Error creating user profile from auth event", e);
        }
    }

    private void handleUserUpdatedEvent(JsonNode eventNode) {
        log.debug("Processing user updated event");
        try {
            Integer authUserId = eventNode.get("userId").asInt();
            String email = eventNode.get("email").asText();

            // อัปเดตอีเมลในโปรไฟล์ผู้ใช้
            userProfileService.updateUserEmailByAuthUserId(authUserId, email);
            log.info("Updated email for user profile with auth user ID: {}", authUserId);
        } catch (Exception e) {
            log.error("Error updating user profile from auth event", e);
        }
    }
}