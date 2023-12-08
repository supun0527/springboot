package com.brixo.productcatalogue;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AppTemplateApplicationIntegrationTest {

//    @Autowired
//    private TestRestTemplate testRestTemplate;
//
//    @Test
//    @DisplayName("Should return get ping response")
//    void testGetPing() {
//        var response = testRestTemplate.getForEntity("/ping", String.class, Map.of());
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("pong", response.getBody());
//    }


//        @Test
////    @DisplayName("Should return get ping response")
//    void testGetPing() {
//    // var response = testRestTemplate.getForEntity("/ping", String.class, Map.of());
//    System.out.println("test");
//    }



}
