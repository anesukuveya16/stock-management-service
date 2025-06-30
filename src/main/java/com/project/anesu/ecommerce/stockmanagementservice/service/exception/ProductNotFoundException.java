package com.project.anesu.ecommerce.stockmanagementservice.service.exception;

public class ProductNotFoundException extends RuntimeException {

  private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found with id: %s";

  public ProductNotFoundException(Long message) {
    super(PRODUCT_NOT_FOUND_MESSAGE.formatted(message));
  }
}
