package com.project.anesu.ecommerce.stockmanagementservice.controller;

public class StockControllerEndpoints {

  public static final String LANDING_PAGE = "/api/stock";

  public static final String CREATE_PRODUCT = "/product";
  public static final String UPDATE_PRODUCT = "/product/{productId}";
  public static final String GET_PRODUCT_BY_ID = "/product/{productId}";
  public static final String GET_ALL_PRODUCTS = "/products";
  public static final String DELETE_PRODUCT = "/product/{productId}";

  public static final String CREATE_CATEGORY = "/category";
  public static final String GET_CATEGORY_BY_ID = "/category/{categoryId}";
  public static final String UPDATE_CATEGORY = "/category/{categoryId}";
  public static final String DELETE_CATEGORY = "/category/{categoryId}";

  public static final String VALIDATE_AND_DEDUCT_PRODUCT_FROM_INVENTORY =
      "/validate-and-deduct-product";
  public static final String ADD_RETURNED_INVENTORY = "/add-returned-inventory";

  private StockControllerEndpoints() {}
}
