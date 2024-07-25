package com.cloud.shopping.item.controller;

import com.cloud.shopping.item.pojo.Category;
import com.cloud.shopping.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Category Management Controller
 * Handles product category operations in the e-commerce system
 */
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Query categories by parent ID
     * Special case: when pid = -1, returns the last category in the database
     *
     * @param pid Parent category ID
     * @return List of child categories or last category for pid=-1
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryByParentId(@RequestParam(value = "pid") Long pid) {
        if (pid == -1) {
            List<Category> last = this.categoryService.queryLast();
            return ResponseEntity.ok(last);
        } else {
            List<Category> list = this.categoryService.queryCategoryListByPid(pid);
            if (list == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(list);
        }
    }

    /**
     * Query multiple categories by their IDs
     *
     * @param ids List of category IDs
     * @return List of matching categories
     */
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(categoryService.queryByIds(ids));
    }

    /**
     * Query complete category path from level 1 to 3 by level 3 category ID
     * Used for breadcrumb navigation and category path display
     *
     * @param id Level 3 category ID
     * @return List of categories from root to leaf
     */
    @GetMapping("all/level")
    public ResponseEntity<List<Category>> queryAllByCid3(@RequestParam("id") Long id) {
        return ResponseEntity.ok(categoryService.queryAllByCid3(id));
    }

    /**
     * Query categories associated with a specific brand
     * Used for displaying category information in brand management
     *
     * @param bid Brand ID
     * @return List of categories associated with the brand
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid) {
        List<Category> list = categoryService.queryByBrandId(bid);
        if (list == null || list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(list);
    }

    /**
     * Create a new category
     *
     * @param category Category information to create
     * @return Empty response with CREATED status
     */
    @PostMapping
    public ResponseEntity<Void> saveCategory(Category category) {
        this.categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Delete a category
     *
     * @param id Category ID to delete
     * @return Empty response with OK status
     */
    @DeleteMapping("cid/{cid}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("cid") Long id) {
        this.categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Update category information
     *
     * @param category Updated category information
     * @return Empty response with ACCEPTED status
     */
    @PutMapping
    public ResponseEntity<Void> updateCategory(Category category) {
        this.categoryService.updateCategory(category);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
