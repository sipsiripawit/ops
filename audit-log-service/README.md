# Audit Log Service

บริการสำหรับเก็บบันทึกการกระทำ (action/audit logs) ของระบบ microservices  
ใช้สำหรับตรวจสอบและติดตามการเปลี่ยนแปลงและกิจกรรมต่าง ๆ ในระบบ

## การใช้งาน

### Build and Run

#### ด้วย Maven
1. Build โปรเจค:
   ```bash
   mvn clean package

2. รันโปรเจค:
   ```bash
   java -jar target/audit-log-service-1.0-SNAPSHOT.jar

#### ด้วย Docker
1. Build Docker image:
   ```bash
   docker build -t audit-log-service .

2. รัน Docker container:
   ```bash
   docker run -p 8090:8090 audit-log-service

## Configuration
- ตั้งค่า datasource, Flyway, logging, management, และ resilience4j ในไฟล์ `application.yml`
- ตั้งค่า Kafka (bootstrap-servers), RabbitMQ (host, queue) และ Redis ในไฟล์ `application.yml`

## API Endpoints
- **POST /audit-logs:** สร้าง audit log
- **GET /audit-logs:** ดึงข้อมูล audit logs ทั้งหมด

## Project Structure
    audit-log-service/
    ├── pom.xml
    ├── Dockerfile
    ├── README.md
    └── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── yipintsoi
    │   │           └── auditlogservice
    │   │               ├── AuditLogServiceApplication.java
    │   │               ├── config
    │   │               │   ├── KafkaProducerConfig.java
    │   │               │   ├── KafkaConsumerConfig.java
    │   │               │   └── RabbitMQConfig.java
    │   │               ├── controller
    │   │               │   └── AuditLogController.java
    │   │               ├── domain
    │   │               │   └── entity
    │   │               │       └── AuditLog.java
    │   │               ├── exception
    │   │               │   ├── CustomException.java
    │   │               │   ├── GlobalExceptionHandler.java
    │   │               │   └── ProblemDetails.java
    │   │               ├── repository
    │   │               │   └── AuditLogRepository.java
    │   │               ├── response
    │   │               │   └── ApiResponse.java
    │   │               ├── service
    │   │               │   └── impl
    │   │               │       └── AuditLogServiceIml.java
    │   │               │   └── AuditLogService.java
    │   └── resources
    │       ├── application.yml
    │       └── db
    │           └── migration
    │               └── V1__Create_audit_logs.sql
    └── test
      └── java
         └── com
            └── yipintsoi
               └── auditlogservice
                  └── controller
                     └── AuditLogControllerTest.java

## License

This project is licensed under the MIT License.
