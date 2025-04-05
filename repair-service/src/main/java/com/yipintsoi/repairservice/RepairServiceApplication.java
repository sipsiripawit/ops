package com.yipintsoi.repairservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class RepairServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RepairServiceApplication.class, args);
    }
}
