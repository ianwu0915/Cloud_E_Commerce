package com.cloud.shopping.cart.web;

import com.cloud.shopping.cart.pojo.CartItem;
import com.cloud.shopping.cart.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Shopping Cart REST Controller
 * Handles all cart-related HTTP requests
 * All endpoints require user authentication via JWT
 */
@Slf4j
@RequestMapping
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * Add item to shopping cart
     *
     * @param cartItem Cart item details including SKU and quantity
     * @return Empty response with CREATED (201) status
     */
    @PostMapping
    public ResponseEntity<Void> addItemToCart(@RequestBody CartItem cartItem) {
        cartService.addItemToCart(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Retrieve user's shopping cart contents
     *
     * @return List of cart items with OK (200) status
     */
    @GetMapping("list")
    public ResponseEntity<List<CartItem>> queryCartList() {
        return ResponseEntity.ok(cartService.quertyCartList());
    }

    /**
     * Update item quantity in cart
     *
     * @param skuId Product SKU identifier
     * @param num New quantity
     * @return Empty response with NO_CONTENT (204) status
     */
    @PutMapping
    public ResponseEntity<Void> updateCartNum(
            @RequestParam("id") Long skuId,
            @RequestParam("num") Integer num) {
        log.info("Received SKU ID: {}", skuId);
        log.info("Received quantity: {}", num);
        cartService.updateItemNum(skuId, num);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Remove item from cart
     *
     * @param skuId Product SKU identifier to remove
     * @return Empty response with NO_CONTENT (204) status
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable("skuId") Long skuId) {
        cartService.deleteCartItem(skuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}