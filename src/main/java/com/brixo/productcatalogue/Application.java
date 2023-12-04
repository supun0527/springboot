package com.brixo.productcatalogue;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class Application {
    private static final Logger logger = getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Application is starting");
        SpringApplication.run(Application.class, args);
    }
}
