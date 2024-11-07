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
        return TypeConverts.getTypeConvert(DbType.getDbType(url));
    }

}
