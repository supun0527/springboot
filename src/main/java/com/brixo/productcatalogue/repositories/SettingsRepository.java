package com.brixo.productcatalogue.repositories;

import com.brixo.productcatalogue.models.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<Setting, Long> {


}
