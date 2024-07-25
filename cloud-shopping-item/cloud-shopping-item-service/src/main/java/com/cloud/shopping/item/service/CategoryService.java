package com.cloud.shopping.item.service;

import com.cloud.shopping.common.enums.ExceptionEnum;
import com.cloud.shopping.common.exception.LyException;
import com.cloud.shopping.item.pojo.Category;
import com.cloud.shopping.item.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Category Management Service
 * Handles business logic for product categories
 *
 * Note: This is currently implemented as a concrete class without an interface.
 * Could be improved by extracting an interface following SOLID principles.
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * Query child categories by parent ID
     *
     * @param pid Parent category ID
     * @return List of child categories
     * @throws LyException if no categories found
     */
    public List<Category> queryCategoryListByPid(Long pid) {
        // Create query criteria using non-null properties
        Category t = new Category();
        t.setParentId(pid);
        List<Category> list = categoryMapper.select(t);

        // Validate results
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }

    /**
     * Query multiple categories by their IDs
     *
     * @param ids List of category IDs to query
     * @return List of matching categories
     * @throws LyException if no categories found
     */
    public List<Category> queryByIds(List<Long> ids) {
        List<Category> list = categoryMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException((ExceptionEnum.CATEGORY_NOT_FOUND));
        }
        return list;
    }

    /**
     * Query complete category path from level 3 to level 1
     * Returns categories in order: [level1, level2, level3]
     *
     * @param id Level 3 category ID
     * @return Ordered list of categories from root to leaf
     */
    public List<Category> queryAllByCid3(Long id) {
        Category c3 = this.categoryMapper.selectByPrimaryKey(id);
        Category c2 = this.categoryMapper.selectByPrimaryKey(c3.getParentId());
        Category c1 = this.categoryMapper.selectByPrimaryKey(c2.getParentId());
        return Arrays.asList(c1,c2,c3);
    }

    /**
     * Query categories associated with a specific brand
     *
     * @param bid Brand ID
     * @return List of categories associated with the brand
     */
    public List<Category> queryByBrandId(Long bid) {
        return this.categoryMapper.queryByBrandId(bid);
    }

    /**
     * Create a new category
     * Also updates parent category's isParent status
     *
     * @param category Category information to save
     */
    public void saveCategory(Category category) {
        // Insert new category
        category.setId(null);
        this.categoryMapper.insert(category);

        // Update parent category status
        Category parent = new Category();
        parent.setId(category.getParentId());
        parent.setIsParent(true);
        this.categoryMapper.updateByPrimaryKeySelective(parent);
    }

    /**
     * Delete a category and manage related data
     * Handles both leaf nodes and parent nodes differently
     *
     * @param id Category ID to delete
     */
    public void deleteCategory(Long id) {
        Category category = this.categoryMapper.selectByPrimaryKey(id);
        if(category.getIsParent()){
            // Handle parent category deletion
            List<Category> leafNodes = new ArrayList<>();
            List<Category> allNodes = new ArrayList<>();

            // Collect all affected categories
            queryAllLeafNode(category, leafNodes);
            queryAllNode(category, allNodes);

            // Delete categories and their brand associations
            for (Category c : allNodes){
                this.categoryMapper.delete(c);
            }
            for (Category c : leafNodes){
                this.categoryMapper.deleteByCategoryIdInCategoryBrand(c.getId());
            }
        } else {
            // Handle leaf category deletion
            Example example = new Example(Category.class);
            example.createCriteria().andEqualTo("parentId", category.getParentId());
            List<Category> siblings = this.categoryMapper.selectByExample(example);

            if(siblings.size() != 1){
                // Has siblings - simple deletion
                this.categoryMapper.deleteByPrimaryKey(category.getId());
            } else {
                // Last child - update parent status
                this.categoryMapper.deleteByPrimaryKey(category.getId());
                Category parent = new Category();
                parent.setId(category.getParentId());
                parent.setIsParent(false);
                this.categoryMapper.updateByPrimaryKeySelective(parent);
            }
            // Clean up brand associations
            this.categoryMapper.deleteByCategoryIdInCategoryBrand(category.getId());
        }
    }

    /**
     * Recursively collect all leaf nodes under a category
     * Used for maintaining category-brand associations
     *
     * @param category Starting category node
     * @param leafNode Collection to store found leaf nodes
     */
    private void queryAllLeafNode(Category category, List<Category> leafNode){
        if(!category.getIsParent()){
            leafNode.add(category);
            return;
        }
        Example example = new Example(Category.class);
        example.createCriteria().andEqualTo("parentId", category.getId());
        List<Category> list = this.categoryMapper.selectByExample(example);

        for (Category child : list){
            queryAllLeafNode(child, leafNode);
        }
    }

    /**
     * Recursively collect all nodes under a category
     * Used for complete category tree deletion
     *
     * @param category Starting category node
     * @param node Collection to store all found nodes
     */
    private void queryAllNode(Category category, List<Category> node){
        node.add(category);
        Example example = new Example(Category.class);
        example.createCriteria().andEqualTo("parentId", category.getId());
        List<Category> children = this.categoryMapper.selectByExample(example);

        for (Category child : children){
            queryAllNode(child, node);
        }
    }

    /**
     * Update category information
     *
     * @param category Updated category information
     */
    public void updateCategory(Category category) {
        this.categoryMapper.updateByPrimaryKeySelective(category);
    }

    /**
     * Query the last category in the database
     *
     * @return List containing the last category
     */
    public List<Category> queryLast() {
        return this.categoryMapper.selectLast();
    }
}
