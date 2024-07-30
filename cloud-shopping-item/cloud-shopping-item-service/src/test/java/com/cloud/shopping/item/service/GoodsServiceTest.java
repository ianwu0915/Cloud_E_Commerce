package com.cloud.shopping.item.service;

import com.cloud.shopping.common.dto.CartDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // Ensures the database changes are rolled back after each test
public class GoodsServiceTest {

    @Autowired
    private GoodsService goodsService;

    private List<CartDTO> cartList;

    @BeforeEach
    void setUp() {
        // Sample cart items for testing
        cartList = Arrays.asList(
                new CartDTO(2600242L, 2),
                new CartDTO(2600248L, 2)
        );
    }

    @Test
    void testDecreaseStock() {
        // Call the method
        assertDoesNotThrow(() -> goodsService.decreaseStock(cartList), "Stock decrease should not throw exceptions");

        // Optionally, verify stock update (requires a method to fetch stock)
        for (CartDTO cart : cartList) {
            int remainingStock = goodsService.querySkuById(cart.getSkuId()).getStock();
            assertTrue(remainingStock >= 0, "Stock should not be negative after decrement");
        }
    }
}
