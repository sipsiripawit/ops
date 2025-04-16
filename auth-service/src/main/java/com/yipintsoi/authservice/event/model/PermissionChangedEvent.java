package com.yipintsoi.authservice.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionChangedEvent {
    private String username;
    private List<String> permissions;
    private Instant timestamp;
}