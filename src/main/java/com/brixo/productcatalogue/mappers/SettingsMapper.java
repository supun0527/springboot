package com.brixo.productcatalogue.mappers;

import com.brixo.productcatalogue.dtos.SettingsDto;
import com.brixo.productcatalogue.dtos.SettingsRequestDto;
import com.brixo.productcatalogue.dtos.SettingsValueDto;
import com.brixo.productcatalogue.models.Settings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
        if(settings.getValue().getFuture().getActivatedAt().isBefore(LocalDateTime.now())){
            dto.getValue().getCurrent().setValue(settings.getValue().getFuture().getValue());
            dto.getValue().getCurrent().setActivatedAt(settings.getValue().getFuture().getActivatedAt());
        }
        return dto;
    }

    public Settings convert(SettingsRequestDto settingsRequestDto) {
        return modelMapper.map(settingsRequestDto, Settings.class);
    }


    public String serialize(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SettingsDto> convertListToDtoList(List<Settings> settings){
        return settings.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public String serializeValueObject(final SettingsValueDto value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}
