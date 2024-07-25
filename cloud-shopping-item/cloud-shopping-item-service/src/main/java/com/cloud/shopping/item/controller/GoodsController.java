package com.cloud.shopping.item.controller;

import com.cloud.shopping.common.dto.CartDTO;
import com.cloud.shopping.common.vo.PageResult;
import com.cloud.shopping.item.pojo.Sku;
import com.cloud.shopping.item.pojo.Spu;
import com.cloud.shopping.item.pojo.SpuDetail;
import com.cloud.shopping.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Goods Management Controller
 * Handles all product-related operations including SPUs and SKUs
 */
@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * Query SPUs with pagination and filtering
     * @param page Current page number, defaults to 1
     * @param rows Items per page, defaults to 5
     * @param saleable Filter by product availability
     * @param key Search keyword
     * @return Paginated list of SPUs
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key) {
        return ResponseEntity.ok(goodsService.querySpuByPage(page, rows, saleable, key));
    }

    /**
     * Create a new product (SPU and associated SKUs)
     * @param spu Product information including SKUs
     * @return Empty response with CREATED status
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu) {
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Update existing product information
     * @param spu Updated product information
     * @return Empty response with NO_CONTENT status
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu) {
        goodsService.updateGoods(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Delete one or multiple products
     * @param ids Single ID or hyphen-separated IDs (e.g., "1" or "1-2-3")
     * @return Empty response with OK status
     */
    @DeleteMapping("goods/spu/{id}")
    public ResponseEntity<Void> deleteGoods(@PathVariable("id") String ids) {
        String separator="-";
        if (ids.contains(separator)){
            String[] goodsId = ids.split(separator);
            for (String id:goodsId){
                this.goodsService.deleteGoods(Long.parseLong(id));
            }
        }
        else {
            this.goodsService.deleteGoods(Long.parseLong(ids));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Query detailed product information by SPU ID
     * @param spuId SPU identifier
     * @return Detailed product information
     */
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> queryDetailById(@PathVariable("id") Long spuId) {
        return ResponseEntity.ok(goodsService.queryDetailById(spuId));
    }

    /**
     * Query all SKUs for a specific SPU
     * @param spuId SPU identifier
     * @return List of SKUs belonging to the SPU
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long spuId) {
        return ResponseEntity.ok(goodsService.querySkuBySpuId(spuId));
    }

    /**
     * Query multiple SKUs by their IDs
     * @param ids List of SKU identifiers
     * @return List of matching SKUs
     */
    @GetMapping("sku/list/ids")
    public ResponseEntity<List<Sku>> querySkuByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(goodsService.querySkuByIds(ids));
    }

    /**
     * Query SPU by its ID
     * @param id SPU identifier
     * @return SPU information
     */
    @GetMapping("spu/{id}")
    ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(goodsService.querySpuById(id));
    }

    /**
     * Query SKU by its ID
     * @param id SKU identifier
     * @return SKU information or NOT_FOUND if not exists
     */
    @GetMapping("/sku/{id}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("id") Long id) {
        Sku sku = this.goodsService.querySkuById(id);
        if (sku == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(sku);
    }

    /**
     * Decrease stock levels for items in shopping cart
     * @param carts List of cart items containing SKU IDs and quantities
     * @return Empty response with NO_CONTENT status
     */
    @PostMapping("stock/decrease")
    public ResponseEntity<Void> decreaseStock(@RequestBody List<CartDTO> carts) {
        goodsService.decreaseStock(carts);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Toggle product availability status (on/off shelf)
     * @param id SPU identifier
     * @return Empty response with OK status
     */
    @PutMapping("goods/spu/out/{id}")
    public ResponseEntity<Void> goodsSoldOut(@PathVariable("id") Long id) {
        this.goodsService.goodsSoldOut(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}