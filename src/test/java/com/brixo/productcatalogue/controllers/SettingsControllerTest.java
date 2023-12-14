package com.brixo.productcatalogue.controllers;

import com.brixo.json.JsonUtil;
import com.brixo.productcatalogue.Fixture;
import com.brixo.productcatalogue.IntegrationTestSuperclass;
import com.brixo.productcatalogue.dtos.SettingsDto;
import com.brixo.productcatalogue.dtos.SettingsSubValueDto;
import com.brixo.productcatalogue.dtos.SettingsValueDto;
import com.brixo.productcatalogue.models.Product;
import com.brixo.productcatalogue.models.Service;
import com.brixo.productcatalogue.models.ServiceType;
import com.brixo.productcatalogue.models.Settings;
import com.brixo.productcatalogue.repositories.ProductRepository;
import com.brixo.productcatalogue.repositories.ServiceRepository;
import com.brixo.productcatalogue.repositories.ServiceTypeRepository;
import com.brixo.productcatalogue.repositories.SettingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HexFormat;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SettingsControllerTest extends IntegrationTestSuperclass {


    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;
    @Autowired
    private MockMvc mockMvc;
    private final static LocalDateTime PAST_DATE = LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0);
    private final static LocalDateTime FUTURE_DATE = LocalDateTime.of(3000, Month.JANUARY, 1, 0, 0);


    @AfterEach
    void afterEach() {
        settingsRepository.deleteAll();
        productRepository.deleteAll();
        serviceTypeRepository.deleteAll();
        serviceRepository.deleteAll();
    }

    @Test
    public void getAllSettings_success() throws Exception {
        Settings settings1 = saveSettingsWithServiceAndServiceTypeAndProduct("name_1", "key_1", "user_1", "service_1", "service_type_1", "product_key_1", "product_name_1");
        Settings settings2 = saveSettingsWithServiceAndServiceTypeAndProduct("name_2", "key_2", "user_2", "service_2", "service_type_2", "product_key_2", "product_name_2");

        mockMvc.perform(get("/v1/settings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(settings1.getId()))
                .andExpect(jsonPath("$[0].name").value(settings1.getName()))
                .andExpect(jsonPath("$[0].key").value(settings1.getKey()))
                .andExpect(jsonPath("$[0].updatedBy").value(settings1.getUpdatedBy()))
                .andExpect(jsonPath("$[1].id").value(settings2.getId()))
                .andExpect(jsonPath("$[1].name").value(settings2.getName()))
                .andExpect(jsonPath("$[1].key").value(settings2.getKey()))
                .andExpect(jsonPath("$[1].updatedBy").value(settings2.getUpdatedBy()))
                .andExpect(jsonPath("$[*].createdAt").exists())
                .andExpect(jsonPath("$[*].updatedAt").exists());
    }

    @Test
    public void getSettingsById_success() throws Exception {
        Settings settings = saveSettingsWithServiceAndServiceTypeAndProduct("name_3", "key_3", "user_3", "service_3", "service_type_3", "product_key_3", "product_name_3");


        mockMvc.perform(get("/v1/settings/" + settings.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(settings.getId()))
                .andExpect(jsonPath("$.name").value(settings.getName()))
                .andExpect(jsonPath("$.key").value(settings.getKey()))
                .andExpect(jsonPath("$.updatedBy").value(settings.getUpdatedBy()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    void getSettingsById_whenSettingsNotAvailableWithGivenId_throwNotFoundException() throws Exception {
        mockMvc.perform(get("/v1/settings/666666"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Settings not found with id: 666666"));
    }

    @Test
    void createSettings_success() throws Exception {
        Product product = saveServiceAndServiceTypeAndProduct("service_name_4", "service_type_4", "key_4", "product_4");
        SettingsDto settingsDto = Fixture.settingsDto;
        settingsDto.setProductId(product.getId());

        mockMvc.perform(post("/v1/settings").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(settingsDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(settingsDto.getName()))
                .andExpect(jsonPath("$.key").value(settingsDto.getKey()))
                .andExpect(jsonPath("$.updatedBy").value(settingsDto.getUpdatedBy()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    void createSettings_whenProductIsMissing_throwNotFoundException() throws Exception {
        SettingsDto settingsDto = Fixture.settingsDto;

        mockMvc.perform(post("/v1/settings").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(settingsDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Unable to create or update setting, product not found with id: " + settingsDto.getProductId()));
    }

    @Test
    void createSettings_whenProductIdIsMissing_badRequest() throws Exception {
        SettingsDto settingsDto = SettingsDto.builder().updatedBy("").build();

        mockMvc.perform(post("/v1/settings").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(settingsDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("One or more attributes were invalid"));
    }

    @Test
    void createSettings_whenUpdatedByMissing_badRequest() throws Exception {
        SettingsDto settingsDto = SettingsDto.builder().productId(1).build();

        mockMvc.perform(post("/v1/settings").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(settingsDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("One or more attributes were invalid"));
    }

    @Test // this test is failing due to "value" being null, not sure why that is
    void updateSettings_success() throws Exception {
        Settings existingSettings = saveSettingsWithServiceAndServiceTypeAndProduct("name_5", "key_5", "user_5", "service_5", "service_type_5", "product_key_5", "product_name_5");

        HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");

        SettingsValueDto newValue = SettingsValueDto.builder()
                .current(SettingsSubValueDto.builder().value("current_value_1").activatedAt(PAST_DATE).build())
                .future(SettingsSubValueDto.builder().value("future_value_1").activatedAt(FUTURE_DATE).build())
                .build();

        SettingsDto settingsToUpdate = SettingsDto.builder()
                .id(existingSettings.getId())
                .productId(existingSettings.getProduct().getId())
                .name("Updated Settings Name")
                .updatedBy("new user")
                .value(newValue)
                .build();

        mockMvc.perform(post("/v1/settings").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(settingsToUpdate)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value(settingsToUpdate.getName()))
                .andExpect(jsonPath("$.updatedBy").value(settingsToUpdate.getUpdatedBy()))
                .andExpect(jsonPath("$.value").value(settingsToUpdate.getValue()))
                .andExpect(jsonPath("$.productId").value(existingSettings.getProduct().getId()));
    }

    @Test
    void updateSettings_whenSettingsNotAvailableWithGivenId_throwNotFoundException() throws Exception {
        Settings settings = saveSettingsWithServiceAndServiceTypeAndProduct("name_6", "key_6", "user_6", "service_6", "service_type_6", "product_key_6", "product_name_6");

        SettingsDto settingsToBeUpdated = SettingsDto.builder()
                .id(666666L)
                .name("Updated Settings Name")
                .productId(settings.getProduct().getId())
                .updatedBy("user_7")
                .build();

        mockMvc.perform(post("/v1/settings")
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(settingsToBeUpdated)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Settings not found with id: 666666"));
    }

    @Test
    void updateSettings_whenProductNotAvailableWithGivenId_throwNotFoundException() throws Exception {
        SettingsDto settingsToBeUpdated = SettingsDto.builder()
                .id(666666L)
                .name("Updated Settings Name")
                .productId(1)
                .updatedBy("a user")
                .build();

        mockMvc.perform(post("/v1/settings")
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(settingsToBeUpdated)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Unable to create or update setting, product not found with id: 1"));
    }

    private Settings getSettingWithNameAndKeyAndUpdatedBy(final String name, final String key, final String updatedBy) {
        Settings settings = new Settings();
        settings.setName(name);
        settings.setKey(key);
        settings.setUpdatedBy(updatedBy);
        return settings;
    }
    private Settings saveSettingsWithServiceAndServiceTypeAndProduct(final String name, final String key, final String updatedBy, final String serviceName, final String serviceTypeName, final String productKey, final String productName) {
        Product product = saveServiceAndServiceTypeAndProduct(serviceName, serviceTypeName, productKey, productName);
        Settings settings = getSettingWithNameAndKeyAndUpdatedBy(name, key, updatedBy);
        settings.setProduct(product);
        return settingsRepository.save(settings);
    }
    private Product saveServiceAndServiceTypeAndProduct(String serviceName, String serviceTypeName, String productKey, String productName) {
        Service service = Service.builder().name(serviceName).build();
        service = serviceRepository.save(service);

        ServiceType serviceType = ServiceType.builder().name(serviceTypeName).service(service).build();
        serviceType = serviceTypeRepository.save(serviceType);

        Product product = Product.builder().isDisabled(false).productKey(productKey).name(productName).serviceType(serviceType).build();
        product = productRepository.save(product);
        return product;
    }
}