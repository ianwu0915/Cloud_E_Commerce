package com.cloud.shopping.item.mapper;

import com.cloud.shopping.common.mapper.BaseMapper;
import com.cloud.shopping.item.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@org.apache.ibatis.annotations.Mapper
public interface StockMapper extends BaseMapper<Stock> {
    @Update("update tb_stock set stock = stock - #{num} where sku_id = #{skuId} and stock >= #{num}")
    int decreaseStock(@Param("skuId") Long skuId, @Param("num") Integer num);
}
