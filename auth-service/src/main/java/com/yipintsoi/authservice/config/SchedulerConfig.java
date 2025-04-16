package com.yipintsoi.authservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    // ไม่จำเป็นต้องมีโค้ดเพิ่มเติม เนื่องจาก @EnableScheduling จะเปิดใช้งาน scheduling อัตโนมัติ
}