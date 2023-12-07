package com.brixo.productcatalogue;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.brixo")
public class ProductCatalogueApplication {
    private static final Logger logger = getLogger(ProductCatalogueApplication.class);

    public static void main(String[] args) {
        logger.info("Application is starting");
        SpringApplication.run(ProductCatalogueApplication.class, args);
    }
}
