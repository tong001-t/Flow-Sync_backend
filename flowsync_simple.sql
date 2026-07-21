/*
 Navicat Premium Dump SQL

 Source Server         : mysql8
 Source Server Type    : MySQL
 Source Server Version : 80019 (8.0.19)
 Source Host           : localhost:3306
 Source Schema         : flowsync_simple

 Target Server Type    : MySQL
 Target Server Version : 80019 (8.0.19)
 File Encoding         : 65001

 Date: 07/07/2026 08:33:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for project_info
-- ----------------------------
DROP TABLE IF EXISTS `project_info`;
CREATE TABLE `project_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目说明',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目状态',
  `priority` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '优先级',
  `owner_id` bigint NULL DEFAULT NULL COMMENT '负责人ID',
  `start_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_project_owner`(`owner_id` ASC) USING BTREE,
  CONSTRAINT `fk_project_owner` FOREIGN KEY (`owner_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project_info
-- ----------------------------
INSERT INTO `project_info` VALUES (1, '迎新活动协同项目', '用于安排迎新活动方案、海报和执行分工。', '进行中', '高', 2, '2026-07-01', '2026-07-15', '2026-07-05 17:50:12');
INSERT INTO `project_info` VALUES (2, '课程答辩准备项目', '用于整理项目报告、演示文稿和答辩分工。', '未开始', '中', 2, '2026-07-06', '2026-07-20', '2026-07-05 17:50:12');
INSERT INTO `project_info` VALUES (3, '1', '', '未开始', '中', 2, NULL, NULL, '2026-07-06 11:36:44');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '真实姓名',
  `role` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (2, 'leader', '123456', '项目负责人', '负责人', '13800000002', 'leader@flowsync.com', '2026-07-05 17:50:12');
INSERT INTO `sys_user` VALUES (3, 'member1', '123456', '张三', '成员', '13800000003', 'zhangsan@flowsync.com', '2026-07-05 17:50:12');
INSERT INTO `sys_user` VALUES (4, 'member2', '123456', '李四', '成员', '13800000004', 'lisi@flowsync.com', '2026-07-05 17:50:12');

-- ----------------------------
-- Table structure for task_info
-- ----------------------------
DROP TABLE IF EXISTS `task_info`;
CREATE TABLE `task_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_id` bigint NOT NULL COMMENT '所属项目ID',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父任务ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务标题',
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务说明',
  `assignee_id` bigint NULL DEFAULT NULL COMMENT '负责人ID',
  `creator_id` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务状态',
  `priority` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '优先级',
  `due_date` date NULL DEFAULT NULL COMMENT '截止日期',
  `ai_suggestion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '千问建议',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_task_project`(`project_id` ASC) USING BTREE,
  INDEX `fk_task_assignee`(`assignee_id` ASC) USING BTREE,
  INDEX `fk_task_creator`(`creator_id` ASC) USING BTREE,
  CONSTRAINT `fk_task_assignee` FOREIGN KEY (`assignee_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_task_creator` FOREIGN KEY (`creator_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_task_project` FOREIGN KEY (`project_id`) REFERENCES `project_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task_info
-- ----------------------------
INSERT INTO `task_info` VALUES (39, 1, NULL, '制定迎新活动整体方案', '明确活动时间、地点、流程、预算及分工，形成可执行的详细方案文档', 2, 2, '进行中', '高', '2026-07-09', NULL, '2026-07-06 10:25:39');
INSERT INTO `task_info` VALUES (40, 1, NULL, '迎新物资采购与分发', '根据方案清单完成横幅、指引牌、新生礼包、饮用水等物资的采购、验收与现场分发安排', 3, 2, '未开始', '高', '2026-07-09', NULL, '2026-07-06 10:25:39');
INSERT INTO `task_info` VALUES (41, 1, NULL, '迎新志愿者招募与培训', '面向各学院招募志愿者，组织岗前培训（含流程、话术、应急处理），建立排班表', 4, 2, '未开始', '高', '2026-07-10', NULL, '2026-07-06 10:25:40');
INSERT INTO `task_info` VALUES (42, 1, NULL, '迎新系统调试与数据准备', '测试迎新信息平台（含报到登记、宿舍分配、绿色通道等模块），导入新生基础数据，确保系统稳定运行', 3, 2, '未开始', '高', '2026-07-09', NULL, '2026-07-06 10:25:40');
INSERT INTO `task_info` VALUES (43, 1, NULL, '迎新现场协调与应急响应', '活动当天统筹各点位运行，处理突发情况（如网络故障、人员拥堵、物资短缺等），实时同步进展给项目负责人', 4, 2, '未开始', '高', '2026-07-08', NULL, '2026-07-06 10:25:40');
INSERT INTO `task_info` VALUES (44, 2, NULL, '整理答辩PPT内容', '根据课程要求，梳理核心知识点，制作逻辑清晰、重点突出的答辩PPT，包含封面、目录、内容页、总结与致谢页', 3, 2, '未开始', '高', '2026-07-08', NULL, '2026-07-06 11:36:30');
INSERT INTO `task_info` VALUES (45, 2, NULL, '撰写答辩讲稿', '围绕PPT内容撰写逐页讲解脚本，控制时长在8-10分钟，确保语言简洁、表达准确、有逻辑衔接', 4, 2, '未开始', '高', '2026-07-08', NULL, '2026-07-06 11:36:30');
INSERT INTO `task_info` VALUES (46, 2, NULL, '模拟答辩演练', '组织至少两次完整模拟答辩，邀请成员反馈，优化语速、肢体语言、时间把控及问答应对', 3, 2, '未开始', '高', '2026-07-08', NULL, '2026-07-06 11:36:30');
INSERT INTO `task_info` VALUES (47, 2, NULL, '准备答辩问答题库', '预判5-8个可能被提问的问题，整理标准答案与延伸思路，覆盖技术细节、设计决策与反思改进', 4, 2, '未开始', '中', '2026-07-07', NULL, '2026-07-06 11:36:30');
INSERT INTO `task_info` VALUES (48, 2, NULL, '最终材料整合与提交', '合并尾与张狼', 2, 2, '进行中', '高', '2026-07-07', NULL, '2026-07-06 11:36:31');

-- ----------------------------
-- Table structure for task_log
-- ----------------------------
DROP TABLE IF EXISTS `task_log`;
CREATE TABLE `task_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id` bigint NOT NULL COMMENT '任务ID',
  `progress_percent` int NOT NULL COMMENT '进度百分比',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '进度说明',
  `operator_id` bigint NULL DEFAULT NULL COMMENT '记录人ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_log_task`(`task_id` ASC) USING BTREE,
  INDEX `fk_log_user`(`operator_id` ASC) USING BTREE,
  CONSTRAINT `fk_log_task` FOREIGN KEY (`task_id`) REFERENCES `task_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_log_user` FOREIGN KEY (`operator_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务进度记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task_log
-- ----------------------------

-- ----------------------------
-- Table structure for task_summary
-- ----------------------------
DROP TABLE IF EXISTS `task_summary`;
CREATE TABLE `task_summary`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_id` bigint NOT NULL COMMENT '所属项目ID',
  `task_id` bigint NULL DEFAULT NULL COMMENT '关联任务ID',
  `summary_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '总结类型',
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '总结内容',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_summary_project`(`project_id` ASC) USING BTREE,
  INDEX `fk_summary_task`(`task_id` ASC) USING BTREE,
  INDEX `fk_summary_user`(`created_by` ASC) USING BTREE,
  CONSTRAINT `fk_summary_project` FOREIGN KEY (`project_id`) REFERENCES `project_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_summary_task` FOREIGN KEY (`task_id`) REFERENCES `task_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_summary_user` FOREIGN KEY (`created_by`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '总结表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task_summary
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
