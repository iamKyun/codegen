package com.iamkyun.codegen.core;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DbClomunTypeConverter {

    // 映射达梦数据库类型到Java类型
    private static final Map<Integer, Class<?>> damengToJavaTypeMap = new HashMap<>();

    static {
        // 填充达梦数据库类型到Java类型的映射
        damengToJavaTypeMap.put(Types.INTEGER, Integer.class);
        damengToJavaTypeMap.put(Types.BIGINT, Long.class);
        damengToJavaTypeMap.put(Types.FLOAT, Float.class);
        damengToJavaTypeMap.put(Types.DOUBLE, Double.class);
        damengToJavaTypeMap.put(Types.VARCHAR, String.class);
        damengToJavaTypeMap.put(Types.DATE, java.sql.Date.class);
        damengToJavaTypeMap.put(Types.TIMESTAMP, Timestamp.class);
        damengToJavaTypeMap.put(Types.BOOLEAN, Boolean.class);
        damengToJavaTypeMap.put(Types.DECIMAL, java.math.BigDecimal.class);
        damengToJavaTypeMap.put(Types.CLOB, Clob.class);
        damengToJavaTypeMap.put(Types.BLOB, Blob.class);
    }


}

