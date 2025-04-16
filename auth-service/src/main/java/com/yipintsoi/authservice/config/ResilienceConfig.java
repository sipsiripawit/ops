package com.yipintsoi.authservice.config;

import com.yipintsoi.authservice.common.Constants;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResilienceConfig {

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        return RateLimiterRegistry.ofDefaults();
    }

    @Bean
    public RateLimiter loginRateLimiter(RateLimiterRegistry rateLimiterRegistry) {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .limitForPeriod(10)
                .timeoutDuration(Duration.ofMillis(10))
                .build();

        return rateLimiterRegistry.rateLimiter(Constants.RATE_LIMITER_LOGIN, config);
    }

    @Bean
    public RateLimiter refreshTokenRateLimiter(RateLimiterRegistry rateLimiterRegistry) {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .limitForPeriod(30)
                .timeoutDuration(Duration.ofMillis(10))
                .build();

        return rateLimiterRegistry.rateLimiter(Constants.RATE_LIMITER_REFRESH, config);
    }
}