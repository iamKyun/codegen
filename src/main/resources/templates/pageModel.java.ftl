package ${package};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
<#list imports as import>
import ${import};
</#list>

@Data
@ApiModel("${tableComment}-分页项")
public class ${className}PageModel implements Serializable{
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
}
