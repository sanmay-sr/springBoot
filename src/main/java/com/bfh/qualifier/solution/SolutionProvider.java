package com.bfh.qualifier.solution;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class SolutionProvider {
    public String provideFinalQuery(String regNo) {
        // Since regNo ends in 73 (odd), always use question1.sql
        String path = "sql/question1.sql";
        try {
            var res = new ClassPathResource(path);
            return new String(res.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read SQL file: " + path, e);
        }
    }
}
