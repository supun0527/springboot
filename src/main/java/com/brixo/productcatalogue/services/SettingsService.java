package com.brixo.productcatalogue.services;

import com.brixo.exceptionmanagement.exceptions.EntityNotFoundException;
import com.brixo.productcatalogue.dtos.SettingsDto;
import com.brixo.productcatalogue.mappers.SettingsMapper;
import com.brixo.productcatalogue.models.Settings;
import com.brixo.productcatalogue.repositories.ProductRepository;
import com.brixo.productcatalogue.repositories.SettingsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<SettingsDto> createOrUpdateSettings(final SettingsDto settingsDto) {
        if (!productRepository.existsById(settingsDto.getProductId())) {
            throw new EntityNotFoundException("Unable to create or update setting, product not found with id: " + settingsDto.getProductId());
        }
        if (settingsDto.getId() == null) {
            // validate if value is null
            final Settings settings = settingsMapper.convertToEntity(settingsDto);
            return new ResponseEntity<>(settingsMapper.convertToDto(settingsRepository.save(settings)), HttpStatus.CREATED);
        }
        Optional<Settings> byKeyAndProductId = findByKeyAndProductId(settingsDto.getKey(), settingsDto.getProductId());
        if (byKeyAndProductId.isPresent()) {
            return new ResponseEntity<>(updateSettings(settingsDto), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(updateSettings(settingsDto), HttpStatus.ACCEPTED);
    }
    public SettingsDto updateSettings(final SettingsDto settingsDto) {
        Settings existingSettings = findById(settingsDto.getId());
        existingSettings.setName(settingsDto.getName());
        existingSettings.setUpdatedBy(settingsDto.getUpdatedBy());
        try {
            existingSettings.setValue(settingsMapper.serializeValueObject(settingsDto.getValue()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return settingsMapper.convertToDto(settingsRepository.save(existingSettings));
    }
}
