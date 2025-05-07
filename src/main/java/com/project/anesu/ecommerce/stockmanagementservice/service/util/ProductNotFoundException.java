package com.project.anesu.ecommerce.stockmanagementservice.service.util;

public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(String message) {
    super(message);
  }
}
