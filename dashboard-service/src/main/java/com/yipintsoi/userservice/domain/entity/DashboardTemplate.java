package com.yipintsoi.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dashboard_templates", schema = "dashboard")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "template_name", nullable = false)
    private String templateName;
    
    @Column(name = "template_config", columnDefinition = "TEXT")
    private String templateConfig;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
