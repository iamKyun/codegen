package ${package};

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import ${basePackage}.mapper.${className}Mapper;
import ${basePackage}.model.${className}Model;

import java.util.List;

<#if hasAttachment>
import com.gzcss.cloud.system.feign.SysAttachmentFeign;
</#if>
<#if isMainTable>
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzcss.common.model.PageParam;
import com.gzcss.starter.web.utils.SequenceUtil;
import ${basePackage}.entity.${className};
    <#list subClasses as subClass>
import ${basePackage}.service.${subClass.className}Service;
import ${basePackage}.model.${subClass.className}PageModel;
    </#list>
import ${basePackage}.service.${className}Service;
import ${basePackage}.model.${className}PageModel;
import ${basePackage}.model.${className}SaveModel;
import ${basePackage}.model.${className}SearchParam;
import org.apache.commons.lang3.StringUtils;
<#else>
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import ${basePackage}.entity.${className};
import ${basePackage}.model.${className}SaveModel;
import ${basePackage}.service.${className}Service;
import ${basePackage}.model.${className}PageModel;
import static java.util.stream.Collectors.toList;
</#if>

/**
 * ${classComment.comment!''}
 *
 * @author ${classComment.author!''}
 * @date   ${classComment.date!''}
 */
@Service
public class ${className}ServiceImpl extends ServiceImpl<${className}Mapper, ${className}> implements ${className}Service {
<#if hasAttachment>
    @Autowired
    private SysAttachmentFeign sysAttachmentFeign;
</#if>
<#if isMainTable>
    <#list subClasses as subClass>
    @Autowired
    private ${subClass.className}Service ${subClass.varName}Service;
    </#list>
    @Override
    public IPage<${className}PageModel> pageQuery(${className}SearchParam searchParam, PageParam pageParam) {
        IPage<?> page = pageParam.toPage();
        return this.baseMapper.pageQuery(page, searchParam);
    }

    @Override
    @Transactional
    public void saveOrUpdateModel(${className}SaveModel model) {
        ${className} entity = new ${className}();
        if (StringUtils.isBlank(model.getId())) {
            model.setId(SequenceUtil.generateRandomString());
        }
        BeanUtils.copyProperties(model, entity);
        <#if hasAttachment>
        <#list attachmentFields as attachmentField>
        // 保存附件
        List<String> ${attachmentField.varName} = model.get${attachmentField.fieldName}();
        sysAttachmentFeign.saveRelateId(model.getId(), ${attachmentField.varName});
        </#list>
        </#if>
        <#list subClasses as subClass>
        // 保存子表
        ${subClass.varName}Service.saveModels(model.getId(), model.get${subClass.className}List());
        </#list>
        this.saveOrUpdate(entity);
    }

    @Override
    public ${className}Model getModelById(String id) {
        ${className}Model model = this.baseMapper.getModelById(id);
        <#list subClasses as subClass>
        model.set${subClass.className}List(${subClass.varName}Service.getModelsByForeignKey(id));
        </#list>
        return model;
    }
<#else>
    @Override
    public List<${className}PageModel> getModelsByForeignKey(String foreignKey) {
        List<${className}PageModel> models = this.baseMapper.getModelsByForeignKey(foreignKey);
        return models;
    }

    @Override
    @Transactional
    public void saveModels(String foreignKey, List<${className}SaveModel> models) {
        // 删除关联
        this.remove(Wrappers.<${className}>lambdaQuery().eq(${className}::get${foreignKey}, foreignKey));
        if (models != null && !models.isEmpty()) {
            List<${className}> entities = models.stream().map(model -> {
                ${className} entity = new ${className}();
                entity.set${foreignKey}(foreignKey);
                BeanUtils.copyProperties(model, entity);
                <#list attachmentFields as attachmentField>
                // 保存附件
                List<String> ${attachmentField.varName} = model.get${attachmentField.fieldName}();
                sysAttachmentFeign.saveRelateId(model.getId(), ${attachmentField.varName});
                </#list>
                return entity;
            }).collect(toList());
            this.saveBatch(entities);
        }
    }
</#if>
}
