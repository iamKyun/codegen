package com.iamkyun.codegen.core;

import com.iamkyun.codegen.model.TableColumn;
import com.iamkyun.codegen.model.TableInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CoreController {
    private final CoreService coreService;

    @GetMapping("tables")
    public List<TableInfo> getTables() {
        return coreService.getTables();
    }

    @GetMapping("columns")
    public List<TableColumn> getTableColumns(@RequestParam String tableName) {
        return coreService.getTableColumns(tableName);
    }
}
