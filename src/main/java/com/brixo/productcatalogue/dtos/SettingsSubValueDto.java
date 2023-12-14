package com.brixo.productcatalogue.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SettingsSubValueDto {
    private Object value;
    private LocalDateTime activatedAt;
}
