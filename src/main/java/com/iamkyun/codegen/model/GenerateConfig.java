package com.iamkyun.codegen.model;

import lombok.Data;

import java.util.List;

@Data
public class GenerateConfig {

    // 表设置
    private GeneralConfig general;
    // 搜索项设置
    private List<SearchFieldConfig> search;
    // 列表设置
    private List<TableFieldConfig> table;
    // 表单设置
    private List<FormFieldConfig> form;
    // 子表设置
    private List<SubTableConfig> subTables;
    // 生成配置
    private PathConfig packageConfig;
}

