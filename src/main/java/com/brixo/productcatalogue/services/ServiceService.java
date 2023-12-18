package com.brixo.productcatalogue.services;

import com.brixo.exceptionmanagement.exceptions.EntityNotFoundException;
import com.brixo.exceptionmanagement.exceptions.InvalidMethodInputException;
import com.brixo.productcatalogue.dtos.ServiceDto;
import com.brixo.productcatalogue.mappers.AbstractMapper;
import com.brixo.productcatalogue.models.Service;
import com.brixo.productcatalogue.models.ServiceType;
import com.brixo.productcatalogue.repositories.ServiceRepository;
import com.brixo.productcatalogue.utils.StringUtil;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {


  private final ServiceRepository serviceRepository;
  private final AbstractMapper abstractMapper;

  public ServiceService(ServiceRepository serviceRepository, AbstractMapper abstractMapper) {
    this.serviceRepository = serviceRepository;
    this.abstractMapper = abstractMapper;
  }

  public List<ServiceDto> getAllServices() {
    return abstractMapper.mapAll(serviceRepository.findAll(), ServiceDto.class);
  }

  public ServiceDto getServiceByKey(String key) {
    return abstractMapper.map(findByKey(key), ServiceDto.class);
  }

  public ServiceDto getServiceById(Integer id) {
    return abstractMapper.map(findById(id), ServiceDto.class);
  }

  public Service findById(Integer id) {
    return serviceRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + id));
  }

  public Service findByKey(String key) {
    return serviceRepository
        .findByKey(key)
        .orElseThrow(() -> new EntityNotFoundException("Service not found with key: " + key));
  }

  public ServiceDto createService(ServiceDto serviceDto) {
    if (serviceDto.getId() == null) {
      if (StringUtil.isEmpty(serviceDto.getKey())) {
        throw new InvalidMethodInputException("key can not be empty.");
      }
      Service service = abstractMapper.map(serviceDto, Service.class);
      return abstractMapper.map(serviceRepository.save(service), ServiceDto.class);
    }
    return updateService(serviceDto);
  }

  public ServiceDto updateService(ServiceDto serviceDto) {
    Service existingService = findById(serviceDto.getId());
    existingService.setName(serviceDto.getName());
    return abstractMapper.map(serviceRepository.save(existingService), ServiceDto.class);
  }
}
