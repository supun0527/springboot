package com.brixo.productcatalogue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AppTemplateApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("Should return get ping response")
    void testGetPing() {
        var response = testRestTemplate.getForEntity("/ping", String.class, Map.of());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("pong", response.getBody());
    }
}
