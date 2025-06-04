package com.project.anesu.ecommerce.stockmanagementservice.service.util;

public class LowStockException extends RuntimeException {
  public LowStockException(String message) {
    super(message);
  }
}
