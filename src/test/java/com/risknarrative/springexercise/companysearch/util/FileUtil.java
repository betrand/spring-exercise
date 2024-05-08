package com.risknarrative.springexercise.companysearch.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public final class FileUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> Object readFile(String filePath, Class<T> clazz) {
        try {
            File file = new File(filePath);
            return objectMapper.readValue(file, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON file into class " + clazz, e);
        }
    }

}
