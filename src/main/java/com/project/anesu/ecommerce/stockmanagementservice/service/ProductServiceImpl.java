package com.project.anesu.ecommerce.stockmanagementservice.service;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Product;
import com.project.anesu.ecommerce.stockmanagementservice.model.ProductService;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.ProductRepository;
import com.project.anesu.ecommerce.stockmanagementservice.service.util.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCT_NOT_FOUND_EXCEPTION = "Product not found with id";

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product addNewProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    //TODO: add product to category?

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(Long productId, Product updatedProduct) throws ProductNotFoundException {
       Product existingProduct = productRepository.findById(productId)
               .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_EXCEPTION + productId));

        newlyUpdatedProduct(updatedProduct, existingProduct);

        return productRepository.save(existingProduct);
    }


    @Override
    public void deleteProduct(Long productId) throws ProductNotFoundException {
       if (!productRepository.existsById(productId)) {
           throw new ProductNotFoundException(PRODUCT_NOT_FOUND_EXCEPTION + productId);
       }
       productRepository.deleteById(productId);
    }

    private static void newlyUpdatedProduct(Product updatedProduct, Product existingProduct) {
        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setSize(updatedProduct.getSize());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setColour(updatedProduct.getColour());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setProductDescription(updatedProduct.getProductDescription());
    }


}
