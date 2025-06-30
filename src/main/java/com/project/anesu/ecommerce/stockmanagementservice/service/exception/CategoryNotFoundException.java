package com.project.anesu.ecommerce.stockmanagementservice.service.exception;

public class CategoryNotFoundException extends RuntimeException {

  private static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found with id: %s";

  public CategoryNotFoundException(Long message) {
    super(CATEGORY_NOT_FOUND_MESSAGE.formatted(message));
  }
}
