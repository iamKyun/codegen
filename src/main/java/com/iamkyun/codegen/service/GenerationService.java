package com.iamkyun.codegen.service;

import cn.hutool.core.util.StrUtil;

import com.iamkyun.codegen.convert.FieldType;
import com.iamkyun.codegen.convert.TypeConvertor;
import com.iamkyun.codegen.core.IColumnType;
import com.iamkyun.codegen.core.ITypeConvert;
import com.iamkyun.codegen.model.data.TableColumn;
import com.iamkyun.codegen.model.request.ExtraConfig;
import com.iamkyun.codegen.model.request.GeneralConfig;
import com.iamkyun.codegen.model.request.GenerationConfig;
import com.iamkyun.codegen.model.request.GenerationRequest;
import com.iamkyun.codegen.model.request.SubTableConfig;
import com.iamkyun.codegen.model.template.ClassComment;
import com.iamkyun.codegen.model.template.GenerationResult;
import com.iamkyun.codegen.model.template.GenerationTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenerationService {
    private final TableService tableService;
    private final TemplateService templateService;
    private final ITypeConvert typeConvert;
    private final TypeConvertor typeConvertor;
    private final String SLASH = "/";
    private final String DOT = ".";

    public GenerationResult generate(GenerationRequest request) throws Exception {

        List<GenerationConfig> generationConfigs = preprocess(request);
        ExtraConfig extraConfig = request.getExtra();
        // 所有要生成的文件
        List<GenerationTemplate> templates = new ArrayList<>();
        // 实体类
        templates.addAll(preprocessEntity(generationConfigs, extraConfig));
        // mapper
        templates.addAll(preprocessMapperXml(generationConfigs, extraConfig));
        templates.addAll(preprocessMapperClass(generationConfigs, extraConfig));
        // service
        templates.addAll(preprocessService(generationConfigs, extraConfig));
        templates.addAll(preprocessServiceImpl(generationConfigs, extraConfig));
        // controller
        templates.addAll(preprocessController(generationConfigs, extraConfig));
        // model
        templates.addAll(preprocessSaveModel(generationConfigs, extraConfig));
        templates.addAll(preprocessPageModel(generationConfigs, extraConfig));
        templates.addAll(preprocessSearchModel(generationConfigs, extraConfig));
        templates.addAll(preprocessModel(generationConfigs, extraConfig));
        // 生成文件
        String zip = templateService.generateAndPackageFiles(templates);
        return GenerationResult.withZip(zip);
    }

    private List<GenerationTemplate> preprocessModel(List<GenerationConfig> generationConfigs,
            ExtraConfig extraConfig) {
        String packagePath = extraConfig.getPackagePath() + DOT + extraConfig.getModule() + ".model";
        String generateDir = packagePath.replace(DOT, SLASH);
        List<GenerationTemplate> templates = new ArrayList<>();

        for (GenerationConfig generationConfig : generationConfigs) {
            GeneralConfig tableInfo = generationConfig.getGeneral();
            String tableName = tableInfo.getTableName();
            String className = StrUtil.upperFirst(StrUtil.toCamelCase(tableName.toLowerCase()));

            // 准备模板数据
            Map<String, Object> param = new HashMap<>();
            param.put("package", packagePath);
            param.put("tableComment", tableInfo.getTableComment());
            param.put("className", className);
            param.put("basePackage", extraConfig.getPackagePath() + DOT + extraConfig.getModule());

            // 处理字段
            List<Map<String, Object>> fields = new ArrayList<>();
            Set<String> imports = new HashSet<>();

            if (generationConfig.getForm() != null) {
                generationConfig.getForm().stream()
                        .forEach(form -> {
                            Map<String, Object> field = new HashMap<>();
                            field.put("comment", form.getLabel());
                            field.put("name", form.getAttrName());
                            field.put("type", form.getType());
                            FieldType fieldType = typeConvertor.convertFormType(form.getType(),
                                    form.getColumnDataType());
                            field.put("javaType", fieldType.getJavaType());
                            field.put("dateFormat", form.getDateFormat());
                            field.put("dictCode", form.getDictCode());
                            fields.add(field);

                            // 如果字段类型需要导入，添加到imports
                            if (fieldType.getImportPackage() != null) {
                                imports.addAll(fieldType.getImportPackage());
                            }
                        });
            }
            param.put("fields", fields);
            param.put("imports", imports);

            // 处理子表关系
            if (generationConfig.isMainTable()) {
                boolean hasSubTable = generationConfigs.stream().anyMatch(config -> !config.isMainTable());
                param.put("hasSubTable", hasSubTable);

                if (hasSubTable) {
                    List<Map<String, Object>> subTables = new ArrayList<>();
                    generationConfigs.stream()
                            .filter(config -> !config.isMainTable())
                            .forEach(subConfig -> {
                                Map<String, Object> subTable = new HashMap<>();
                                String subTableName = subConfig.getGeneral().getTableName().toLowerCase();
                                String subClassName = StrUtil.upperFirst(StrUtil.toCamelCase(subTableName));
                                subTable.put("className", subClassName);
                                subTable.put("varName", StrUtil.toCamelCase(subTableName));
                                subTable.put("comment", subConfig.getGeneral().getTableComment());
                                subTables.add(subTable);
                            });
                    param.put("subTables", subTables);
                }
            } else {
                param.put("hasSubTable", false);
            }

            // 生成文件
            String outputPath = generateDir + SLASH + className + "Model.java";
            templates.add(new GenerationTemplate("model.java.ftl", outputPath, param));
        }
        return templates;
    }

    private List<GenerationTemplate> preprocessSearchModel(List<GenerationConfig> generationConfigs,
            ExtraConfig extraConfig) {
        String packagePath = extraConfig.getPackagePath() + DOT + extraConfig.getModule() + ".model";
        String generateDir = packagePath.replace(DOT, SLASH);
        List<GenerationTemplate> templates = new ArrayList<>();

        for (GenerationConfig generationConfig : generationConfigs) {
            GeneralConfig tableInfo = generationConfig.getGeneral();
            String tableName = tableInfo.getTableName().toLowerCase();
            String className = StrUtil.upperFirst(StrUtil.toCamelCase(tableName));

            // 准备模板数据
            Map<String, Object> param = new HashMap<>();
            param.put("package", packagePath);
            param.put("tableComment", tableInfo.getTableComment());
            param.put("className", className);

            // 处理字段
            List<Map<String, Object>> fields = new ArrayList<>();
            Set<String> imports = new HashSet<>();

            if (generationConfig.getSearch() != null) {
                generationConfig.getSearch().stream()
                        .forEach(search -> {
                            Map<String, Object> field = new HashMap<>();
                            field.put("comment", search.getLabel());
                            field.put("name", search.getAttrName());
                            field.put("type", search.getType());
                            field.put("rangeSearch", !Boolean.FALSE.equals(search.getRangeSearch()));
                            FieldType fieldType = typeConvertor.convertSearchType(search.getType(),
                                    search.getColumnDataType());
                            field.put("javaType", fieldType.getJavaType());
                            field.put("dateFormat", search.getDateFormat());
                            fields.add(field);

                            // 如果字段类型需要导入，添加到imports
                            if (fieldType.getImportPackage() != null) {
                                imports.addAll(fieldType.getImportPackage());
                            }
                        });
            }
            param.put("fields", fields);
            param.put("imports", imports);

            // 生成文件
            String outputPath = generateDir + SLASH + className + "SearchParam.java";
            templates.add(new GenerationTemplate("searchModel.java.ftl", outputPath, param));
        }
        return templates;
    }

    private List<GenerationTemplate> preprocessPageModel(List<GenerationConfig> generationConfigs,
            ExtraConfig extraConfig) {
        String packagePath = extraConfig.getPackagePath() + DOT + extraConfig.getModule() + ".model";
        String generateDir = packagePath.replace(DOT, SLASH);
        List<GenerationTemplate> templates = new ArrayList<>();

        for (GenerationConfig generationConfig : generationConfigs) {
            GeneralConfig tableInfo = generationConfig.getGeneral();
            String tableName = tableInfo.getTableName().toLowerCase();
            String className = StrUtil.upperFirst(StrUtil.toCamelCase(tableName));

            // 准备模板数据
            Map<String, Object> param = new HashMap<>();
            param.put("package", packagePath);
            param.put("tableComment", tableInfo.getTableComment());
            param.put("className", className);

            // 处理字段
            List<Map<String, Object>> fields = new ArrayList<>();
            Set<String> imports = new HashSet<>();

            if (generationConfig.getTable() != null) {
                generationConfig.getTable().stream()
                        .forEach(tableField -> {
                            Map<String, Object> field = new HashMap<>();
                            field.put("comment", tableField.getLabel());
                            field.put("name", tableField.getAttrName());
                            field.put("type", tableField.getType());
                            FieldType fieldType = typeConvertor.convertTableType(tableField.getType());
                            field.put("javaType", fieldType.getJavaType());
                            field.put("dateFormat", tableField.getDateFormat());
                            field.put("dictCode", tableField.getDictCode());
                            fields.add(field);

                            // 如果字段类型需要导入，添加到imports
                            if (fieldType.getImportPackage() != null) {
                                imports.addAll(fieldType.getImportPackage());
                            }
                        });
            }
            param.put("fields", fields);
            param.put("imports", imports);

            // 生成文件
            String outputPath = generateDir + SLASH + className + "PageModel.java";
            templates.add(new GenerationTemplate("pageModel.java.ftl", outputPath, param));
        }
        return templates;
    }

    private List<GenerationTemplate> preprocessSaveModel(List<GenerationConfig> generationConfigs,
            ExtraConfig extraConfig) {
        String packagePath = extraConfig.getPackagePath() + DOT + extraConfig.getModule() + ".model";
        String generateDir = packagePath.replace(DOT, SLASH);
        List<GenerationTemplate> templates = new ArrayList<>();

        for (GenerationConfig generationConfig : generationConfigs) {
            GeneralConfig tableInfo = generationConfig.getGeneral();
            String tableName = tableInfo.getTableName().toLowerCase();
            String className = StrUtil.upperFirst(StrUtil.toCamelCase(tableName));

            // 准备模板数据
            Map<String, Object> param = new HashMap<>();
            param.put("package", packagePath);
            param.put("tableComment", tableInfo.getTableComment());
            param.put("isMainTable", generationConfig.isMainTable());
            param.put("className", className);
            param.put("basePackage", extraConfig.getPackagePath() + DOT + extraConfig.getModule());

            // 处理字段
            List<Map<String, Object>> fields = new ArrayList<>();
            Set<String> imports = new HashSet<>();

            if (generationConfig.getForm() != null) {
                generationConfig.getForm().stream()
                        .forEach(form -> {
                            Map<String, Object> field = new HashMap<>();
                            field.put("comment", form.getLabel());
                            field.put("name", form.getAttrName());
                            field.put("type", form.getType());
                            FieldType fieldType = typeConvertor.convertFormType(form.getType(),
                                    form.getColumnDataType());
                            field.put("javaType", fieldType.getJavaType());
                            field.put("dateFormat", form.getDateFormat());
                            fields.add(field);

                            // 如果字段类型需要导入，添加到imports
                            if (fieldType.getImportPackage() != null) {
                                imports.addAll(fieldType.getImportPackage());
                            }
                        });
            }
            param.put("fields", fields);
            param.put("imports", imports);

            // 处理子表关系
            if (generationConfig.isMainTable()) {
                boolean hasSubTable = generationConfigs.stream().anyMatch(config -> !config.isMainTable());
                param.put("hasSubTable", hasSubTable);

                if (hasSubTable) {
                    List<Map<String, Object>> subTables = new ArrayList<>();
                    generationConfigs.stream()
                            .filter(config -> !config.isMainTable())
                            .forEach(subConfig -> {
                                Map<String, Object> subTable = new HashMap<>();
                                String subTableName = subConfig.getGeneral().getTableName().toLowerCase();
                                String subClassName = StrUtil.upperFirst(StrUtil.toCamelCase(subTableName));
                                subTable.put("className", subClassName);
                                subTable.put("varName", StrUtil.toCamelCase(subTableName));
                                subTable.put("comment", subConfig.getGeneral().getTableComment());
                                subTables.add(subTable);
                            });
                    param.put("subTables", subTables);
                }
            } else {
                param.put("hasSubTable", false);
            }

            // 生成文件
            String outputPath = generateDir + SLASH + className + "SaveModel.java";
            templates.add(new GenerationTemplate("saveModel.java.ftl", outputPath, param));
        }
        return templates;
    }

    private List<GenerationTemplate> preprocessController(List<GenerationConfig> generationConfigs,
            ExtraConfig extraConfig) {
        String packagePath = extraConfig.getPackagePath() + DOT + extraConfig.getModule() + ".controller";
        String generateDir = packagePath.replace(DOT, SLASH);
        List<GenerationTemplate> templates = new ArrayList<>();

        for (GenerationConfig generationConfig : generationConfigs) {
            GeneralConfig tableInfo = generationConfig.getGeneral();
            String tableName = tableInfo.getTableName().toLowerCase();
            String classNameVar = StrUtil.toCamelCase(tableName);
            String className = StrUtil.upperFirst(StrUtil.toCamelCase(tableName));

            // 准备模板数据
            Map<String, Object> param = new HashMap<>();
            param.put("package", packagePath);
            param.put("basePackage", extraConfig.getPackagePath() + DOT + extraConfig.getModule());
            param.put("className", className);
            param.put("classNameVar", classNameVar);
            param.put("isMainTable", generationConfig.isMainTable());
            param.put("moduleCn", extraConfig.getModuleCn());

            // 处理类注释
            param.put("classComment", ClassComment.of(tableInfo.getTableComment(), extraConfig.getAuthor()));

            // 生成文件
            String outputPath = generateDir + SLASH + className + "Controller.java";
            templates.add(new GenerationTemplate("controller.java.ftl", outputPath, param));
        }
        return templates;
    }

    private List<GenerationTemplate> preprocessServiceImpl(List<GenerationConfig> generationConfigs,
            ExtraConfig extraConfig) {
        String packagePath = extraConfig.getPackagePath() + DOT + extraConfig.getModule() + ".service.impl";
        String generateDir = packagePath.replace(DOT, SLASH);
        List<GenerationTemplate> templates = new ArrayList<>();

        for (GenerationConfig generationConfig : generationConfigs) {
            GeneralConfig tableInfo = generationConfig.getGeneral();
            String tableName = tableInfo.getTableName().toLowerCase();
            String className = StrUtil.upperFirst(StrUtil.toCamelCase(tableName));

            // 准备模板数据
            Map<String, Object> param = new HashMap<>();
            param.put("package", packagePath);
            param.put("basePackage", extraConfig.getPackagePath() + DOT + extraConfig.getModule());
            param.put("className", className);
            param.put("isMainTable", generationConfig.isMainTable());
            // 处理类注释
            param.put("classComment", ClassComment.of(tableInfo.getTableComment(), extraConfig.getAuthor()));

            // 处理附件相关
            boolean hasAttachment = false;
            List<Map<String, Object>> attachmentFields = new ArrayList<>();
            if (generationConfig.getForm() != null) {
                generationConfig.getForm().stream()
                        .filter(form -> "attachment".equals(form.getType()))
                        .forEach(form -> {
                            Map<String, Object> field = new HashMap<>();
                            field.put("fieldName", StrUtil.upperFirst(form.getAttrName()));
                            field.put("varName", form.getAttrName());
                            attachmentFields.add(field);
                        });
                hasAttachment = !attachmentFields.isEmpty();
            }
            param.put("hasAttachment", hasAttachment);
            param.put("attachmentFields", attachmentFields);

            // 处理子表关系
            if (generationConfig.isMainTable()) {
                List<Map<String, Object>> subClasses = new ArrayList<>();
                for (GenerationConfig subTable : generationConfigs) {
                    if (!subTable.isMainTable()) {
                        String varName = StrUtil.toCamelCase(subTable.getGeneral().getTableName().toLowerCase());
                        Map<String, Object> subClass = new HashMap<>();
                        subClass.put("className", StrUtil.upperFirst(varName));
                        subClass.put("varName", varName);
                        subClass.put("comment", subTable.getGeneral().getTableComment());
                        subClasses.add(subClass);
                    }
                }
                param.put("subClasses", subClasses);
            } else {
                String foreignKey = generationConfig.getGeneral().getForeignKey();
                if (StrUtil.isNotBlank(foreignKey)) {
                    param.put("foreignKey", StrUtil.upperFirst(StrUtil.toCamelCase(foreignKey.toLowerCase())));
                }
            }

            // 生成文件
            String outputPath = generateDir + SLASH + className + "ServiceImpl.java";
            templates.add(new GenerationTemplate("serviceImpl.java.ftl", outputPath, param));
        }
        return templates;
    }

    private List<GenerationTemplate> preprocessService(List<GenerationConfig> generationConfigs,
            ExtraConfig extraConfig) {
        String packagePath = extraConfig.getPackagePath() + DOT + extraConfig.getModule() + ".service";
        String generateDir = packagePath.replace(DOT, SLASH);
        List<GenerationTemplate> templates = new ArrayList<>();

        for (GenerationConfig generationConfig : generationConfigs) {
            GeneralConfig tableInfo = generationConfig.getGeneral();
            String tableName = tableInfo.getTableName().toLowerCase();
            String className = StrUtil.upperFirst(StrUtil.toCamelCase(tableName));

            // 准备模板数据
            Map<String, Object> param = new HashMap<>();
            param.put("package", packagePath);
            param.put("basePackage", extraConfig.getPackagePath() + DOT + extraConfig.getModule());
            param.put("className", className);
            param.put("isMainTable", generationConfig.isMainTable());

            // 处理类注释
            param.put("classComment", ClassComment.of(tableInfo.getTableComment(), extraConfig.getAuthor()));

            // 生成文件
            String outputPath = generateDir + SLASH + className + "Service.java";
            templates.add(new GenerationTemplate("service.java.ftl", outputPath, param));
        }
        return templates;
    }

    private List<GenerationTemplate> preprocessEntity(List<GenerationConfig> generationConfigs,
            ExtraConfig extraConfig) {
        String packagePath = extraConfig.getPackagePath() + DOT + extraConfig.getModule() + ".entity";
        String generateDir = packagePath.replace(DOT, SLASH);
        List<GenerationTemplate> templates = new ArrayList<>();

        for (GenerationConfig generationConfig : generationConfigs) {
            GeneralConfig tableInfo = generationConfig.getGeneral();
            String tableName = tableInfo.getTableName();

            // 获取表的列信息
            List<TableColumn> columns = tableService.getTableColumns(tableName);
            // 准备模板数据
            Map<String, Object> param = new HashMap<>();
            param.put("packageName", packagePath);
            param.put("tableName", tableName);
            param.put("classComment", ClassComment.of(tableInfo.getTableComment(), extraConfig.getAuthor()));
            param.put("className", StrUtil.upperFirst(StrUtil.toCamelCase(tableName.toLowerCase())));
            param.put("hasTableLogic", Boolean.TRUE.equals(tableInfo.getIsLogicDel()));
            // 处理字段信息
            List<Map<String, Object>> fields = new ArrayList<>();
            Set<String> imports = new HashSet<>();

            for (TableColumn column : columns) {
                Map<String, Object> field = new HashMap<>();
                field.put("comment", column.getColumnComment());
                field.put("name", StrUtil.toCamelCase(column.getColumnName().toLowerCase()));

                // 转换数据类型，并收集需要导入的包
                IColumnType columnType = typeConvert.processTypeConvert(column.getDataType());
                String javaType = columnType.getType();
                field.put("type", javaType);
                // 处理主键
                field.put("isTableId", "PRI".equals(column.getKey()));
                // 处理逻辑删除
                field.put("isTableLogic", Boolean.TRUE.equals(tableInfo.getIsLogicDel()) &&
                        StrUtil.equals(column.getColumnName(), tableInfo.getLogicDelField()));

                // 添加需要导入的类型
                if (columnType.getPkg() != null) {
                    imports.add(columnType.getPkg());
                }

                fields.add(field);
            }

            param.put("fields", fields);
            param.put("imports", imports);

            // 生成文件
            String outputPath = generateDir + SLASH + StrUtil.upperFirst(StrUtil.toCamelCase(tableName.toLowerCase()))
                    + ".java";
            templates.add(new GenerationTemplate("entity.java.ftl", outputPath, param));
        }
        return templates;
    }

    private List<GenerationTemplate> preprocessMapperXml(List<GenerationConfig> generationConfigs,
            ExtraConfig extraConfig) {
        String packagePath = extraConfig.getPackagePath() + DOT + extraConfig.getModule() + ".mapper.xml";
        String generateDir = packagePath.replace(DOT, SLASH);
        List<GenerationTemplate> templates = new ArrayList<>();

        for (GenerationConfig generationConfig : generationConfigs) {
            GeneralConfig tableInfo = generationConfig.getGeneral();
            String tableName = tableInfo.getTableName();
            String className = StrUtil.upperFirst(StrUtil.toCamelCase(tableName.toLowerCase()));

            // 准备模板数据
            Map<String, Object> param = new HashMap<>();
            param.put("basePackage", extraConfig.getPackagePath() + DOT + extraConfig.getModule());
            param.put("className", className);
            param.put("tableName", tableName);
            param.put("isMainTable", generationConfig.isMainTable());

            // 处理查询列
            List<Map<String, Object>> tableItems = new ArrayList<>();
            generationConfig.getTable()
                    .stream()
                    .filter(i -> StrUtil.isNotBlank(i.getColumnName()))
                    .forEach(i -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("columnName", i.getColumnName());
                        item.put("columnNameAlias", StrUtil.toUnderlineCase(i.getAttrName()));
                        tableItems.add(item);
                    });
            param.put("tableItems", tableItems);

            if (generationConfig.isMainTable()) {
                // 处理搜索条件
                List<Map<String, Object>> searchItems = new ArrayList<>();
                generationConfig.getSearch()
                        .stream()
                        .filter(i -> StrUtil.isNotBlank(i.getColumnName()))
                        .forEach(i -> {
                            Map<String, Object> item = new HashMap<>();
                            item.put("type", i.getType());
                            item.put("fuzzySearch", !Boolean.FALSE.equals(i.getFuzzySearch()));
                            item.put("rangeSearch", !Boolean.FALSE.equals(i.getRangeSearch()));
                            item.put("columnName", i.getColumnName());
                            item.put("attrName", i.getAttrName());
                            searchItems.add(item);
                        });
                param.put("searchItems", searchItems);

                // 处理表单项
                List<Map<String, Object>> formItems = new ArrayList<>();
                generationConfig.getForm()
                        .stream()
                        .filter(i -> StrUtil.isNotBlank(i.getColumnName()))
                        .forEach(i -> {
                            Map<String, Object> item = new HashMap<>();
                            item.put("columnName", i.getColumnName());
                            item.put("columnNameAlias", StrUtil.toUnderlineCase(i.getAttrName()));
                            formItems.add(item);
                        });
                param.put("formItems", formItems);
            } else {
                param.put("foreignKey", tableInfo.getForeignKey());
            }

            // 生成文件
            String outputPath = generateDir + SLASH + className + "Mapper.xml";
            templates.add(new GenerationTemplate("mapper.xml.ftl", outputPath, param));
        }
        return templates;
    }

    private List<GenerationTemplate> preprocessMapperClass(List<GenerationConfig> generationConfigs,
            ExtraConfig extraConfig) {
        String packagePath = extraConfig.getPackagePath() + DOT + extraConfig.getModule() + ".mapper";
        String generateDir = packagePath.replace(DOT, SLASH);
        List<GenerationTemplate> templates = new ArrayList<>();

        for (GenerationConfig generationConfig : generationConfigs) {
            GeneralConfig tableInfo = generationConfig.getGeneral();
            String tableName = tableInfo.getTableName();
            String className = StrUtil.upperFirst(StrUtil.toCamelCase(tableName.toLowerCase()));

            // 准备模板数据
            Map<String, Object> param = new HashMap<>();
            param.put("package", packagePath);
            param.put("basePackage", extraConfig.getPackagePath() + DOT + extraConfig.getModule());
            param.put("className", className);
            param.put("isMainTable", generationConfig.isMainTable());

            // 处理类注释
            param.put("classComment", ClassComment.of(tableInfo.getTableComment(), extraConfig.getAuthor()));

            // 生成文件
            String outputPath = generateDir + SLASH + className + "Mapper.java";
            templates.add(new GenerationTemplate("mapper.java.ftl", outputPath, param));
        }
        return templates;
    }

    private List<GenerationConfig> preprocess(GenerationRequest config) {
        List<GenerationConfig> configs = new ArrayList<>();

        config.getGeneral()
                .setTableComment(tableService.getTable(config.getGeneral().getTableName()).getTableComment());
        GenerationConfig mainConfig = new GenerationConfig();
        mainConfig.setGeneral(config.getGeneral());
        mainConfig.setSearch(config.getSearch());
        mainConfig.setTable(config.getTable());
        mainConfig.setForm(config.getForm());
        mainConfig.setMainTable(true);
        configs.add(mainConfig);

        for (SubTableConfig subTable : config.getSubTables()) {
            subTable.getGeneral()
                    .setTableComment(tableService.getTable(subTable.getGeneral().getTableName()).getTableComment());
            GenerationConfig subConfig = new GenerationConfig();
            subConfig.setGeneral(subTable.getGeneral());
            subConfig.setTable(subTable.getTable());
            subConfig.setForm(subTable.getForm());
            subConfig.setMainTable(false);
            configs.add(subConfig);
        }
        return configs;
    }
}
