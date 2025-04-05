CREATE SCHEMA IF NOT EXISTS dashboard;

CREATE TABLE IF NOT EXISTS dashboard.dashboard_templates (
    id SERIAL PRIMARY KEY,
    template_name VARCHAR(100) NOT NULL,
    template_config TEXT,  -- JSON configuration
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS dashboard.user_dashboards (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    template_id INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_dashboard_template FOREIGN KEY (template_id) REFERENCES dashboard.dashboard_templates(id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS dashboard.dashboard_widgets (
    id SERIAL PRIMARY KEY,
    user_dashboard_id INTEGER NOT NULL,
    widget_type VARCHAR(50) NOT NULL,
    widget_config TEXT,  -- JSON configuration
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_dashboard_widget FOREIGN KEY (user_dashboard_id) REFERENCES dashboard.user_dashboards(id) ON DELETE CASCADE
    );
