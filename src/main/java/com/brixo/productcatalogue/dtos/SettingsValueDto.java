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
public class SettingsValueDto implements Serializable {
    private SettingsSubValueDto current;
    private SettingsSubValueDto future;
}
