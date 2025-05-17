package com.project.anesu.ecommerce.stockmanagementservice.model.repository;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Inventory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

  Optional<Inventory> findByProductId(Long productId);
}
