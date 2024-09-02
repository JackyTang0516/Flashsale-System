-- Description: Seckill Activity Table
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for seckill_activity
-- ----------------------------
DROP TABLE IF EXISTS `seckill_activity`;
CREATE TABLE `seckill_activity`  (
                                     `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Seckill Activity ID',
                                     `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Seckill Activity Name',
                                     `commodity_id` bigint NOT NULL,
                                     `old_price` decimal(10, 2) NOT NULL COMMENT 'Original Price',
                                     `seckill_price` decimal(10, 2) NOT NULL COMMENT 'Seckill Price',
                                     `activity_status` int NOT NULL DEFAULT 0 COMMENT 'Activity Status, 0: Inactive 1: Active',
                                     `start_time` datetime(0) NULL DEFAULT NULL COMMENT 'Start Time',
                                     `end_time` datetime(0) NULL DEFAULT NULL COMMENT 'End Time',
                                     `total_stock` bigint UNSIGNED NOT NULL COMMENT 'Total Stock',
                                     `available_stock` int NOT NULL COMMENT 'Available Stock',
                                     `lock_stock` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'Locked Stock Quantity',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `id`(`id`, `name`, `commodity_id`) USING BTREE,
                                     INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seckill_activity
-- ----------------------------
INSERT INTO `seckill_activity` VALUES (1, 'Test 1', 999, 2.88, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `seckill_activity` VALUES (2, 'Test 2', 999, 3.88, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `seckill_activity` VALUES (3, 'Test 3', 999, 8.99, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `seckill_activity` VALUES (4, 'Test 4', 999, 0.00, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `seckill_activity` VALUES (5, 'Test 5', 999, 0.00, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `seckill_activity` VALUES (6, 'Test 6', 999, 0.00, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `seckill_activity` VALUES (7, 'Test', 999, 0.00, 99.00, 16, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `seckill_activity` VALUES (8, 'Test', 999, 0.00, 99.00, 16, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `seckill_activity` VALUES (9, 'Test 9', 999, 99.99, 88.88, 1, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `seckill_activity` VALUES (10, 'Special Deal - Apple iPhone 11 Pro 256GB', 999, 8769.00, 7769.00, 1, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `seckill_activity` VALUES (11, 'Special Deal - Apple iPhone 11 Pro 256GB', 999, 8769.00, 7769.00, 1, '2020-11-01 19:21:20', '2020-11-20 16:50:40', 10, 0, 0);
INSERT INTO `seckill_activity` VALUES (12, 'Special Deal - Apple iPhone 11 Pro 256GB', 999, 99.99, 88.88, 1, '2020-11-01 19:21:20', '2020-11-18 16:50:33', 10, 0, 0);
INSERT INTO `seckill_activity` VALUES (19, 'iPhone 12 Pro Seckill Event', 1001, 7888.00, 5888.00, 1, '2020-11-05 08:39:24', '2020-11-05 08:39:24', 10, 9, 0);

-- ----------------------------
-- Table structure for seckill_commodity
-- ----------------------------
DROP TABLE IF EXISTS `seckill_commodity`;
CREATE TABLE `seckill_commodity`  (
                                      `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
                                      `commodity_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                      `commodity_desc` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                      `commodity_price` int NOT NULL,
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1002 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seckill_commodity
-- ----------------------------
INSERT INTO `seckill_commodity` VALUES (11, 'Item 11', 'Description 11', 100);
INSERT INTO `seckill_commodity` VALUES (12, 'Item 12', 'Description 12', 100);
INSERT INTO `seckill_commodity` VALUES (999, 'Golden Partner for Seniors', 'Resolution: 1920*1080(FHD)\nRear Camera: 12MP\nFront Camera: 5MP\nNumber of Cores: Other\nFrequency: See official info\nBrand: Apple\nProduct Name: APPLE iPhone 6s Plus\nProduct ID: 1861098\nWeight: 0.51kg\nOrigin: Mainland China\nHighlights: Fingerprint ID, Apple Pay, Metal Body, Photography Expert\nSystem: iOS\nPixels: 1000-1600MP\nInternal Memory: 64GB', 999);
INSERT INTO `seckill_commodity` VALUES (1001, 'iPhone 12 Pro', 'iPhone 12 Pro is great', 9999);

-- ----------------------------
-- Table structure for seckill_order
-- ----------------------------
DROP TABLE IF EXISTS `seckill_order`;
CREATE TABLE `seckill_order`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `order_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                  `order_status` int NOT NULL,
                                  `seckill_activity_id` bigint NOT NULL,
                                  `user_id` bigint NOT NULL,
                                  `order_amount` decimal(10, 0) UNSIGNED NOT NULL,
                                  `create_time` datetime(0) NOT NULL,
                                  `pay_time` datetime(0) NULL DEFAULT NULL,
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seckill_order
-- ----------------------------
INSERT INTO `seckill_order` VALUES (5, '524743559841189888', 1, 19, 1234, 5888, '2020-11-13 07:44:40', NULL);
INSERT INTO `seckill_order` VALUES (6, '524744128538480640', 2, 19, 1234, 5888, '2020-11-13 07:46:55', '2020-11-13 08:01:19');

-- ----------------------------
-- Table structure for seckill_user
-- ----------------------------
DROP TABLE IF EXISTS `seckill_user`;
CREATE TABLE `seckill_user`  (
                                 `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'User ID',
                                 `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'User Name',
                                 `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Address',
                                 `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Phone Number',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seckill_user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;