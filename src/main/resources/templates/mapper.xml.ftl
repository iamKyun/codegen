<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${basePackage}.mapper.${className}Mapper">
<#if isMainTable>
    <select id="pageQuery" resultType="${basePackage}.model.${className}PageModel">
        SELECT
        <#list tableItems as column>
            ${column.columnName} as ${column.columnNameAlias}<#if column_has_next>,</#if>
        </#list>
        FROM ${tableName}
        <where>
    <#list searchItems as item>
        <#if (item.type == 'date' || item.type == 'number') && item.rangeSearch>
            <#if item.rangeSearch>
            <if test="param.${item.attrName}Start != null">
                AND ${item.columnName} >= ${"#"}{param.${item.attrName}Start}
            </if>
            <if test="param.${item.attrName}End != null">
                AND ${item.columnName} &lt;= ${"#"}{param.${item.attrName}End}
            </if>
            </#if>
        <#else>
            <if test="param.${item.attrName} != null<#if (item.type != 'date' && item.type != 'number')> and param.${item.attrName} != ''</#if>">
            <#if item.type == 'text' && item.fuzzySearch>
                AND ${item.columnName} LIKE CONCAT('%',${"#"}{param.${item.attrName}},'%')
            <#else>
                AND ${item.columnName} = ${"#"}{param.${item.attrName}}
            </#if>
            </if>
        </#if>
    </#list>
        </where>
    </select>

    <select id="getModelById" resultType="${basePackage}.model.${className}Model">
        SELECT ID as id,
        <#list formItems as column>
            ${column.columnName} as ${column.columnNameAlias}<#if column_has_next>,</#if>
    </#list>
        FROM ${tableName}
        WHERE id = ${"#"}{id}
    </select>
<#else>
    <select id="getModelsByForeignKey" resultType="${basePackage}.model.${className}PageModel">
        SELECT ID as id,
        <#list tableItems as column>
            ${column.columnName} as ${column.columnNameAlias}<#if column_has_next>,</#if>
    </#list>
        FROM ${tableName}
        WHERE ${foreignKey} = ${"#"}{foreignKey}
    </select>
</#if>

</mapper>
