package com.iamkyun.codegen.model.template;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerationTemplate {
    private String template;
    private String outputPath;
    private Object param;
}
