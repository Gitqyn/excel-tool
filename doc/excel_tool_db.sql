/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : excel_tool_db

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-01-25 18:06:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for demo
-- ----------------------------
DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_name` varchar(64) DEFAULT '' COMMENT '用户名',
  `department` varchar(255) DEFAULT '' COMMENT '部门',
  `user_code` varchar(255) DEFAULT '' COMMENT '编号',
  `age` int(20) DEFAULT NULL COMMENT '年龄',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of demo
-- ----------------------------
