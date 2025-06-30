package com.project.anesu.ecommerce.stockmanagementservice.controller;

import static com.project.anesu.ecommerce.stockmanagementservice.controller.StockControllerEndpoints.*;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Category;
import com.project.anesu.ecommerce.stockmanagementservice.entity.Product;
import com.project.anesu.ecommerce.stockmanagementservice.service.CategoryServiceImpl;
import com.project.anesu.ecommerce.stockmanagementservice.service.ProductServiceImpl;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.CategoryNotFoundException;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.InvalidProductException;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.LowStockException;
import com.project.anesu.ecommerce.stockmanagementservice.service.util.InventoryValidator;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LANDING_PAGE)
@AllArgsConstructor
public class StockController {

  private final ProductServiceImpl productService;
  private final CategoryServiceImpl categoryService;
  private final InventoryValidator inventoryValidator;

  @PostMapping(CREATE_CATEGORY)
  public Category createCategory(@RequestBody Category category) {

    return categoryService.createNewCategory(category);
  }

  @GetMapping(GET_CATEGORY_BY_ID)
  public Category getCategoryById(@PathVariable Long categoryId) {

    return categoryService.getCategoryById(categoryId);
  }

  @DeleteMapping(DELETE_CATEGORY)
  public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) throws CategoryNotFoundException {

    categoryService.deleteCategory(categoryId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping(CREATE_PRODUCT)
  public Product addProduct(@RequestBody Product product) {

    return productService.addNewProduct(product);
  }

  @GetMapping(GET_PRODUCT_BY_ID)
  public Product getProductById(@PathVariable Long productId) {

    return productService.getProductById(productId);
  }

  @GetMapping(GET_ALL_PRODUCTS)
  public List<Product> getAllProducts() {

    return productService.getAllProducts();
  }

  @PutMapping(UPDATE_PRODUCT)
  public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct)
      throws InvalidProductException {

    Product update = productService.updateProduct(productId, updatedProduct);
    if (update != null) {
      return ResponseEntity.ok(update);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping(DELETE_PRODUCT)
  public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) throws InvalidProductException {

    productService.deleteProduct(productId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping(VALIDATE_AND_DEDUCT_PRODUCT_FROM_INVENTORY)
  public ResponseEntity<String> validateInventory(
      @RequestBody List<Map<String, Object>> orderItems) {

    try {
      inventoryValidator.validateInventoryAvailability(orderItems);
      return ResponseEntity.ok().build();
    } catch (InvalidProductException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (LowStockException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PostMapping(ADD_RETURNED_INVENTORY)
  public ResponseEntity<String> addReturnedInventory(
      @RequestBody List<Map<String, Object>> returnOrderItems) {

    try {
      inventoryValidator.addInventoryReturns(returnOrderItems);
      return ResponseEntity.ok().build();
    } catch (InvalidProductException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}
