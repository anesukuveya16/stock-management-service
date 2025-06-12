package com.project.anesu.ecommerce.stockmanagementservice.service.exception;

public class InvalidProductException extends RuntimeException {
  public InvalidProductException(String message) {
    super(message);
  }
}
