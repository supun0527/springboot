package com.brixo.productcatalogue.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    private final com.brixo.PingService pingService;

    public PingController(com.brixo.PingService pingService) {
        this.pingService = pingService;
    }

    @GetMapping("/ping")
    public String getPing() {
        return pingService.getPingResponse();
    }
}
