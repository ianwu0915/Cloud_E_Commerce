package com.cloud.shopping.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionEnum {
    BRAND_NOT_FOUND(404, "Brand not found"),
    CATEGORY_NOT_FOUND(404, "Category not found"),
    BRAND_SAVE_ERROR(500, "Failed to add new brand"),
    CATEGORY_BRAND_SAVE_ERROR(500, "Failed to add new brand category"),
    UPLOAD_FILE_ERROR(500, "File upload failed"),
    INVALID_FILE_TYPE(400, "Invalid file type"),
    SPEC_GROUP_NOT_FOUND(404, "Specification group not found"),
    SPEC_PARAM_NOT_FOUND(404, "Specification parameter not found"),
    GOODS_NOT_FOUND(404, "Product not found"),
    GOODS_SAVE_ERROR(500, "Failed to save product"),
    GOODS_DETAIL_NOT_FOUND(404, "Product details not found"),
    GOODS_SKU_NOT_FOUND(404, "Product SKU not found"),
    GOODS_STOCK_NOT_FOUND(404, "Product stock not found"),
    GOODS_UPDATE_ERROR(500, "Failed to update product"),
    GOODS_ID_CANNOT_BE_NULL(400, "Product ID cannot be null"),
    INVALID_USER_DATA_TYPE(400, "Invalid user data type"),
    INVALID_VERIFY_CODE(400, "Invalid verification code"),
    INVALID_USERNAME_PASSWORD(400, "Incorrect username or password"),
    CREATE_TOKEN_ERROR(500, "Failed to generate user token"),
    UN_AUTHORIZED(403, "Unauthorized"),
    CART_NOT_FOUND(404, "Shopping cart not found"),
    STOCK_NOT_FOUND(404, "Stock not found"),
    RECEIVER_ADDRESS_NOT_FOUND(404, "Recipient address not found"),
    CREATED_ORDER_ERROR(500, "Failed to create order"),
    STOCK_NOT_ENOUGH(500, "Insufficient stock"),
    ORDER_NOT_FOUND(404, "Order not found"),
    ORDER_DETAIL_NOT_FOUND(404, "Order detail not found"),
    ORDER_STATUS_NOT_FOUND(404, "Order status not found"),
    WX_PAY_ORDER_FAIL(500, "WeChat payment order failed"),
    ORDER_STATUS_ERROR(400, "Incorrect order status"),
    INVALID_SIGN_ERROR(400, "Invalid signature"),
    INVALID_ORDER_PARAM(400, "Invalid order amount parameter"),
    UPDATE_ORDER_STATUS_ERROR(500, "Failed to update order status"),
    IS_NOT_AN_ADMIN(500, "User is not an administrator");

    private int code;
    private String msg;
}
