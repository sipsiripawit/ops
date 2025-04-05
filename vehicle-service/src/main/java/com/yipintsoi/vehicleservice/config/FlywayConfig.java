package com.yipintsoi.vehicleservice.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
