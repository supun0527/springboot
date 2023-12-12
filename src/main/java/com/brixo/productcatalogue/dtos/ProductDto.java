package com.brixo.productcatalogue.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProductDto {

  private Integer id;

  @NotBlank(message = "Name cannot be blank")
  private String name;

  @NotBlank private String productKey;

  private Integer serviceTypeId;

  private boolean isDisabled;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String updatedBy;
}
