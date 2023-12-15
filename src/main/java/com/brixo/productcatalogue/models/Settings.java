package com.brixo.productcatalogue.models;

import com.brixo.productcatalogue.dtos.SettingsValueDto;
import com.brixo.productcatalogue.dtos.SettingsValueDtoDeSerializer;
import com.brixo.productcatalogue.dtos.SettingsValueDtoSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;


@Entity
@Table(name = "settings")
@NoArgsConstructor
@Getter
@Setter
public class Settings extends BaseEntity implements Serializable{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "BIGINT")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;


  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "value",nullable = false)
  private SettingsValueDto value;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String key;

  @Column(name = "updated_by", nullable = false)
  private String updatedBy;
}
