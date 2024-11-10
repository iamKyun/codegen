package com.iamkyun.codegen.convert;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.iamkyun.codegen.core.IColumnType;
import com.iamkyun.codegen.core.ITypeConvert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TypeConvertor {

    private final ITypeConvert typeConvert;

    public FieldType convertFormType(String type, String columnDataType) {
        switch (type) {
            case "text":
            case "textarea":
            case "password":
            case "radio":
            case "select":
                return new FieldType("String", null);
            case "number":
                IColumnType processTypeConvert = typeConvert.processTypeConvert(columnDataType);
                if (processTypeConvert != null) {
                    return new FieldType(processTypeConvert.getType(), Arrays.asList(processTypeConvert.getPkg()));
                } else {
                    return new FieldType("BigDecimal", Arrays.asList("java.math.BigDecimal"));

                }
            case "date":
                return new FieldType("Date",
                        Arrays.asList("java.util.Date", "com.fasterxml.jackson.annotation.JsonFormat",
                                "org.springframework.format.annotation.DateTimeFormat"));
            case "attachment":
                return new FieldType("List<String>", Arrays.asList("java.util.List"));
            default:
                return null;
        }
    }

    public FieldType convertTableType(String type) {
        switch (type) {
            case "text":
                return new FieldType("String", null);
            case "date":
                return new FieldType("Date",
                        Arrays.asList("java.util.Date", "com.fasterxml.jackson.annotation.JsonFormat",
                                "org.springframework.format.annotation.DateTimeFormat"));
            case "dict":
                return new FieldType("String", Arrays.asList("com.gzcss.common.aspect.annotation.Dict"));
            default:
                return null;
        }
    }

    public FieldType convertSearchType(String type, String columnDataType) {
        switch (type) {
            case "text":
            case "radio":
            case "select":
                return new FieldType("String", null);
            case "number":
                IColumnType processTypeConvert = typeConvert.processTypeConvert(columnDataType);
                if (processTypeConvert != null) {
                    return new FieldType(processTypeConvert.getType(), Arrays.asList(processTypeConvert.getPkg()));
                } else {
                    return new FieldType("BigDecimal", Arrays.asList("java.math.BigDecimal"));

                }
            case "date":
                return new FieldType("Date",
                        Arrays.asList("java.util.Date", "com.fasterxml.jackson.annotation.JsonFormat",
                                "org.springframework.format.annotation.DateTimeFormat"));
            default:
                return null;
        }
    }
}
