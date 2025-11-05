package com.app.hungrify.main.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;

@Converter(autoApply = true)
public class JsonMapConverter implements AttributeConverter<Map<String,Object>, String> {
    private static final ObjectMapper M = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return attribute == null ? null : M.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to convert map to JSON string", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? new java.util.HashMap<>() : M.readValue(dbData, Map.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to convert JSON string to map", e);
        }
    }
}