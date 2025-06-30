package com.project.anesu.ecommerce.stockmanagementservice.service;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Category;
import com.project.anesu.ecommerce.stockmanagementservice.model.CategoryService;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.CategoryRepository;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.CategoryNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Category createNewCategory(Category category) {

    return categoryRepository.save(category);
  }

  @Override
  public Category getCategoryById(Long categoryId) throws CategoryNotFoundException {

    return categoryRepository
        .findById(categoryId)
        .orElseThrow(() -> new CategoryNotFoundException(categoryId));
  }

  @Override
  public Category updateCategory(Long categoryId, Category updatedCategory)
      throws CategoryNotFoundException {

    Category existingCategory = getCategoryById(categoryId);

    existingCategory.setCategoryName(updatedCategory.getCategoryName());

    return categoryRepository.save(existingCategory);
  }

  @Override
  public void deleteCategory(Long categoryId) throws CategoryNotFoundException {

    if (!categoryRepository.existsById(categoryId)) {
      throw new CategoryNotFoundException(categoryId);
    }
    categoryRepository.deleteById(categoryId);
  }
}
