CREATE SCHEMA IF NOT EXISTS "user";

CREATE TABLE IF NOT EXISTS "user".user_profiles (
    id SERIAL PRIMARY KEY,
    auth_user_id INTEGER,  -- สามารถใช้เป็น foreign key จาก Auth Service (ถ้ารวม DB กัน) หรือเก็บ reference id
    full_name VARCHAR(100),
    phone_number VARCHAR(20),
    address TEXT,
    preferences TEXT,  -- JSON configuration สำหรับการตั้งค่า
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
