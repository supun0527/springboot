package com.brixo.productcatalogue;

import com.brixo.productcatalogue.dtos.ServiceDto;
import com.brixo.productcatalogue.dtos.ServiceTypeDto;
import com.brixo.productcatalogue.models.Service;
import com.brixo.productcatalogue.models.ServiceType;

public class Fixture {

    public static final String TEST_SERVICE_NAME = "Test Service";
    public static final String TEST_SERVICE_KEY = "test_service";
    public static final String TEST_SERVICE_TYPE_NAME = "Test Service Type";
    public static final String TEST_SERVICE_TYPE_KEY = "test_service_type";

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
}
