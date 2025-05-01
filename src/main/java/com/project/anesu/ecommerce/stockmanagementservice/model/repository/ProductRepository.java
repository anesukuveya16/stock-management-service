package com.project.anesu.ecommerce.stockmanagementservice.model.repository;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
