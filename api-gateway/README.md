# API Gateway

API Gateway นี้ถูกพัฒนาด้วย Spring Cloud Gateway และ Spring Boot 3.4.3 บน Java 21  
เพื่อเป็น entry point สำหรับระบบ microservices ของเรา ซึ่งรองรับการ routing ไปยัง:

- Auth Service (port: 8081)
- Inventory Service (port: 8082)
- Vehicle Service (port: 8083)
- Repair Service (port: 8084)
- Workflow Service (port: 8085)
- OCR Service (port: 8000)
- User Service (port: 8086)
- Notification Service (port: 8090)

## Prerequisites

- **JDK 21** และ **Maven** ติดตั้งในเครื่อง
- Docker (สำหรับ build และ run ผ่าน container)
- PostgreSQL สำหรับฐานข้อมูล (และ Flyway migration สำหรับแต่ละ service)
- Kafka, RabbitMQ และ Redis (สำหรับ event-driven communication และ caching)

## Build and Run

### Using Maven (Local)
1. Build the project:
   ```bash
   mvn clean package
   
2. Run the application:
   ```bash
   java -jar target/api-gateway-1.0-SNAPSHOT.jar

### Using Docker
1. Build Docker image:
   ```bash
   docker build -t api-gateway .

2. Run Docker container:
   ```bash
   docker run -p 8080:8080 api-gateway

## Configuration
ไฟล์ **application.yml** ของ API Gateway มีการตั้งค่าดังนี้:

- **Server Port:** 8080
- **Management Endpoints:** เปิด expose endpoints เช่น health, info, prometheus
- **Resilience4j:** ตั้งค่า circuit breaker สำหรับ API Gateway
- **Routing Configuration:**  
  กำหนด routes สำหรับ microservices ต่าง ๆ ดังนี้:
    - `/auth/**` → ส่งไปที่ Auth Service (http://localhost:8081)
    - `/inventory/**` → ส่งไปที่ Inventory Service (http://localhost:8082)
    - `/vehicle/**` → ส่งไปที่ Vehicle Service (http://localhost:8083)
    - `/repair/**` → ส่งไปที่ Repair Service (http://localhost:8084)
    - `/workflow/**` → ส่งไปที่ Workflow Service (http://localhost:8085)
    - `/ocr/**` → ส่งไปที่ OCR Service (http://localhost:8000)
    - `/user/**` → ส่งไปที่ User Service (http://localhost:8086)
    - `/notifications/**` → ส่งไปที่ Notification Service (http://localhost:8090)
- **JWT:** ใช้สำหรับ token validation โดยรับค่า secret จาก configuration

## API Endpoints

API Gateway จะทำหน้าที่ route requests ไปยัง microservices ดังนี้:
- **/auth/** → Auth Service
- **/inventory/** → Inventory Service
- **/vehicle/** → Vehicle Service
- **/repair/** → Repair Service
- **/workflow/** → Workflow Service
- **/ocr/** → OCR Service
- **/user/** → User Service
- **/notifications/** → Notification Service

## License

This project is licensed under the MIT License.
