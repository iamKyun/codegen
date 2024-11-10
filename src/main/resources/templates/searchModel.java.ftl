package ${package};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
<#list imports as import>
import ${import};
</#list>

@Data
@ApiModel("${tableComment}-查询")
public class ${className}SearchParam implements Serializable {
<#list fields as field>

    <#if field.type == "date" && field.rangeSearch>
    @ApiModelProperty("${field.comment!''} 开始")
    @JsonFormat(pattern = "${field.dateFormat}", timezone = "GMT+8")
    @DateTimeFormat(pattern = "${field.dateFormat}")
    private ${field.javaType} ${field.name}Start;

    @ApiModelProperty("${field.comment!''} 结束")
    @JsonFormat(pattern = "${field.dateFormat}", timezone = "GMT+8")
    @DateTimeFormat(pattern = "${field.dateFormat}")
    private ${field.javaType} ${field.name}End;

    <#elseif field.type == "number" && field.rangeSearch>
    @ApiModelProperty("${field.comment!''} 开始")
    private ${field.javaType} ${field.name}Start;

    @ApiModelProperty("${field.comment!''} 结束")
    private ${field.javaType} ${field.name}End;

    <#else>
    @ApiModelProperty("${field.comment!''}")
    private ${field.javaType} ${field.name};

    </#if>
</#list>
}
