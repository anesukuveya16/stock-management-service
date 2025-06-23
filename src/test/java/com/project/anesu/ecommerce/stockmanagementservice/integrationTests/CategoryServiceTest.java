package com.project.anesu.ecommerce.stockmanagementservice.integrationTests;

import static com.project.anesu.ecommerce.stockmanagementservice.controller.StockControllerEndpoints.*;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryServiceTest {

  @LocalServerPort private int port;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  @Test
  void shouldSuccessfullyCreateNewCategory() {

    String categoryRequestBody =
        """
            {
                "categoryName": "Tops",
                "products": []
            }
        """;

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(categoryRequestBody)
        .when()
        .post(LANDING_PAGE + CREATE_CATEGORY)
        .then()
        .statusCode(200)
        .body("products.size()", equalTo(0));
  }

  @Test
  void shouldSuccessfullyRetrieveCategoryByGivenId() {

    String categoryRequestBody =
        """
                {
                    "categoryName": "Denim Jeans",
                    "products": []
                }
            """;

    Integer categoryId =
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(categoryRequestBody)
            .when()
            .post(LANDING_PAGE + CREATE_CATEGORY)
            .then()
            .statusCode(200)
            .body("products.size()", equalTo(0))
            .extract()
            .path("id");

    String retrieveCategory =
        """
                {
                    "categoryName": "Denim Jeans",
                    "products": []
                }
            """;

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(retrieveCategory)
        .when()
        .get(LANDING_PAGE + GET_CATEGORY_BY_ID, categoryId)
        .then()
        .statusCode(200);
  }

  @Test
  void shouldDeleteCategoryFromTHeDatabase() {

    String categoryRequestBody =
        """
                {
                    "categoryName": "Tops",
                    "products": []
                }
            """;

    Integer categoryId =
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(categoryRequestBody)
            .when()
            .post(LANDING_PAGE + CREATE_CATEGORY)
            .then()
            .statusCode(200)
            .body("products.size()", equalTo(0))
            .extract()
            .path("id");

    String deleteCategory =
        """
                    {
                        "categoryName": "Denim Jeans",
                        "products": []
                    }
                """;

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(deleteCategory)
        .when()
        .delete(LANDING_PAGE + DELETE_CATEGORY, categoryId)
        .then()
        .statusCode(200);
  }
}
