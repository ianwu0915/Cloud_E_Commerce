package com.cloud.shopping.item.controller;

import com.cloud.shopping.item.pojo.SpecGroup;
import com.cloud.shopping.item.pojo.SpecParam;
import com.cloud.shopping.item.pojo.Specification;
import com.cloud.shopping.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product Specification Management Controller
 * Handles operations for product specifications, groups, and parameters
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specService;

    /**
     * Query specification groups by category ID
     *
     * @param cid Category ID
     * @return List of specification groups
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable("cid") Long cid) {
        return ResponseEntity.ok(specService.querySpecGroupByCid(cid));
    }

    /**
     * Query specification parameters with optional filters
     *
     * @param gid Group ID - filters params by specification group (optional)
     * @param cid Category ID - filters params by product category (optional)
     * @param searching Boolean flag indicating if the parameter is searchable
     * @return List of specification parameters matching the criteria
     */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecParamList(
            @RequestParam(value="gid", required = false) Long gid,
            @RequestParam(value="cid", required = false) Long cid,
            @RequestParam(value="searching", required = false) Boolean searching
    ) {
        return ResponseEntity.ok(specService.querySpecParamList(gid, cid, searching));
    }

    /**
     * Query specification groups with their parameters by category ID
     * Returns complete specification structure for a category
     *
     * @param cid Category ID
     * @return List of specification groups including their parameters
     */
    @GetMapping("group")
    public ResponseEntity<List<SpecGroup>> queryListByCid(@RequestParam("cid") Long cid) {
        return ResponseEntity.ok(specService.queryListByCid(cid));
    }

    /**
     * Create a new specification template
     *
     * @param specification Specification template information
     * @return Empty response with OK status
     */
    @PostMapping("group")
    public ResponseEntity<Void> saveSpecification(Specification specification) {
        this.specService.saveSpecification(specification);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Update an existing specification template
     *
     * @param specification Updated specification information
     * @return Empty response with OK status
     */
    @PutMapping
    public ResponseEntity<Void> updateSpecification(Specification specification) {
        this.specService.updateSpecification(specification);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Delete a specification template by category ID
     *
     * @param id Category ID whose specification template should be deleted
     * @return Empty response with OK status
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSpecification(@PathVariable("id") Long id) {
        Specification specification = new Specification();
        specification.setCategoryId(id);
        this.specService.deleteSpecification(specification);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
