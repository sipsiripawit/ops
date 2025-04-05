package com.yipintsoi.vehicleservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

@Configuration
public class AuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // ใน production ให้ดึง username ของผู้ใช้ที่ login จริง
        return () -> Optional.of("system");
    }
}
