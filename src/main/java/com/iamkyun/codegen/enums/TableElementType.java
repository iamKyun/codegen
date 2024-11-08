package com.iamkyun.codegen.enums;

public enum TableElementType {
    // 文本
    TEXT("text"),
    // 日期
    DATE("date"),
    // 字典
    DICT("dict");

    private final String value;

    TableElementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
} 