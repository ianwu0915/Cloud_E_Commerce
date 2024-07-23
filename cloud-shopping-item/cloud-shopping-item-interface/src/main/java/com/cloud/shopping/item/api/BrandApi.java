package com.cloud.shopping.item.api;

import com.cloud.shopping.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BrandApi {
    /**
     * Search for brand by id
     * @param id
     * @return
     */
    @GetMapping("brand/{id}")
    Brand queryBrandById(@PathVariable("id") Long id);

    @GetMapping("brand/brandsList")
    List<Brand> queryBrandByIds(@RequestParam("ids")List<Long> ids);
;}
