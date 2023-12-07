package com.brixo.productcatalogue.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@NoArgsConstructor
@Getter
@Setter
public class Product extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private String name;

  @Column(name = "product_key", unique = true, nullable = false)
  private String productKey;

  @ManyToOne
  @JoinColumn(name = "service_type_id", nullable = false)
  private ServiceType serviceType;

  @Column(name = "is_disabled", nullable = false)
  private boolean isDisabled;
}
