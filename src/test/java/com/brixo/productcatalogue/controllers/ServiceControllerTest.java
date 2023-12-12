package com.brixo.productcatalogue.controllers;

import com.brixo.json.JsonUtil;
import com.brixo.productcatalogue.Fixture;
import com.brixo.productcatalogue.IntegrationTestSuperclass;
import com.brixo.productcatalogue.dtos.ServiceDto;
import com.brixo.productcatalogue.models.Service;
import com.brixo.productcatalogue.repositories.ServiceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
class ServiceControllerTest extends IntegrationTestSuperclass {


    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void afterEach() {
        serviceRepository.deleteAll();
    }


    @Test
    public void getAllServices_success() throws Exception {
        // Arrange
        Service service1 = serviceRepository.save(Service.builder().name("Test Service 1").build());
        Service service2 = serviceRepository.save(Service.builder().name("Test Service 1").build());

        // Act
        mockMvc.perform(get("/v1/services/all"))
                // Assert
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id").value(service1.getId())).andExpect(jsonPath("$[0].name").value(service1.getName())).andExpect(jsonPath("$[1].id").value(service2.getId())).andExpect(jsonPath("$[1].name").value(service2.getName())).andExpect(jsonPath("$[*].createdAt").exists()).andExpect(jsonPath("$[*].updatedAt").exists());
    }

    @Test
    public void getServiceById_success() throws Exception {
        // Arrange
        Service service = serviceRepository.save(Fixture.serviceBuilder().build());

        // Act
        mockMvc.perform(get("/v1/services?id=" + service.getId()))
                // Assert
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(service.getId())).andExpect(jsonPath("$.name").value(service.getName())).andExpect(jsonPath("$.createdAt").exists()).andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    public void getServiceByKey_success() throws Exception {
        // Arrange
        Service service = serviceRepository.save(Fixture.serviceBuilder().build());

        // Act
        mockMvc.perform(get("/v1/services?key=" + service.getKey()))
                // Assert
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(service.getId())).andExpect(jsonPath("$.name").value(service.getName())).andExpect(jsonPath("$.createdAt").exists()).andExpect(jsonPath("$.updatedAt").exists());
    }


    @Test
    void getService_whenIdAndKeyNotProvided_throwBadRequestException() throws Exception {
        // Act
        mockMvc.perform(get("/v1/services"))
                // Assert
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Either id or key should provided."));
    }


    @Test
    void getServiceById_whenServiceNotAvailableWithGivenId_throwNotFoundException() throws Exception {
        // Act
        mockMvc.perform(get("/v1/services?id=99999"))
                // Assert
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Service not found with id: 99999"));
    }

    @Test
    void getServiceById_whenServiceNotAvailableWithGivenKey_throwNotFoundException() throws Exception {
        // Act
        mockMvc.perform(get("/v1/services?key=test99999"))
                // Assert
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Service not found with key: test99999"));
    }

    @Test
    void createService_success() throws Exception {
        // Arrange
        ServiceDto serviceDto = Fixture.serviceDtoBuilder().build();

        // Act
        mockMvc.perform(post("/v1/services").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(serviceDto)))
                // Assert
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.name").value(serviceDto.getName())).andExpect(jsonPath("$.createdAt").exists()).andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    void createService_whenKeyIsNotProvided_throwBadRequestException() throws Exception {
        // Arrange
        ServiceDto serviceDto = Fixture.serviceDtoBuilder().key(null).build();

        // Act
        mockMvc.perform(post("/v1/services").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(serviceDto)))
                // Assert
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("key can not be empty."));
    }

    @Test
    void getServiceById_whenServiceNameNotProvided_throwBadRequestException() throws Exception {
        // Act
        mockMvc.perform(post("/v1/services").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(ServiceDto.builder().name("").build())))
                // Assert
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("One or more attributes were invalid")).andExpect(jsonPath("$.fieldErrors[0].field").value("name")).andExpect(jsonPath("$.fieldErrors[0].errorMessage").value("must not be blank"));
    }

    @Test
    void updateService_success() throws Exception {
        // Arrange
        Service service = serviceRepository.save(Fixture.serviceBuilder().build());
        ServiceDto updateServiceDto = ServiceDto.builder().id(service.getId()).name("Updated Service Name").build();

        // Act
        mockMvc.perform(post("/v1/services").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(updateServiceDto)))
                // Assert
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(updateServiceDto.getId())).andExpect(jsonPath("$.name").value(updateServiceDto.getName()));
    }


    @Test
    void updateService_whenServiceNotAvailableWithGivenId_throwNotFoundException() throws Exception {
        // Arrange
        ServiceDto updateServiceDto = ServiceDto.builder().id(99999).name("Updated Service Name").build();

        // Act
        mockMvc.perform(post("/v1/services").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(updateServiceDto)))
                // Assert
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Service not found with id: 99999"));
    }

}