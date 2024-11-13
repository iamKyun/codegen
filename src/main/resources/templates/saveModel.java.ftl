package ${package};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
<#list imports as import>
import ${import};
</#list>
<#if hasSubTable>
import java.util.List;
    <#list subTables as subTable>
import ${basePackage}.model.${subTable.className}SaveModel;
    </#list>
</#if>

@Data
@ApiModel("${tableComment}-保存")
public class ${className}SaveModel implements Serializable {

<#if isMainTable>
    @ApiModelProperty("ID")
    private String id;
</#if>
<#list fields as field>
    @ApiModelProperty("${field.comment!''} - 长度${field.dataLength}")
    <#if field.type == "date">
    @JsonFormat(pattern = "${field.dateFormat}", timezone = "GMT+8")
    @DateTimeFormat(pattern = "${field.dateFormat}")
    </#if>
    private ${field.javaType} ${field.name};

</#list>

<#if hasSubTable>
    <#list subTables as subTable>
    @ApiModelProperty("${subTable.comment}")
    private List<${subTable.className}SaveModel> ${subTable.varName}List;

    </#list>
</#if>
}
