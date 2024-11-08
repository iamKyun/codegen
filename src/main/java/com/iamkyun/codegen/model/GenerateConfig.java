package com.iamkyun.codegen.model;

import lombok.Data;

import java.util.List;

@Data
public class GenerateConfig {

    private General general;
    private List<SearchField> search;
    private List<TableField> table;
    private List<FormField> form;
    private List<SubTable> subTables;

    @Data
    public static class General {
        private String tableName;
        private Boolean isShowNum;
        private Boolean isUseCommonGroup;
        private Boolean isFixedNum;
        private Boolean isFixedAction;
        private Boolean isLogicDel;
        private String logicDelField;
        private String groupCode;
        private String groupRelateId;
    }

    @Data
    public static class SearchField {
        private String id;
        private String label;
        private String type;
        private String columnName;
        private String attrName;
        private Boolean displayFull;
        private String dictCode;
        private List<Option> options;
        private Boolean rangeSearch;
    }

    @Data
    public static class TableField {
        private String id;
        private String type;
        private String label;
        private String attrName;
        private String width;
        private String dictCode;
    }

    @Data
    public static class FormField {
        private String id;
        private String label;
        private String type;
        private String columnName;
        private String attrName;
        private Boolean displayFull;
        private List<Option> options;
    }

    @Data
    public static class SubTable {
        private String id;
        private SubGeneral general;
        private List<TableField> table;
        private List<FormField> form;
    }

    @Data
    public static class SubGeneral {
        private String tableName;
        private Boolean isShowNum;
        private String foreignKey;
    }

    @Data
    public static class Option {
        private String key;
        private String value;
    }
}

