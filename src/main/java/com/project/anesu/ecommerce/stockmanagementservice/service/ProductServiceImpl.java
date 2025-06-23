package com.project.anesu.ecommerce.stockmanagementservice.service;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Product;
import com.project.anesu.ecommerce.stockmanagementservice.model.ProductService;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.ProductRepository;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.InvalidProductException;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.ProductNotFoundException;
import com.project.anesu.ecommerce.stockmanagementservice.service.util.ProductValidator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  private final ProductValidator productValidator;

  private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found with id";

  @Override
  public Product addNewProduct(Product product) {

    productValidator.validateProduct(product);
    return productRepository.save(product);
  }

  @Override
  public Product getProductById(Long productId) {

    return productRepository
        .findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MESSAGE));
  }

  // TODO: add product to category?

  @Override
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @Override
  public Product updateProduct(Long productId, Product updatedProduct)
      throws InvalidProductException {

    Product existingProduct =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MESSAGE + productId));

    Product updatedExistingProduct = updateExistingProduct(updatedProduct, existingProduct);

    productValidator.validateProduct(updatedExistingProduct);

    return productRepository.save(updatedExistingProduct);
  }

  @Override
  public void deleteProduct(Long productId) throws ProductNotFoundException {
    if (!productRepository.existsById(productId)) {
      throw new ProductNotFoundException(PRODUCT_NOT_FOUND_MESSAGE + productId);
    }
    productRepository.deleteById(productId);
  }

  private Product updateExistingProduct(Product updatedProduct, Product existingProduct) {
    existingProduct.setSize(updatedProduct.getSize());
    existingProduct.setPrice(updatedProduct.getPrice());
    existingProduct.setColour(updatedProduct.getColour());
    existingProduct.setProductName(updatedProduct.getProductName());
    existingProduct.setProductDescription(updatedProduct.getProductDescription());
    return existingProduct;
  }
}
