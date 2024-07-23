package com.cloud.shopping.item.api;

import com.cloud.shopping.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@RequestMapping("category")
public interface CategoryApi {
    @GetMapping("list/ids")
    List<Category> queryCategoryByIds(@RequestParam("ids") List<Long> ids);
    @GetMapping("all/level")
    List<Category> queryAllByCategoryId3(@RequestParam("id") Long id);
}
