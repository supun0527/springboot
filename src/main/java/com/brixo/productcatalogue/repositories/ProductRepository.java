package com.brixo.productcatalogue.repositories;


import com.brixo.productcatalogue.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByIsDisabledFalse();

    Optional<Product> findByProductKey(String productKey);


