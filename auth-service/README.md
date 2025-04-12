# Auth Service

Auth Service นี้ถูกออกแบบมาเพื่อจัดการ Authentication และ Authorization โดยรองรับการจัดการ JWT, Refresh Token, RBAC, Single Active Session และอื่น ๆ  
นอกจากนี้ยังรองรับการส่ง event ผ่าน Kafka, RabbitMQ และ caching ผ่าน Redis  
Flyway ถูกใช้ในการจัดการ migration สำหรับสร้างตารางใน schema `auth`

## ภาพรวม
ระบบนี้ถูกพัฒนาเพื่อรองรับการ Login แบบ Single Session ซึ่งหมายความว่าผู้ใช้แต่ละคนจะสามารถเข้าสู่ระบบได้เพียงหนึ่ง Session เท่านั้นในแต่ละช่วงเวลา หากมีการเข้าสู่ระบบจากอุปกรณ์หรือเบราว์เซอร์อื่น Session เก่าจะถูกยกเลิกอัตโนมัติ

## คุณสมบัติหลัก

1. **Single Session Enforcement**: ระบบจะไม่อนุญาตให้มีการ Login ซ้อนหากผู้ใช้ยังมี Session ที่ Active อยู่
2. **Device และ Browser Tracking**: บันทึกข้อมูลอุปกรณ์และเบราว์เซอร์ที่ใช้เข้าสู่ระบบ
3. **Automatic Session Cleanup**: ระบบจะตรวจสอบและยกเลิก Session ที่ไม่มีการใช้งานเกิน 30 นาที
4. **Session Activity Tracking**: บันทึกเวลาการใช้งานล่าสุดของผู้ใช้เพื่อตรวจสอบว่า Session ยังคงใช้งานอยู่หรือไม่

## การทำงานของระบบ

### การเข้าสู่ระบบ (Login)
เมื่อผู้ใช้เข้าสู่ระบบ ระบบจะตรวจสอบว่ามี Session ที่ Active อยู่แล้วหรือไม่:
- ถ้ามี Session ที่ Active อยู่ ระบบจะยกเลิก Session เก่าก่อนสร้าง Session ใหม่
- ระบบจะบันทึกข้อมูลอุปกรณ์ เบราว์เซอร์ และ IP Address ของผู้ใช้

### การใช้งาน Session
- Session จะมีอายุตามที่กำหนด (ค่าเริ่มต้นคือ 7 วัน)
- ทุกครั้งที่มีการใช้งาน (เช่น การ Refresh Token) ระบบจะอัปเดตเวลาการใช้งานล่าสุด

### การออกจากระบบ (Logout)
- เมื่อผู้ใช้ออกจากระบบ Session ปัจจุบันจะถูกยกเลิกทันที

### การทำความสะอาด Session
- ระบบมีการทำงานตามกำหนดเวลาทุก 5 นาทีเพื่อตรวจสอบและยกเลิก Session ที่ไม่มีการใช้งานเกิน 30 นาที

## การตั้งค่า

คุณสามารถเปิดหรือปิดการบังคับใช้ Single Session ได้ผ่านไฟล์ `application.yml`:

```yaml
app:
  force-single-session: true  # true เพื่อเปิดใช้ single session, false เพื่อปิดใช้งาน
```

## ตารางฐานข้อมูล

ระบบใช้ตาราง `user_sessions` ในการเก็บข้อมูล Session ซึ่งมี Schema ดังนี้:

- `id`: รหัส Session (Primary Key)
- `user_id`: รหัสผู้ใช้ (Foreign Key ไปยังตาราง `users`)
- `refresh_token`: Refresh Token ที่ใช้สำหรับ Session นี้
- `ip_address`: IP Address ของผู้ใช้
- `active`: สถานะว่า Session ยังคงใช้งานอยู่หรือไม่
- `expires_at`: เวลาที่ Session จะหมดอายุ
- `device_info`: ข้อมูลอุปกรณ์ที่ใช้เข้าสู่ระบบ
- `browser_info`: ข้อมูลเบราว์เซอร์ที่ใช้เข้าสู่ระบบ
- `last_activity_timestamp`: เวลาการใช้งานล่าสุด
- `created_by`, `created_date`, `updated_by`, `updated_date`: ข้อมูล Audit

## การทดสอบระบบ

ในการพัฒนาระบบ มีการสร้างข้อมูลทดสอบใน Migration Script เพื่อทดสอบฟังก์ชันต่างๆ ของระบบ Single Session:

1. **Active Session**: Session ที่ยังคงใช้งานอยู่
2. **Inactive Session**: Session ที่ถูกยกเลิกแล้ว
3. **Expired Session**: Session ที่หมดอายุแล้ว
4. **Session ที่ไม่มีการใช้งาน**: Session ที่ไม่มีการใช้งานเกินเวลาที่กำหนด (30 นาที)

## คำแนะนำสำหรับการใช้งาน

1. **การพัฒนา Frontend**: Frontend ควรมีการจัดการกรณีที่ Session ถูกยกเลิกเนื่องจากมีการเข้าสู่ระบบจากที่อื่น โดยอาจแสดงข้อความแจ้งเตือนและนำผู้ใช้กลับไปยังหน้า Login
2. **ความปลอดภัย**: ระบบ Single Session ช่วยเพิ่มความปลอดภัยโดยป้องกันการใช้งานพร้อมกันหลายอุปกรณ์ แต่ไม่ควรใช้เป็นมาตรการความปลอดภัยหลักเพียงอย่างเดียว
3. **การปรับแต่ง**: คุณสามารถปรับระยะเวลาการหมดอายุของ Session และระยะเวลาการตรวจสอบการไม่มีการใช้งานได้ตามความเหมาะสม

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
