package com.yipintsoi.workflowservice.domain.dto;

import lombok.Data;

@Data
public class ProcessDefinitionDTO {
    private String id;
    private String key;
    private String name;
    private int version;
}
