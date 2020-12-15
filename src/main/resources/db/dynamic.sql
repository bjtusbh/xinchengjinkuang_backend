/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : schoolMate

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 01/11/2020 17:12:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dynamic
-- ----------------------------
DROP TABLE IF EXISTS `dynamic`;
CREATE TABLE `dynamic` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dynamic_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `pub_date` datetime(6) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
  `dynamic_contendynamic_content` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `dynamic_visits` int DEFAULT NULL,
  `dynamic_author` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dynamic
-- ----------------------------
BEGIN;
INSERT INTO `dynamic` VALUES (1, '湖北校友分会参加高校湖北校友会联盟活动', '2020-10-28 22:44:29.000000', '2020年8月1日，高校湖北校友会\n联盟召开秘书长会议，上海交大校友分会秘书长匡运清、西南财大校友分会秘书长马俊、西北农林大学校友分会秘书长南永平、中\n南大学校友分会秘书长张金河、北京交大校友分会副秘书长张建予、国防科大校友分会秘书长张昌天、三峡大学校友分会副秘书长\n王金菊、扬州大学校友分会秘书长刘鹏、副秘书长曾现廷等部分在汉高校校友分会秘书长参加活动。\n联盟召开秘书长会议，上海交大校友分会秘书长匡运清、西南财大校友分会秘书长马俊、西北农林大学校友分会秘书长南永平、中\n南大学校友分会秘书长张金河、北京交大校友分会副秘书长张建予、国防科大校友分会秘书长张昌天、三峡大学校友分会副秘书长\n王金菊、扬州大学校友分会秘书长刘鹏、副秘书长曾现廷等部分在汉高校校友分会秘书长参加活动。', 200, '书俊俊');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
