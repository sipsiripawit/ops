# Notification Service

บริการสำหรับจัดการแจ้งเตือนและ Audit Logs ในระบบ microservices  
รวมถึงการ integration กับ Kafka, RabbitMQ และ Redis สำหรับส่งและรับ event message

## การใช้งาน

### Build and Run

#### ด้วย Maven
1. Build โปรเจค:
   ```bash
   mvn clean package

2. รันโปรเจค:
   ```bash
   java -jar target/notification-service-1.0-SNAPSHOT.jar

#### ด้วย Docker
1. Build Docker image:
   ```bash
   docker build -t notification-service .

2. รัน Docker container:
   ```bash
   docker run -p 8090:8090 notification-service

## Configuration
- ตั้งค่า datasource, Flyway, logging, management, และ resilience4j ในไฟล์ `application.yml`
- ตั้งค่า Kafka (bootstrap-servers), RabbitMQ (host, queue) และ Redis ในไฟล์ `application.yml`

## API Endpoints
- **POST /notifications/log:** สร้าง notification log
- **POST /notifications/audit:** สร้าง audit log
- **GET /notifications/log:** ดึงข้อมูล notification logs ทั้งหมด
- **GET /notifications/audit:** ดึงข้อมูล audit logs ทั้งหมด

## Project Structure
    notification-service/
    ├── pom.xml
    ├── Dockerfile
    ├── README.md
    └── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── yipintsoi
    │   │           └── notificationservice
    │   │               ├── NotificationServiceApplication.java
    │   │               ├── config
    │   │               │   ├── KafkaProducerConfig.java
    │   │               │   ├── KafkaConsumerConfig.java
    │   │               │   └── RabbitMQConfig.java
    │   │               ├── controller
    │   │               │   └── NotificationController.java
    │   │               ├── domain
    │   │               │   └── entity
    │   │               │       ├── NotificationLog.java
    │   │               │       └── AuditLog.java
    │   │               ├── exception
    │   │               │   ├── CustomException.java
    │   │               │   ├── GlobalExceptionHandler.java
    │   │               │   └── ProblemDetails.java
    │   │               ├── repository
    │   │               │   ├── NotificationLogRepository.java
    │   │               │   └── AuditLogRepository.java
    │   │               ├── response
    │   │               │   └── ApiResponse.java
    │   │               └── service
    │   │                   └── NotificationService.java
    │   └── resources
    │       ├── application.yml
    │       └── db
    │           └── migration
    │               └── V1__Create_notification_tables.sql
    └── test
    └── java
    └── com
    └── yipintsoi
    └── notificationservice
    └── controller
    └── NotificationControllerTest.java

## License

This project is licensed under the MIT License.
