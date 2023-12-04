package com.brixo.productcatalogue.repository;

import org.springframework.stereotype.Repository;

@Repository
public class PingRepository {

    public String fetchPingResponse() {
        return "pong";
    }
}
