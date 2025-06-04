package com.project.anesu.ecommerce.stockmanagementservice.service.util;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Inventory;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.InventoryRepository;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.ProductNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InventoryValidator {

  private final InventoryRepository inventoryRepository;

  public void validateInventoryAvailability(
      List<Map<String, Object>> orderItemsToDeductForOrderCreation) {

    Map<Long, Inventory> productInventories = new HashMap<>();

    // look up the productId´s and see if there is an error - throw an exception.
    for (Map<String, Object> item : orderItemsToDeductForOrderCreation) {
      Long productId = getProductId(item);
      int requestedQuantity = getRequestedQuantity(item);

      Inventory inventory =
          productInventories.computeIfAbsent(
              productId,
              id ->
                  inventoryRepository
                      .findById(productId)
                      .orElseThrow(
                          () ->
                              new ProductNotFoundException(
                                  "Product with id " + productId + " not found")));

      // match the requestedQuantity from the currentQuantity
      if (inventory.getAvailableQuantity() < requestedQuantity) {
        throw new LowStockException(
            "Only "
                + inventory.getAvailableQuantity()
                + "from "
                + productId
                + " is currently available.");
      }
    }

    // now that we have found the product and the products are "up" for being sold - we deduct the
    // requestQuantity from the availableQuantity. this only works when there are no exceptions from
    // the first step.
    for (Map<String, Object> item : orderItemsToDeductForOrderCreation) {

      Long productId = getProductId(item);
      int requestedQuantity = getRequestedQuantity(item);
      Inventory inventory = productInventories.get(productId);

      inventory.setAvailableQuantity(inventory.getAvailableQuantity() - requestedQuantity);
      inventoryRepository.save(inventory);
    }
  }

  private int getRequestedQuantity(Map<String, Object> item) {
    return (Integer) item.get("requestedQuantity");
  }

  private Long getProductId(Map<String, Object> item) {
    return ((Number) item.get("productId")).longValue();
  }
}
