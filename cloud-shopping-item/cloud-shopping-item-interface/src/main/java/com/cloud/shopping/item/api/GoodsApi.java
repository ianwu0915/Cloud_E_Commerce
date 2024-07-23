package com.cloud.shopping.item.api;

import com.cloud.shopping.common.dto.CartDTO;
import com.cloud.shopping.common.vo.PageResult;
import com.cloud.shopping.item.pojo.Spu;
import com.cloud.shopping.item.pojo.SpuDetail;
import com.cloud.shopping.item.pojo.Sku;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Goods Management API Interface
 * Provides endpoints for managing products (SPUs and SKUs) in the e-commerce system.
 * This interface is designed to be implemented by the service layer and consumed by other microservices.
 */
public interface GoodsApi {

    /**
     * Retrieve detailed information for a specific SPU
     * @param spuId The unique identifier of the SPU
     * @return SpuDetail containing extended product information
     */
    @GetMapping("/spu/detail/{id}")
    SpuDetail querySpuDetailByspuId(@PathVariable("id") Long spuId);

    /**
     * Retrieve all SKUs associated with a specific SPU
     * @param spuId The unique identifier of the parent SPU
     * @return List of SKUs belonging to the specified SPU
     */
    @GetMapping("sku/list")
    List<Sku> queryAllSkuBySpuId(@RequestParam("id") Long spuId);

    /**
     * Retrieve a paginated list of SPUs with optional filtering
     * @param page Current page number (defaults to 1)
     * @param rows Items per page (defaults to 5)
     * @param saleable Filter by whether item is saleable (optional)
     * @param key Search keyword for filtering results (optional)
     * @return PageResult containing list of SPUs and total count
     */
    @GetMapping("/spu/page")
    PageResult<Spu> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key);

    /**
     * Retrieve a specific SPU by its ID
     * @param id The unique identifier of the SPU
     * @return The requested SPU entity
     */
    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);

    /**
     * Retrieve a specific SKU by its ID
     * @param id The unique identifier of the SKU
     * @return ResponseEntity containing the requested SKU
     */
    @GetMapping("/sku/{id}")
    ResponseEntity<Sku> querySkuById(@PathVariable("id") Long id);

    /**
     * Retrieve multiple SKUs by their IDs
     * @param ids List of SKU identifiers to retrieve
     * @return List of matching SKUs
     */
    @GetMapping("sku/list/ids")
    List<Sku> querySkuByIds(@RequestParam("ids") List<Long> ids);

    /**
     * Decrease stock levels for items in shopping cart
     * Used during order processing to update inventory
     * @param carts List of CartDTO objects containing SKU IDs and quantities
     * @return Void response indicating success
     */
    @PostMapping("stock/decrease")
    Void decreaseStock(@RequestBody List<CartDTO> carts);
}
