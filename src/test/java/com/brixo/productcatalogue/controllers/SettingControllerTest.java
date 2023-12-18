package com.brixo.productcatalogue.controllers;

import com.brixo.json.JsonUtil;
import com.brixo.productcatalogue.Fixture;
import com.brixo.productcatalogue.IntegrationTestSuperclass;
import com.brixo.productcatalogue.dtos.SettingSubValueDto;
import com.brixo.productcatalogue.dtos.SettingDto;
import com.brixo.productcatalogue.dtos.SettingValueDto;
import com.brixo.productcatalogue.models.Product;
import com.brixo.productcatalogue.models.Service;
import com.brixo.productcatalogue.models.ServiceType;
import com.brixo.productcatalogue.models.Setting;
import com.brixo.productcatalogue.repositories.ProductRepository;
import com.brixo.productcatalogue.repositories.ServiceRepository;
import com.brixo.productcatalogue.repositories.ServiceTypeRepository;
import com.brixo.productcatalogue.repositories.SettingRepository;
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
class SettingControllerTest extends IntegrationTestSuperclass {


    @Autowired
    private SettingRepository settingRepository;
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
        settingRepository.deleteAll();
        productRepository.deleteAll();
        serviceTypeRepository.deleteAll();
        serviceRepository.deleteAll();
    }

    @Test
    public void getAllSettings_success() throws Exception {
        Setting setting1 = saveSettingsWithServiceAndServiceTypeAndProduct("name_1", "key_1", "user_1", "service_1", "service_type_1", "product_key_1", "product_name_1");
        Setting setting2 = saveSettingsWithServiceAndServiceTypeAndProduct("name_2", "key_2", "user_2", "service_2", "service_type_2", "product_key_2", "product_name_2");

        mockMvc.perform(get("/v1/settings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(setting1.getId()))
                .andExpect(jsonPath("$[0].name").value(setting1.getName()))
                .andExpect(jsonPath("$[0].key").value(setting1.getKey()))
                .andExpect(jsonPath("$[0].updatedBy").value(setting1.getUpdatedBy()))
                .andExpect(jsonPath("$[1].id").value(setting2.getId()))
                .andExpect(jsonPath("$[1].name").value(setting2.getName()))
                .andExpect(jsonPath("$[1].key").value(setting2.getKey()))
                .andExpect(jsonPath("$[1].updatedBy").value(setting2.getUpdatedBy()))
                .andExpect(jsonPath("$[*].createdAt").exists())
                .andExpect(jsonPath("$[*].updatedAt").exists());
    }

    @Test
    public void getSettingsById_success() throws Exception {
        Setting setting = saveSettingsWithServiceAndServiceTypeAndProduct("name_3", "key_3", "user_3", "service_3", "service_type_3", "product_key_3", "product_name_3");


        mockMvc.perform(get("/v1/settings/" + setting.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(setting.getId()))
                .andExpect(jsonPath("$.name").value(setting.getName()))
                .andExpect(jsonPath("$.key").value(setting.getKey()))
                .andExpect(jsonPath("$.updatedBy").value(setting.getUpdatedBy()))
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
        SettingDto settingDto = Fixture.settingDto;
        settingDto.setProductId(product.getId());

        mockMvc.perform(post("/v1/settings").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(settingDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(settingDto.getName()))
                .andExpect(jsonPath("$.key").value(settingDto.getKey()))
                .andExpect(jsonPath("$.updatedBy").value(settingDto.getUpdatedBy()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    void createSettings_whenProductIsMissing_throwNotFoundException() throws Exception {
        SettingDto settingDto = Fixture.settingDto;

        mockMvc.perform(post("/v1/settings").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(settingDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Unable to create or update setting, product not found with id: " + settingDto.getProductId()));
    }

    @Test
    void createSettings_whenProductIdIsMissing_badRequest() throws Exception {
        SettingDto settingDto = SettingDto.builder().updatedBy("").build();

        mockMvc.perform(post("/v1/settings").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(settingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("One or more attributes were invalid"));
    }

    @Test
    void createSettings_whenUpdatedByMissing_badRequest() throws Exception {
        SettingDto settingDto = SettingDto.builder().productId(1).build();

        mockMvc.perform(post("/v1/settings").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(settingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("One or more attributes were invalid"));
    }

    @Test // this test is failing due to "value" being null, not sure why that is
    void updateSettings_success() throws Exception {
        Setting existingSetting = saveSettingsWithServiceAndServiceTypeAndProduct("name_5", "key_5", "user_5", "service_5", "service_type_5", "product_key_5", "product_name_5");

        HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");

        SettingValueDto newValue = SettingValueDto.builder()
                .current(SettingSubValueDto.builder().value("current_value_1").activatedAt(PAST_DATE).build())
                .future(SettingSubValueDto.builder().value("future_value_1").activatedAt(FUTURE_DATE).build())
                .build();

        SettingDto settingsToUpdate = SettingDto.builder()
                .id(existingSetting.getId())
                .productId(existingSetting.getProduct().getId())
                .name("Updated Settings Name")
                .updatedBy("new user")
                .value(newValue)
                .build();

        mockMvc.perform(post("/v1/settings").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(settingsToUpdate)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value(settingsToUpdate.getName()))
                .andExpect(jsonPath("$.updatedBy").value(settingsToUpdate.getUpdatedBy()))
                .andExpect(jsonPath("$.value").value(settingsToUpdate.getValue()))
                .andExpect(jsonPath("$.productId").value(existingSetting.getProduct().getId()));
    }

    @Test
    void updateSettings_whenSettingsNotAvailableWithGivenId_throwNotFoundException() throws Exception {
        Setting setting = saveSettingsWithServiceAndServiceTypeAndProduct("name_6", "key_6", "user_6", "service_6", "service_type_6", "product_key_6", "product_name_6");

        SettingDto settingsToBeUpdated = SettingDto.builder()
                .id(666666L)
                .name("Updated Settings Name")
                .productId(setting.getProduct().getId())
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
        SettingDto settingsToBeUpdated = SettingDto.builder()
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

    private Setting getSettingWithNameAndKeyAndUpdatedBy(final String name, final String key, final String updatedBy) {
        Setting setting = new Setting();
        setting.setName(name);
        setting.setKey(key);
        setting.setUpdatedBy(updatedBy);
        return setting;
    }
    private Setting saveSettingsWithServiceAndServiceTypeAndProduct(final String name, final String key, final String updatedBy, final String serviceName, final String serviceTypeName, final String productKey, final String productName) {
        Product product = saveServiceAndServiceTypeAndProduct(serviceName, serviceTypeName, productKey, productName);
        Setting setting = getSettingWithNameAndKeyAndUpdatedBy(name, key, updatedBy);
        setting.setProduct(product);
        return settingRepository.save(setting);
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