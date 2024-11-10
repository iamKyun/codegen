package com.iamkyun.codegen.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.iamkyun.codegen.model.template.GenerationTemplate;

import cn.hutool.core.util.ZipUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateService {
    private final String SLASH = "/";

    public String generateAndPackageFiles(List<GenerationTemplate> templates) throws Exception {
        // 创建临时目录
        String id = String.valueOf(System.currentTimeMillis());
        String tempPath = System.getProperty("java.io.tmpdir") + "/codegen/" + id;
        String javaTempPath = tempPath + "/java";
        String vueTempPath = tempPath + "/vue";
        File tempDir = new File(tempPath);
        File javaTempDir = new File(javaTempPath);
        File vueTempDir = new File(vueTempPath);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        if (!javaTempDir.exists()) {
            javaTempDir.mkdirs();
        }
        if (!vueTempDir.exists()) {
            vueTempDir.mkdirs();
        }
        for (GenerationTemplate template : templates) {
            generateFile(template, javaTempDir.getAbsolutePath());
        }

        // tempDir打包成zip
        String zipPath = tempPath + ".zip";
        log.info("ZIP ==> {}", zipPath);
        ZipUtil.zip(tempDir.getAbsolutePath(), zipPath, false);
        return id;
    }

    private void generateFile(GenerationTemplate generationTemplate, String tempDir)
            throws Exception {
        String template = generationTemplate.getTemplate();
        Object param = generationTemplate.getParam();
        String outputPath = generationTemplate.getOutputPath();
        // 使用模板引擎来生成文件
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setClassForTemplateLoading(this.getClass(), "/templates");
        Template temp = cfg.getTemplate(template);
        File outFile = new File(tempDir + SLASH + outputPath);
        // 确保目标目录存在
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }

        // 使用FreeMarker生成文件
        Writer out = new FileWriter(outFile);
        temp.process(param, out);
        out.close();
    }

    public void download(String zip, HttpServletResponse response) throws Exception {
        File file = new File(System.getProperty("java.io.tmpdir") + "/codegen/" + zip + ".zip");
        if (file.exists()) {
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            response.setContentLength((int) file.length());
            FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
        } else {
            throw new RuntimeException("文件不存在");
        }
    }
}
