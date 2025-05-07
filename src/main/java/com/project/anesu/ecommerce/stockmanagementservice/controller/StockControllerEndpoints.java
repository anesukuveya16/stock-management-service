package com.project.anesu.ecommerce.stockmanagementservice.controller;

public class StockControllerEndpoints {

  // product
  public static final String CREATE_PRODUCT = "/product";
  public static final String UPDATE_PRODUCT = "/product/{id}";
  public static final String GET_PRODUCT_BY_ID = "/product/{id}";
  public static final String DELETE_PRODUCT = "/product/{id}";

  // category
  public static final String CREATE_CATEGORY = "/category";
  public static final String GET_CATEGORY_BY_ID = "/category/{id}";
  public static final String DELETE_CATEGORY = "/category/{id}";

  private StockControllerEndpoints() {
    // Prevent instantiation
  }
}
