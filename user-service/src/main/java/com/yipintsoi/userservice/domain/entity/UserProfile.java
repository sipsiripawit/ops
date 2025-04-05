package com.yipintsoi.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles", schema = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference ID จาก Auth Service (ถ้ามี)
    @Column(name = "auth_user_id")
    private Integer authUserId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String address;

    // JSON ที่เก็บการตั้งค่าต่าง ๆ ของผู้ใช้ เช่น theme, notification preferences เป็นต้น
    @Column(columnDefinition = "TEXT")
    private String preferences;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
