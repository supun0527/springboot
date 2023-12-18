package com.brixo.productcatalogue.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceTypeDto {
    private Integer id;
    @NotBlank
    private String name;
    private String key;
    private Integer serviceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ServiceDto service;
}
