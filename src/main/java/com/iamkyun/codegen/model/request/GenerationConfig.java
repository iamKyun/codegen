package com.iamkyun.codegen.model.request;

import java.util.List;

import lombok.Data;

@Data
public class GenerationConfig {
    // 表设置
    private GeneralConfig general;
    // 搜索项设置
    private List<SearchFieldConfig> search;
    // 列表设置
    private List<TableFieldConfig> table;
    // 表单设置
    private List<FormFieldConfig> form;
    // 是否主表
    private boolean isMainTable;
}
