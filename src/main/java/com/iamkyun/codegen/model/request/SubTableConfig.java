package com.iamkyun.codegen.model.request;

import lombok.Data;
import java.util.List;

@Data
public class SubTableConfig {
    private GeneralConfig general;
    private List<TableFieldConfig> table;
    private List<FormFieldConfig> form;
}