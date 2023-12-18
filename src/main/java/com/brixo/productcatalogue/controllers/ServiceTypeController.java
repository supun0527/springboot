package com.brixo.productcatalogue.controllers;

import com.brixo.exceptionmanagement.exceptions.InvalidMethodInputException;
import com.brixo.productcatalogue.dtos.ServiceTypeDto;
import com.brixo.productcatalogue.services.ServiceTypeService;
import com.brixo.productcatalogue.utils.StringUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Controller is responsible for serviceType end-points. */
@Slf4j
@Validated
@RestController
@RequestMapping("/v1/service-types")
public class ServiceTypeController {

  private final ServiceTypeService serviceTypeService;

  public ServiceTypeController(ServiceTypeService serviceTypeService) {
    this.serviceTypeService = serviceTypeService;
  }

  @GetMapping("/all")
  public ResponseEntity<List<ServiceTypeDto>> getAllServiceTypes() {
    log.info("Request for get all ServiceTypes");
    return new ResponseEntity<>(serviceTypeService.getAllServiceTypes(), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<ServiceTypeDto> getServiceTypeById(
      @RequestParam(value = "id", required = false) Integer id,
      @RequestParam(value = "key", required = false) String key) {
    if (id == null && StringUtil.isEmpty(key)) {
      throw new InvalidMethodInputException("Either id or key should provided.");
    }
    log.info("Request for get all ServiceType by id: {} or key: {}", id, key);
    return new ResponseEntity<>(
        id != null
            ? serviceTypeService.getServiceTypeById(id)
            : serviceTypeService.getServiceTypeByKey(key),
        HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ServiceTypeDto> createServiceType(
      @RequestBody @Valid ServiceTypeDto serviceTypeDto) {
    log.info("Request for create ServiceType: {}", serviceTypeDto);
    return new ResponseEntity<>(
        serviceTypeService.createServiceType(serviceTypeDto), HttpStatus.CREATED);
  }
}
