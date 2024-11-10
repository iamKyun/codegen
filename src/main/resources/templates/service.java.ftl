package ${package};

import com.baomidou.mybatisplus.extension.service.IService;
<#if isMainTable>
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzcss.common.model.PageParam;
import ${basePackage}.entity.${className};
import ${basePackage}.model.${className}Model;
import ${basePackage}.model.${className}PageModel;
import ${basePackage}.model.${className}SearchParam;
<#else>
import java.util.List;
import ${basePackage}.model.${className}PageModel;
</#if>
import ${basePackage}.model.${className}SaveModel;
import ${basePackage}.entity.${className};

/**
 * ${classComment.comment!''}
 *
 * @author ${classComment.author!''}
 * @date   ${classComment.date!''}
 */
public interface ${className}Service extends IService<${className}> {
<#if isMainTable>
    IPage<${className}PageModel> pageQuery(${className}SearchParam searchParam, PageParam pageParam);

    void saveOrUpdateModel(${className}SaveModel model);

    ${className}Model getModelById(String id);
<#else>
    List<${className}PageModel> getModelsByForeignKey(String foreignKey);

    void saveModels(String foreignKey, List<${className}SaveModel> models);
</#if>
}
