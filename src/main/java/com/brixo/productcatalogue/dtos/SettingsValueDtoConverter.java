package com.brixo.productcatalogue.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SettingsValueDtoConverter implements
        AttributeConverter<SettingsValueDto, String> {
    private final ObjectMapper objectMapper;

    public SettingsValueDtoConverter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public String convertToDatabaseColumn(SettingsValueDto settingsValueDto) {
        try {
            return objectMapper.writeValueAsString(settingsValueDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SettingsValueDto convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, SettingsValueDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}