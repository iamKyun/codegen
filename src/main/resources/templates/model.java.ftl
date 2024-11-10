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
import ${basePackage}.model.${subTable.className}PageModel;
    </#list>
</#if>

@Data
@ApiModel("${tableComment}-表单")
public class ${className}Model implements Serializable{
    @ApiModelProperty("ID")
    private String id;

<#list fields as field>
    @ApiModelProperty("${field.comment!''}")
    <#if field.type == "date">
    @JsonFormat(pattern = "${field.dateFormat}", timezone = "GMT+8")
    @DateTimeFormat(pattern = "${field.dateFormat}")
    <#elseif field.type == "dict">
    @Dict(target = "${field.name}Text", dictCode = "${field.dictCode}")
    </#if>
    private ${field.javaType} ${field.name};
    <#if field.type == "dict">
    private ${field.javaType} ${field.name}Text;
    </#if>

</#list>
<#if hasSubTable>
    <#list subTables as subTable>
        @ApiModelProperty("${subTable.comment}")
        private List<${subTable.className}PageModel> ${subTable.varName}List;
    </#list>

</#if>
}
