package com.iamkyun.codegen.convert;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldType {
    private String javaType;
    private List<String> importPackage;
}
