package com.yipintsoi.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_dashboards", schema = "dashboard")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    
    @ManyToOne
    @JoinColumn(name = "template_id")
    private DashboardTemplate template;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
