package com.brixo.productcatalogue.repository;

import com.brixo.productcatalogue.entity.ServiceType;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeRepository  extends JpaRepository<ServiceType, Integer> {}
