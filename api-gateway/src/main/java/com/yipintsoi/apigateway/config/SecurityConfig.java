package com.yipintsoi.apigateway.config;

import com.yipintsoi.apigateway.filter.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public GlobalFilter customGlobalFilter() {
        // ใช้ JwtAuthenticationFilter เป็น global filter สำหรับทุก request
        return (exchange, chain) -> jwtAuthenticationFilter.filter(exchange, chain);
    }
}
