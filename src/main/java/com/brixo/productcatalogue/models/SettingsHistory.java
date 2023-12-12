package com.brixo.productcatalogue.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "settings_history")
@NoArgsConstructor
@Getter
@Setter
public class SettingsHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "BIGINT")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "setting_id", nullable = false)
  private Settings setting;

  @JdbcTypeCode(SqlTypes.JSON)
  private String value;

  @Column(name = "activated_at", nullable = false)
  private LocalDateTime activatedAt;
}
