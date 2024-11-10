package com.iamkyun.codegen.config;

import com.iamkyun.codegen.core.DbType;
import com.iamkyun.codegen.core.ITypeConvert;
import com.iamkyun.codegen.core.TypeConverts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Value("${spring.datasource.url}")
    private String url;

    @Bean
    public ITypeConvert typeConvert() {
        // 确保url不为空
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalStateException("数据库连接URL未配置");
        }

        // 获取数据库类型
        DbType dbType = getDbType(url);
        if (dbType == null) {
            throw new IllegalStateException("无法识别数据库类型,URL:" + url);
        }

        // 获取类型转换器
        ITypeConvert typeConvert = TypeConverts.getTypeConvert(dbType);
        if (typeConvert == null) {
            throw new IllegalStateException("未找到对应数据库类型的转换器:" + dbType);
        }

        return typeConvert;
    }

    private DbType getDbType(String str) {
        if (str.contains(":mysql:") || str.contains(":cobar:")) {
            return DbType.MYSQL;
        } else if (str.contains(":oracle:")) {
            return DbType.ORACLE;
        } else if (str.contains(":postgresql:")) {
            return DbType.POSTGRE_SQL;
        } else if (str.contains(":sqlserver:")) {
            return DbType.SQL_SERVER;
        } else if (str.contains(":db2:")) {
            return DbType.DB2;
        } else if (str.contains(":mariadb:")) {
            return DbType.MARIADB;
        } else if (str.contains(":sqlite:")) {
            return DbType.SQLITE;
        } else if (str.contains(":h2:")) {
            return DbType.H2;
        } else if (str.contains(":kingbase:") || str.contains(":kingbase8:")) {
            return DbType.KINGBASE_ES;
        } else if (str.contains(":dm:")) {
            return DbType.DM;
        } else if (str.contains(":zenith:")) {
            return DbType.GAUSS;
        } else if (str.contains(":oscar:")) {
            return DbType.OSCAR;
        } else if (str.contains(":firebird:")) {
            return DbType.FIREBIRD;
        } else if (str.contains(":xugu:")) {
            return DbType.XU_GU;
        } else if (str.contains(":clickhouse:")) {
            return DbType.CLICK_HOUSE;
        } else if (str.contains(":sybase:")) {
            return DbType.SYBASE;
        } else {
            return DbType.OTHER;
        }
    }

}
