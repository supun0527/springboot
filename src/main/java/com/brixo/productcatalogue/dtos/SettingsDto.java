package com.brixo.productcatalogue.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingsDto {
    private Long id;
    @NotNull private Integer productId;
    private SettingsValueDto value;
    private String name;
    private String key;
    @NotBlank private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @AssertTrue(message = "'id' or 'key' is required")
    private boolean hasIdOrKey() {
        return id != null || key != null;
    }
}


