package com.mindflow.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mindflow")
public class MindFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindFlowApplication.class, args);
    }
}
