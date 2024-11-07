package com.iamkyun.codegen.core;

public record TableColumn(String columnName, String dataType,Integer dataLength,Integer dataScale, String columnComment,String nullable) {
}
