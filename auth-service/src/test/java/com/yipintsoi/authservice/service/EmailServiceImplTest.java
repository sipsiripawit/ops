package com.yipintsoi.authservice.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class EmailServiceImplTest {

    @Autowired
    private EmailService emailService;
    
    @MockBean
    private JavaMailSender mailSender;
    
    @Test
    void shouldSendPasswordResetEmail() {
        // Given
        String to = "test@example.com";
        String resetLink = "https://yourdomain.com/reset?token=abc123";
        
        // When
        emailService.sendPasswordResetEmail(to, resetLink);
        
        // Then
        // ตรวจสอบว่า mailSender.send() ถูกเรียกด้วยพารามิเตอร์ที่ถูกต้อง
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}