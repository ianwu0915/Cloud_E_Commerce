package com.cloud.shopping.item.controller;

import com.cloud.shopping.common.vo.PageResult;
import com.cloud.shopping.item.mapper.BrandMapper;
import com.cloud.shopping.item.pojo.Brand;
import com.cloud.shopping.item.service.BrandService;
import com.cloud.shopping.item.service.CategoryService;
import org.springframework.http.HttpStatus;
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

    /**
     * Query brands with pagination
     * @param page Current page number
     * @param rows Items per page
     * @param sortBy Sort field
     * @param desc Sort direction (true for descending)
     * @param search Search keyword
     * @return Paginated brand results
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "search", required = false) String search)
    {
        return ResponseEntity.ok(brandService.queryBrandByPage(page,rows,sortBy,desc, search));
    }

    /**
     * Create a new brand
     * @param brand Brand information
     * @param cids List of category IDs to associate with the brand
     * @return Empty response with CREATED status
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        this.brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Update brand information
     * @param brand Updated brand information
     * @param categories List of category IDs to associate with the brand
     * @return Empty response with ACCEPTED status
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand,@RequestParam("categories") List<Long> categories){
        this.brandService.updateBrand(brand,categories);
        return  ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * Query brands by category ID
     * @param cid Category ID
     * @return List of brands in the specified category
     */
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid) {
        return ResponseEntity.ok(brandService.queryBrandByCid(cid));
    }

    /**
     * Query brand by ID
     * @param id Brand ID
     * @return Brand information
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
        return ResponseEntity.ok(brandService.queryById(id));
    }

    /**
     * Query multiple brands by IDs
     * @param ids List of brand IDs
     * @return List of brand information
     */
    @GetMapping("brandsList")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(brandService.queryBrandsByIds(ids));
    }

    /**
     * Delete brand(s) from tb_brand table
     * Supports both single and batch deletion
     * @param bid Single ID or hyphen-separated IDs (e.g., "1" or "1-2-3")
     * @return Empty response with OK status
     */
    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deleteBrandById(@PathVariable("bid") String bid){
        String separator="-";
        if(bid.contains(separator)){
            String[] ids=bid.split(separator);
            for (String id:ids){
                this.brandService.deleteBrand(Long.parseLong(id));
            }
        }
        else {
            this.brandService.deleteBrand(Long.parseLong(bid));
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Delete brand association from tb_category_brand table
     * @param bid Brand ID to remove from category associations
     * @return Empty response with OK status
     */
    @DeleteMapping("cid_bid/{bid}")
    public ResponseEntity<Void> deleteByBrandIdInCategoryBrand(@PathVariable("bid") Long bid){
        brandService.deleteByBrandIdInCategoryBrand(bid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
