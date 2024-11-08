package com.iamkyun.codegen.model;

import lombok.Data;

@Data
public class GeneralConfig {
    private String tableName;
    private Boolean isShowNum;
    private Boolean isFixedNum;
    private Boolean isFixedAction;
    private Boolean isLogicDel;
    private String logicDelField;
    private Boolean isUseCommonGroup;
    private String groupCode;
    private String groupRelateId;
} 