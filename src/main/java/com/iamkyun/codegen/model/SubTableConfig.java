package com.iamkyun.codegen.model;

import lombok.Data;
import java.util.List;

@Data
public class SubTableConfig {
    private String id;
    private SubGeneralConfig general;
    private List<TableFieldConfig> table;
    private List<FormFieldConfig> form;
} 