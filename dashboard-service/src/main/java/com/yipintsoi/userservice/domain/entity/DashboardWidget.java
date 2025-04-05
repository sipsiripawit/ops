package com.yipintsoi.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dashboard_widgets", schema = "dashboard")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardWidget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_dashboard_id", nullable = false)
    private UserDashboard userDashboard;
    
    @Column(name = "widget_type", nullable = false)
    private String widgetType;
    
    @Column(name = "widget_config", columnDefinition = "TEXT")
    private String widgetConfig;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
