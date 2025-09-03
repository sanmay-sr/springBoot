package com.bfh.qualifier.solution;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

@Component
public class SolutionProvider {
    public String provideFinalQuery(String regNo) {
        boolean even = isEven(lastTwoDigits(regNo));
        String path = even ? "sql/question2.sql" : "sql/question1.sql";
        try {
            var res = new ClassPathResource(path);
            return new String(res.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read SQL file: " + path, e);
        }
    }

    private int lastTwoDigits(String regNo) {
        String digits = regNo.replaceAll("\\D", "");
        if (digits.length() < 2) {
            return Integer.parseInt(digits);
        }
        return Integer.parseInt(digits.substring(digits.length() - 2));
    }

    private boolean isEven(int num) {
        return num % 2 == 0;
    }
}


