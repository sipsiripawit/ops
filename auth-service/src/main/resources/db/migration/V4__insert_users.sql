-- สร้างข้อมูลผู้ใช้งานเริ่มต้นสำหรับระบบ SmartOps Suite

-- เพิ่มข้อมูลผู้ใช้ (รหัสผ่าน: bcrypt ของ "password123")
INSERT INTO users (username, email, password, status, active, created_by, created_date)
VALUES
    -- ผู้ดูแลระบบ
    ('admin', 'admin@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),

    -- ผู้จัดการสต็อก
    ('stock.manager', 'stock.manager@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),

    -- เจ้าหน้าที่สต็อก
    ('stock.staff1', 'stock.staff1@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),
    ('stock.staff2', 'stock.staff2@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),

    -- ผู้จัดการยานพาหนะ
    ('vehicle.manager', 'vehicle.manager@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),

    -- เจ้าหน้าที่ยานพาหนะ
    ('vehicle.staff1', 'vehicle.staff1@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),
    ('vehicle.staff2', 'vehicle.staff2@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),

    -- ผู้จัดการงานซ่อม
    ('repair.manager', 'repair.manager@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),

    -- ช่างซ่อม
    ('repair.tech1', 'repair.tech1@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),
    ('repair.tech2', 'repair.tech2@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),

    -- ฝ่ายการเงิน
    ('finance.manager', 'finance.manager@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),
    ('finance.staff', 'finance.staff@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),

    -- หัวหน้าแผนก
    ('dept.head1', 'dept.head1@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),
    ('dept.head2', 'dept.head2@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),

    -- ผู้ใช้ทั่วไป
    ('user1', 'user1@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),
    ('user2', 'user2@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),
    ('user3', 'user3@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),

    -- ผู้ดูรายงาน
    ('report.viewer1', 'report.viewer1@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),
    ('report.viewer2', 'report.viewer2@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),

    -- นักพัฒนา API
    ('api.developer', 'api.developer@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'ACTIVE', true, 'system', CURRENT_TIMESTAMP),

    -- ผู้ใช้ที่ไม่ได้เปิดใช้งาน (Inactive)
    ('inactive.user', 'inactive.user@yipintsoi.com', '$2a$10$NpUUAJD5.DZslP4.GvzSAuLv43YzGbrHtNB3YhY/JFk14vMpWXfXi', 'INACTIVE', false, 'system', CURRENT_TIMESTAMP);

-- เพิ่มข้อมูล user_roles
-- Admin
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE name = 'ADMIN'), 'system', CURRENT_TIMESTAMP);

-- Stock Manager
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES ((SELECT id FROM users WHERE username = 'stock.manager'), (SELECT id FROM roles WHERE name = 'STOCK_MANAGER'), 'system', CURRENT_TIMESTAMP);

-- Stock Staff
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES
    ((SELECT id FROM users WHERE username = 'stock.staff1'), (SELECT id FROM roles WHERE name = 'STOCK_STAFF'), 'system', CURRENT_TIMESTAMP),
    ((SELECT id FROM users WHERE username = 'stock.staff2'), (SELECT id FROM roles WHERE name = 'STOCK_STAFF'), 'system', CURRENT_TIMESTAMP);

-- Vehicle Manager
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES ((SELECT id FROM users WHERE username = 'vehicle.manager'), (SELECT id FROM roles WHERE name = 'VEHICLE_MANAGER'), 'system', CURRENT_TIMESTAMP);

-- Vehicle Staff
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES
    ((SELECT id FROM users WHERE username = 'vehicle.staff1'), (SELECT id FROM roles WHERE name = 'VEHICLE_STAFF'), 'system', CURRENT_TIMESTAMP),
    ((SELECT id FROM users WHERE username = 'vehicle.staff2'), (SELECT id FROM roles WHERE name = 'VEHICLE_STAFF'), 'system', CURRENT_TIMESTAMP);

-- Repair Manager
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES ((SELECT id FROM users WHERE username = 'repair.manager'), (SELECT id FROM roles WHERE name = 'REPAIR_MANAGER'), 'system', CURRENT_TIMESTAMP);

-- Repair Technicians
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES
    ((SELECT id FROM users WHERE username = 'repair.tech1'), (SELECT id FROM roles WHERE name = 'REPAIR_TECHNICIAN'), 'system', CURRENT_TIMESTAMP),
    ((SELECT id FROM users WHERE username = 'repair.tech2'), (SELECT id FROM roles WHERE name = 'REPAIR_TECHNICIAN'), 'system', CURRENT_TIMESTAMP);

-- Finance
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES
    ((SELECT id FROM users WHERE username = 'finance.manager'), (SELECT id FROM roles WHERE name = 'FINANCE'), 'system', CURRENT_TIMESTAMP),
    ((SELECT id FROM users WHERE username = 'finance.staff'), (SELECT id FROM roles WHERE name = 'FINANCE'), 'system', CURRENT_TIMESTAMP);

-- Department Heads
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES
    ((SELECT id FROM users WHERE username = 'dept.head1'), (SELECT id FROM roles WHERE name = 'DEPARTMENT_HEAD'), 'system', CURRENT_TIMESTAMP),
    ((SELECT id FROM users WHERE username = 'dept.head2'), (SELECT id FROM roles WHERE name = 'DEPARTMENT_HEAD'), 'system', CURRENT_TIMESTAMP);

-- Standard Users
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES
    ((SELECT id FROM users WHERE username = 'user1'), (SELECT id FROM roles WHERE name = 'STANDARD_USER'), 'system', CURRENT_TIMESTAMP),
    ((SELECT id FROM users WHERE username = 'user2'), (SELECT id FROM roles WHERE name = 'STANDARD_USER'), 'system', CURRENT_TIMESTAMP),
    ((SELECT id FROM users WHERE username = 'user3'), (SELECT id FROM roles WHERE name = 'STANDARD_USER'), 'system', CURRENT_TIMESTAMP);

-- Report Viewers
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES
    ((SELECT id FROM users WHERE username = 'report.viewer1'), (SELECT id FROM roles WHERE name = 'REPORT_VIEWER'), 'system', CURRENT_TIMESTAMP),
    ((SELECT id FROM users WHERE username = 'report.viewer2'), (SELECT id FROM roles WHERE name = 'REPORT_VIEWER'), 'system', CURRENT_TIMESTAMP);

-- API Developer
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES ((SELECT id FROM users WHERE username = 'api.developer'), (SELECT id FROM roles WHERE name = 'API_DEVELOPER'), 'system', CURRENT_TIMESTAMP);

-- เพิ่ม Role เพิ่มเติมให้กับบางผู้ใช้ (Multi-role)

-- ให้ Stock Manager มีสิทธิ์ดูรายงานด้วย
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES ((SELECT id FROM users WHERE username = 'stock.manager'), (SELECT id FROM roles WHERE name = 'REPORT_VIEWER'), 'system', CURRENT_TIMESTAMP);

-- ให้ Vehicle Manager มีสิทธิ์ดูรายงานด้วย
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES ((SELECT id FROM users WHERE username = 'vehicle.manager'), (SELECT id FROM roles WHERE name = 'REPORT_VIEWER'), 'system', CURRENT_TIMESTAMP);

-- ให้ Repair Manager มีสิทธิ์ดูรายงานด้วย
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES ((SELECT id FROM users WHERE username = 'repair.manager'), (SELECT id FROM roles WHERE name = 'REPORT_VIEWER'), 'system', CURRENT_TIMESTAMP);

-- ให้ Department Head สามารถจองรถได้ด้วย
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES
    ((SELECT id FROM users WHERE username = 'dept.head1'), (SELECT id FROM roles WHERE name = 'STANDARD_USER'), 'system', CURRENT_TIMESTAMP),
    ((SELECT id FROM users WHERE username = 'dept.head2'), (SELECT id FROM roles WHERE name = 'STANDARD_USER'), 'system', CURRENT_TIMESTAMP);

-- ให้ user1 สามารถดูรายงานได้ด้วย
INSERT INTO user_roles (user_id, role_id, created_by, created_date)
VALUES ((SELECT id FROM users WHERE username = 'user1'), (SELECT id FROM roles WHERE name = 'REPORT_VIEWER'), 'system', CURRENT_TIMESTAMP);