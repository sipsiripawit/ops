package com.yipintsoi.authservice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class ResetPasswordRequest {
    @NotBlank(message = "Token ไม่สามารถเป็นค่าว่างได้")
    private String token;
    
    @NotBlank(message = "รหัสผ่านใหม่ไม่สามารถเป็นค่าว่างได้")
    @Size(min = 8, message = "รหัสผ่านต้องมีความยาวอย่างน้อย 8 ตัวอักษร")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
             message = "รหัสผ่านต้องประกอบด้วยตัวอักษรพิมพ์เล็ก พิมพ์ใหญ่ และตัวเลข")
    private String newPassword;
}