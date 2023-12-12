package com.brixo.productcatalogue.repositories;


import com.brixo.productcatalogue.models.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {


}
