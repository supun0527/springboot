package com.brixo.productcatalogue;

import com.brixo.productcatalogue.dtos.ProductDto;
import com.brixo.productcatalogue.dtos.ServiceDto;
import com.brixo.productcatalogue.dtos.ServiceTypeDto;
import com.brixo.productcatalogue.dtos.SettingDto;
import com.brixo.productcatalogue.models.Service;
import com.brixo.productcatalogue.models.ServiceType;

import java.util.ArrayList;
import java.util.List;

public class Fixture {

  public static final String TEST_SERVICE_NAME = "Test Service";
  public static final String TEST_SERVICE_KEY = "test_service";
  public static final String TEST_SERVICE_TYPE_NAME = "Test Service Type";
  public static final String TEST_SERVICE_TYPE_KEY = "test_service_type";

  private static final String PRODUCT_KEY = "brixo annuity";
  private static final String NAME = "Brixo Annuity";
  private static final String UPDATED_BY = "brixo user";

  public static ProductDto createProduct(boolean isDisabled) {
    return ProductDto.builder()
        .productKey(PRODUCT_KEY)
        .name(NAME)
        .isDisabled(isDisabled)
        .updatedBy(UPDATED_BY)
        .build();
  }

  public static List<ProductDto> createProducts(int numberOfProducts) {
    List<ProductDto> products = new ArrayList<>();

    for (int i = 0; i < numberOfProducts; i++) {
      boolean isDisabled = i % 2 == 0; // Alternate between true and false for isDisabled
      ProductDto product = createProduct(isDisabled);
      products.add(product);
    }

    return products;
  }

  public static Service.ServiceBuilder serviceBuilder() {
    return Service.builder().id(1).name(TEST_SERVICE_NAME).key(TEST_SERVICE_KEY);
  }

  public static ServiceDto.ServiceDtoBuilder serviceDtoBuilder() {
    return ServiceDto.builder().name(TEST_SERVICE_NAME).key(TEST_SERVICE_KEY);
  }

  public static ServiceType.ServiceTypeBuilder serviceTypeBuilder() {
    return ServiceType.builder().id(1).name(TEST_SERVICE_TYPE_NAME).key(TEST_SERVICE_TYPE_KEY);
  }

  public static ServiceTypeDto.ServiceTypeDtoBuilder serviceTypeDtoBuilder() {
    return ServiceTypeDto.builder().name(TEST_SERVICE_TYPE_NAME).key(TEST_SERVICE_TYPE_KEY);
  }
  public static SettingDto settingDto = SettingDto.builder()
          .name("Test Settings")
          .key("test_key")
          .updatedBy("test_user")
          .productId(1)
          .build();
}
