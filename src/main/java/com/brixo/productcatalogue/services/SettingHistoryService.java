package com.brixo.productcatalogue.services;

import com.brixo.json.JsonUtil;
import com.brixo.productcatalogue.models.Setting;
import com.brixo.productcatalogue.models.SettingHistory;
import com.brixo.productcatalogue.repositories.SettingHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SettingHistoryService {

    private final SettingHistoryRepository settingHistoryRepository;

    public SettingHistoryService(SettingHistoryRepository settingHistoryRepository) {
        this.settingHistoryRepository = settingHistoryRepository;
    }

    public void createSettingHistory(Setting setting){
        updateHistory(setting.getId());
        createHistory(setting);
    }

    private void updateHistory(Long settingId) {
        settingHistoryRepository.findFirstBySettingIdOrderByIdDesc(settingId).ifPresent(settingHistory -> {
            if(settingHistory.getActivatedAt().isBefore(LocalDateTime.now())){
                return;
            }
            settingHistory.setActivationStatus(false);
            settingHistoryRepository.save(settingHistory);
        });
    }

    private void createHistory(Setting setting) {
        SettingHistory settingHistory = SettingHistory.builder()
                .setting(setting)
                .value(setting.getValue())
                .activationStatus(true)
                .activatedAt(setting.getValue().getFuture().getActivatedAt())
                .build();
        settingHistoryRepository.save(settingHistory);
    }


}
