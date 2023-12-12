package com.brixo.productcatalogue.mappers;

import com.brixo.productcatalogue.dtos.ServiceDto;
import com.brixo.productcatalogue.models.Service;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceMapper {

    private final ModelMapper modelMapper;

    public ServiceMapper() {
        this.modelMapper = new ModelMapper();
    }

    public ServiceDto convertToDto(Service service) {
        return modelMapper.map(service, ServiceDto.class);
    }

    public Service convertToEntity(ServiceDto serviceDto) {
        return modelMapper.map(serviceDto, Service.class);
    }

    public List<ServiceDto> convertListToDtoList(List<Service> services){
        return services.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
