package com.cloud.shopping.item.controller;

import com.cloud.shopping.common.vo.PageResult;
import com.cloud.shopping.item.mapper.BrandMapper;
import com.cloud.shopping.item.pojo.Brand;
import com.cloud.shopping.item.service.BrandService;
import com.cloud.shopping.item.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    private BrandService brandService;
    private CategoryService categoryService;

    public BrandController(BrandService brandService, CategoryService categoryService) {
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>>  queryBrandByPage() {

    }

    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids ) {

    }

    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("categories") List<Long> categories) {

    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid) {

    }

    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id) {
    }

    @GetMapping("brandsList")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids")List<Long> ids){

    }

    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deleteBrandById(@PathVariable("bid") String bid) {

    }

    @DeleteMapping("cid_bid/{bid")
    public ResponseEntity<Void> deleteByBrandIdInCategoryBrand(@PathVariable("bid") Long bid) {

    }
}