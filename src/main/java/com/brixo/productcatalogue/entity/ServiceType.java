package com.brixo.productcatalogue.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_type")
@NoArgsConstructor
@Getter
@Setter
public class ServiceType extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "service_id", nullable = false)
  private Service service;
}
