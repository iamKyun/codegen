package com.iamkyun.codegen.model.request;

import lombok.Data;

import java.util.List;

@Data
public class GenerationRequest {
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
    // 额外配置
    private ExtraConfig extra;
}
