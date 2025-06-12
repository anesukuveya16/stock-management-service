package com.project.anesu.ecommerce.stockmanagementservice.service;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Product;
import com.project.anesu.ecommerce.stockmanagementservice.model.ProductService;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.ProductRepository;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.InvalidProductException;
import com.project.anesu.ecommerce.stockmanagementservice.service.util.ProductValidator;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  private final ProductValidator productValidator;

  private static final String PRODUCT_NOT_FOUND_EXCEPTION = "Product not found with id";

  @Override
  public Product addNewProduct(Product product) {

    productValidator.validateProduct(product);
    return productRepository.save(product);
  }

  @Override
  public Optional<Product> getProductById(Long productId) {
    return productRepository.findById(productId);
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
            .orElseThrow(
                () -> new InvalidProductException(PRODUCT_NOT_FOUND_EXCEPTION + productId));

    Product updatedExistingProduct = updateExistingProduct(updatedProduct, existingProduct);

    productValidator.validateProduct(updatedExistingProduct);

    return productRepository.save(updatedExistingProduct);
  }

  @Override
  public void deleteProduct(Long productId) throws InvalidProductException {
    if (!productRepository.existsById(productId)) {
      throw new InvalidProductException(PRODUCT_NOT_FOUND_EXCEPTION + productId);
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
