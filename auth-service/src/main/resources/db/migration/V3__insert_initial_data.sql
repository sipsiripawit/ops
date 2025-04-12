-- สร้างข้อมูลเริ่มต้นสำหรับ SmartOps Suite

-- เพิ่มข้อมูล permissions ตามโมดูลต่างๆ
INSERT INTO permissions (code, description, active, created_by, created_date)
VALUES
    -- Stock Management Permissions
    ('STOCK_VIEW', 'ดูข้อมูลสินค้าคงคลัง', true, 'system', CURRENT_TIMESTAMP),
    ('STOCK_CREATE', 'เพิ่มข้อมูลสินค้าใหม่', true, 'system', CURRENT_TIMESTAMP),
    ('STOCK_UPDATE', 'แก้ไขข้อมูลสินค้า', true, 'system', CURRENT_TIMESTAMP),
    ('STOCK_DELETE', 'ลบข้อมูลสินค้า', true, 'system', CURRENT_TIMESTAMP),
    ('STOCK_BORROW', 'ยืมสินค้า', true, 'system', CURRENT_TIMESTAMP),
    ('STOCK_RETURN', 'คืนสินค้า', true, 'system', CURRENT_TIMESTAMP),
    ('STOCK_WITHDRAW', 'เบิกสินค้า', true, 'system', CURRENT_TIMESTAMP),
    ('STOCK_IMPORT', 'นำเข้าข้อมูลสินค้า', true, 'system', CURRENT_TIMESTAMP),
    ('STOCK_EXPORT', 'ส่งออกข้อมูลสินค้า', true, 'system', CURRENT_TIMESTAMP),

    -- Vehicle Booking Permissions
    ('VEHICLE_VIEW', 'ดูข้อมูลรถและการจอง', true, 'system', CURRENT_TIMESTAMP),
    ('VEHICLE_BOOK', 'จองรถ', true, 'system', CURRENT_TIMESTAMP),
    ('VEHICLE_CANCEL', 'ยกเลิกการจองรถ', true, 'system', CURRENT_TIMESTAMP),
    ('VEHICLE_APPROVE', 'อนุมัติการจองรถ', true, 'system', CURRENT_TIMESTAMP),
    ('VEHICLE_REJECT', 'ปฏิเสธการจองรถ', true, 'system', CURRENT_TIMESTAMP),
    ('VEHICLE_CHECKIN', 'เช็คอินรถ', true, 'system', CURRENT_TIMESTAMP),
    ('VEHICLE_CHECKOUT', 'เช็คเอาท์รถ', true, 'system', CURRENT_TIMESTAMP),
    ('VEHICLE_MANAGE', 'จัดการข้อมูลรถ', true, 'system', CURRENT_TIMESTAMP),

    -- Repair Management Permissions
    ('REPAIR_VIEW', 'ดูข้อมูลงานซ่อม', true, 'system', CURRENT_TIMESTAMP),
    ('REPAIR_CREATE', 'สร้างงานซ่อม', true, 'system', CURRENT_TIMESTAMP),
    ('REPAIR_UPDATE', 'อัปเดตสถานะงานซ่อม', true, 'system', CURRENT_TIMESTAMP),
    ('REPAIR_CLOSE', 'ปิดงานซ่อม', true, 'system', CURRENT_TIMESTAMP),
    ('REPAIR_COST', 'บันทึกค่าใช้จ่ายการซ่อม', true, 'system', CURRENT_TIMESTAMP),
    ('REPAIR_PRIORITY', 'กำหนดลำดับความสำคัญงานซ่อม', true, 'system', CURRENT_TIMESTAMP),

    -- Dashboard & Reporting Permissions
    ('DASHBOARD_VIEW', 'ดูแดชบอร์ด', true, 'system', CURRENT_TIMESTAMP),
    ('DASHBOARD_CUSTOMIZE', 'ปรับแต่งแดชบอร์ด', true, 'system', CURRENT_TIMESTAMP),
    ('REPORT_VIEW', 'ดูรายงาน', true, 'system', CURRENT_TIMESTAMP),
    ('REPORT_EXPORT', 'ส่งออกรายงาน', true, 'system', CURRENT_TIMESTAMP),
    ('REPORT_CREATE', 'สร้างรายงานใหม่', true, 'system', CURRENT_TIMESTAMP),

    -- API & Integration Permissions
    ('API_ACCESS', 'เข้าถึง API', true, 'system', CURRENT_TIMESTAMP),
    ('API_MANAGE', 'จัดการการเชื่อมต่อ API', true, 'system', CURRENT_TIMESTAMP),
    ('DB_LINK_MANAGE', 'จัดการการเชื่อมต่อฐานข้อมูล', true, 'system', CURRENT_TIMESTAMP),

    -- OCR Integration Permissions
    ('OCR_USE', 'ใช้งานระบบ OCR', true, 'system', CURRENT_TIMESTAMP),
    ('OCR_MANAGE', 'จัดการการตั้งค่า OCR', true, 'system', CURRENT_TIMESTAMP),

    -- Workflow Permissions
    ('WORKFLOW_VIEW', 'ดู Workflow', true, 'system', CURRENT_TIMESTAMP),
    ('WORKFLOW_CUSTOMIZE', 'ปรับแต่ง Workflow', true, 'system', CURRENT_TIMESTAMP),

    -- System Administration
    ('SYSTEM_CONFIG', 'ตั้งค่าระบบ', true, 'system', CURRENT_TIMESTAMP),
    ('USER_MANAGE', 'จัดการผู้ใช้', true, 'system', CURRENT_TIMESTAMP),
    ('ROLE_MANAGE', 'จัดการสิทธิ์และบทบาท', true, 'system', CURRENT_TIMESTAMP),
    ('LOGS_VIEW', 'ดูประวัติการใช้งานระบบ', true, 'system', CURRENT_TIMESTAMP);

-- เพิ่มข้อมูล roles
INSERT INTO roles (name, description, active, created_by, created_date)
VALUES
    ('ADMIN', 'ผู้ดูแลระบบ มีสิทธิ์เต็มในการจัดการทุกส่วนของระบบ', true, 'system', CURRENT_TIMESTAMP),
    ('STOCK_MANAGER', 'ผู้จัดการสต็อก รับผิดชอบการจัดการสินค้าคงคลังทั้งหมด', true, 'system', CURRENT_TIMESTAMP),
    ('STOCK_STAFF', 'เจ้าหน้าที่สต็อก ดูแลเรื่องการเบิก-ยืม-คืนสินค้า', true, 'system', CURRENT_TIMESTAMP),
    ('VEHICLE_MANAGER', 'ผู้จัดการยานพาหนะ รับผิดชอบการจัดการและอนุมัติการจองรถ', true, 'system', CURRENT_TIMESTAMP),
    ('VEHICLE_STAFF', 'เจ้าหน้าที่ยานพาหนะ ดูแลการจองและเช็คอิน-เช็คเอาท์รถ', true, 'system', CURRENT_TIMESTAMP),
    ('REPAIR_MANAGER', 'ผู้จัดการงานซ่อม รับผิดชอบการจัดการงานซ่อมทั้งหมด', true, 'system', CURRENT_TIMESTAMP),
    ('REPAIR_TECHNICIAN', 'ช่างซ่อม ดำเนินการซ่อมและบันทึกสถานะงาน', true, 'system', CURRENT_TIMESTAMP),
    ('FINANCE', 'ฝ่ายการเงิน ดูแลค่าใช้จ่ายและงบประมาณ', true, 'system', CURRENT_TIMESTAMP),
    ('DEPARTMENT_HEAD', 'หัวหน้าแผนก มีสิทธิ์ในการอนุมัติและดูรายงาน', true, 'system', CURRENT_TIMESTAMP),
    ('STANDARD_USER', 'ผู้ใช้ทั่วไป สามารถจองรถและดูข้อมูลพื้นฐานได้', true, 'system', CURRENT_TIMESTAMP),
    ('REPORT_VIEWER', 'ผู้ดูรายงาน สามารถดูแดชบอร์ดและรายงานได้', true, 'system', CURRENT_TIMESTAMP),
    ('API_DEVELOPER', 'นักพัฒนา API สำหรับเชื่อมต่อระบบภายนอก', true, 'system', CURRENT_TIMESTAMP);

-- เพิ่มข้อมูล role_permissions - ADMIN (ให้สิทธิ์ทั้งหมด)
INSERT INTO role_permissions (role_id, permission_id, created_by, created_date)
SELECT
    (SELECT id FROM roles WHERE name = 'ADMIN'),
    id,
    'system',
    CURRENT_TIMESTAMP
FROM permissions;

-- เพิ่มข้อมูล role_permissions - STOCK_MANAGER
INSERT INTO role_permissions (role_id, permission_id, created_by, created_date)
SELECT
    (SELECT id FROM roles WHERE name = 'STOCK_MANAGER'),
    id,
    'system',
    CURRENT_TIMESTAMP
FROM permissions
WHERE code IN (
               'STOCK_VIEW', 'STOCK_CREATE', 'STOCK_UPDATE', 'STOCK_DELETE',
               'STOCK_BORROW', 'STOCK_RETURN', 'STOCK_WITHDRAW', 'STOCK_IMPORT', 'STOCK_EXPORT',
               'DASHBOARD_VIEW', 'REPORT_VIEW', 'REPORT_EXPORT'
    );

-- เพิ่มข้อมูล role_permissions - STOCK_STAFF
INSERT INTO role_permissions (role_id, permission_id, created_by, created_date)
SELECT
    (SELECT id FROM roles WHERE name = 'STOCK_STAFF'),
    id,
    'system',
    CURRENT_TIMESTAMP
FROM permissions
WHERE code IN (
               'STOCK_VIEW', 'STOCK_BORROW', 'STOCK_RETURN', 'STOCK_WITHDRAW',
               'DASHBOARD_VIEW'
    );

-- เพิ่มข้อมูล role_permissions - VEHICLE_MANAGER
INSERT INTO role_permissions (role_id, permission_id, created_by, created_date)
SELECT
    (SELECT id FROM roles WHERE name = 'VEHICLE_MANAGER'),
    id,
    'system',
    CURRENT_TIMESTAMP
FROM permissions
WHERE code IN (
               'VEHICLE_VIEW', 'VEHICLE_BOOK', 'VEHICLE_CANCEL', 'VEHICLE_APPROVE', 'VEHICLE_REJECT',
               'VEHICLE_CHECKIN', 'VEHICLE_CHECKOUT', 'VEHICLE_MANAGE',
               'DASHBOARD_VIEW', 'REPORT_VIEW', 'REPORT_EXPORT'
    );

-- เพิ่มข้อมูล role_permissions - VEHICLE_STAFF
INSERT INTO role_permissions (role_id, permission_id, created_by, created_date)
SELECT
    (SELECT id FROM roles WHERE name = 'VEHICLE_STAFF'),
    id,
    'system',
    CURRENT_TIMESTAMP
FROM permissions
WHERE code IN (
               'VEHICLE_VIEW', 'VEHICLE_BOOK', 'VEHICLE_CHECKIN', 'VEHICLE_CHECKOUT',
               'DASHBOARD_VIEW'
    );

-- เพิ่มข้อมูล role_permissions - REPAIR_MANAGER
INSERT INTO role_permissions (role_id, permission_id, created_by, created_date)
SELECT
    (SELECT id FROM roles WHERE name = 'REPAIR_MANAGER'),
    id,
    'system',
    CURRENT_TIMESTAMP
FROM permissions
WHERE code IN (
               'REPAIR_VIEW', 'REPAIR_CREATE', 'REPAIR_UPDATE', 'REPAIR_CLOSE', 'REPAIR_COST', 'REPAIR_PRIORITY',
               'DASHBOARD_VIEW', 'REPORT_VIEW', 'REPORT_EXPORT'
    );

-- เพิ่มข้อมูล role_permissions - REPAIR_TECHNICIAN
INSERT INTO role_permissions (role_id, permission_id, created_by, created_date)
SELECT
    (SELECT id FROM roles WHERE name = 'REPAIR_TECHNICIAN'),
    id,
    'system',
    CURRENT_TIMESTAMP
FROM permissions
WHERE code IN (
               'REPAIR_VIEW', 'REPAIR_UPDATE', 'REPAIR_COST',
               'DASHBOARD_VIEW'
    );

-- เพิ่มข้อมูล role_permissions - FINANCE
INSERT INTO role_permissions (role_id, permission_id, created_by, created_date)
SELECT
    (SELECT id FROM roles WHERE name = 'FINANCE'),
    id,
    'system',
    CURRENT_TIMESTAMP
FROM permissions
WHERE code IN (
               'STOCK_VIEW', 'VEHICLE_VIEW', 'REPAIR_VIEW', 'REPAIR_COST',
               'DASHBOARD_VIEW', 'REPORT_VIEW', 'REPORT_EXPORT', 'REPORT_CREATE'
    );

-- เพิ่มข้อมูล role_permissions - DEPARTMENT_HEAD
INSERT INTO role_permissions (role_id, permission_id, created_by, created_date)
SELECT
    (SELECT id FROM roles WHERE name = 'DEPARTMENT_HEAD'),
    id,
    'system',
    CURRENT_TIMESTAMP
FROM permissions
WHERE code IN (
               'STOCK_VIEW', 'VEHICLE_VIEW', 'VEHICLE_APPROVE', 'VEHICLE_REJECT', 'REPAIR_VIEW',
               'DASHBOARD_VIEW', 'DASHBOARD_CUSTOMIZE', 'REPORT_VIEW', 'REPORT_EXPORT'
    );

-- เพิ่มข้อมูล role_permissions - STANDARD_USER
INSERT INTO role_permissions (role_id, permission_id, created_by, created_date)
SELECT
    (SELECT id FROM roles WHERE name = 'STANDARD_USER'),
    id,
    'system',
    CURRENT_TIMESTAMP
FROM permissions
WHERE code IN (
               'STOCK_VIEW', 'VEHICLE_VIEW', 'VEHICLE_BOOK', 'VEHICLE_CANCEL', 'REPAIR_VIEW', 'REPAIR_CREATE',
               'DASHBOARD_VIEW'
    );

-- เพิ่มข้อมูล role_permissions - REPORT_VIEWER
INSERT INTO role_permissions (role_id, permission_id, created_by, created_date)
SELECT
    (SELECT id FROM roles WHERE name = 'REPORT_VIEWER'),
    id,
    'system',
    CURRENT_TIMESTAMP
FROM permissions
WHERE code IN (
               'DASHBOARD_VIEW', 'DASHBOARD_CUSTOMIZE', 'REPORT_VIEW', 'REPORT_EXPORT'
    );

-- เพิ่มข้อมูล role_permissions - API_DEVELOPER
INSERT INTO role_permissions (role_id, permission_id, created_by, created_date)
SELECT
    (SELECT id FROM roles WHERE name = 'API_DEVELOPER'),
    id,
    'system',
    CURRENT_TIMESTAMP
FROM permissions
WHERE code IN (
               'API_ACCESS', 'API_MANAGE', 'DB_LINK_MANAGE'
    );