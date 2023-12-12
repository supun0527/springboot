package com.brixo.productcatalogue.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.brixo.exceptionmanagement.exceptions.EntityNotFoundException;
import com.brixo.productcatalogue.Fixture;
import com.brixo.productcatalogue.dtos.ServiceTypeDto;
import com.brixo.productcatalogue.mappers.ServiceTypeMapper;
import com.brixo.productcatalogue.models.Service;
import com.brixo.productcatalogue.models.ServiceType;
import com.brixo.productcatalogue.repositories.ServiceTypeRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ServiceTypeServiceTest {

  @Mock private ServiceTypeRepository serviceTypeRepository;
  @Mock private ServiceService serviceService;
  @Mock private ServiceTypeMapper serviceTypeMapper;

  @InjectMocks private ServiceTypeService serviceTypeService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllServiceTypes_success() {
    // Arrange
    int serviceId1 = 1;
    String serviceTypeName1 = "ServiceType1";

    int serviceId2 = 2;
    String serviceTypeName2 = "ServiceType2";

    ServiceType serviceType1 = ServiceType.builder().id(serviceId1).name(serviceTypeName1).build();
    ServiceType serviceType2 = ServiceType.builder().id(serviceId2).name(serviceTypeName2).build();

    ServiceTypeDto serviceTypeDto1 =
        ServiceTypeDto.builder().id(serviceId1).name(serviceTypeName1).build();
    ServiceTypeDto serviceTypeDto2 =
        ServiceTypeDto.builder().id(serviceId2).name(serviceTypeName2).build();

    when(serviceTypeRepository.findAll()).thenReturn(List.of(serviceType1, serviceType2));
    when(serviceTypeMapper.convertListToDtoList(any()))
        .thenReturn(List.of(serviceTypeDto1, serviceTypeDto2));

    // Act
    List<ServiceTypeDto> result = serviceTypeService.getAllServiceTypes();

    // Assert
    assertEquals(2, result.size());
    assertEquals(serviceTypeDto1, result.get(0));
    assertEquals(serviceTypeDto2, result.get(1));

    verify(serviceTypeRepository, times(1)).findAll();
    verify(serviceTypeMapper, times(1)).convertListToDtoList(any());
  }

  @Test
  void getServiceTypeById_success() {
    // Arrange
    ServiceType serviceType = Fixture.serviceTypeBuilder().build();
    serviceType.setCreatedAt(LocalDateTime.now());
    serviceType.setUpdatedAt(LocalDateTime.now());

    ServiceTypeDto serviceTypeDto = Fixture.serviceTypeDtoBuilder().build();

    when(serviceTypeRepository.findById(serviceType.getId())).thenReturn(Optional.of(serviceType));
    when(serviceTypeMapper.convertToDto(any())).thenReturn(serviceTypeDto);

    // Act
    ServiceTypeDto result = serviceTypeService.getServiceTypeById(serviceType.getId());

    // Assert
    assertEquals(serviceTypeDto, result);

    verify(serviceTypeRepository, times(1)).findById(serviceType.getId());
    verify(serviceTypeMapper, times(1)).convertToDto(any());
  }

  @Test
  void getServiceTypeById_whenServiceTypeNotFound_throwException() {
    // Arrange
    int nonExistentServiceTypeId = 999;
    when(serviceTypeRepository.findById(nonExistentServiceTypeId)).thenReturn(Optional.empty());

    // Act & Assert
    RuntimeException exception =
        assertThrows(
            EntityNotFoundException.class,
            () -> serviceTypeService.getServiceTypeById(nonExistentServiceTypeId));
    assertEquals(
        "ServiceType not found with id: " + nonExistentServiceTypeId, exception.getMessage());

    // Verify that the serviceTypeRepository.findById() method was called once with the specified ID
    verify(serviceTypeRepository, times(1)).findById(nonExistentServiceTypeId);
  }

  @Test
  void createServiceType_success() {
    // Arrange

    Service service = Fixture.serviceBuilder().build();
    ServiceType serviceType = Fixture.serviceTypeBuilder().build();
    ServiceTypeDto serviceTypeDto = Fixture.serviceTypeDtoBuilder().build();

    when(serviceService.findById(any())).thenReturn(service);
    when(serviceTypeRepository.save(serviceType)).thenReturn(serviceType);
    when(serviceTypeMapper.convertToEntity(any())).thenReturn(serviceType);
    when(serviceTypeMapper.convertToDto(any())).thenReturn(serviceTypeDto);

    // Act
    ServiceTypeDto result =
        serviceTypeService.createServiceType(
            Fixture.serviceTypeDtoBuilder().serviceId(service.getId()).build());

    // Assert
    assertEquals(serviceTypeDto, result);

    verify(serviceService, times(serviceType.getId())).findById(service.getId());
    verify(serviceTypeRepository, times(serviceType.getId())).save(serviceType);
    verify(serviceTypeMapper, times(serviceType.getId())).convertToEntity(any());
    verify(serviceTypeMapper, times(serviceType.getId())).convertToDto(serviceType);
  }

  @Test
  void updateServiceType_success() {
    // Arrange
    String updatedServiceTypeName = "Updated ServiceType";
    ServiceType existingServiceType = Fixture.serviceTypeBuilder().build();
    ServiceType updatedServiceType =
        Fixture.serviceTypeBuilder().name(updatedServiceTypeName).build();
    ServiceTypeDto serviceTypeDto =
        Fixture.serviceTypeDtoBuilder().name(updatedServiceTypeName).build();

    when(serviceTypeRepository.findById(existingServiceType.getId()))
        .thenReturn(Optional.of(existingServiceType));
    when(serviceTypeRepository.save(any())).thenReturn(updatedServiceType);
    when(serviceTypeMapper.convertToDto(any())).thenReturn(serviceTypeDto);

    // Act
    ServiceTypeDto result =
        serviceTypeService.updateServiceType(
            ServiceTypeDto.builder()
                .id(existingServiceType.getId())
                .name(updatedServiceTypeName)
                .build());

    // Assert
    assertEquals(serviceTypeDto, result);

    verify(serviceTypeRepository, times(1)).findById(existingServiceType.getId());
    verify(serviceTypeRepository, times(1)).save(any());
    verify(serviceTypeMapper, times(1)).convertToDto(updatedServiceType);
  }

  @Test
  void updateServiceType_whenServiceTypeNotFound_throwException() {

    // Arrange
    int nonExistentServiceTypeId = 999;
    when(serviceTypeRepository.findById(nonExistentServiceTypeId)).thenReturn(Optional.empty());

    // Act & Assert
    RuntimeException exception =
        assertThrows(
            EntityNotFoundException.class,
            () ->
                serviceTypeService.updateServiceType(
                    Fixture.serviceTypeDtoBuilder().id(nonExistentServiceTypeId).build()));
    assertEquals(
        "ServiceType not found with id: " + nonExistentServiceTypeId, exception.getMessage());

    // Verify that the serviceTypeRepository.findById() method was called once with the specified ID
    verify(serviceTypeRepository, times(1)).findById(nonExistentServiceTypeId);
  }
}
