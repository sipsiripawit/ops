package com.yipintsoi.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_logs", schema = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "notification_type")
    private String notificationType;
    
    @Column(columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
