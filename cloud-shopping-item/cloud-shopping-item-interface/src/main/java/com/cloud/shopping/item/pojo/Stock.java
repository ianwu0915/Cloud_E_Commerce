package com.cloud.shopping.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_stock")
public class Stock {
    @Id
    private Long skuId;
    private Integer seckillStock;// the number of items that can be purchased in a seckill
    private Integer seckillTotal;// the number of items that have been purchased in a seckill
    private Integer stock;// the number of items in stock
}