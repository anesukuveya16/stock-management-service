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
import java.util.Optional;
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

  @PostMapping(StockControllerEndpoints.CREATE_PRODUCT)
  public Product addProduct(@RequestBody Product product) {
    return productService.addNewProduct(product);
  }

  @GetMapping(StockControllerEndpoints.GET_PRODUCT_BY_ID)
  public Optional<Product> getProductById(@PathVariable Long id) {
    return productService.getProductById(id);
  }

  @GetMapping(StockControllerEndpoints.GET_ALL_PRODUCTS)
  public List<Product> getAllProducts() {
    return productService.getAllProducts();
  }

  @PutMapping(StockControllerEndpoints.UPDATE_PRODUCT)
  public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct)
      throws InvalidProductException {
    return productService.updateProduct(id, updatedProduct);
  }

  @DeleteMapping(StockControllerEndpoints.DELETE_PRODUCT)
  public void deleteProduct(@PathVariable Long id) throws InvalidProductException {
    productService.deleteProduct(id);
  }

  @PostMapping(StockControllerEndpoints.CREATE_CATEGORY)
  public Category createCategory(@RequestBody Category category) {
    return categoryService.createNewCategory(category);
  }

  @GetMapping(StockControllerEndpoints.GET_CATEGORY_BY_ID)
  public Optional<Category> getCategoryById(@PathVariable Long id) {
    return categoryService.getCategoryById(id);
  }

  @DeleteMapping(StockControllerEndpoints.DELETE_CATEGORY)
  public void deleteCategory(@PathVariable Long id) throws CategoryNotFoundException {
    categoryService.deleteCategory(id);
  }

  @PostMapping(StockControllerEndpoints.VALIDATE_AND_DEDUCT_PRODUCT_FROM_INVENTORY)
  public ResponseEntity<String> validateInventory(
      @RequestBody List<Map<String, Object>> orderItems) {
    try {
      inventoryValidator.validateInventoryAvailability(orderItems);
      return ResponseEntity.ok("Inventory successfully validated!");
    } catch (InvalidProductException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (LowStockException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}
