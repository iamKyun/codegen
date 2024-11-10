package com.iamkyun.codegen.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableFieldConfig {
    private String type;
    private String label;
    private String columnName;
    private String attrName;
    private String width;
    private String dictCode;
    private String dateFormat = "YYYY-MM-DD HH:mm:ss";
}