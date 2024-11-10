package com.iamkyun.codegen.model.template;

import lombok.Data;

@Data
public class GenerationResult {
    private String message;
    private String zip;

    public static GenerationResult withMessage(String message) {
        GenerationResult generationResult = new GenerationResult();
        generationResult.setMessage(message);
        return generationResult;
    }

    public static GenerationResult withZip(String zip) {
        GenerationResult generationResult = new GenerationResult();
        generationResult.setZip(zip);
        return generationResult;
    }
}
