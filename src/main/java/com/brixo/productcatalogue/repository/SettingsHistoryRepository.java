package com.brixo.productcatalogue.repository;


import com.brixo.productcatalogue.entity.SettingsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsHistoryRepository extends JpaRepository<SettingsHistory, Long> {



}
