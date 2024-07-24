package com.cloud.shopping.item.mapper;

import com.cloud.shopping.common.mapper.BaseMapper;
import com.cloud.shopping.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Brand Data Access Layer
 * Handles database operations for brand management including:
 * - Basic CRUD operations (inherited from BaseMapper)
 * - Category-Brand relationship management
 * - Custom brand queries
 */
@org.apache.ibatis.annotations.Mapper
public interface BrandMapper extends BaseMapper<Brand> {

    /**
     * Create a new association between a category and a brand
     * Inserts a record into the category-brand junction table
     *
     * @param cid Category ID to associate with the brand
     * @param bid Brand ID to associate with the category
     * @return Number of rows affected (should be 1 for successful insertion)
     */
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    /**
     * Query all brands associated with a specific category
     * Uses LEFT JOIN to handle cases where a category might have no brands
     *
     * @param cid Category ID to search for
     * @return List of brands associated with the specified category
     */
    @Select("SELECT b.* FROM tb_brand b " +
            "LEFT JOIN tb_category_brand cb ON b.id=cb.brand_id " +
            "WHERE cb.category_id=#{cid}")
    List<Brand> queryByCategoryId(@Param("cid") Long cid);

    /**
     * Delete all category associations for a specific brand
     * Removes all entries from the category-brand junction table for the given brand
     *
     * @param bid Brand ID whose category associations should be removed
     */
    @Delete("DELETE FROM tb_category_brand WHERE brand_id = #{bid}")
    void deleteByBrandIdInCategoryBrand(@Param("bid") Long bid);
}
