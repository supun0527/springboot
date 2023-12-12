package com.brixo.productcatalogue.dtos;

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
public class ServiceDto {
  private Integer id;
  @NotBlank private String name;
  private String key;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
