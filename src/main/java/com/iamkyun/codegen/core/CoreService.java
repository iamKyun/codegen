package com.iamkyun.codegen.core;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CoreService {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Value("${spring.datasource.username}")
    private String scheme;

    public List<TableInfo> getTables() {
        String sql = "SELECT t.TABLE_NAME, c.COMMENTS AS TABLE_COMMENT " +
                     "FROM ALL_TABLES t " +
                     "LEFT JOIN ALL_TAB_COMMENTS c ON t.TABLE_NAME = c.TABLE_NAME AND t.OWNER = c.OWNER " +
                     "WHERE t.OWNER = :owner";
        Map<String, Object> params = new HashMap<>();
        params.put("owner", scheme);
        return namedParameterJdbcTemplate.query(sql, params,
            (rs, rowNum) -> new TableInfo(rs.getString("TABLE_NAME"), rs.getString("TABLE_COMMENT")));
    }

    public List<TableColumn> getTableColumns(String tableName) {
        String sql =
            "SELECT c.COLUMN_NAME, c.DATA_TYPE, c.DATA_LENGTH, c.DATA_SCALE, co.COMMENTS AS COLUMN_COMMENT,c.NULLABLE NULLABLE " +
                     "FROM ALL_TAB_COLUMNS c " +
                     "LEFT JOIN ALL_COL_COMMENTS co ON c.COLUMN_NAME = co.COLUMN_NAME " +
                     "AND c.TABLE_NAME = co.TABLE_NAME " +
                     "AND c.OWNER = co.OWNER " +
            "WHERE c.OWNER = :owner " +
                     "AND c.TABLE_NAME = :name";
        Map<String, Object> params = new HashMap<>();
        params.put("owner", scheme);
        params.put("name", tableName);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            String columnName = rs.getString("COLUMN_NAME");
            String columnType = rs.getString("DATA_TYPE");
            Integer dataLength = rs.getInt("DATA_LENGTH");
            Integer dataScale = rs.getInt("DATA_SCALE");
            String columnComment = rs.getString("COLUMN_COMMENT");
            String nullable = rs.getString("NULLABLE");
            return new TableColumn(columnName, columnType, dataLength, dataScale, columnComment, nullable);
        });
    }
}
