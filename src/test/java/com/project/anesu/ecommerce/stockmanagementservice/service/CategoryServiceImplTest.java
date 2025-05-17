package com.project.anesu.ecommerce.stockmanagementservice.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Category;
import com.project.anesu.ecommerce.stockmanagementservice.entity.Product;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.CategoryRepository;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.CategoryNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

  @Mock private CategoryRepository categoryRepositoryMock;

  private CategoryServiceImpl cut;

  @BeforeEach
  void setUp() {
    cut = new CategoryServiceImpl(categoryRepositoryMock);
  }

  @Test
  void shouldSuccessfullyCreateAndSaveNewCategory() {
    // Given
    Long id = 115L;
    Category category = new Category();
    category.setId(id);
    category.setCategoryName("Women´s Clothing");
    category.setProducts(new ArrayList<Product>());

    when(categoryRepositoryMock.save(any(Category.class))).thenReturn(category);

    // When
    cut.createNewCategory(category);

    // Then
    assertNotNull(category);

    verify(categoryRepositoryMock, times(1)).save(category);
  }

  @Test
  void shouldRetrieveRequestedCategory_ByGivenCategoryId() {
    // Given
    Long categoryId = 20L;
    Category category = new Category();
    category.setId(categoryId);

    when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(category));

    // When
    cut.getCategoryById(categoryId);

    // Then
    verify(categoryRepositoryMock, times(1)).findById(categoryId);
  }

  @Test
  void deleteCategory_ShouldThrowExceptionWhen_GivenCategoryIdIsNotFound() {
    // Given
    Long categoryId = 20L;
    Category category = new Category();
    category.setId(categoryId);

    when(categoryRepositoryMock.existsById(categoryId)).thenReturn(false);

    // When
    assertThrows(CategoryNotFoundException.class, () -> cut.deleteCategory(categoryId));

    // Then
    verify(categoryRepositoryMock, times(1)).existsById(categoryId);
    verifyNoMoreInteractions(categoryRepositoryMock);
  }
}
