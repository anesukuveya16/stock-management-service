package com.project.anesu.ecommerce.stockmanagementservice.service.util;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Product;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.ProductNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {

    public void validateProduct(Product product) {

        validateProductInfo(product);
    }

    private void validateProductInfo(Product product) throws ProductNotFoundException {

        if (product.getId() == null) {
            throw new ProductNotFoundException("Invalid product id.");
        }

        if (product.getProductName() == null) {
            throw new ProductNotFoundException("Product name cannot be empty.");
        }

    }
}
