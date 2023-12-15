package com.brixo.productcatalogue.dtos;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SettingsSubValueDto implements Serializable {
    private String value;
    private LocalDateTime activatedAt;
}
