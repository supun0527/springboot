package com.brixo.productcatalogue.services;

import com.brixo.exceptionmanagement.exceptions.EntityNotFoundException;
import com.brixo.productcatalogue.dtos.SettingSubValueDto;
import com.brixo.productcatalogue.dtos.SettingDto;
import com.brixo.productcatalogue.dtos.SettingRequestDto;
import com.brixo.productcatalogue.dtos.SettingValueDto;
import com.brixo.productcatalogue.mappers.SettingMapper;
import com.brixo.productcatalogue.models.Setting;
import com.brixo.productcatalogue.repositories.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SettingService {

    private final SettingRepository settingRepository;
    private final ProductService productService;
    private final SettingMapper settingMapper;
    private final SettingHistoryService settingHistoryService;

    @Autowired
    public SettingService(final SettingRepository settingRepository,
                          final ProductService productService,
                          final SettingMapper settingMapper, SettingHistoryService settingHistoryService) {
        this.settingRepository = settingRepository;
        this.productService = productService;
        this.settingMapper = settingMapper;
        this.settingHistoryService = settingHistoryService;
    }

    public List<SettingDto> getAllSetting() {
        return settingMapper.convertListToDtoList(settingRepository.findAll());
    }

    public SettingDto getSettingById(final Long id) {
        return settingMapper.convertToDto(findById(id));
    }

    public Setting findById(final Long id) {
        return settingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Setting not found with id: " + id));
    }


    public Setting findByKeyAndProductId(final String key, final int productId) {
        return settingRepository.findByKeyAndProductId(key, productId).orElse(null);
    }

    public SettingDto createOrUpdateSetting(final SettingRequestDto settingRequestDto) {
        Setting setting = findByKeyAndProductId(settingRequestDto.getKey(), settingRequestDto.getProductId());
        if (setting == null) {
            setting = createSetting(settingRequestDto);
        } else if (isFutureValueActivated(setting)) {
            updateCurrentValueFromFuture(setting);
        }
        setPayloadValueToFuture(settingRequestDto, setting);
        setting = settingRepository.save(setting);
        settingHistoryService.createSettingHistory(setting);
        return settingMapper.convertToDto(setting);

    }

    private void setPayloadValueToFuture(SettingRequestDto settingRequestDto, Setting setting) {
        setting.setFuture(SettingSubValueDto.builder().value(settingRequestDto.getValue()).activatedAt(settingRequestDto.getActivateAt()).build());
    }

    private static void updateCurrentValueFromFuture(Setting setting) {
        setting.setCurrent(setting.getConvertedSettingValue().getFuture());
    }

    private static boolean isFutureValueActivated(Setting setting) {
        return setting.getConvertedSettingValue().getFuture().getActivatedAt().isBefore(LocalDateTime.now());
    }

    private Setting createSetting(SettingRequestDto settingRequestDto) {
        productService.findById(settingRequestDto.getProductId());
        Setting setting = settingMapper.convertToEntity(settingRequestDto);
        setting.setValueBySettingValueDto(SettingValueDto.builder().current(SettingSubValueDto.builder().build()).future(SettingSubValueDto.builder().build()).build());
        return setting;
    }

}
