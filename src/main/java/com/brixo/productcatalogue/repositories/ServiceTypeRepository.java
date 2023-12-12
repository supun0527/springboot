package com.brixo.productcatalogue.repositories;

import com.brixo.productcatalogue.models.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceTypeRepository  extends JpaRepository<ServiceType, Integer> {
    Optional<ServiceType> findByKey(String key);
}
