package com.yipintsoi.apigateway.service;

import org.springframework.stereotype.Service;

@Service
public class GatewayService {
    // ตัวอย่าง service สำหรับจัดการ logic ใน API Gateway (ถ้าต้องการ)
    public String processRequest(String input) {
        // Logic ประมวลผลเพิ่มเติม
        return input;
    }
}
