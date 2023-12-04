package com.brixo.productcatalogue.service;

import org.springframework.stereotype.Service;

@Service
public class PingService {

    private final com.brixo.PingRepository pingRepository;

    public PingService(com.brixo.PingRepository pingRepository) {
        this.pingRepository = pingRepository;
    }

    public String getPingResponse() {
        return pingRepository.fetchPingResponse();
    }
}
