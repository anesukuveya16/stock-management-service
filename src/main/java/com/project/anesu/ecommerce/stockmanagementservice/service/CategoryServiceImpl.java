package com.project.anesu.ecommerce.stockmanagementservice.service;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Category;
import com.project.anesu.ecommerce.stockmanagementservice.model.CategoryService;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.CategoryRepository;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.CategoryNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found.";
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Category createNewCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> getCategoryById(Long categoryId) {
       return categoryRepository.findById(categoryId);
    }

    @Override
    public void deleteCategory(Long categoryId) throws CategoryNotFoundException {
        if (categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(CATEGORY_NOT_FOUND_MESSAGE);
        }
        categoryRepository.deleteById(categoryId);
    }
}
