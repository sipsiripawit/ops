package com.yipintsoi.authservice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * คำขอเข้าสู่ระบบ
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "ชื่อผู้ใช้ไม่สามารถเป็นค่าว่างได้")
    private String username;

    @NotBlank(message = "รหัสผ่านไม่สามารถเป็นค่าว่างได้")
    private String password;

    private boolean rememberMe;
}
