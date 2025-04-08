package com.yipintsoi.authservice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * คำขอรีเซ็ตรหัสผ่าน
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {
    @NotBlank(message = "ชื่อผู้ใช้ไม่สามารถเป็นค่าว่างได้")
    private String username;
}
