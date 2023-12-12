package com.brixo.productcatalogue.controllers;

import com.brixo.json.JsonUtil;
import com.brixo.productcatalogue.Fixture;
import com.brixo.productcatalogue.IntegrationTestSuperclass;
import com.brixo.productcatalogue.dtos.ServiceTypeDto;
import com.brixo.productcatalogue.models.Service;
import com.brixo.productcatalogue.models.ServiceType;
import com.brixo.productcatalogue.repositories.ServiceRepository;
import com.brixo.productcatalogue.repositories.ServiceTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class ServiceTypeControllerTest extends IntegrationTestSuperclass {

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void afterEach() {
        serviceTypeRepository.deleteAll();
        serviceRepository.deleteAll();
    }

    @Test
    public void getAllServiceTypes_success() throws Exception {
        // Arrange
        Service service = serviceRepository.save(Fixture.serviceBuilder().build());
        ServiceType serviceType1 = serviceTypeRepository.save(ServiceType.builder().name("Test ServiceType 1").service(service).build());
        ServiceType serviceType2 = serviceTypeRepository.save(ServiceType.builder().name("Test ServiceType 1").service(service).build());

        // Act
        mockMvc.perform(get("/v1/service-types/all"))
                // Assert
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id").value(serviceType1.getId())).andExpect(jsonPath("$[0].name").value(serviceType1.getName())).andExpect(jsonPath("$[1].id").value(serviceType2.getId())).andExpect(jsonPath("$[1].name").value(serviceType2.getName())).andExpect(jsonPath("$[*].createdAt").exists()).andExpect(jsonPath("$[*].updatedAt").exists());
    }

    @Test
    @Transactional
    public void getServiceTypeById_success() throws Exception {
        // Arrange
        Service service = serviceRepository.save(Fixture.serviceBuilder().build());
        ServiceType serviceType = Fixture.serviceTypeBuilder().build();
        serviceType.setService(service);
        serviceType = serviceTypeRepository.save(serviceType);


        // Act
        mockMvc.perform(get("/v1/service-types?id=" + serviceType.getId()))
                // Assert
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(serviceType.getId())).andExpect(jsonPath("$.name").value(serviceType.getName())).andExpect(jsonPath("$.createdAt").exists()).andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    void getServiceTypeById_whenServiceTypeNotAvailableWithGivenId_throwNotFoundException() throws Exception {
        // Act
        mockMvc.perform(get("/v1/service-types?id=99999"))
                // Assert
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("ServiceType not found with id: 99999"));
    }

    @Test
    @Transactional
    public void getServiceTypeByKey_success() throws Exception {
        // Arrange
        Service service = serviceRepository.save(Fixture.serviceBuilder().build());
        ServiceType serviceType = Fixture.serviceTypeBuilder().build();
        serviceType.setService(service);
        serviceType = serviceTypeRepository.save(serviceType);


        // Act
        mockMvc.perform(get("/v1/service-types?key=" + serviceType.getKey()))
                // Assert
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(serviceType.getId())).andExpect(jsonPath("$.name").value(serviceType.getName())).andExpect(jsonPath("$.createdAt").exists()).andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    void getServiceTypeById_whenServiceTypeNotAvailableWithGivenKey_throwNotFoundException() throws Exception {
        // Act
        mockMvc.perform(get("/v1/service-types?key=test99999"))
                // Assert
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("ServiceType not found with key: test99999"));
    }

    @Test
    void getService_whenIdAndKeyNotProvided_throwBadRequestException() throws Exception {
        // Act
        mockMvc.perform(get("/v1/service-types"))
                // Assert
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Either id or key should provided."));
    }

    @Test
    @Transactional
    void createServiceType_success() throws Exception {
        // Arrange
        Service service = serviceRepository.save(Fixture.serviceBuilder().build());
        ServiceTypeDto serviceTypeDto = Fixture.serviceTypeDtoBuilder().build();
        serviceTypeDto.setServiceId(service.getId());


        // Act
        mockMvc.perform(post("/v1/service-types").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(serviceTypeDto)))
                // Assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(serviceTypeDto.getName()))
                .andExpect(jsonPath("$.createdAt").exists()).andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    void createServiceType_whenKeyNotProvided_throwBadRequestException() throws Exception {
        // Act
        mockMvc.perform(post("/v1/service-types").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(ServiceTypeDto.builder().name("test").build())))
                // Assert
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("key can not be empty."));
    }


    @Test
    void createServiceType_whenServiceTypeNameNotProvided_throwBadRequestException() throws Exception {
        // Act
        mockMvc.perform(post("/v1/service-types").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(ServiceTypeDto.builder().name("").build())))
                // Assert
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("One or more attributes were invalid"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
                .andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("must not be blank"));
    }

    @Test
    @Transactional
    void updateServiceType_success() throws Exception {
        // Arrange
        Service service = serviceRepository.save(Fixture.serviceBuilder().build());
        ServiceType serviceType = Fixture.serviceTypeBuilder().build();
        serviceType.setService(service);
        serviceType = serviceTypeRepository.save(serviceType);
        ServiceTypeDto updateServiceTypeDto = ServiceTypeDto.builder().id(serviceType.getId()).serviceId(service.getId()).name("Updated ServiceType Name").build();

        // Act
        mockMvc.perform(post("/v1/service-types").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(updateServiceTypeDto)))
                // Assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(updateServiceTypeDto.getId()))
                .andExpect(jsonPath("$.name").value(updateServiceTypeDto.getName()));
    }

    @Test
    void updateServiceType_whenServiceTypeNotAvailableWithGivenId_throwNotFoundException() throws Exception {
        // Arrange
        Service service = serviceRepository.save(Fixture.serviceBuilder().build());
        ServiceTypeDto updateServiceTypeDto = ServiceTypeDto.builder()
                .id(99999)
                .serviceId(service.getId())
                .name("Updated ServiceType Name")
                .build();

        // Act
        mockMvc.perform(post("/v1/service-types").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(updateServiceTypeDto)))
                // Assert
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ServiceType not found with id: 99999"));
    }

}