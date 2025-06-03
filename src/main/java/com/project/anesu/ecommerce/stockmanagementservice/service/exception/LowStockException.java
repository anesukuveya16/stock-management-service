package com.project.anesu.ecommerce.stockmanagementservice.service.exception;

public class LowStockException extends RuntimeException {
  public LowStockException(String message) {
    super(message);
  }
}
