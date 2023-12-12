package com.brixo.productcatalogue;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = IntegrationTestSuperclass.Initializer.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = AFTER_CLASS)
public abstract class IntegrationTestSuperclass {
  @Container
  public static final PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:16.0")
          .withDatabaseName("testdatabase")
          .withUsername("test")
          .withPassword("password");

  static {
    postgres.start();
  }

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      TestPropertyValues.of(
              "spring.datasource.url=" + postgres.getJdbcUrl(),
              "spring.datasource.username=" + postgres.getUsername(),
              "spring.datasource.password=" + postgres.getPassword())
          .applyTo(applicationContext);
    }
  }
}
