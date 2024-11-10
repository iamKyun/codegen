package ${package};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
<#if isMainTable>
import com.baomidou.mybatisplus.core.metadata.IPage;
import ${basePackage}.model.${className}Model;
import ${basePackage}.model.${className}PageModel;
import ${basePackage}.model.${className}SearchParam;
<#else>
import ${basePackage}.model.${className}PageModel;
import java.util.List;
</#if>
import ${basePackage}.entity.${className};
import org.springframework.stereotype.Repository;

/**
 * ${classComment.comment!''}
 *
 * @author ${classComment.author!''}
 * @date   ${classComment.date!''}
 */
@Repository
public interface ${className}Mapper extends BaseMapper<${className}> {
<#if isMainTable>
    IPage<${className}PageModel> pageQuery(IPage<?> page, @Param("param") ${className}SearchParam searchParam);

    ${className}Model getModelById(@Param("id") String id);
<#else>
    List<${className}PageModel> getModelsByForeignKey(@Param("foreignKey") String foreignKey);
</#if>
}
