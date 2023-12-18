package com.brixo.productcatalogue.controllers;

import com.brixo.productcatalogue.dtos.SettingDto;
import com.brixo.productcatalogue.dtos.SettingRequestDto;
import com.brixo.productcatalogue.services.SettingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller is responsible for settings end-points.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/v1/settings")
public class SettingsController {

    private final SettingService settingService;

    public SettingsController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping
    public ResponseEntity<List<SettingDto>> getAllSettings() {
        log.info("Request for get all Settings");
        return new ResponseEntity<>(settingService.getAllSetting(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SettingDto> getSettingsById(@PathVariable Long id) {
        log.info("Request for get Settings by id: {}", id);
        return new ResponseEntity<>(settingService.getSettingById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SettingDto> createSetting(@RequestBody @Valid SettingRequestDto settingRequestDto) {
        log.info("Request for create/update Settings: {}", settingRequestDto);
        return new ResponseEntity<>(settingService.createOrUpdateSetting(settingRequestDto), HttpStatus.CREATED);
    }

}
