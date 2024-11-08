package com.iamkyun.codegen.model;

import lombok.Data;
import java.util.List;

@Data
public class FormFieldConfig {
    private String id;
    private String label;
    private String type;
    private String columnName;
    private String attrName;
    private Boolean displayFull;
    private List<OptionConfig> options;
} 