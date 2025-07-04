package com.project.anesu.ecommerce.stockmanagementservice.model;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Category;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.CategoryNotFoundException;

/**
 * Service interface for managing {@link Category} entities in the Stock Management Service.
 *
 * <p>Provides methods to create, retrieve, update, and delete product categories.
 */
public interface CategoryService {

  /**
   * Creates a new category in the system.
   *
   * @param category the category to be created
   * @return the created {@link Category} with any auto-generated fields populated (e.g., ID)
   */
  Category createNewCategory(Category category);

  /**
   * Retrieves a category by its unique ID.
   *
   * @param categoryId the ID of the category to retrieve
   * @return the {@link Category} with the given ID
   */
  Category getCategoryById(Long categoryId);

  /**
   * Updates an existing category.
   *
   * @param categoryId the ID of the category to update
   * @param updatedCategory the updated category details
   * @return the updated {@link Category}
   * @throws CategoryNotFoundException if the category with the given ID is not found
   */
  Category updateCategory(Long categoryId, Category updatedCategory) throws CategoryNotFoundException;

  /**
   * Deletes a category by its unique ID.
   *
   * @param categoryId the ID of the category to delete
   * @throws CategoryNotFoundException if the category with the given ID is not found
   */
  void deleteCategory(Long categoryId) throws CategoryNotFoundException;
}
