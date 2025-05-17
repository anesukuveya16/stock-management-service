package com.project.anesu.ecommerce.stockmanagementservice.service.util;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Inventory;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.InventoryRepository;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InventoryValidator {

  private final InventoryRepository inventoryRepository;

  public void validateProduct(Long productId, int requestedQuantity)
      throws ProductNotFoundException {
    Inventory inventory =
        inventoryRepository
            .findByProductId(productId)
            .orElseThrow(() -> new ProductNotFoundException("Product not found in inventory"));

    if (inventory.getCurrentQuantity() < requestedQuantity) {
      throw new IllegalStateException(
          "Only " + inventory.getCurrentQuantity() + " items left in stock.");
    }

    if (inventory.getCurrentQuantity() <= 0) {
      throw new IllegalStateException("This product is currently out of stock.");
    }

    inventory.setCurrentQuantity(inventory.getCurrentQuantity() - requestedQuantity);
    inventoryRepository.save(inventory);
  }
}
