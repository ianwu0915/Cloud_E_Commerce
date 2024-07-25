package com.cloud.shopping.common.vo;

import lombok.Data;
import org.mockito.cglib.core.CollectionUtils;

import java.util.List;

/**
 * Generic Pagination Result Container
 * Used to wrap paginated data responses throughout the application.
 *
 * @param <T> The type of items being paginated (e.g., Product, Order, etc.)
 */
@Data
public class PageResult<T> {
    private Long total;      // Total number of items across all pages
    private Long totalPage;  // Total number of pages available
    private List<T> items;   // Current page's items

    /**
     * Default constructor
     */
    public PageResult() {
    }

    /**
     * Constructor for simple pagination results
     * Used when total pages can be calculated by the client if needed
     * or when total pages information is not necessary
     *
     * @param total Total number of items across all pages
     * @param items List of items for the current page
     */
    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
        // Note: totalPage is not set, remains null
    }

    /**
     * Constructor for complete pagination results including total pages
     * Used when total pages calculation is done by the server
     * or when exact page count is important for UI pagination controls
     *
     * @param total Total number of items across all pages
     * @param totalPage Total number of pages available
     * @param items List of items for the current page
     */
    public PageResult(Long total, Long totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
}