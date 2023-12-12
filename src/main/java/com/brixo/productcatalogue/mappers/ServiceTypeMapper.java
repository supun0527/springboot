package com.brixo.productcatalogue.mappers;

import com.brixo.productcatalogue.dtos.ServiceTypeDto;
import com.brixo.productcatalogue.models.Service;
import com.brixo.productcatalogue.models.ServiceType;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceTypeMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public ServiceTypeMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public ServiceTypeDto convertToDto(ServiceType serviceType) {
        return modelMapper.map(serviceType, ServiceTypeDto.class);
    }

    public ServiceType convertToEntity(ServiceTypeDto serviceTypeDto) {
        ServiceType serviceType = modelMapper.map(serviceTypeDto, ServiceType.class);
        serviceType.setService(Service.builder().id(serviceTypeDto.getServiceId()).build());
        return serviceType;
    }

    public List<ServiceTypeDto> convertListToDtoList(List<ServiceType> serviceTypes){
        return serviceTypes.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
