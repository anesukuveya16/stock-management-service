package com.project.anesu.ecommerce.stockmanagementservice.service;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Product;
import com.project.anesu.ecommerce.stockmanagementservice.model.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public Product addNewProduct(Product product) {
        return null;
    }

    @Override
    public Product getProductById(Long productId) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public Product updateProduct(Long productId, Product updatedSchedule) {
        return null;
    }

    @Override
    public void deleteProduct(Long productId) {

    }
}
