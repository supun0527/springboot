package com.brixo.productcatalogue.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingsRequestDto {
    private Long id;
    @NotNull private Integer productId;
    private String name;
    @NotBlank private String key;
    @NotNull private Object value;
    private LocalDateTime activateAt;
    @NotBlank private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @AssertTrue(message = "'id' or 'key' is required")
    private boolean hasIdOrKey() {
        return id != null || key != null;
    }
}


