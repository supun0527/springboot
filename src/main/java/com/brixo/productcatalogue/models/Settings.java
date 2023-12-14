package com.brixo.productcatalogue.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "settings")
@NoArgsConstructor
@Getter
@Setter
public class Settings extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(columnDefinition = "BIGINT")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @JdbcTypeCode(SqlTypes.JSON)
  private String value;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String key;

  @Column(name = "updated_by", nullable = false)
  private String updatedBy;
}
