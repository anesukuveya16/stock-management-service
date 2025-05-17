package com.project.anesu.ecommerce.stockmanagementservice.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Category;
import com.project.anesu.ecommerce.stockmanagementservice.entity.Product;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.ProductRepository;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.ProductNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

  @Mock private ProductRepository productRepositoryMock;

  private ProductServiceImpl cut;

  @BeforeEach
  void setUp() {
    cut = new ProductServiceImpl(productRepositoryMock);
  }

  @Test
  void shouldSuccessfullyCreateAndSave_NewProduct() {
    // Given
    Long productId = 1L;

    Product product = new Product();
    product.setId(productId);
    product.setSize("M");
    product.setCategory(new Category());
    product.setProductName("Ladies shirts");
    product.setColour("Pink, White, Blue, Black, Yellow");
    product.setProductDescription("Women´s formal shirts");

    when(productRepositoryMock.save(any(Product.class))).thenReturn(product);

    // When
    Product newProduct = cut.addNewProduct(product);

    // Then
    assertNotNull(newProduct);

    verify(productRepositoryMock, times(1)).save(newProduct);
  }

  @Test
  void shouldSuccessfullyRetrieveProductId_ByGivenProductId() {
    // Given
    Long productId = 105L;
    Product product = new Product();
    product.setId(productId);

    when(productRepositoryMock.findById(productId)).thenReturn(Optional.of(product));

    // When
    cut.getProductById(productId);

    // Then
    verify(productRepositoryMock, times(1)).findById(productId);
  }

  @Test
  void updateProduct_ShouldSuccessfullyUpdateAndSaveProduct() {
    // Given
    Long productId = 1L;

    Product existingProduct = new Product();
    existingProduct.setId(productId);
    existingProduct.setSize("M");
    existingProduct.setCategory(new Category());
    existingProduct.setProductName("Ladies shirts");
    existingProduct.setColour("Pink, White, Blue, Black, Yellow");
    existingProduct.setProductDescription("Women´s formal shirts");

    Product updatedProduct = new Product();
    updatedProduct.setId(productId);
    updatedProduct.setSize("M");
    updatedProduct.setCategory(new Category());
    updatedProduct.setProductName("Ladies shirts");
    updatedProduct.setColour("Pink, White, Blue, Black, Yellow");
    updatedProduct.setProductDescription("Women´s formal shirts");

    when(productRepositoryMock.findById(productId)).thenReturn(Optional.of(existingProduct));
    when(productRepositoryMock.save(any(Product.class))).thenReturn(updatedProduct);

    // When
    cut.updateProduct(productId, updatedProduct);

    // Then
    assertNotNull(updatedProduct);
    assertThat(updatedProduct.getId()).isEqualTo(existingProduct.getId());

    verify(productRepositoryMock, times(1)).findById(productId);
  }

  @Test
  void update_ShouldThrowException_WhenProductNotFoundByGivenProductId() {
    // Given
    Long productId = 105L;
    Product product = new Product();
    product.setId(productId);

    when(productRepositoryMock.findById(productId)).thenReturn(Optional.empty());

    // When
    ProductNotFoundException productNotFoundException =
        assertThrows(ProductNotFoundException.class, () -> cut.updateProduct(productId, product));

    // Then
    verify(productRepositoryMock, times(1)).findById(productId);

    assertThat(productNotFoundException.getMessage())
        .isEqualTo("Product not found with Id " + productId);
  }

  @Test
  void deleteProduct_ShouldThrowException_WhenProductNotFoundByGivenProductId() {
    // Given
    Long productId = 20L;
    Product product = new Product();
    product.setId(productId);

    when(productRepositoryMock.existsById(productId)).thenReturn(false);

    // When
    assertThrows(ProductNotFoundException.class, () -> cut.deleteProduct(productId));

    // Then
    verify(productRepositoryMock, times(1)).existsById(productId);
    verifyNoMoreInteractions(productRepositoryMock);
  }
}
