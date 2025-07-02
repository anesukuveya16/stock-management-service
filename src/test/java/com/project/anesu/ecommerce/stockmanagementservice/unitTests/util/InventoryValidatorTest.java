package com.project.anesu.ecommerce.stockmanagementservice.unitTests.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Inventory;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.InventoryRepository;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.InvalidProductException;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.LowStockException;
import com.project.anesu.ecommerce.stockmanagementservice.service.util.InventoryValidator;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryValidatorTest {

  @Mock private InventoryRepository inventoryRepositoryMock;

  private InventoryValidator cut;

  @BeforeEach
  void setUp() {
    cut = new InventoryValidator(inventoryRepositoryMock);
  }

  @Test
  void validateInventoryAvailability_ShouldSuccessfullyValidateAndDeductInventoryFroOrderCreation() {

    // Given
    Long productId = 1L;
    int requestedQuantity = 5;
    int currentQuantity = 200;

    Inventory inventory = new Inventory();
    inventory.setProductId(productId);
    inventory.setAvailableQuantity(currentQuantity);

    List<Map<String, Object>> orderItems =
        getOrderItemsForOrderCreation(productId, requestedQuantity);
    when(inventoryRepositoryMock.findById(productId)).thenReturn(Optional.of(inventory));
    when(inventoryRepositoryMock.save(any(Inventory.class))).thenReturn(inventory);

    // When
    cut.validateInventoryAvailability(orderItems);

    // Then
    assertNotNull(inventory);

    verify(inventoryRepositoryMock, times(1)).findById(productId);
    verify(inventoryRepositoryMock, times(1)).save(inventory);
  }

  @Test
  void
      validateInventoryAvailability_ShouldThrowExceptionWhenQuantityRequestedIsMoreThanCurrentQuantity() {

    // Given
    Long productId = 1L;
    int requestedQuantity = 5;
    int currentQuantity = 2;

    Inventory inventory = new Inventory();
    inventory.setProductId(productId);
    inventory.setAvailableQuantity(currentQuantity);

    List<Map<String, Object>> orderItems = new ArrayList<>();
    Map<String, Object> orderItem = new HashMap<>();
    orderItem.put("productId", productId);
    orderItem.put("requestedQuantity", requestedQuantity);
    orderItems.add(orderItem);

    when(inventoryRepositoryMock.findById(productId)).thenReturn(Optional.of(inventory));

    // When
    LowStockException exception =
        assertThrows(LowStockException.class, () -> cut.validateInventoryAvailability(orderItems));

    // Then

    assertEquals(
        "Only " + currentQuantity + " from product " + productId + " is currently available.",
        exception.getMessage());

    verify(inventoryRepositoryMock, times(1)).findById(productId);
    verifyNoMoreInteractions(inventoryRepositoryMock);
  }

  @Test
  void
      validateInventoryAvailability_ShouldThrowExceptionWhenProductIdIsNotFoundInTheInventoryRepository() {

    // Given
    Long productId = 1L;
    int requestedQuantity = 2;

    Inventory inventory = new Inventory();
    inventory.setProductId(productId);
    inventory.setAvailableQuantity(requestedQuantity);

    List<Map<String, Object>> orderItemsForOrderCreation =
        getOrderItemsForOrderCreation(productId, requestedQuantity);

    when(inventoryRepositoryMock.findById(productId)).thenReturn(Optional.empty());

    // When
    InvalidProductException exception =
        assertThrows(
            InvalidProductException.class,
            () -> cut.validateInventoryAvailability(orderItemsForOrderCreation));

    // Then
    assertEquals("Product with id " + productId + " not found", exception.getMessage());

    verify(inventoryRepositoryMock, times(1)).findById(productId);
    verifyNoMoreInteractions(inventoryRepositoryMock);
  }

  @Test
  void shouldSuccessfullyAddReturnedInventoryFromCustomerBackToProductDatabase() {

    // Given
    Long productId = 105L;
    int returningInventoryQuantity = 4;

    Inventory inventory = new Inventory();
    inventory.setProductId(productId);
    inventory.setAvailableQuantity(returningInventoryQuantity);

    List<Map<String, Object>> returningInventory = getOrderItemsFromOrderCancellation(productId);

    when(inventoryRepositoryMock.findById(productId)).thenReturn(Optional.of(inventory));
    when(inventoryRepositoryMock.save(inventory)).thenReturn(inventory);

    // When

    cut.addInventoryReturns(returningInventory);

    // Then
    assertNotNull(returningInventory);

    verify(inventoryRepositoryMock, times(1)).findById(productId);
    verify(inventoryRepositoryMock, times(1)).save(inventory);
  }

  @Test
  void addInventoryReturns_ShouldThrowExceptionWhenProductIdIsNotFoundInRepository() {

    // Given
    Long productId = 105L;
    List<Map<String, Object>> returnedInventory = getOrderItemsFromOrderCancellation(productId);

    when(inventoryRepositoryMock.findById(productId)).thenReturn(Optional.empty());

    // When
    InvalidProductException exception =
        assertThrows(
            InvalidProductException.class, () -> cut.addInventoryReturns(returnedInventory));

    // Then
    assertEquals("Product with id " + productId + " not found.", exception.getMessage());

    verify(inventoryRepositoryMock, times(1)).findById(productId);
    verifyNoMoreInteractions(inventoryRepositoryMock);
  }

  private List<Map<String, Object>> getOrderItemsForOrderCreation(
      Long productId, int requestedQuantity) {
    List<Map<String, Object>> orderItemsForOrderCreation = new ArrayList<>();
    Map<String, Object> orderItem = new HashMap<>();
    orderItem.put("productId", productId);
    orderItem.put("requestedQuantity", requestedQuantity);
    orderItemsForOrderCreation.add(orderItem);
    return orderItemsForOrderCreation;
  }

  private List<Map<String, Object>> getOrderItemsFromOrderCancellation(Long productId) {
    int returningInventoryQuantity = 2;

    Inventory inventory = new Inventory();
    inventory.setProductId(productId);
    inventory.setAvailableQuantity(returningInventoryQuantity);

    List<Map<String, Object>> orderItems = new ArrayList<>();
    Map<String, Object> returningOrderItems = new HashMap<>();
    returningOrderItems.put("productId", productId);
    returningOrderItems.put("requestedQuantity", returningInventoryQuantity);
    orderItems.add(returningOrderItems);
    return orderItems;
  }
}
