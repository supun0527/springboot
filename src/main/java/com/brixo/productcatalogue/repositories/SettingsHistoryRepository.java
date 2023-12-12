package com.brixo.productcatalogue.repositories;


import com.brixo.productcatalogue.models.SettingsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsHistoryRepository extends JpaRepository<SettingsHistory, Long> {



}
