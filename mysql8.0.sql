/*
 Updated MySQL 8.0+ Schema with American Brands and Sample Items
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_brand
-- ----------------------------
DROP TABLE IF EXISTS `tb_brand`;
CREATE TABLE `tb_brand`
(
    `id`     BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'Brand ID',
    `name`   VARCHAR(32) NOT NULL COMMENT 'Brand Name',
    `image`  VARCHAR(128) DEFAULT '' COMMENT 'Brand Image URL',
    `letter` CHAR(1)      DEFAULT '' COMMENT 'First Letter of Brand Name',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Brand table with American brands';

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category`
(
    `id`        BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'Category ID',
    `name`      VARCHAR(32) NOT NULL COMMENT 'Category Name',
    `parent_id` BIGINT      NOT NULL COMMENT 'Parent Category ID, 0 for root',
    `is_parent` BOOLEAN     NOT NULL COMMENT 'Is Parent Category',
    `sort`      INT         NOT NULL COMMENT 'Sort Order',
    PRIMARY KEY (`id`),
    INDEX       `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1424 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Product Category Table';

-- ----------------------------
-- Table structure for tb_category_brand
-- ----------------------------
DROP TABLE IF EXISTS `tb_category_brand`;
CREATE TABLE `tb_category_brand`
(
    `category_id` BIGINT NOT NULL COMMENT 'Category ID',
    `brand_id`    BIGINT NOT NULL COMMENT 'Brand ID',
    PRIMARY KEY (`category_id`, `brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Category-Brand Relationship Table';

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order`
(
    `order_id`      BIGINT         NOT NULL COMMENT 'Order ID',
    `total_pay`     DECIMAL(10, 2) NOT NULL COMMENT 'Total Amount in USD',
    `actual_pay`    DECIMAL(10, 2) NOT NULL COMMENT 'Actual Payment in USD',
    `promotion_ids` JSON        DEFAULT NULL COMMENT 'Promotion IDs as JSON Array',
    `payment_type`  ENUM('Online', 'Cash on Delivery') NOT NULL COMMENT 'Payment Type',
    `post_fee`      DECIMAL(10, 2) NOT NULL COMMENT 'Shipping Fee in USD',
    `create_time`   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP COMMENT 'Order Creation Time',
    `shipping_name` VARCHAR(50) DEFAULT NULL COMMENT 'Shipping Carrier',
    `shipping_code` VARCHAR(50) DEFAULT NULL COMMENT 'Tracking Number',
    `user_id`       UUID           NOT NULL COMMENT 'User ID',
    `buyer_message` TEXT        DEFAULT NULL COMMENT 'Buyer Message',
    `buyer_nick`    VARCHAR(50)    NOT NULL COMMENT 'Buyer Nickname',
    `buyer_rate`    BOOLEAN     DEFAULT FALSE COMMENT 'Buyer Review Status',
    PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='Order table';

-- ----------------------------
-- Table structure for tb_order_status
-- ----------------------------
DROP TABLE IF EXISTS `tb_order_status`;
CREATE TABLE `tb_order_status`
(
    `order_id` BIGINT NOT NULL COMMENT 'Order ID',
    `status`   ENUM('Unpaid', 'Paid', 'Shipped', 'Completed', 'Closed', 'Reviewed') DEFAULT 'Unpaid' COMMENT 'Order Status',
    PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='Order Status Table';

-- ----------------------------
-- Table structure for tb_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `tb_order_detail`;
CREATE TABLE `tb_order_detail`
(
    `id`       BIGINT         NOT NULL AUTO_INCREMENT COMMENT 'Order Detail ID',
    `order_id` BIGINT         NOT NULL COMMENT 'Order ID',
    `sku_id`   BIGINT         NOT NULL COMMENT 'SKU Product ID',
    `num`      INT            NOT NULL COMMENT 'Quantity Purchased',
    `title`    VARCHAR(256)   NOT NULL COMMENT 'Product Title',
    `own_spec` VARCHAR(1024) DEFAULT '' COMMENT 'Product Dynamic Attribute Key-Value Set',
    `price`    DECIMAL(10, 2) NOT NULL COMMENT 'Price in USD',
    `image`    VARCHAR(128)  DEFAULT '' COMMENT 'Product Image',
    PRIMARY KEY (`id`),
    KEY        `idx_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8mb4 COMMENT='Order Detail Table';

-- ----------------------------
-- Table structure for tb_order_status
-- ----------------------------
DROP TABLE IF EXISTS `tb_order_status`;
CREATE TABLE `tb_order_status`
(
    `order_id`     BIGINT NOT NULL COMMENT 'Order ID',
    `status`       ENUM('Unpaid', 'Paid', 'Shipped', 'Completed', 'Closed', 'Reviewed') DEFAULT 'Unpaid' COMMENT 'Order Status',
    `create_time`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Order Creation Time',
    `payment_time` TIMESTAMP NULL DEFAULT NULL COMMENT 'Payment Time',
    `consign_time` TIMESTAMP NULL DEFAULT NULL COMMENT 'Shipping Time',
    `end_time`     TIMESTAMP NULL DEFAULT NULL COMMENT 'Completion Time',
    `close_time`   TIMESTAMP NULL DEFAULT NULL COMMENT 'Closure Time',
    `comment_time` TIMESTAMP NULL DEFAULT NULL COMMENT 'Review Time',
    PRIMARY KEY (`order_id`),
    INDEX          `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Order Status Table';

-- ----------------------------
-- Table structure for tb_sku
-- ----------------------------
DROP TABLE IF EXISTS `tb_sku`;
CREATE TABLE `tb_sku`
(
    `id`               BIGINT         NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
    `spu_id`           BIGINT         NOT NULL COMMENT 'SPU ID',
    `title`            VARCHAR(256)   NOT NULL COMMENT 'Product Title',
    `images`           VARCHAR(1024)           DEFAULT '' COMMENT 'Product Images, Separated by Comma',
    `price`            DECIMAL(10, 2) NOT NULL DEFAULT '0.00' COMMENT 'Sale Price in USD',
    `indexes`          VARCHAR(32)             DEFAULT '' COMMENT 'Specification Indexes in SPU Template',
    `own_spec`         JSON                    DEFAULT NULL COMMENT 'SKU Specific Attribute Key-Value JSON',
    `enable`           BOOLEAN        NOT NULL DEFAULT TRUE COMMENT 'Is Active (0=Inactive, 1=Active)',
    `create_time`      TIMESTAMP               DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation Time',
    `last_update_time` TIMESTAMP               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last Modification Time',
    PRIMARY KEY (`id`),
    KEY                `idx_spu_id` (`spu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27359021729 DEFAULT CHARSET=utf8mb4 COMMENT='SKU Table for Product Entities';

-- ----------------------------
-- Table structure for tb_pay_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_pay_log`;
CREATE TABLE `tb_pay_log`
(
    `order_id`       BIGINT NOT NULL COMMENT 'Order ID',
    `total_fee`      DECIMAL(10, 2) DEFAULT NULL COMMENT 'Payment Amount in USD',
    `user_id`        BIGINT         DEFAULT NULL COMMENT 'User ID',
    `transaction_id` VARCHAR(32)    DEFAULT NULL COMMENT 'Transaction ID',
    `status`         ENUM('Unpaid', 'Paid', 'Refunded', 'Payment Error', 'Closed') DEFAULT 'Unpaid' COMMENT 'Transaction Status',
    `pay_type`       ENUM('WeChat Pay', 'Cash on Delivery') DEFAULT NULL COMMENT 'Payment Method',
    `bank_type`      VARCHAR(16)    DEFAULT NULL COMMENT 'Bank Type',
    `create_time`    TIMESTAMP      DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation Time',
    `pay_time`       TIMESTAMP NULL DEFAULT NULL COMMENT 'Payment Time',
    `closed_time`    TIMESTAMP NULL DEFAULT NULL COMMENT 'Closure Time',
    `refund_time`    TIMESTAMP NULL DEFAULT NULL COMMENT 'Refund Time',
    PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Payment Log Table';

-- ----------------------------
-- Table structure for tb_spec_group
-- ----------------------------
DROP TABLE IF EXISTS `tb_spec_group`;
CREATE TABLE `tb_spec_group`
(
    `id`   BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `cid`  BIGINT      NOT NULL COMMENT 'Category ID',
    `name` VARCHAR(32) NOT NULL COMMENT 'Specification Group Name',
    PRIMARY KEY (`id`),
    KEY    `idx_category` (`cid`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COMMENT='Specification Groups per Category';

-- ----------------------------
-- Table structure for tb_spec_param
-- ----------------------------
DROP TABLE IF EXISTS `tb_spec_param`;
CREATE TABLE `tb_spec_param`
(
    `id`        BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `cid`       BIGINT       NOT NULL COMMENT 'Category ID',
    `group_id`  BIGINT       NOT NULL COMMENT 'Specification Group ID',
    `name`      VARCHAR(256) NOT NULL COMMENT 'Parameter Name',
    `numeric`   BOOLEAN      NOT NULL COMMENT 'Is Numeric Parameter',
    `unit`      VARCHAR(256)  DEFAULT '' COMMENT 'Unit for Numeric Parameters',
    `generic`   BOOLEAN      NOT NULL COMMENT 'Is Generic SKU Attribute',
    `searching` BOOLEAN      NOT NULL COMMENT 'Is Used for Filtering',
    `segments`  VARCHAR(1024) DEFAULT '' COMMENT 'Segment Ranges for Numeric Filters',
    PRIMARY KEY (`id`),
    KEY         `idx_group` (`group_id`),
    KEY         `idx_category` (`cid`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COMMENT='Specification Parameters for Groups';

-- ----------------------------
-- Table structure for tb_spu
-- ----------------------------
DROP TABLE IF EXISTS `tb_spu`;
CREATE TABLE `tb_spu`
(
    `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'SPU ID',
    `title`            VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'Title',
    `sub_title`        VARCHAR(256)          DEFAULT '' COMMENT 'Subtitle',
    `cid1`             BIGINT       NOT NULL COMMENT 'Category Level 1 ID',
    `cid2`             BIGINT       NOT NULL COMMENT 'Category Level 2 ID',
    `cid3`             BIGINT       NOT NULL COMMENT 'Category Level 3 ID',
    `brand_id`         BIGINT       NOT NULL COMMENT 'Brand ID',
    `saleable`         BOOLEAN      NOT NULL DEFAULT TRUE COMMENT 'Is Available for Sale',
    `valid`            BOOLEAN      NOT NULL DEFAULT TRUE COMMENT 'Is Valid',
    `create_time`      TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation Time',
    `last_update_time` TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last Update Time',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=195 DEFAULT CHARSET=utf8mb4 COMMENT='SPU Table for Abstract Product Descriptions';

-- ----------------------------
-- Table structure for tb_spu_detail
-- ----------------------------
DROP TABLE IF EXISTS `tb_spu_detail`;
CREATE TABLE `tb_spu_detail`
(
    `spu_id`        BIGINT NOT NULL COMMENT 'SPU ID',
    `description`   TEXT COMMENT 'Product Description',
    `generic_spec`  JSON   NOT NULL COMMENT 'Generic Specification Data',
    `special_spec`  JSON   NOT NULL COMMENT 'Special Specification JSON',
    `packing_list`  VARCHAR(1024) DEFAULT '' COMMENT 'Packing List',
    `after_service` VARCHAR(1024) DEFAULT '' COMMENT 'After-Sales Service',
    PRIMARY KEY (`spu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SPU Detailed Information';

-- ----------------------------
-- Table structure for tb_stock
-- ----------------------------
DROP TABLE IF EXISTS `tb_stock`;
CREATE TABLE `tb_stock`
(
    `sku_id`        BIGINT NOT NULL COMMENT 'SKU ID',
    `seckill_stock` INT DEFAULT '0' COMMENT 'Flash Sale Stock',
    `seckill_total` INT DEFAULT '0' COMMENT 'Flash Sale Total Quantity',
    `stock`         INT    NOT NULL COMMENT 'Total Stock Quantity',
    PRIMARY KEY (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Stock Information Table';

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`
(
    `id`       BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'User ID',
    `username` VARCHAR(32) NOT NULL UNIQUE COMMENT 'Username',
    `password` VARCHAR(64) NOT NULL COMMENT 'Encrypted Password',
    `phone`    VARCHAR(15) DEFAULT NULL COMMENT 'Registered Phone Number',
    `created`  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP COMMENT 'Account Creation Time',
    `salt`     VARCHAR(32) NOT NULL COMMENT 'Encryption Salt',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COMMENT='User Table';

-- ----------------------------
-- Sample Data for tb_brand
-- ----------------------------
INSERT INTO `tb_brand` (`id`, `name`, `image`, `letter`)
VALUES (1001, 'Apple', 'https://example.com/apple.jpg', 'A'),
       (1002, 'Amazon', 'https://example.com/amazon.jpg', 'A'),
       (1003, 'Ford', 'https://example.com/ford.jpg', 'F'),
       (1004, 'Tesla', 'https://example.com/tesla.jpg', 'T'),
       (1005, 'Nike', 'https://example.com/nike.jpg', 'N'),
       (1006, 'Samsung', 'https://example.com/samsung.jpg', 'S'),
       (1007, 'Sony', 'https://example.com/sony.jpg', 'S'),
       (1008, 'Adidas', 'https://example.com/adidas.jpg', 'A'),
       (1009, 'Intel', 'https://example.com/intel.jpg', 'I'),
       (1010, 'Microsoft', 'https://example.com/microsoft.jpg', 'M');

-- ----------------------------
-- Sample Data for tb_category
-- ----------------------------
INSERT INTO `tb_category` (`id`, `name`, `parent_id`, `is_parent`, `sort`)
VALUES (1, 'Electronics', 0, 1, 1),
       (2, 'Clothing', 0, 1, 2),
       (3, 'Smartphones', 1, 0, 3),
       (4, 'Shoes', 2, 0, 4),
       (5, 'Laptops', 1, 0, 5),
       (6, 'Accessories', 1, 0, 6);

-- ----------------------------
-- Sample Data for tb_category_brand
-- ----------------------------
INSERT INTO `tb_category_brand` (`category_id`, `brand_id`)
VALUES (1, 1001),
       (1, 1002),
       (1, 1006),
       (2, 1005),
       (2, 1008),
       (3, 1001),
       (3, 1006),
       (4, 1005),
       (4, 1008);

-- ----------------------------
-- Sample Data for tb_order
-- ----------------------------
INSERT INTO `tb_order` (`order_id`, `total_pay`, `actual_pay`, `payment_type`, `post_fee`, `user_id`, `buyer_nick`,
                        `buyer_rate`)
VALUES (5001, 999.99, 899.99, 'Online', 5.99, UUID(), 'JohnDoe', TRUE),
       (5002, 699.99, 649.99, 'Online', 4.99, UUID(), 'JaneDoe', FALSE),
       (5003, 1299.99, 1199.99, 'Cash on Delivery', 9.99, UUID(), 'MarkSmith', TRUE);

-- ----------------------------
-- Sample Data for tb_order_detail
-- ----------------------------
INSERT INTO `tb_order_detail` (`id`, `order_id`, `sku_id`, `num`, `title`, `own_spec`, `price`, `image`)
VALUES (2001, 5001, 3001, 1, 'iPhone 15', '{"color": "Black", "storage": "128GB"}', 899.99,
        'https://example.com/iphone15.jpg'),
       (2002, 5002, 3002, 2, 'Galaxy S23', '{"color": "White", "storage": "256GB"}', 649.99,
        'https://example.com/galaxy23.jpg'),
       (2003, 5003, 3003, 1, 'MacBook Pro', '{"size": "16-inch", "RAM": "16GB"}', 1199.99,
        'https://example.com/macbook.jpg');

-- ----------------------------
-- Sample Data for tb_sku
-- ----------------------------
INSERT INTO `tb_sku` (`id`, `spu_id`, `title`, `images`, `price`, `indexes`, `own_spec`, `enable`)
VALUES (3001, 4001, 'iPhone 15', 'https://example.com/iphone15.jpg', 999.99, '1_2', '{
  "color": "Black",
  "storage": "128GB"
}', TRUE),
       (3002, 4002, 'Galaxy S23', 'https://example.com/galaxy23.jpg', 699.99, '2_3', '{
         "color": "White",
         "storage": "256GB"
       }', TRUE),
       (3003, 4003, 'MacBook Pro', 'https://example.com/macbook.jpg', 1299.99, '3_4', '{
         "size": "16-inch",
         "RAM": "16GB"
       }', TRUE);

-- ----------------------------
-- Sample Data for tb_stock
-- ----------------------------
INSERT INTO `tb_stock` (`sku_id`, `seckill_stock`, `seckill_total`, `stock`)
VALUES (3001, 10, 50, 1000),
       (3002, 20, 100, 800),
       (3003, 5, 30, 500);

-- ----------------------------
-- Sample Data for tb_user
-- ----------------------------
INSERT INTO `tb_user` (`id`, `username`, `password`, `phone`, `salt`)
VALUES (1, 'johndoe', 'hashed_password', '1234567890', 'random_salt'),
       (2, 'janedoe', 'hashed_password2', '0987654321', 'random_salt2'),
       (3, 'marksmith', 'hashed_password3', '1122334455', 'random_salt3');


