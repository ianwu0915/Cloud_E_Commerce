package com.cloud.shopping.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to store the shopping cart information
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long skuId;
    private Integer num; // Number of items in the cart
}
