package com.yipintsoi.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs", schema = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String action;
    private String entity;
    
    @Column(name = "entity_id")
    private Integer entityId;
    
    @Column(columnDefinition = "TEXT")
    private String oldValue;
    
    @Column(columnDefinition = "TEXT")
    private String newValue;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
