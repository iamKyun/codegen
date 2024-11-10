package com.iamkyun.codegen.model.request;

import lombok.Data;
import java.util.List;

@Data
public class SearchFieldConfig {
    private String label;
    private String type;
    private String columnName;
    private String columnDataType;
    private String columnKey;
    private String attrName;
    private Boolean displayFull;
    private String dictCode;
    private String dateFormat = "YYYY-MM-DD HH:mm:ss";
    private List<OptionConfig> options;
    private Boolean rangeSearch;
    private Boolean fuzzySearch;
}
