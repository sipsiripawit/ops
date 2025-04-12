-- ปรับปรุงระบบเพื่อรองรับการ Login แบบ Single Session
-- จะไม่อนุญาตให้มีการ Login ซ้อนขณะที่ผู้ใช้คนเดิมยังใช้งานอยู่

-- อัปเดตตาราง user_sessions เพื่อรองรับการทำ Single Session
-- เพิ่มคอลัมน์เพื่อเก็บข้อมูลอุปกรณ์/เบราว์เซอร์ในกรณีที่ต้องการแสดงข้อมูลให้ผู้ใช้เห็น
ALTER TABLE user_sessions
ADD COLUMN device_info VARCHAR(255),
ADD COLUMN browser_info VARCHAR(255),
ADD COLUMN last_activity_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- เพิ่ม function ในฐานข้อมูล (PostgreSQL) เพื่ออัปเดตเวลาใช้งานล่าสุด
CREATE OR REPLACE FUNCTION update_last_activity()
RETURNS TRIGGER AS $$
BEGIN
   NEW.last_activity_timestamp = CURRENT_TIMESTAMP;
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- สร้าง trigger สำหรับอัปเดตเวลาใช้งานล่าสุดอัตโนมัติเมื่อมีการอัปเดต user_sessions
CREATE TRIGGER update_last_activity_trigger
BEFORE UPDATE ON user_sessions
FOR EACH ROW
EXECUTE FUNCTION update_last_activity();

-- สร้าง function สำหรับเช็ค inactive sessions ที่หมดเวลา (เช่น ไม่มีกิจกรรมเกิน 30 นาที)
CREATE OR REPLACE FUNCTION check_inactive_sessions()
RETURNS void AS $$
BEGIN
   UPDATE user_sessions
   SET active = false
   WHERE active = true
   AND last_activity_timestamp < (CURRENT_TIMESTAMP - INTERVAL '30 minutes');
END;
$$ LANGUAGE plpgsql;

-- นำเข้าตัวอย่าง user_sessions สำหรับทดสอบระบบ Single Session
INSERT INTO user_sessions (user_id, refresh_token, ip_address, active, expires_at, created_by, created_date, device_info, browser_info, last_activity_timestamp)
VALUES
    -- Active session สำหรับทดสอบ admin
    ((SELECT id FROM users WHERE username = 'admin'),
     'eyJhbGciOiJIUzUxMiJ9.admin-active-session-token.signature',
     '192.168.1.100',
     true,
     (CURRENT_TIMESTAMP + INTERVAL '7 days'),
     'system',
     CURRENT_TIMESTAMP,
     'Desktop Computer',
     'Chrome 120.0.0.0',
     CURRENT_TIMESTAMP),

    -- Active session สำหรับทดสอบ stock.manager
    ((SELECT id FROM users WHERE username = 'stock.manager'),
     'eyJhbGciOiJIUzUxMiJ9.stock-manager-active-session-token.signature',
     '192.168.1.101',
     true,
     (CURRENT_TIMESTAMP + INTERVAL '7 days'),
     'system',
     CURRENT_TIMESTAMP,
     'Laptop',
     'Firefox 119.0',
     CURRENT_TIMESTAMP),

    -- Inactive session สำหรับทดสอบ user1
    ((SELECT id FROM users WHERE username = 'user1'),
     'eyJhbGciOiJIUzUxMiJ9.user1-inactive-session-token.signature',
     '192.168.1.102',
     false,
     (CURRENT_TIMESTAMP + INTERVAL '7 days'),
     'system',
     CURRENT_TIMESTAMP,
     'Mobile Phone',
     'Safari Mobile 17.0',
     (CURRENT_TIMESTAMP - INTERVAL '2 hours')),

    -- Session ที่หมดเวลาไปแล้วแต่ยังถูกตั้งเป็น active
    ((SELECT id FROM users WHERE username = 'user2'),
     'eyJhbGciOiJIUzUxMiJ9.user2-expired-but-active-session-token.signature',
     '192.168.1.103',
     true,
     (CURRENT_TIMESTAMP - INTERVAL '1 day'),
     'system',
     CURRENT_TIMESTAMP,
     'Tablet',
     'Edge 120.0',
     (CURRENT_TIMESTAMP - INTERVAL '1 day'));

-- รัน function เพื่อทำการตรวจสอบและอัปเดต sessions ที่หมดเวลา
SELECT check_inactive_sessions();