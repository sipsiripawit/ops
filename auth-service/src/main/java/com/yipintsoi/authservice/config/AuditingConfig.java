package com.yipintsoi.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

@Configuration
public class AuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // ใน production ใช้ security context ในการระบุ username จริง
        return () -> Optional.of("system");
    }
}
