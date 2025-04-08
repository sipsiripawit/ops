package com.yipintsoi.authservice.domain.entity;

import com.yipintsoi.authservice.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

/**
 * Entity สำหรับเก็บข้อมูล Refresh Token
 */
@Entity
@Table(name = "refresh_tokens", schema = "auth")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

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

    @Column(name = "is_revoked")
    private boolean revoked = false;
}