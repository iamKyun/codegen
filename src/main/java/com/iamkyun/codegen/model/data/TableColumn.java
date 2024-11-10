package com.iamkyun.codegen.model.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableColumn {
    private String columnName;
    private String columnComment;
    private String dataType;
    private String key;
}
