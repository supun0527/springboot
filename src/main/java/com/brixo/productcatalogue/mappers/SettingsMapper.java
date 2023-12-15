package com.brixo.productcatalogue.mappers;

import com.brixo.productcatalogue.dtos.SettingsDto;
import com.brixo.productcatalogue.dtos.SettingsRequestDto;
import com.brixo.productcatalogue.models.Settings;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SettingsMapper {

    private final ModelMapper modelMapper;

    public SettingsMapper() {
        this.modelMapper = new ModelMapper();
    }

    public SettingsDto convertToDto(Settings settings) {
        SettingsDto dto = modelMapper.map(settings, SettingsDto.class);
        if(settings.getValue().getFuture().getActivatedAt().isBefore(LocalDateTime.now())){
            dto.getValue().getCurrent().setValue(settings.getValue().getFuture().getValue());
            dto.getValue().getCurrent().setActivatedAt(settings.getValue().getFuture().getActivatedAt());
            dto.getValue().setFuture(null);
        }
        return dto;
    }

    public Settings convert(SettingsRequestDto settingsRequestDto) {
        return modelMapper.map(settingsRequestDto, Settings.class);
    }

    public List<SettingsDto> convertListToDtoList(List<Settings> settings){
        return settings.stream().map(this::convertToDto).toList();
    }


}
