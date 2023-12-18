package com.brixo.productcatalogue.models;

import com.brixo.productcatalogue.dtos.SettingValueDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "settings_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SettingHistory extends BaseEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "BIGINT")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "setting_id", nullable = false)
  private Setting setting;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "value",nullable = false)
  private SettingValueDto value;

  @Column(name = "activation_status", nullable = false)
  private Boolean activationStatus;

  @Column(name = "activated_at", nullable = false)
  private LocalDateTime activatedAt;
}
