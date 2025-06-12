package com.project.anesu.ecommerce.stockmanagementservice.service.util;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Product;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.InvalidProductException;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {

  public void validateProduct(Product product) {

    validateProductInfo(product);
  }

  private void validateProductInfo(Product product) throws InvalidProductException {

    if (product.getProductName() == null || product.getProductDescription() == null) {
      throw new InvalidProductException("Product name or product description cannot be empty!");
    }

    if (product.getPrice() == 0) {
      throw new InvalidProductException("Product price cannot be empty.");
    }

    if (product.getSize() == 0 || product.getColour() == null) {
      throw new InvalidProductException("Product size or color cannot be empty.");
    }
  }
}
