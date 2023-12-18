package com.brixo.productcatalogue.repositories;


import com.brixo.productcatalogue.models.SettingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingHistoryRepository extends JpaRepository<SettingHistory, Long> {

    Optional<SettingHistory> findFirstBySettingIdOrderByIdDesc(Long settingId);
}
