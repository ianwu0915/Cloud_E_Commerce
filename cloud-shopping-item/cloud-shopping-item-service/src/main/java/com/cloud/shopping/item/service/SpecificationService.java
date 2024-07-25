package com.cloud.shopping.item.service;

import com.cloud.shopping.common.enums.ExceptionEnum;
import com.cloud.shopping.common.exception.LyException;
import com.cloud.shopping.item.pojo.SpecGroup;
import com.cloud.shopping.item.pojo.SpecParam;
import com.cloud.shopping.item.pojo.Specification;
import com.cloud.shopping.item.mapper.SpecGroupMapper;
import com.cloud.shopping.item.mapper.SpecParamMapper;
import com.cloud.shopping.item.mapper.SpecificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Product Specification Management Service
 * Handles business logic for product specifications, groups, and parameters
 *
 * Note: This is currently implemented as a concrete class without an interface.
 * Could be improved by extracting an interface following SOLID principles.
 */
@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    @Autowired
    private SpecificationMapper specificationMapper;

    /**
     * Query specification groups by category ID
     *
     * @param cid Category ID
     * @return List of specification groups
     * @throws LyException if no groups found
     */
    public List<SpecGroup> querySpecGroupByCid(Long cid) {
        // Create query criteria
        SpecGroup group = new SpecGroup();
        group.setCid(cid);

        // Execute query
        List<SpecGroup> list = specGroupMapper.select(group);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }

        return list;
    }

    /**
     * Query specification parameters with optional filters
     *
     * @param gid Group ID - filters params by specification group (optional)
     * @param cid Category ID - filters params by product category (optional)
     * @param searching Boolean flag indicating if the parameter is searchable (optional)
     * @return List of specification parameters matching the criteria
     * @throws LyException if no parameters found
     */
    public List<SpecParam> querySpecParamList(Long gid, Long cid, Boolean searching) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setSearching(searching);

        List<SpecParam> list = specParamMapper.select(param);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return list;
    }

    /**
     * Query specification groups with their parameters by category ID
     * Returns a complete specification structure for a category
     *
     * @param cid Category ID
     * @return List of specification groups with their parameters
     */
    public List<SpecGroup> queryListByCid(Long cid) {
        // Query specification groups
        List<SpecGroup> specGroups = querySpecGroupByCid(cid);

        // Query all parameters for this category
        List<SpecParam> specParams = querySpecParamList(null, cid, null);

        // Create a map of group ID to parameters list for efficient lookup
        Map<Long, List<SpecParam>> map = new HashMap<>();
        for (SpecParam param : specParams) {
            if (!map.containsKey(param.getGroupId())) {
                map.put(param.getGroupId(), new ArrayList<>());
            }
            map.get(param.getGroupId()).add(param);
        }

        // Associate parameters with their groups
        for (SpecGroup specGroup : specGroups) {
            specGroup.setParams(map.get(specGroup.getId()));
        }

        return specGroups;
    }

    /**
     * 根据id查询规格参数模板
     * @param id
     * @return
     */
//    public Specification queryById(Long id) {
//        return this.specificationMapper.selectByPrimaryKey(id);
//    }

    /**
     * Create a new specification template
     *
     * @param specification Specification template information
     */
    public void saveSpecification(Specification specification) {
        this.specificationMapper.insert(specification);
    }

    /**
     * Update an existing specification template
     *
     * @param specification Updated specification information
     */
    public void updateSpecification(Specification specification) {
        this.specificationMapper.updateByPrimaryKeySelective(specification);
    }

    /**
     * Delete a specification template
     *
     * @param specification Specification to delete (must contain category ID)
     */
    public void deleteSpecification(Specification specification) {
        this.specificationMapper.deleteByPrimaryKey(specification);
    }

}
