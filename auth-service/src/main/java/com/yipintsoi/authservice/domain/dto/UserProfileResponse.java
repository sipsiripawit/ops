package com.yipintsoi.authservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * DTO สำหรับการตอบกลับข้อมูลโปรไฟล์ผู้ใช้
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Integer id;
    private String username;
    private String email;
    private String status;
    private List<String> roles;
    private Set<String> permissions;
}