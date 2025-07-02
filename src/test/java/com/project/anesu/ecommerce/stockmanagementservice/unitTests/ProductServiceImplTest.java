package com.project.anesu.ecommerce.stockmanagementservice.unitTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Category;
import com.project.anesu.ecommerce.stockmanagementservice.entity.Product;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.ProductRepository;
import com.project.anesu.ecommerce.stockmanagementservice.service.ProductServiceImpl;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.InvalidProductException;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.ProductNotFoundException;
import com.project.anesu.ecommerce.stockmanagementservice.service.util.ProductValidator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

  @Mock private ProductRepository productRepositoryMock;
  @Mock private ProductValidator productValidatorMock;

  private ProductServiceImpl cut;

  @BeforeEach
  void setUp() {
    cut = new ProductServiceImpl(productRepositoryMock, productValidatorMock);
  }

  @Test
  void shouldSuccessfullyCreateAndSaveNewProduct_WhenValidationIsPassed() {

    // Given
    Product product = getNewProduct();

    Category category = new Category();
    category.setId(10L);
    category.setCategoryName("Shorts");

    doNothing().when(productValidatorMock).validateProduct(product);
    when(productRepositoryMock.save(product)).thenReturn(product);

    // When
    Product createdProduct = cut.addNewProduct(product);

    // Then
    assertNotNull(createdProduct);
    assertEquals(product, createdProduct);

    verify(productValidatorMock).validateProduct(product);
    verify(productRepositoryMock, times(1)).save(product);
  }

  @Test
  void shouldNotSaveProduct_WhenValidationIsNotPassed() {

    // Given
    Product product = getNewProduct();
    doThrow(InvalidProductException.class).when(productValidatorMock).validateProduct(product);

    // When
    assertThrows(InvalidProductException.class, () -> cut.addNewProduct(product));

    // Then
    verifyNoInteractions(productRepositoryMock);
    verify(productValidatorMock).validateProduct(product);
  }

  @Test
  void shouldSuccessfullyUpdateProduct_WhenValidationIsPassed() {

    // Given
    Long productId = 1L;
    Product existingProduct = getRequestedProduct(1L, "Denim short", "Summer shorts");

    Product updatedProduct = getRequestedProduct(1L, "Women Denim short", "Casual summer shorts");

    when(productRepositoryMock.findById(productId)).thenReturn(Optional.of(existingProduct));
    doNothing().when(productValidatorMock).validateProduct(any(Product.class));
    when(productRepositoryMock.save(existingProduct)).thenReturn(updatedProduct);

    // When

    Product updateProduct = cut.updateProduct(productId, updatedProduct);

    // Then
    assertNotNull(updateProduct);

    verify(productValidatorMock).validateProduct(existingProduct);
    verify(productRepositoryMock).save(existingProduct);
    verify(productRepositoryMock, times(1)).findById(productId);
  }

  @Test
  void shouldSuccessfullyRetrieveProductById() {

    // Given
    Long productId = 1L;
    Product product = new Product();
    product.setId(productId);

    when(productRepositoryMock.findById(productId)).thenReturn(Optional.of(product));

    // When
    cut.getProductById(productId);

    // Then
    verify(productRepositoryMock, times(1)).findById(productId);
  }

  @Test
  void shouldThrowExceptionWhenProductNotFoundByGivenId() {

    // Given
    Long productId = 1L;
    when(productRepositoryMock.findById(productId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(ProductNotFoundException.class, () -> cut.getProductById(productId));
  }

  @Test
  void shouldRetrieveAllProducts() {

    // Given
    when(productRepositoryMock.findAll()).thenReturn(List.of());

    // When

    List<Product> allProducts = cut.getAllProducts();

    // Then
    assertNotNull(allProducts);
    verify(productRepositoryMock, times(1)).findAll();
  }

  @Test
  void shouldSuccessfullyDeleteProductByGivenId() {

    // Given
    Long productId = 1L;
    when(productRepositoryMock.existsById(productId)).thenReturn(true);

    // When
    cut.deleteProduct(productId);

    // Then
    verify(productRepositoryMock, times(1)).deleteById(productId);
    verifyNoMoreInteractions(productRepositoryMock);
  }

  @Test
  void shouldThrowException_WhenProductToDeleteIsNotFoundByGivenId() {

    // Given
    Long productId = 1L;
    when(productRepositoryMock.existsById(productId)).thenReturn(false);

    // When
    assertThrows(ProductNotFoundException.class, () -> cut.deleteProduct(productId));

    // Then
    verifyNoMoreInteractions(productRepositoryMock);
  }

  private Product getNewProduct() {
    Product product = new Product();
    product.setProductName("Denim short");
    product.setProductDescription("Summer shorts");
    product.setPrice(2.50);
    product.setColour("white");
    product.setSize(30);
    return product;
  }

  private Product getRequestedProduct(Long productId, String Denim_short, String Summer_shorts) {
    Product existingProduct = new Product();
    existingProduct.setId(productId);
    existingProduct.setProductName(Denim_short);
    existingProduct.setProductDescription(Summer_shorts);
    existingProduct.setPrice(2.50);
    existingProduct.setColour("white");
    existingProduct.setSize(30);
    return existingProduct;
  }
}
