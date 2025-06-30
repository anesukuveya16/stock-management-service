package com.project.anesu.ecommerce.stockmanagementservice.integrationTests;

import static com.project.anesu.ecommerce.stockmanagementservice.controller.StockControllerEndpoints.*;
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

    String addProduct = getAddProduct();

    String addedProduct = String.format(addProduct, categoryId);

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(addedProduct)
        .when()
        .post(LANDING_PAGE + CREATE_PRODUCT)
        .then()
        .statusCode(200)
        .body("productName", equalTo("Denim Jacket"))
        .body("price", equalTo(69.99f))
        .body("size", equalTo(36));
  }

  @Test
  void shouldSuccessfullyRetrieveProductByGivenProductId() {

    String addProduct = getAddProduct();

    String addedProduct = String.format(addProduct, categoryId);

    Integer productId =
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(addedProduct)
            .when()
            .post(LANDING_PAGE + CREATE_PRODUCT)
            .then()
            .statusCode(200)
            .body("productName", equalTo("Denim Jacket"))
            .body("price", equalTo(69.99f))
            .body("size", equalTo(36))
            .extract()
            .path("id");

    String retrievedProduct =
        """
              {
                "productName": "Denim Jacket",
                "productDescription": "Blue cropped denim jacket.",
                "price": 69.99,
                "size": 36,
                "colour": "Blue",
                "category": {
                   "id": %d
                    }
              }
              """;

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(retrievedProduct)
        .when()
        .get(LANDING_PAGE + GET_PRODUCT_BY_ID, productId)
        .then()
        .statusCode(200)
        .body("productName", equalTo("Denim Jacket"))
        .body("price", equalTo(69.99f))
        .body("colour", equalTo("Blue"));
  }

  @Test
  void shouldSuccessfullyUpdateProductSuccessfully() {

    String addProduct = getAddProduct();

    String addedProduct = String.format(addProduct, categoryId);

    Integer productId =
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(addedProduct)
            .when()
            .post(LANDING_PAGE + CREATE_PRODUCT)
            .then()
            .statusCode(200)
            .body("productName", equalTo("Denim Jacket"))
            .body("price", equalTo(69.99f))
            .body("size", equalTo(36))
            .extract()
            .path("id");

    String updateExistingProduct =
        """
                      {
                        "productName": "Denim Jacket",
                        "productDescription": "Cropped denim jacket.",
                        "price": 29.99,
                        "size": 36,
                        "colour": "baby blue",
                        "category": {
                           "id": %d
                            }
                      }
                      """;

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(updateExistingProduct)
        .when()
        .put(LANDING_PAGE + UPDATE_PRODUCT, productId)
        .then()
        .statusCode(200)
        .body("productDescription", equalTo("Cropped denim jacket."))
        .body("colour", equalTo("baby blue"));
  }

  @Test
  void shouldDeleteProductFromDatabase() {

    String addProduct = getAddProduct();
    String addedProduct = String.format(addProduct, categoryId);

    Integer productId =
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(addedProduct)
            .when()
            .post(LANDING_PAGE + CREATE_PRODUCT)
            .then()
            .statusCode(200)
            .body("productName", equalTo("Denim Jacket"))
            .body("price", equalTo(69.99f))
            .body("size", equalTo(36))
            .extract()
            .path("id");

    String deleteProduct =
        """
                      {
                        "productName": "Denim Jacket",
                        "productDescription": "Blue cropped denim jacket.",
                        "price": 69.99,
                        "size": 36,
                        "colour": "Blue",
                        "category": {
                           "id": %d
                            }
                      }
                      """;

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(deleteProduct)
        .when()
        .get(LANDING_PAGE + DELETE_PRODUCT, productId)
        .then()
        .statusCode(200);
  }

  @Test
  void shouldRetrieveAllProductsFromDatabase() {

    String addProductRequestBody = getAddProduct();
    String addedProduct = String.format(addProductRequestBody, categoryId);

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(addedProduct)
        .when()
        .post(LANDING_PAGE + CREATE_PRODUCT)
        .then()
        .statusCode(200)
        .body("productName", equalTo("Denim Jacket"))
        .body("price", equalTo(69.99f))
        .body("size", equalTo(36));

    String addedProductTwoRequestBody =
        """
                    {
                      "productName": "Long formal skirt",
                      "productDescription": "Knee length summer skirt.",
                      "price": 9.99,
                      "size": 38,
                      "colour": "Green",
                      "category": {
                         "id": %d
                          }
                    }
                    """;
    String addedProductTwo = String.format(addedProductTwoRequestBody, categoryId);

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(addedProductTwo)
        .when()
        .post(LANDING_PAGE + CREATE_PRODUCT)
        .then()
        .statusCode(200)
        .body("productName", equalTo("Long formal skirt"))
        .body("price", equalTo(9.99f))
        .body("size", equalTo(38));

    RestAssured.given()
        .contentType(ContentType.JSON)
        .when()
        .get(LANDING_PAGE + GET_ALL_PRODUCTS)
        .then()
        .statusCode(200);
  }

  private static String getAddProduct() {
    return """
                            {
                              "productName": "Denim Jacket",
                              "productDescription": "Blue cropped denim jacket.",
                              "price": 69.99,
                              "size": 36,
                              "colour": "Blue",
                              "category": {
                                "id": %d
                              }
          }
          """;
  }
}
