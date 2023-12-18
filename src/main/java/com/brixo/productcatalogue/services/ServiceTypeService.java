package com.brixo.productcatalogue.services;

import com.brixo.exceptionmanagement.exceptions.EntityNotFoundException;
import com.brixo.exceptionmanagement.exceptions.InvalidMethodInputException;
import com.brixo.productcatalogue.dtos.ServiceTypeDto;
import com.brixo.productcatalogue.mappers.AbstractMapper;
import com.brixo.productcatalogue.models.ServiceType;
import com.brixo.productcatalogue.repositories.ServiceTypeRepository;
import com.brixo.productcatalogue.utils.StringUtil;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceService serviceService;
    private final AbstractMapper abstractMapper;

    public ServiceTypeService(ServiceTypeRepository serviceTypeRepository, ServiceService serviceService, AbstractMapper abstractMapper) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.serviceService = serviceService;
        this.abstractMapper = abstractMapper;
    }

    public List<ServiceTypeDto> getAllServiceTypes() {
        return abstractMapper.mapAll(serviceTypeRepository.findAll(), ServiceTypeDto.class);
    }


    public ServiceTypeDto getServiceTypeById(Integer id) {
        return abstractMapper.map(findById(id), ServiceTypeDto.class);
    }

    public ServiceTypeDto getServiceTypeByKey(String key) {
        return abstractMapper.map(findByKey(key), ServiceTypeDto.class);
    }

    private ServiceType findById(Integer id) {
        return serviceTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ServiceType not found with id: " + id));
    }

    private ServiceType findByKey(String key) {
        return serviceTypeRepository.findByKey(key)
                .orElseThrow(() -> new EntityNotFoundException("ServiceType not found with key: " + key));
    }


    public ServiceTypeDto createServiceType(ServiceTypeDto serviceTypeDto) {
        if (serviceTypeDto.getId() == null) {
            if (StringUtil.isEmpty(serviceTypeDto.getKey())) {
                throw new InvalidMethodInputException("key can not be empty.");
            }
            if (serviceTypeDto.getServiceId() == null) {
                throw new InvalidMethodInputException("ServiceId must not be null.");
            }
            ServiceType serviceType = abstractMapper.map(serviceTypeDto, ServiceType.class);
            serviceType.setService(serviceService.findById(serviceTypeDto.getServiceId()));
            return abstractMapper.map(serviceTypeRepository.save(serviceType), ServiceTypeDto.class);
        }
        return updateServiceType(serviceTypeDto);
    }

    public ServiceTypeDto updateServiceType(ServiceTypeDto serviceTypeDto) {
        ServiceType existingServiceType = findById(serviceTypeDto.getId());
        existingServiceType.setName(serviceTypeDto.getName());
        return abstractMapper.map(serviceTypeRepository.save(existingServiceType), ServiceTypeDto.class);
    }


}
