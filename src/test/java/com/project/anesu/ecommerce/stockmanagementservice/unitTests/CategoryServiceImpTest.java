package com.project.anesu.ecommerce.stockmanagementservice.unitTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Category;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.CategoryRepository;
import com.project.anesu.ecommerce.stockmanagementservice.service.CategoryServiceImpl;
import com.project.anesu.ecommerce.stockmanagementservice.service.exception.CategoryNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImpTest {

  @Mock private CategoryRepository categoryRepositoryMock;

  private CategoryServiceImpl cut;

  @BeforeEach
  void setUp() {
    cut = new CategoryServiceImpl(categoryRepositoryMock);
  }

  @Test
  void shouldSuccessfully_CreateNewCategory() {

    // Given
    Category category = new Category();

    when(categoryRepositoryMock.save(category)).thenReturn(category);

    // When
    Category createdCategory = cut.createNewCategory(category);

    // Then
    assertNotNull(createdCategory);
    assertEquals(category, createdCategory);

    verify(categoryRepositoryMock, times(1)).save(category);
  }

  @Test
  void shouldRetrieveCategory_ByGivenId() {

    // Given
    Long categoryId = 1L;
    Category category = new Category();
    category.setId(categoryId);

    when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(category));

    // When
    cut.getCategoryById(categoryId);

    // Then
    verify(categoryRepositoryMock, times(1)).findById(categoryId);
  }

  @Test
  void getCategoryById_ShouldThrowException_WhenCategoryNotFound() {

    // Given
    Long categoryId = 105L;
    when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.empty());

    // When
    assertThrows(CategoryNotFoundException.class, () -> cut.getCategoryById(categoryId));

    // Then
    verify(categoryRepositoryMock, times(1)).findById(categoryId);
    verifyNoMoreInteractions(categoryRepositoryMock);
  }

  // shouldUpdateCategorySuccessfully
  @Test
  void shouldSuccessfully_UpdateCategory() {

    // Given
    Long categoryId = 1L;
    Category existingCategory = getRequestedCategory(1L, "Mens´shirts.");

    Category updatedCategory = getRequestedCategory(1L, "Mens´formal shirts.");

    when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(existingCategory));

    when(categoryRepositoryMock.save(existingCategory)).thenReturn(updatedCategory);


    // When
    Category updatedCategoryUpdated = cut.updateCategory(categoryId, updatedCategory);

    // Then
    assertNotNull(updatedCategoryUpdated);
    assertEquals(updatedCategory, updatedCategoryUpdated);

    verify(categoryRepositoryMock, times(1)).findById(categoryId);
    verify(categoryRepositoryMock, times(1)).save(existingCategory);

  }


  @Test
  void deleteCategoryById_ShouldSuccessfullyDeleteCategory() {

    // Given
    Long categoryId = 100L;
    when(categoryRepositoryMock.existsById(categoryId)).thenReturn(true);

    // When
    cut.deleteCategory(categoryId);

    // Then
    verify(categoryRepositoryMock, times(1)).existsById(categoryId);
  }

  @Test
  void deleteCategoryById_ShouldThrowException_WhenCategoryNotFound() {

    // Given
    Long categoryId = 100L;
    when(categoryRepositoryMock.existsById(categoryId)).thenReturn(false);

    // When & Then
    assertThrows(CategoryNotFoundException.class, () -> cut.deleteCategory(categoryId));
  }

  private Category getRequestedCategory(Long categoryId, String Mens_shirts) {
    Category existingCategory = new Category();
    existingCategory.setId(categoryId);
    existingCategory.setCategoryName(Mens_shirts);
    return existingCategory;
  }
}
