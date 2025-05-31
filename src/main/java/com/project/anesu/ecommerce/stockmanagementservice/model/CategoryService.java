package com.project.anesu.ecommerce.stockmanagementservice.model;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Category;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.CategoryNotFoundException;

import java.util.Optional;

/**
 * Service interface for managing {@link Category} entities in the Stock Management Service.
 * <p>
 * Provides methods to create, retrieve, and delete product categories.
 * </p>
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
    Optional<Category> getCategoryById(Long categoryId);

    /**
     * Deletes a category by its unique ID.
     *
     * @param categoryId the ID of the category to delete
     * @throws CategoryNotFoundException if the category with the given ID is not found
     */
     void deleteCategory(Long categoryId) throws CategoryNotFoundException;
}
