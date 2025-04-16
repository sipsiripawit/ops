package com.yipintsoi.authservice.event.publisher;

import com.yipintsoi.authservice.common.Constants;
import com.yipintsoi.authservice.event.model.AuthEvent;
import com.yipintsoi.authservice.event.model.PermissionChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishLoginEvent(String username, String deviceInfo, String ipAddress) {
        log.debug("Publishing login event for user: {}", username);
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("deviceInfo", deviceInfo);
        metadata.put("ipAddress", ipAddress);
        
        AuthEvent event = AuthEvent.builder()
                .eventType(Constants.EVENT_LOGIN)
                .username(username)
                .timestamp(Instant.now())
                .metadata(metadata)
                .build();
        
        kafkaTemplate.send(Constants.TOPIC_AUTH_EVENTS, username, event);
    }

    public void publishLogoutEvent(String username) {
        log.debug("Publishing logout event for user: {}", username);
        
        AuthEvent event = AuthEvent.builder()
                .eventType(Constants.EVENT_LOGOUT)
                .username(username)
                .timestamp(Instant.now())
                .build();
        
        kafkaTemplate.send(Constants.TOPIC_AUTH_EVENTS, username, event);
    }

    public void publishPermissionChangedEvent(String username, List<String> newPermissions) {
        log.debug("Publishing permission changed event for user: {}", username);
        
        PermissionChangedEvent event = PermissionChangedEvent.builder()
                .username(username)
                .permissions(newPermissions)
                .timestamp(Instant.now())
                .build();
        
        kafkaTemplate.send(Constants.TOPIC_AUTH_EVENTS, username, event);
    }

    public void publishForceLogoutEvent(String username, String adminUsername) {
        log.debug("Publishing force logout event for user: {} by admin: {}", username, adminUsername);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("adminUsername", adminUsername);

        AuthEvent event = AuthEvent.builder()
                .eventType(Constants.EVENT_FORCE_LOGOUT)
                .username(username)
                .timestamp(Instant.now())
                .metadata(metadata)
                .build();

        kafkaTemplate.send(Constants.TOPIC_AUTH_EVENTS, username, event);
    }
}