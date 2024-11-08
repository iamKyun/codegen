package com.iamkyun.codegen.model;

import lombok.Data;

@Data
public class SubGeneralConfig {
    private String tableName;
    private Boolean isShowNum;
    private String foreignKey;
} 