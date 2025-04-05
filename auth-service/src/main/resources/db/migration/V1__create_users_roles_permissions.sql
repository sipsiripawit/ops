-- ตาราง users
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       status VARCHAR(20) DEFAULT 'ACTIVE',
                       active BOOLEAN DEFAULT TRUE,
                       created_by VARCHAR(50),
                       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_by VARCHAR(50),
                       updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ตาราง roles
CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE,
                       description VARCHAR(255),
                       active BOOLEAN DEFAULT TRUE,
                       created_by VARCHAR(50),
                       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_by VARCHAR(50),
                       updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ตาราง user_roles (mapping ระหว่าง users กับ roles)
CREATE TABLE user_roles (
                            user_id INTEGER NOT NULL,
                            role_id INTEGER NOT NULL,
                            active BOOLEAN DEFAULT TRUE,
                            created_by VARCHAR(50),
                            created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_by VARCHAR(50),
                            updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (user_id, role_id),
                            CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- ตาราง permissions
CREATE TABLE permissions (
                             id SERIAL PRIMARY KEY,
                             code VARCHAR(50) NOT NULL UNIQUE,
                             description VARCHAR(255),
                             active BOOLEAN DEFAULT TRUE,
                             created_by VARCHAR(50),
                             created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_by VARCHAR(50),
                             updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ตาราง role_permissions (mapping ระหว่าง roles กับ permissions)
CREATE TABLE role_permissions (
                                  role_id INTEGER NOT NULL,
                                  permission_id INTEGER NOT NULL,
                                  active BOOLEAN DEFAULT TRUE,
                                  created_by VARCHAR(50),
                                  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated_by VARCHAR(50),
                                  updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (role_id, permission_id),
                                  CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
                                  CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);

-- ตาราง user_sessions
CREATE TABLE user_sessions (
                               id SERIAL PRIMARY KEY,
                               user_id INTEGER NOT NULL,
                               refresh_token VARCHAR(500) NOT NULL,
                               ip_address VARCHAR(50),
                               active BOOLEAN DEFAULT TRUE,
                               created_by VARCHAR(50),
                               created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_by VARCHAR(50),
                               updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               expires_at TIMESTAMP NOT NULL,
                               CONSTRAINT fk_user_session FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
