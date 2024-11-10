package com.iamkyun.codegen.service;

import com.iamkyun.codegen.model.data.TableColumn;
import com.iamkyun.codegen.model.data.TableInfo;
import com.iamkyun.codegen.model.request.GenerationRequest;

import com.iamkyun.codegen.model.template.GenerationResult;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CoreController {
    private final GenerationService generationService;
    private final TableService tableService;
    private final TemplateService templateService;

    @GetMapping("tables")
    public List<TableInfo> getTables() {
        return tableService.getTables();
    }

    @GetMapping("table")
    public TableInfo getTableInfo(@RequestParam String tableName) {
        return tableService.getTable(tableName);
    }

    @GetMapping("columns")
    public List<TableColumn> getTableColumns(@RequestParam String tableName) {
        return tableService.getTableColumns(tableName);
    }

    @PostMapping("generate")
    public GenerationResult generate(@RequestBody GenerationRequest request) {
        try {
            return generationService.generate(request);
        } catch (Exception e) {
            log.error("生成代码失败", e);
            return GenerationResult.withMessage("生成代码失败");
        }
    }

    @GetMapping("download")
    public void download(@RequestParam String zip, HttpServletResponse response) {
        try {
            templateService.download(zip, response);
        } catch (Exception e) {
            log.error("下载文件失败", e);
            throw new RuntimeException("下载文件失败", e);
        }
    }
}
