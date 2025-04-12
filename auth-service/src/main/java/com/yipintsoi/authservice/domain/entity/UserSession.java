package com.yipintsoi.authservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.Instant;

@Entity
@Table(name = "user_sessions", schema = "auth")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String refreshToken;

    @Column
    private String ipAddress;

    @Column(nullable = false)
    private Boolean active;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_date")
    private Instant updatedDate;

    @Column(nullable = false)
    private Instant expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // คอลัมน์ใหม่สำหรับ Single Session
    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "browser_info")
    private String browserInfo;

    @Column(name = "last_activity_timestamp")
    private Instant lastActivityTimestamp;
}