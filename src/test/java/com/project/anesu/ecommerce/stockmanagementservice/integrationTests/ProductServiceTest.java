package com.project.anesu.ecommerce.stockmanagementservice.integrationTests;

import static com.project.anesu.ecommerce.stockmanagementservice.controller.StockControllerEndpoints.*;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Category;
import com.project.anesu.ecommerce.stockmanagementservice.entity.Inventory;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.CategoryRepository;
import com.project.anesu.ecommerce.stockmanagementservice.model.repository.InventoryRepository;
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
  @Autowired private InventoryRepository inventoryRepository;

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

    String addedProduct = format(addProduct, categoryId);

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

    String addedProduct = format(addProduct, categoryId);

    Long productId =
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
            .jsonPath()
            .getLong("id");

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
  void shouldRetrieveAllProductsFromDatabase() {

    String addProductRequestBody = getAddProduct();
    String addedProduct = format(addProductRequestBody, categoryId);

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
    String addedProductTwo = format(addedProductTwoRequestBody, categoryId);

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

  @Test
  void shouldSuccessfullyUpdateProductSuccessfully() {

    String addProduct = getAddProduct();

    String addedProduct = format(addProduct, categoryId);

    Long productId =
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
            .jsonPath()
            .getLong("id");

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

    String updateExistingProductBodyFinal = format(updateExistingProduct, categoryId);

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(updateExistingProductBodyFinal)
        .when()
        .put(LANDING_PAGE + UPDATE_PRODUCT, productId)
        .then()
        .statusCode(200)
        .log()
        .all()
        .body("productDescription", equalTo("Cropped denim jacket."))
        .body("colour", equalTo("baby blue"));
  }

  @Test
  void shouldDeleteProductFromDatabase() {

    String addProduct = getAddProduct();
    String addedProduct = format(addProduct, categoryId);

    Long productId =
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
            .jsonPath()
            .getLong("id");

    RestAssured.given()
        .contentType(ContentType.JSON)
        .when()
        .get(LANDING_PAGE + DELETE_PRODUCT, productId)
        .then()
        .statusCode(200);
  }

  @Test
  void shouldSuccessfullyValidateAndDeductInventory() {

    String addProduct = getAddProduct();
    String addedProduct = format(addProduct, categoryId);

    Long productId =
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
            .jsonPath()
            .getLong("id");

    String getRequestedInventoryRequestBody = getOrderItemInventoryInformation(productId, 50);

    int initialRequestedQuantity = 50;
    Inventory inventory = new Inventory();
    inventory.setProductId(productId);
    inventory.setAvailableQuantity(initialRequestedQuantity);
    inventoryRepository.save(inventory);

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(getRequestedInventoryRequestBody)
        .log()
        .all()
        .when()
        .post(LANDING_PAGE + VALIDATE_AND_DEDUCT_PRODUCT_FROM_INVENTORY)
        .then()
        .statusCode(200);
  }

  @Test
  void validateAndDeductProduct_ShouldThrowExceptionWhenProductIdIsNotFoundInInventoryDatabase() {

    Long productId = 50L;

    String getRequestedInventoryRequestBody = getOrderItemInventoryInformation(productId, 2);

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(getRequestedInventoryRequestBody)
        .when()
        .post(LANDING_PAGE + VALIDATE_AND_DEDUCT_PRODUCT_FROM_INVENTORY)
        .then()
        .statusCode(404);
  }

  @Test
  void
      validateAndDeductInventory_ShouldThrowExceptionWhenThereIsInsufficientInventory_ToFulfillSuccessfulOrderCreation() {

    String addProduct = getAddProduct();
    String addedProduct = format(addProduct, categoryId);

    Long productId =
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
            .jsonPath()
            .getLong("id");

    String getRequestedInventoryRequestBody = getOrderItemInventoryInformation(productId, 6);

    int currentInventoryQuantity = 5;

    Inventory inventory = new Inventory();
    inventory.setProductId(productId);
    inventory.setAvailableQuantity(currentInventoryQuantity);
    inventoryRepository.save(inventory);

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(getRequestedInventoryRequestBody)
        .when()
        .post(LANDING_PAGE + VALIDATE_AND_DEDUCT_PRODUCT_FROM_INVENTORY)
        .then()
        .statusCode(400);
  }

  @Test
  void addReturnedInventory_ShouldSuccessfullyAddReturnedOrderItems_BackToInventoryDatabase() {

    String addProduct = getAddProduct();
    String addedProduct = format(addProduct, categoryId);

    Long productId =
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
            .jsonPath()
            .getLong("id");

    String getReturnedInventoryRequestBody = getOrderItemInventoryInformation(productId, 4);

    int initialReturnedQuantity = 4;
    Inventory inventory = new Inventory();
    inventory.setProductId(productId);
    inventory.setAvailableQuantity(initialReturnedQuantity);
    inventoryRepository.save(inventory);

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(getReturnedInventoryRequestBody)
        .when()
        .post(LANDING_PAGE + ADD_RETURNED_INVENTORY)
        .then()
        .statusCode(200);
  }

  @Test
  void addReturnedInventory_ShouldThrowAnExceptionWhenAnErrorHasOccurredInTheReturnProcess() {

    Long productId = 500L;

    String returnInventory = getOrderItemInventoryInformation(productId, 4);

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(returnInventory)
        .when()
        .post(LANDING_PAGE + ADD_RETURNED_INVENTORY)
        .then()
        .statusCode(400);
  }

  private String getOrderItemInventoryInformation(Long productId, int requestedQuantity) {
    return String.format(
        """
             [
            {
              "productId": %d,
              "requestedQuantity": %d
               }
             ]
           """,
        productId, requestedQuantity);
  }

  private String getAddProduct() {
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
