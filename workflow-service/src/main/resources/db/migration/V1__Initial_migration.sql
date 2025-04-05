CREATE SCHEMA IF NOT EXISTS workflow;

CREATE TABLE IF NOT EXISTS workflow.camunda_history (
                                               id SERIAL PRIMARY KEY,
                                               event_type VARCHAR(255),
    event_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details TEXT
    );
