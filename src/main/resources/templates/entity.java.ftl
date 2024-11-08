package ${package.entity};

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

<#-- 导入包配置 -->
<#list imports as import>
import ${import};
</#list>

/**
 * ${table.comment!''}
 *
 * @author： ${author!''}
 * @date： ${date}
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
    private ${field.type} ${field.name};
    </#list>
}
