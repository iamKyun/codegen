package com.iamkyun.codegen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.iamkyun")
public class CodegenApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodegenApplication.class, args);
	}

}
