CREATE SCHEMA IF NOT EXISTS notification;

-- ตาราง notification_logs สำหรับเก็บ log การแจ้งเตือน
CREATE TABLE IF NOT EXISTS notification.notification_logs (
    id SERIAL PRIMARY KEY,
    notification_type VARCHAR(50),
    message TEXT,
    user_id INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- ตาราง audit_logs สำหรับเก็บ log การกระทำ (Audit Log)
CREATE TABLE IF NOT EXISTS notification.audit_logs (
    id SERIAL PRIMARY KEY,
    action VARCHAR(100),
    entity VARCHAR(100),
    entity_id INTEGER,
    old_value TEXT,
    new_value TEXT,
    user_id INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
