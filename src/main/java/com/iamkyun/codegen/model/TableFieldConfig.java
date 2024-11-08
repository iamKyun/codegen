package com.iamkyun.codegen.model;

import lombok.Data;

@Data
public class TableFieldConfig {
    private String id;
    private String type;
    private String label;
    private String attrName;
    private String width;
    private String dictCode;
} 