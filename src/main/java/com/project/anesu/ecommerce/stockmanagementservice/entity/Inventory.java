package com.project.anesu.ecommerce.stockmanagementservice.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

  @Id private Long productId;
  private int availableQuantity;
}
