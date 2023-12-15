package com.brixo.productcatalogue.controllers;

import com.brixo.productcatalogue.dtos.SettingsDto;
import com.brixo.productcatalogue.dtos.SettingsRequestDto;
import com.brixo.productcatalogue.services.SettingsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Controller is responsible for settings end-points. */
@Slf4j
@Validated
@RestController
@RequestMapping("/v1/settings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<List<SettingsDto>> getAllSettings() {
        log.info("Request for get all Settings");
        return new ResponseEntity<>(settingsService.getAllSettings(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SettingsDto> getSettingsById(@PathVariable Long id) {
        log.info("Request for get Settings by id: {}", id);
        return new ResponseEntity<>(settingsService.getSettingsById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SettingsDto> createSetting(@RequestBody @Valid SettingsRequestDto settingsRequestDto) {
        log.info("Request for create/update Settings: {}", settingsRequestDto);
        return settingsService.createOrUpdateSettings(settingsRequestDto);
    }

}
