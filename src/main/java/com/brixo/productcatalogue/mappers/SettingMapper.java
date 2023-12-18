package com.brixo.productcatalogue.mappers;

import com.brixo.productcatalogue.dtos.SettingDto;
import com.brixo.productcatalogue.dtos.SettingRequestDto;
import com.brixo.productcatalogue.models.Setting;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SettingMapper {

    private final ModelMapper modelMapper;

    public SettingMapper() {
        this.modelMapper = new ModelMapper();
    }

    public SettingDto convertToDto(Setting setting) {
        SettingDto dto = modelMapper.map(setting, SettingDto.class);
        if (setting.getConvertedSettingValue().getFuture().getActivatedAt().isBefore(LocalDateTime.now())) {
            dto.getValue().getCurrent().replaceWith(setting.getConvertedSettingValue().getFuture());
            dto.getValue().setFuture(null);
        }
        else if(setting.getConvertedSettingValue().getCurrent().getValue() == null){
            dto.getValue().setCurrent(null);
        }
        return dto;
    }

    public Setting convertToEntity(SettingRequestDto settingRequestDto) {
        return modelMapper.map(settingRequestDto, Setting.class);
    }

    public List<SettingDto> convertListToDtoList(List<Setting> settings) {
        return settings.stream().map(this::convertToDto).toList();
    }


}
