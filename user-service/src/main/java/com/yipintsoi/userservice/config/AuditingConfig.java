package com.yipintsoi.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class AuditingConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        // ใน production ใช้ SecurityContextHolder เพื่อนำ username ของผู้ใช้จริงมาใช้งาน
        return () -> Optional.of("system");
    }
}
