package com.cloud.shopping.item.api;

import com.cloud.shopping.item.pojo.SpecGroup;
import com.cloud.shopping.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi {
    /**
     * Query specification parameters with optional filters
     *
     * @param gid Group ID - filters params by specification group (optional)
     * @param cid Category ID - filters params by product category (optional)
     * @param searching Boolean flag indicating if the parameter is searchable in product search
     *                 When true, returns only specifications that can be used as search criteria
     *                 When false or null, returns all specifications
     * @return List of specification parameters matching the criteria
     */
    @GetMapping("spec/params")
    List<SpecParam> querySpecParamList(
            @RequestParam(value="gid", required = false) Long gid, // group id
            @RequestParam(value="cid", required = false) Long cid, // category id
            @RequestParam(value="searching", required = false) Boolean searching
    );

    @GetMapping("spec/group")
    List<SpecGroup> queryGroupByCid(@RequestParam("cid") Long cid);


    // Search f
//    @GetMapping("/spec/{cid}")
//    List<SpecGroup> querySpecsByCid(@PathVariable("cid") Long cid);

}
