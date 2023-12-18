package com.brixo.productcatalogue.dtos;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SettingValueDto{
    private SettingSubValueDto current;
    private SettingSubValueDto future;
}
