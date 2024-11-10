package com.iamkyun.codegen.model.request;

import lombok.Data;
import java.util.List;

@Data
public class FormFieldConfig {
    private String label;
    private String type;
    private String columnName;
    private String columnDataType;
    private String columnKey;
    private String dateFormat = "YYYY-MM-DD HH:mm:ss";
    private String dictCode;
    private String attrName;
    private Boolean displayFull;
    private List<OptionConfig> options;
}
