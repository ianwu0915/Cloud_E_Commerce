package com.cloud.shopping.item.mapper;

import com.cloud.shopping.item.pojo.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Category Data Access Layer
 * Handles database operations for product categories including:
 * - Basic CRUD operations (inherited from Mapper)
 * - Batch operations with IDs (inherited from IdListMapper)
 * - Custom category queries and brand associations
 */
@org.apache.ibatis.annotations.Mapper
public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category,Long> {

    /**
     * Query categories associated with a specific brand
     * Uses JOIN to retrieve categories linked through the category-brand junction table
     *
     * @param bid Brand ID to find categories for
     * @return List of categories associated with the brand
     */
    @Select("SELECT * FROM tb_category WHERE id IN " +
            "(SELECT category_id FROM tb_category_brand WHERE brand_id = #{bid})")
    List<Category> queryByBrandId(@Param("bid") Long bid);

    /**
     * Delete category associations from the category-brand junction table
     * Used when deleting a category to maintain referential integrity
     *
     * @param cid Category ID to remove from brand associations
     */
    @Delete("DELETE FROM tb_category_brand WHERE category_id = #{cid}")
    void deleteByCategoryIdInCategoryBrand(@Param("cid") Long cid);

    /**
     * Query the last category in the database
     * Used for special operations requiring the most recently added category
     *
     * @return List containing the last category (typically single element)
     */
    @Select("SELECT * FROM `tb_category` WHERE id = (SELECT MAX(id) FROM tb_category)")
    List<Category> selectLast();
}
