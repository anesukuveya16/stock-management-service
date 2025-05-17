package com.project.anesu.ecommerce.stockmanagementservice.controller;

import static com.project.anesu.ecommerce.stockmanagementservice.controller.StockControllerEndpoints.*;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Category;
import com.project.anesu.ecommerce.stockmanagementservice.entity.Product;
import com.project.anesu.ecommerce.stockmanagementservice.service.CategoryServiceImpl;
import com.project.anesu.ecommerce.stockmanagementservice.service.ProductServiceImpl;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.CategoryNotFoundException;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.ProductNotFoundException;
import com.project.anesu.ecommerce.stockmanagementservice.service.util.InventoryValidator;
import java.util.List;
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

  @PostMapping(CREATE_PRODUCT)
  public Product addProduct(@RequestBody Product product) {
    return productService.addNewProduct(product);
  }

  @GetMapping(GET_PRODUCT_BY_ID)
  public Optional<Product> getProductById(@PathVariable Long id) {
    return productService.getProductById(id);
  }

  @GetMapping
  public List<Product> getAllProducts() {
    return productService.getAllProducts();
  }

  @PutMapping(UPDATE_PRODUCT)
  public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct)
      throws ProductNotFoundException {
    return productService.updateProduct(id, updatedProduct);
  }

  @DeleteMapping(DELETE_PRODUCT)
  public void deleteProduct(@PathVariable Long id) throws ProductNotFoundException {
    productService.deleteProduct(id);
  }


  @PostMapping(CREATE_CATEGORY)
  public Category createCategory(@RequestBody Category category) {
    return categoryService.createNewCategory(category);
  }

  @GetMapping(GET_CATEGORY_BY_ID)
  public Optional<Category> getCategoryById(@PathVariable Long id) {
    return categoryService.getCategoryById(id);
  }

  @DeleteMapping(DELETE_CATEGORY)
  public void deleteCategory(@PathVariable Long id) throws CategoryNotFoundException {
    categoryService.deleteCategory(id);
  }

  @GetMapping(VALIDATE_INVENTORY)
  public ResponseEntity<String> validateInventory(
      @RequestParam Long productId, @RequestBody int requestedQuantity) {
    try {
      inventoryValidator.validateProduct(productId, requestedQuantity);
      return new ResponseEntity<>("Stock validation and update successful.", HttpStatus.OK);
    } catch (ProductNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
