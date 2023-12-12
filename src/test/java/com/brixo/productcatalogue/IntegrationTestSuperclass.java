package com.brixo.productcatalogue;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;
import org.jetbrains.annotations.NotNull;

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

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = IntegrationTestSuperclass.Initializer.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = AFTER_CLASS)
public class IntegrationTestSuperclass {

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
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
      TestPropertyValues.of(
              "spring.datasource.url=" + postgres.getJdbcUrl(),
              "spring.datasource.username=" + postgres.getUsername(),
              "spring.datasource.password=" + postgres.getPassword())
          .applyTo(applicationContext);
    }
  }
}
