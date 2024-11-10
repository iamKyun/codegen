package com.iamkyun.codegen.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iamkyun.codegen.model.data.TableColumn;
import com.iamkyun.codegen.model.data.TableInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TableService {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TableInfo getTable(String tableName) {
        String sql = """
                SELECT DISTINCT T1.TABLE_NAME AS TABLE_NAME, T2.COMMENTS AS TABLE_COMMENT
                FROM USER_TAB_COLUMNS T1
                         INNER JOIN USER_TAB_COMMENTS T2 ON T1.TABLE_NAME = T2.TABLE_NAME
                WHERE T1.TABLE_NAME = :tableName
                LIMIT 1""";

        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        List<TableInfo> result = namedParameterJdbcTemplate.query(sql, params,
                (rs, rowNum) -> new TableInfo(rs.getString("TABLE_NAME"), rs.getString("TABLE_COMMENT")));
        if (result.isEmpty()) {
            return null;
        }
        return result.getFirst();
    }

    public List<TableInfo> getTables() {
        String sql = """
                SELECT DISTINCT T1.TABLE_NAME AS TABLE_NAME, T2.COMMENTS AS TABLE_COMMENT
                FROM USER_TAB_COLUMNS T1
                         INNER JOIN USER_TAB_COMMENTS T2 ON T1.TABLE_NAME = T2.TABLE_NAME""";
        return namedParameterJdbcTemplate.query(sql,
                (rs, rowNum) -> new TableInfo(rs.getString("TABLE_NAME"), rs.getString("TABLE_COMMENT")));
    }

    public List<TableColumn> getTableColumns(String tableName) {
        // 检查表是否存在
        TableInfo tableInfo = getTable(tableName);
        if (tableInfo == null) {
            throw new RuntimeException("表不存在");
        }
        String sql = """
                SELECT T2.COLUMN_NAME,
                       T1.COMMENTS as COLUMN_COMMENT,
                       CASE
                           WHEN T2.DATA_TYPE = 'NUMBER' THEN (CASE
                                                                  WHEN T2.DATA_PRECISION IS NULL THEN T2.DATA_TYPE
                                                                  WHEN NVL(T2.DATA_SCALE, 0) > 0
                                                                      THEN T2.DATA_TYPE || '(' || T2.DATA_PRECISION || ',' || T2.DATA_SCALE || ')'
                                                                  ELSE T2.DATA_TYPE || '(' || T2.DATA_PRECISION || ')' END)
                           ELSE T2.DATA_TYPE END DATA_TYPE,
                       CASE WHEN CONSTRAINT_TYPE = 'P' THEN 'PRI' END AS KEY
                FROM USER_COL_COMMENTS T1, USER_TAB_COLUMNS T2, (SELECT T4.TABLE_NAME, T4.COLUMN_NAME, T5.CONSTRAINT_TYPE
                    FROM USER_CONS_COLUMNS T4, USER_CONSTRAINTS T5
                    WHERE T4.CONSTRAINT_NAME = T5.CONSTRAINT_NAME
                    AND T5.CONSTRAINT_TYPE = 'P') T3
                WHERE T1.TABLE_NAME = T2.TABLE_NAME
                  AND
                    T1.COLUMN_NAME=T2.COLUMN_NAME
                  AND
                    T1.TABLE_NAME = T3.TABLE_NAME(+)
                  AND
                    T1.COLUMN_NAME=T3.COLUMN_NAME(+)
                  AND
                    T1.TABLE_NAME = :tableName
                ORDER BY T2.TABLE_NAME, T2.COLUMN_ID
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            String columnName = rs.getString("COLUMN_NAME");
            String columnComment = rs.getString("COLUMN_COMMENT");
            String columnType = rs.getString("DATA_TYPE");
            String key = rs.getString("KEY");
            return new TableColumn(columnName, columnComment, columnType, key);
        });
    }
}
