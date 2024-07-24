package com.cloud.shopping.item.service;

import com.cloud.shopping.common.enums.ExceptionEnum;
import com.cloud.shopping.common.exception.LyException;
import com.cloud.shopping.common.vo.PageResult;
import com.cloud.shopping.item.pojo.Brand;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cloud.shopping.item.mapper.BrandMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    /**
     * Query brands with pagination and filtering
     *
     * @param page Current page number
     * @param rows Items per page
     * @param sortBy Field to sort by
     * @param desc Sort direction (true for descending)
     * @param search Search keyword for brand name or letter
     * @return PageResult containing matching brands ( One page _
     * @throws LyException if no brands found
     */
    public PageResult<Brand> queryBrandByPage(
            Integer page, Integer rows, String sortBy, Boolean desc, String search) {
        // Initialize pagination
        // Mybatis helper
        PageHelper.startPage(page, rows);

        // Build query criteria
        /**
         * WHERE 'name' LIKE "%x%" OR letter == 'x'
         * ORDER BY id DESC
         * */
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(search)) {
            example.createCriteria()
                    .orLike("name", "%" + search + "%")
                    .orEqualTo("letter", search.toUpperCase());
        }

        // Add sorting if specified
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }

        // Execute query
        List<Brand> brands = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(brands)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        // Create pagination result
        PageInfo<Brand> info = new PageInfo<>(brands);
        return new PageResult<>(info.getTotal(), brands);
    }

    /**
     * Create a new brand
     * @param brand
     * @param cids
     */
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        brand.setId(null);
        int count = this.brandMapper.insert(brand);
        if(count!=1){
           throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
        }

        // Save categories association
        for (Long cid : cids) {
            count = this.brandMapper.insertCategoryBrand(cid, brand.getId());
            if(count!=1){
                throw new LyException(ExceptionEnum.CATEGORY_BRAND_SAVE_ERROR);
            }
        }
    }

    /**
     * Update brand and its category associations
     *
     * @param brand Updated brand information
     * @param categories New category associations
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateBrand(Brand brand,List<Long> categories) {

        // Remove existing category associations
        deleteByBrandIdInCategoryBrand(brand.getId());

        // Update brand information
        this.brandMapper.updateByPrimaryKeySelective(brand);

        // Create new category association
        for (Long cid : categories) {
            this.brandMapper.insertCategoryBrand(cid, brand.getId());
        }
    }

    /**
     * Query brand by ID
     *
     * @param id Brand ID
     * @return Brand information
     * @throws LyException if brand not found
     */
    public Brand queryById(Long id){
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if(brand == null){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    /**
     * Query brands by category ID
     *
     * @param cid Category ID
     * @return List of brands in the category
     * @throws LyException if no brands found
     */
    public List<Brand> queryBrandByCid(Long cid) {
        List<Brand> list = brandMapper.queryByCategoryId(cid);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return list;
    }

    /**
     * Query multiple brands by IDs
     *
     * @param ids List of brand IDs
     * @return List of matching brands
     * @throws LyException if no brands found
     */
    public List<Brand> queryBrandsByIds(List<Long> ids) {
        List<Brand> brands = brandMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(brands)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brands;
    }

    /**
     * Delete brand and its category associations
     *
     * @param id Brand ID to delete
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBrand(Long id) {
        // Delete brand
        brandMapper.deleteByPrimaryKey(id);

        // Delete Category Associations
        brandMapper.deleteByBrandIdInCategoryBrand(id);
    }

    /**
     * Delete brand's category associations
     *
     * @param bid Brand ID
     */
    public void deleteByBrandIdInCategoryBrand(Long bid) {
        brandMapper.deleteByBrandIdInCategoryBrand(bid);
    }
}
