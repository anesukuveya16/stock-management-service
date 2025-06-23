package com.project.anesu.ecommerce.stockmanagementservice.integrationTests;

import static com.project.anesu.ecommerce.stockmanagementservice.controller.StockControllerEndpoints.CREATE_PRODUCT;
import static com.project.anesu.ecommerce.stockmanagementservice.controller.StockControllerEndpoints.LANDING_PAGE;
import static org.hamcrest.Matchers.equalTo;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Category;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.CategoryRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceTest {

  @LocalServerPort private int port;

  @Autowired private CategoryRepository productCategoryRepository;

  private Long categoryId;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;

    Category category = new Category();
    category.setCategoryName("Ladies skirts");

    Category savedCategory = productCategoryRepository.save(category);
    categoryId = savedCategory.getId();
  }

  @Test
  void shouldAddNewProductSuccessfully() {

    String addProduct =
            """
                            {
                              "productName": "Denim Jacket",
                              "productDescription": "Blue cropped denim jacket.",
                              "price": 69.99,
                              "size": 36,
                              "colour": "Blue",
                              "category": {
                                "id": %
                              }
          }
          """;

    String finalProduct = String.format(addProduct, categoryId);

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(finalProduct)
        .when()
        .post(LANDING_PAGE + CREATE_PRODUCT)
        .then()
        .statusCode(200)
        .body("productName", equalTo("Denim Jacket"));
  }
}
