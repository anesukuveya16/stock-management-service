package com.project.anesu.ecommerce.stockmanagementservice.unitTests.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Product;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.InvalidProductException;
import com.project.anesu.ecommerce.stockmanagementservice.service.util.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductValidatorTest {

  private ProductValidator cut;

  @BeforeEach
  void setUp() {
    cut = new ProductValidator();
  }

  private static final String PRODUCT_NAME = "Product Name";
  private static final String PRODUCT_DESCRIPTION = "Product Description";

  @Test
  void shouldThrowExceptionWhen_ProductNameIsNull() {
    // Given
    Product newProductInStock = getNewProduct(2L, null, PRODUCT_DESCRIPTION);

    // When
    InvalidProductException exception =
        assertThrows(InvalidProductException.class, () -> cut.validateProduct(newProductInStock));

    // Then
    assertEquals("Product name or product description cannot be empty!", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhen_ProductDescriptionIsNull() {
    // Given
    Product newProductInStock = getNewProduct(25L, PRODUCT_NAME, null);

    // When
    InvalidProductException exception =
        assertThrows(InvalidProductException.class, () -> cut.validateProduct(newProductInStock));

    // Then
    assertEquals("Product name or product description cannot be empty!", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhen_ProductHasNoPrice() {
    // Given

    Product newProductInStock = getNewProduct(100L, PRODUCT_NAME, PRODUCT_DESCRIPTION);
    newProductInStock.setPrice(0);

    // When
    InvalidProductException exception =
        assertThrows(InvalidProductException.class, () -> cut.validateProduct(newProductInStock));

    // Then
    assertEquals("Product price cannot be empty.", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhen_ProductSizeIsNull() {
    // Given
    Product newProductInStock = getNewProduct(4L, PRODUCT_NAME, PRODUCT_DESCRIPTION);
    newProductInStock.setPrice(3.60);
    newProductInStock.setSize(0);

    // When
    InvalidProductException exception =
        assertThrows(InvalidProductException.class, () -> cut.validateProduct(newProductInStock));

    // Then
    assertEquals("Product size or color cannot be empty.", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhen_ProductColorIsMissing() {
    // Given
    Product newProductInStock = getNewProduct(20L, PRODUCT_NAME, PRODUCT_DESCRIPTION);
    newProductInStock.setPrice(2.00);
    newProductInStock.setSize(36);
    newProductInStock.setColour(null);

    // When
    InvalidProductException exception =
        assertThrows(InvalidProductException.class, () -> cut.validateProduct(newProductInStock));

    // Then

    assertEquals("Product size or color cannot be empty.", exception.getMessage());
  }

  private Product getNewProduct(Long productId, String productName, String productDescription) {
    Product newProductInStock = new Product();
    newProductInStock.setId(productId);
    newProductInStock.setProductName(productName);
    newProductInStock.setProductDescription(productDescription);
    newProductInStock.setPrice(newProductInStock.getPrice());
    newProductInStock.setSize(newProductInStock.getSize());
    newProductInStock.setColour(newProductInStock.getColour());
    return newProductInStock;
  }
}
