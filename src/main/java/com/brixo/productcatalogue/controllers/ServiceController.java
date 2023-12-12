package com.brixo.productcatalogue.controllers;

import com.brixo.exceptionmanagement.exceptions.InvalidMethodInputException;
import com.brixo.productcatalogue.dtos.ServiceDto;
import com.brixo.productcatalogue.mappers.ServiceMapper;
import com.brixo.productcatalogue.services.ServiceService;
import com.brixo.productcatalogue.utils.StringUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Controller is responsible for service end-points. */
@Slf4j
@Validated
@RestController
@RequestMapping("/v1/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        log.info("Request for get all Services");
        return new ResponseEntity<>(serviceService.getAllServices(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<ServiceDto> getService(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "key", required = false) String key) {
        if(id == null && StringUtil.isEmpty(key)){
            throw new InvalidMethodInputException("Either id or key should provided.");
        }
        log.info("Request for get all Service by id: {} or key: {}",id, key);
        return new ResponseEntity<>( id != null ? serviceService.getServiceById(id): serviceService.getServiceByKey(key), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ServiceDto> createService(@RequestBody @Valid ServiceDto serviceDto) {
        log.info("Request for create Service: {}", serviceDto);
        return new ResponseEntity<>(serviceService.createService(serviceDto), HttpStatus.CREATED);
    }

}
