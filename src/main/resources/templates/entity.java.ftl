package ${packageName};

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
<#if hasTableLogic>
import com.baomidou.mybatisplus.annotation.TableLogic;
</#if>
import lombok.Data;

<#-- 导入包配置 -->
<#list imports as import>
import ${import};
</#list>

/**
 * ${classComment.comment!''}
 *
 * @author ${classComment.author!''}
 * @date   ${classComment.date!''}
 */
@Data
@TableName("${tableName}")
public class ${className} {
    <#list fields as field>

    /**
     * ${field.comment!''}
     */
    <#if field.isTableId>
    @TableId(type = IdType.ASSIGN_UUID)
    </#if>
    <#if field.isTableLogic>
    @TableLogic
    </#if>
    private ${field.type} ${field.name};

    </#list>
}
