package com.project.anesu.ecommerce.stockmanagementservice.service.util;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Inventory;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.InventoryRepository;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.InvalidProductException;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.LowStockException;
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
                              new InvalidProductException(
                                  "Product with id " + productId + " not found")));

      if (inventory.getAvailableQuantity() < requestedQuantity) {
        throw new LowStockException(
            "Only "
                + inventory.getAvailableQuantity()
                + " from product "
                + productId
                + " is currently available.");
      }
    }

    for (Map<String, Object> item : orderItemsToDeductForOrderCreation) {

      Long productId = getProductId(item);
      int requestedQuantity = getRequestedQuantity(item);
      Inventory inventory = productInventories.get(productId);

      inventory.setAvailableQuantity(inventory.getAvailableQuantity() - requestedQuantity);
      inventoryRepository.save(inventory);
    }
  }

  public void addInventoryReturns(List<Map<String, Object>> orderItemsReturningToCurrentInventory) {
    for (Map<String, Object> item : orderItemsReturningToCurrentInventory) {
      Long productId = getProductId(item);
      int returningInventoryQuantity = getRequestedQuantity(item);

      Inventory inventory =
          inventoryRepository
              .findById(productId)
              .orElseThrow(
                  () ->
                      new InvalidProductException("Product with id " + productId + " not found."));

      inventory.setAvailableQuantity(inventory.getAvailableQuantity() + returningInventoryQuantity);
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
