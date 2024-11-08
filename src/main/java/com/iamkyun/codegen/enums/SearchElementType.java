package com.iamkyun.codegen.enums;

public enum SearchElementType {
    // 文本框
    TEXT("text"),
    // 数字框
    NUMBER("number"),
    // 日期选择
    DATE("date"),
    // 单选按钮
    RADIO("radio"),
    // 下拉选择
    SELECT("select");

    private final String value;

    SearchElementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
} 