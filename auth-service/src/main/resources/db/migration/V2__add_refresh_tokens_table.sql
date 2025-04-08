-- ตาราง refresh_tokens สำหรับเก็บ refresh token
CREATE TABLE refresh_tokens (
                                id SERIAL PRIMARY KEY,
                                user_id INTEGER NOT NULL,
                                token VARCHAR(500) NOT NULL UNIQUE,
                                expiry_date TIMESTAMP NOT NULL,
                                is_revoked BOOLEAN DEFAULT FALSE,
                                created_by VARCHAR(50),
                                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_by VARCHAR(50),
                                updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- เพิ่ม index สำหรับการค้นหา token
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);