package com.brixo.productcatalogue.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(name = "product_key", unique = true, nullable = false)
  private String productKey;

  @ManyToOne
  @JoinColumn(name = "service_type_id", nullable = false)
  private ServiceType serviceType;

  @Column(name = "is_disabled", nullable = false)
  private boolean isDisabled;

  @Column(name = "updated_by")
  private String updatedBy;
}
