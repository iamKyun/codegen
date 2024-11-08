package com.iamkyun.codegen.enums;

public enum FormElementType {
    // 文本框
    TEXT("text"),
    // 大文本框
    TEXTAREA("textarea"),
    // 密码框
    PASSWORD("password"),
    // 数字框
    NUMBER("number"),
    // 日期选择
    DATE("date"),
    // 单选按钮
    RADIO("radio"),
    // 下拉选择
    SELECT("select"),
    // 文件上传
    ATTACHMENT("attachment");

    private final String value;

    FormElementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
} 