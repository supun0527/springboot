package com.brixo.productcatalogue.repositories;

import com.brixo.productcatalogue.models.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {

    Optional<Setting> findByKeyAndProductId(String key, int productId);

}
