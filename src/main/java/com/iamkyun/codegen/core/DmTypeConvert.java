/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.iamkyun.codegen.core;


import static com.iamkyun.codegen.core.DbColumnType.BIG_DECIMAL;
import static com.iamkyun.codegen.core.DbColumnType.BIG_INTEGER;
import static com.iamkyun.codegen.core.DbColumnType.BLOB;
import static com.iamkyun.codegen.core.DbColumnType.BOOLEAN;
import static com.iamkyun.codegen.core.DbColumnType.BYTE_ARRAY;
import static com.iamkyun.codegen.core.DbColumnType.CLOB;
import static com.iamkyun.codegen.core.DbColumnType.DATE;
import static com.iamkyun.codegen.core.DbColumnType.DOUBLE;
import static com.iamkyun.codegen.core.DbColumnType.FLOAT;
import static com.iamkyun.codegen.core.DbColumnType.INTEGER;
import static com.iamkyun.codegen.core.DbColumnType.STRING;
import static com.iamkyun.codegen.core.TypeConverts.contains;
import static com.iamkyun.codegen.core.TypeConverts.containsAny;

/**
 * DM 字段类型转换
 *
 * @author halower, hanchunlin, daiby
 * @since 2019-06-27
 */
public class DmTypeConvert implements ITypeConvert {
    public static final DmTypeConvert INSTANCE = new DmTypeConvert();

    /**
     * 字符数据类型: CHAR,CHARACTER,VARCHAR
     * <p>
     * 数值数据类型: NUMBER,NUMERIC,DECIMAL,DEC,MONEY,BIT,BOOL,BOOLEAN,INTEGER,INT,BIGINT,TINYINT,BYTE,SMALLINT,BINARY,
     * VARBINARY
     * <p>
     * 近似数值数据类型: FLOAT
     * <p>
     * DOUBLE, DOUBLE PRECISION,REAL
     * <p>
     * 日期时间数据类型
     * <p>
     * 多媒体数据类型: TEXT,LONGVARCHAR,CLOB,BLOB,IMAGE
     *
     * @param config    全局配置
     * @param fieldType 字段类型
     * @return 对应的数据类型
     * @inheritDoc
     */
    @Override
    public IColumnType processTypeConvert(String fieldType) {
        return TypeConverts.use(fieldType)
            .test(containsAny("char", "text").then(STRING))
            .test(contains("number").then(DmTypeConvert::toNumberType))
            .test(containsAny("numeric", "dec", "money").then(BIG_DECIMAL))
            .test(containsAny("bit", "bool").then(BOOLEAN))
            .test(contains("bigint").then(BIG_INTEGER))
            .test(containsAny("int", "byte").then(INTEGER))
            .test(contains("binary").then(BYTE_ARRAY))
            .test(contains("float").then(FLOAT))
            .test(containsAny("double", "real").then(DOUBLE))
            .test(containsAny("date", "time").then(DATE))
            .test(contains("clob").then(CLOB))
            .test(contains("blob").then(BLOB))
            .test(contains("image").then(BYTE_ARRAY))
            .or(STRING);
    }

    private static IColumnType toNumberType(String typeName) {
        if (typeName.matches("number\\([0-9]\\)")) {
            return INTEGER;
        } else if (typeName.matches("number\\(1[0-8]\\)")) {
            return DbColumnType.LONG;
        }
        return BIG_DECIMAL;
    }
}
