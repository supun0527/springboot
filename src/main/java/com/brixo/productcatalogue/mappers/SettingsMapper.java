package com.brixo.productcatalogue.mappers;

import com.brixo.productcatalogue.dtos.SettingsDto;
import com.brixo.productcatalogue.dtos.SettingsValueDto;
import com.brixo.productcatalogue.models.Settings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SettingsMapper {

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    public SettingsMapper() {
        this.modelMapper = new ModelMapper();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public SettingsDto convertToDto(Settings settings) {
        SettingsDto dto = modelMapper.map(settings, SettingsDto.class);
        if (settings.getValue() != null) {
            try {
                dto.setValue(objectMapper.readValue(settings.getValue(), SettingsValueDto.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return dto;
    }

    public Settings convertToEntity(SettingsDto settingsDto) {
        Settings entity = modelMapper.map(settingsDto, Settings.class);
        try {
            entity.setValue(objectMapper.writeValueAsString(settingsDto.getValue()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    public List<SettingsDto> convertListToDtoList(List<Settings> settings){
        return settings.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public String serializeValueObject(final SettingsValueDto value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}
