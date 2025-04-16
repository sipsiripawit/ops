package com.yipintsoi.authservice.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthEvent {
    private String eventType; // "LOGIN" หรือ "LOGOUT"
    private String username;
    private Instant timestamp;
    private Map<String, Object> metadata = new HashMap<>();
}