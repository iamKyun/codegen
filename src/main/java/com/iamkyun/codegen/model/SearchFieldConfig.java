package com.iamkyun.codegen.model;

import lombok.Data;
import java.util.List;

@Data
public class SearchFieldConfig {
    private String id;
    private String label;
    private String type;
    private String columnName;
    private String attrName;
    private Boolean displayFull;
    private String dictCode;
    private List<OptionConfig> options;
    private Boolean rangeSearch;
} 