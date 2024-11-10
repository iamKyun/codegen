package com.iamkyun.codegen.model.request;

import lombok.Data;

@Data
public class ExtraConfig {
    private String module;
    private String moduleCn;
    private String packagePath;
    private String vuePath;
    private String author = "codegen";
}
