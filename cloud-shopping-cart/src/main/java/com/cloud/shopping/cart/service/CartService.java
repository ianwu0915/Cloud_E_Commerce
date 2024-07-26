package com.cloud.shopping.cart.service;

import com.cloud.shopping.auth.entity.UserInfo;
import com.cloud.shopping.cart.pojo.CartItem;
import com.cloud.shopping.common.enums.ExceptionEnum;
import com.cloud.shopping.common.exception.LyException;
import com.cloud.shopping.common.utils.JsonUtils;
import com.cloud.shopping.cart.filters.UserInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Shopping Cart Service
 * Manages shopping cart operations using Redis as storage:
 * cart:uid:{userId} (primary key) -> {skuId(hash key): cartItem}
 */
@Slf4j
@Service
public class CartService {
    @Autowired
    private StringRedisTemplate redisTemplate; // Redis template for string operations

    // Redis key prefix for cart storage: cart:uid:{userId}
    static final String KEY_PREFIX = "cart:uid:";

    /**
     * Add or update item in shopping cart
     * If item exists, quantity will be added to existing amount
     *
     * @param cartItem Cart item to add or update
     */
    public void addItemToCart(CartItem cartItem) {
        // Get current user from thread context
        UserInfo user = UserInterceptor.getUser();
        // Generate Redis key for user's cart
        String key = KEY_PREFIX + user.getId();
        // Generate hash key using SKU ID
        String hashKey = cartItem.getSkuId().toString();
        // Store original quantity for addition
        Integer num = cartItem.getNum();

        // Get Redis hash operations object
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);

        // Check if item already exists in cart
        if(operation.hasKey(hashKey)) {
            // Update existing item quantity
            // cartItem json -> json string -> CartItem
            String json = operation.get(hashKey).toString();
            CartItem existingCartItem = JsonUtils.toBean(json, CartItem.class);
            cartItem.setNum(existingCartItem.getNum() + num);
        }
        // Save to Redis
        operation.put(hashKey, JsonUtils.toString(cartItem));
    }

    /**
     * Query user's shopping cart contents
     *
     * @return List of cart items
     * @throws LyException if cart not found
     */
    public List<CartItem> quertyCartList() {
        // Get current user
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();

        if(!redisTemplate.hasKey(key)) {
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }

        // Get all items from user's cart
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);

        // Convert all values to Cart objects
        return operation.values().stream()
                .map(o -> JsonUtils.toBean(o.toString(), CartItem.class))
                .collect(Collectors.toList());
    }

    /**
     * Update item quantity in cart
     *
     * @param skuId Product SKU ID
     * @param num New quantity
     * @throws LyException if item not found in cart
     */
    public void updateItemNum(Long skuId, Integer num) {
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();
        String hashKey = skuId.toString();

        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);

        // Verify item exists
        if(!operations.hasKey(hashKey)) {
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }

        // Update quantity
        CartItem cartItem = JsonUtils.toBean(operations.get(hashKey).toString(), CartItem.class);
        cartItem.setNum(num);

        // Save back to Redis
        operations.put(hashKey, JsonUtils.toString(cartItem));
    }

    /**
     * Remove item from shopping cart
     *
     * @param skuId Product SKU ID to remove
     */
    public void deleteCartItem(Long skuId) {
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_PREFIX + user.getId();
        redisTemplate.opsForHash().delete(key, skuId.toString());
    }
}
