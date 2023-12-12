package com.brixo.productcatalogue.services;

import com.brixo.exceptionmanagement.exceptions.EntityNotFoundException;
import com.brixo.exceptionmanagement.exceptions.InvalidMethodInputException;
import com.brixo.productcatalogue.dtos.ServiceDto;
import com.brixo.productcatalogue.mappers.ServiceMapper;
import com.brixo.productcatalogue.models.Service;
import com.brixo.productcatalogue.repositories.ServiceRepository;
import com.brixo.productcatalogue.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, ServiceMapper serviceMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
    }

    public List<ServiceDto> getAllServices() {
        return serviceMapper.convertListToDtoList(serviceRepository.findAll());
    }

    public ServiceDto getServiceByKey(String key) {
        return serviceMapper.convertToDto(findByKey(key));
    }

    public ServiceDto getServiceById(Integer id) {
        return serviceMapper.convertToDto(findById(id));
    }

    public Service findById(Integer id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + id));
    }

    public Service findByKey(String key) {
        return serviceRepository.findByKey(key)
                .orElseThrow(() -> new EntityNotFoundException("Service not found with key: " + key));
    }

    public ServiceDto createService(ServiceDto serviceDto) {
        if(serviceDto.getId() == null){
            if(StringUtil.isEmpty(serviceDto.getKey())){
                throw new InvalidMethodInputException("key can not be empty.");
            }
            Service service = serviceMapper.convertToEntity(serviceDto);
            return serviceMapper.convertToDto(serviceRepository.save(service));
        }
        return updateService(serviceDto);
    }

    public ServiceDto updateService(ServiceDto serviceDto) {
        Service existingService = findById(serviceDto.getId());
        existingService.setName(serviceDto.getName());
        return serviceMapper.convertToDto(serviceRepository.save(existingService));
    }


}
