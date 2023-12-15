package com.brixo.productcatalogue.services;

import com.brixo.exceptionmanagement.exceptions.EntityNotFoundException;
import com.brixo.productcatalogue.dtos.SettingsDto;
import com.brixo.productcatalogue.dtos.SettingsRequestDto;
import com.brixo.productcatalogue.mappers.SettingsMapper;
import com.brixo.productcatalogue.models.Settings;
import com.brixo.productcatalogue.repositories.ProductRepository;
import com.brixo.productcatalogue.repositories.SettingsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;
    private final ProductRepository productRepository;
    private final SettingsMapper settingsMapper;

    @Autowired
    public SettingsService(final SettingsRepository settingsRepository,
                           final ProductRepository productRepository,
                           final SettingsMapper settingsMapper) {
        this.settingsRepository = settingsRepository;
        this.productRepository = productRepository;
        this.settingsMapper = settingsMapper;
    }

    public List<SettingsDto> getAllSettings() {
        return settingsMapper.convertListToDtoList(settingsRepository.findAll());
    }

    public SettingsDto getSettingsById(final Long id) {
        return settingsMapper.convertToDto(findById(id));
    }

    public Settings findById(final Long id) {
        return settingsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Settings not found with id: " + id));
    }


    public Optional<Settings> findByKeyAndProductId(final String key, final int productId) {
        return settingsRepository.findByKeyAndProductId(key, productId);
    }

    public ResponseEntity<SettingsDto> createOrUpdateSettings(final SettingsRequestDto settingsRequestDto) {
        if (!productRepository.existsById(settingsRequestDto.getProductId())) {
            throw new EntityNotFoundException("Unable to create or update setting, product not found with id: " + settingsRequestDto.getProductId());
        }
        Settings settings = findByKeyAndProductId(settingsRequestDto.getKey(), settingsRequestDto.getProductId()).orElse(null);
        String value = settingsMapper.serialize(settingsRequestDto.getValue());
        if (settings == null) {
            settings = settingsMapper.convert(settingsRequestDto);
        } else {
            if (settings.getValue().getFuture().getActivatedAt().isBefore(LocalDateTime.now())) {
                settings.getValue().getCurrent().setValue(settings.getValue().getFuture().getValue());
                settings.getValue().getCurrent().setActivatedAt(settings.getValue().getFuture().getActivatedAt());
            }

        }
        settings.getValue().getFuture().setValue(value);
        settings.getValue().getFuture().setActivatedAt(settingsRequestDto.getActivateAt());
        return new ResponseEntity<>(settingsMapper.convertToDto(settingsRepository.save(settings)), HttpStatus.CREATED);
    }

}
