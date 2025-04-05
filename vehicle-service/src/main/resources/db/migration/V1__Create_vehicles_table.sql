CREATE SCHEMA IF NOT EXISTS repair;

CREATE TABLE IF NOT EXISTS repair.repair_orders (
                                                    id SERIAL PRIMARY KEY,
                                                    description TEXT NOT NULL,
                                                    status VARCHAR(50) DEFAULT 'PENDING',  -- PENDING, IN_PROGRESS, COMPLETED, CANCELLED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
