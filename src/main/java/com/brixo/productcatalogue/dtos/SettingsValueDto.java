package com.brixo.productcatalogue.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SettingsValueDto {
    private SettingsSubValueDto current;
    private SettingsSubValueDto future;
}
