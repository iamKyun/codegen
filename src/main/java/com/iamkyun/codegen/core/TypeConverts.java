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

/**
 * 该注册器负责注册并查询类型注册器
 *
 * @author nieqiuqiu, hanchunlin
 * @since 3.3.1
 */
public class TypeConverts {

    /**
     * 查询数据库类型对应的类型转换器
     *
     * @param dbType 数据库类型
     * @return 返回转换器
     */
    public static ITypeConvert getTypeConvert(DbType dbType) {
        switch (dbType) {
            case DM:
            case GAUSS:
                return DmTypeConvert.INSTANCE;
        }
        return null;
    }

    /**
     * 使用指定参数构建一个选择器
     *
     * @param param 参数
     * @return 返回选择器
     */
    static Selector<String, IColumnType> use(String param) {
        return new Selector<>(param.toLowerCase());
    }

    /**
     * 这个分支构建器用于构建用于支持 {@link String#contains(CharSequence)} 的分支
     *
     * @param value 分支的值
     * @return 返回分支构建器
     * @see #containsAny(CharSequence...)
     */
    static BranchBuilder<String, IColumnType> contains(CharSequence value) {
        return BranchBuilder.of(s -> s.contains(value));
    }

    /**
     * @see #contains(CharSequence)
     */
    static BranchBuilder<String, IColumnType> containsAny(CharSequence... values) {
        return BranchBuilder.of(s -> {
            for (CharSequence value : values) {
                if (s.contains(value)) return true;
            }
            return false;
        });
    }
}
