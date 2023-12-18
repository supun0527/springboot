package com.brixo.productcatalogue.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SettingSubValueDto {
    private Object value;
    private LocalDateTime activatedAt;

    public void replaceWith(SettingSubValueDto dto) {
        setAll(dto.getValue(), dto.getActivatedAt());
    }

    public void setAll(Object value, LocalDateTime activatedAt) {
        this.value = value;
        this.activatedAt = activatedAt;
    }

}
