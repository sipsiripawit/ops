# Auth Service

Auth Service นี้ถูกออกแบบมาเพื่อจัดการ Authentication และ Authorization โดยรองรับการจัดการ JWT, Refresh Token, RBAC, Single Active Session และอื่น ๆ  
นอกจากนี้ยังรองรับการส่ง event ผ่าน Kafka, RabbitMQ และ caching ผ่าน Redis  
Flyway ถูกใช้ในการจัดการ migration สำหรับสร้างตารางใน schema `auth`

## Prerequisites

- **JDK 21** และ **Maven** ติดตั้งในเครื่อง
- PostgreSQL สำหรับฐานข้อมูล (และต้องสร้าง database ชื่อ `ops`)
- Kafka, RabbitMQ และ Redis (ถ้าต้องการใช้งาน integration)
- Docker (ถ้าต้องการ build และ deploy ด้วย container)

## Build and Run

### Using Maven (Local)
1. Build โปรเจคด้วยคำสั่ง:
   ```bash
   mvn clean package

2. รันโปรเจค:
   ```bash
   java -jar target/auth-service-1.0-SNAPSHOT.jar

### Using Docker
1. Build Docker image:
   ```bash
   docker build -t auth-service .

2. รัน Docker container:
   ```bash
   docker run -p 8081:8081 auth-service

## Configuration

ไฟล์ **application.yml** ตั้งค่า:
- **Server Port:** 8081
- **Datasource:** เชื่อมต่อ PostgreSQL ที่ database `ops` และ schema `auth`
- **Flyway:** ใช้สำหรับ migration (script อยู่ที่ `src/main/resources/db/migration`)
- **JWT:** ตั้งค่า secret, expirationMs และ refreshExpirationMs
- **Management:** เปิด expose endpoints เช่น `/actuator/health`, `/actuator/prometheus`
- **Resilience4j:** ตั้งค่า circuit breaker instance สำหรับ authServiceCircuitBreaker
- **Integration:** ตั้งค่า Kafka, RabbitMQ, และ Redis

## API Endpoints

Auth Service มี endpoint หลักดังนี้:
- **POST /login:** สำหรับ login และรับ JWT access token และ refresh token
- **POST /refresh-token:** สำหรับขอ refresh token ใหม่

## License

This project is licensed under the MIT License.
