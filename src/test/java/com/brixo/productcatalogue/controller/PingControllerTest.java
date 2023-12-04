package com.brixo.productcatalogue.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(com.brixo.PingController.class)
@Import(com.brixo.PingService.class)
class PingControllerTest {

    @MockBean
    private com.brixo.PingRepository pingRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return get ping response")
    void testGetPing() throws Exception {
        when(pingRepository.fetchPingResponse()).thenReturn("pong");

        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }
}
