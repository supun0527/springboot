package com.brixo.productcatalogue.services;

import com.brixo.exceptionmanagement.exceptions.EntityNotFoundException;
import com.brixo.productcatalogue.Fixture;
import com.brixo.productcatalogue.dtos.ServiceDto;
import com.brixo.productcatalogue.mappers.ServiceMapper;
import com.brixo.productcatalogue.models.Service;
import com.brixo.productcatalogue.repositories.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ServiceServiceTest {

  @Mock private ServiceRepository serviceRepository;
  @Mock private ServiceMapper serviceMapper;

  @InjectMocks private ServiceService serviceService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test void getAllServices_success() {
    // Arrange
    int serviceId1 = 1;
    String serviceName1 = "Service1";
    int serviceId2 = 2;
    String serviceName2 = "Service2";
    Service service1 = Service.builder().id(serviceId1).name(serviceName1).build();
    Service service2 = Service.builder().id(serviceId2).name(serviceName2).build();

    ServiceDto serviceDto1 = ServiceDto.builder().id(serviceId1).name(serviceName1).build();
    ServiceDto serviceDto2 = ServiceDto.builder().id(serviceId2).name(serviceName2).build();

    when(serviceRepository.findAll()).thenReturn(List.of(service1, service2));
    when(serviceMapper.convertListToDtoList(any())).thenReturn(List.of(serviceDto1, serviceDto2));

    // Act
    List<ServiceDto> result = serviceService.getAllServices();

    // Assert
    assertEquals(2, result.size());
    assertEquals(serviceDto1, result.get(0));
    assertEquals(serviceDto2, result.get(1));

    verify(serviceRepository, times(1)).findAll();
    verify(serviceMapper, times(1)).convertListToDtoList(any());
  }

  @Test
  void getServiceById_success() {
    // Arrange
    Service service = Fixture.serviceBuilder().build();
    ServiceDto serviceDto = Fixture.serviceDtoBuilder().build();

    service.setCreatedAt(LocalDateTime.now());
    service.setUpdatedAt(LocalDateTime.now());

    when(serviceRepository.findById(service.getId())).thenReturn(Optional.of(service));
    when(serviceMapper.convertToDto(any())).thenReturn(serviceDto);

    // Act
    ServiceDto result = serviceService.getServiceById(service.getId());

    // Assert
    assertEquals(serviceDto, result);

    verify(serviceRepository, times(1)).findById(service.getId());
    verify(serviceMapper, times(1)).convertToDto(service);
  }

  @Test
  void getServiceById_whenServiceNotFound_throwException() {

    // Arrange
    int nonExistentServiceId = 999;
    when(serviceRepository.findById(nonExistentServiceId)).thenReturn(Optional.empty());

    // Act & Assert
    RuntimeException exception =
        assertThrows(
            EntityNotFoundException.class,
            () -> serviceService.getServiceById(nonExistentServiceId));
    assertEquals("Service not found with id: " + nonExistentServiceId, exception.getMessage());

    // Verify that the serviceRepository.findById() method was called once with the specified ID
    verify(serviceRepository, times(1)).findById(nonExistentServiceId);
  }

  @Test
  void createService_success() {
    // Arrange
    Service service = Fixture.serviceBuilder().build();
    ServiceDto serviceDto = Fixture.serviceDtoBuilder().build();

    when(serviceRepository.save(service)).thenReturn(service);
    when(serviceMapper.convertToEntity(any())).thenReturn(service);
    when(serviceMapper.convertToDto(service)).thenReturn(serviceDto);

    // Act
    ServiceDto result = serviceService.createService(serviceDto);

    // Assert
    assertEquals(serviceDto, result);

    verify(serviceRepository, times(1)).save(service);
    verify(serviceMapper, times(1)).convertToEntity(any());
    verify(serviceMapper, times(1)).convertToDto(service);
  }

  @Test
  void updateService_success() {
    // Arrange
    String updatedServiceName = "Updated Service";
    Service existingService = Fixture.serviceBuilder().build();
    Service updatedService = Fixture.serviceBuilder().name(updatedServiceName).build();
    ServiceDto updatedServiceDto = Fixture.serviceDtoBuilder().name(updatedServiceName).build();

    when(serviceRepository.findById(existingService.getId()))
        .thenReturn(Optional.of(existingService));
    when(serviceRepository.save(updatedService)).thenReturn(updatedService);
    when(serviceMapper.convertToDto(any())).thenReturn(updatedServiceDto);

    // Act
    ServiceDto result =
        serviceService.updateService(
            ServiceDto.builder().id(existingService.getId()).name(updatedServiceName).build());

    // Assert
    assertEquals(updatedServiceDto, result);

    verify(serviceRepository, times(1)).findById(existingService.getId());
    verify(serviceRepository, times(1)).save(any());
    verify(serviceMapper, times(1)).convertToDto(any());
  }

  @Test
  void updateService_whenServiceNotFound_throwException() {

    // Arrange
    int nonExistentServiceId = 999;

    when(serviceRepository.findById(nonExistentServiceId)).thenReturn(Optional.empty());

    // Act & Assert
    RuntimeException exception =
        assertThrows(
            EntityNotFoundException.class,
            () ->
                serviceService.updateService(
                    Fixture.serviceDtoBuilder().id(nonExistentServiceId).build()));
    assertEquals("Service not found with id: " + nonExistentServiceId, exception.getMessage());

    // Verify that the serviceRepository.findById() method was called once with the specified ID
    verify(serviceRepository, times(1)).findById(nonExistentServiceId);
  }
}
