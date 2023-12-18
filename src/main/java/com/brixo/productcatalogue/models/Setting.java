package com.brixo.productcatalogue.models;

import com.brixo.exceptionmanagement.exceptions.BrixoRuntimeException;
import com.brixo.json.JsonUtil;
import com.brixo.productcatalogue.dtos.SettingSubValueDto;
import com.brixo.productcatalogue.dtos.SettingValueDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.postgresql.util.PGobject;

import java.io.Serializable;
import java.sql.SQLException;


@Entity
@Table(name = "settings")
@NoArgsConstructor
@Getter
@Setter
public class Setting extends BaseEntity implements Serializable{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "BIGINT")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;


  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "value",nullable = false)
  private JsonNode value;

  public void setValueBySettingValueDto(SettingValueDto value){
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    this.value = mapper.valueToTree(value);
  }

  public SettingValueDto getConvertedSettingValue() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    try {
      return mapper.treeToValue(this.value, SettingValueDto.class);
    } catch (JsonProcessingException e) {
      throw new BrixoRuntimeException(e);
    }
  }

  public void setCurrent(SettingSubValueDto current){
    SettingValueDto settingValueDto = this.getConvertedSettingValue();
    if(settingValueDto == null){
      settingValueDto = SettingValueDto.builder().build();
    }
    settingValueDto.setCurrent(current);
    setValueBySettingValueDto(settingValueDto);
  }

  public void setFuture(SettingSubValueDto future){
    SettingValueDto settingValueDto = this.getConvertedSettingValue();
    if(settingValueDto == null){
      settingValueDto = SettingValueDto.builder().build();
    }
    settingValueDto.setFuture(future);
    setValueBySettingValueDto(settingValueDto);
  }


  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String key;

  @Column(name = "updated_by", nullable = false)
  private String updatedBy;
}
