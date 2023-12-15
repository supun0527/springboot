package com.brixo.productcatalogue.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SettingsValueDto implements Serializable {
    private SettingsSubValueDto current;
    private SettingsSubValueDto future;
}
