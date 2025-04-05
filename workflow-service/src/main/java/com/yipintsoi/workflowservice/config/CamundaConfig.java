package com.yipintsoi.workflowservice.config;

import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaConfig {

    @Bean
    public ProcessEngineConfiguration processEngineConfiguration() {
        // สามารถกำหนด configuration สำหรับ Camunda BPM engine ได้ที่นี่
        return ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:postgresql://localhost:5432/workflow_db")
                .setJdbcUsername("your_db_username")
                .setJdbcPassword("your_db_password")
                .setJdbcDriver("org.postgresql.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    }
}
