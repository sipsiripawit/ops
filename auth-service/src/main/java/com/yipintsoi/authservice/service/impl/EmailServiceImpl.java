package com.yipintsoi.authservice.service.impl;

import com.yipintsoi.authservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

/**
 * Implementation ของ EmailService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.name}")
    private String appName;

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendPasswordResetEmail(String to, String resetLink) {
        try {
            log.debug("Sending password reset email to: {}", to);
            
            // สร้าง Context สำหรับเทมเพลต
            Context context = new Context();
            context.setVariable("resetLink", resetLink);
            context.setVariable("appName", appName);
            
            // สร้างเนื้อหาอีเมลจากเทมเพลต
            String htmlContent = templateEngine.process("password-reset-email", context);
            
            // สร้างและส่งอีเมล
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, 
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, 
                    StandardCharsets.UTF_8.name());
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(appName + " - รีเซ็ตรหัสผ่าน");
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            
            log.info("Password reset email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to: {}", to, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendRegistrationVerificationEmail(String to, String verificationLink) {
        try {
            log.debug("Sending registration verification email to: {}", to);
            
            // สร้าง Context สำหรับเทมเพลต
            Context context = new Context();
            context.setVariable("verificationLink", verificationLink);
            context.setVariable("appName", appName);
            
            // สร้างเนื้อหาอีเมลจากเทมเพลต
            String htmlContent = templateEngine.process("registration-verification-email", context);
            
            // สร้างและส่งอีเมล
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, 
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, 
                    StandardCharsets.UTF_8.name());
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(appName + " - ยืนยันการลงทะเบียน");
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            
            log.info("Registration verification email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send registration verification email to: {}", to, e);
        }
    }
    
    /**
     * ส่งอีเมลแบบง่าย (ใช้สำหรับทดสอบหรือแบ็คอัพกรณีที่เทมเพลตมีปัญหา)
     * @param to อีเมลปลายทาง
     * @param subject หัวข้ออีเมล
     * @param text เนื้อหาอีเมล
     */
    private void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            
            log.info("Simple email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send simple email to: {}", to, e);
        }
    }
}