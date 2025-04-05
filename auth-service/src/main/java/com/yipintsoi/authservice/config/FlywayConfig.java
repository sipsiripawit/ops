package com.yipintsoi.authservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;

@Configuration
public class FlywayConfig {

    /**
     * ถ้าต้องการปรับแต่ง Strategy, เช่น Clean DB ก่อน migrate
     */
    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            // flyway.clean(); // ถ้าต้องการล้างตารางก่อน
            flyway.migrate();
        };
    }
}
