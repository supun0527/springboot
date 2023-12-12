package com.brixo.productcatalogue.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ServiceType extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String key;

  @ManyToOne
  @JoinColumn(name = "service_id", nullable = false)
  private Service service;
}
