package com.cloud.shopping.cart.pojo;

import lombok.Data;

@Data
public class CartItem {
    private Long skuId;// product id
    private String title;
    private String image;
    private Long price;// price when added to cart
    private Integer num; // number of items
    private String ownSpec;// product specification

}