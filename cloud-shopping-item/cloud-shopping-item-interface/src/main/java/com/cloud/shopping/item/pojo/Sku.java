package com.cloud.shopping.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/*
 * The Sku class represents a Stock Keeping Unit (SKU) in a typical e-commerce system.
 * In e-commerce, SKUs are unique identifiers for specific product variations, such as size, color, or model.
 */
@Data
@Table(name = "tb_sku")
public class Sku {
    @Id
    @KeySql(useGeneratedKeys=true)
    private Long id;
    private Long spuId; //spuId (Stock Production Unit ID) links the SKU to its corresponding parent product (SPU),
                        // which represents a general product type (e.g., "Nike Air Max Sneakers").
    // Product details
    private String title;
    private String images;
    private Long price;

    // Specifications & Variants
    private String ownSpec;// Key-value pairs of product specifications, e.g., {"Color": "Red", "Size": "XL"}
    private String indexes;// Indexes for searching and filtering

    // Status and Timestamp
    private Boolean enable;// a flag to indicate whether the SKU is enabled
    private Date createTime;
    private Date lastUpdateTime;

    // This field does not persist in the database but is used to temporarily store stock information when retrieving SKU data.
    @Transient
    private Integer stock;// Stock quantity
}