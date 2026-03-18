-- ============================================================
-- pm-system 数据库初始化脚本（MySQL 8）
-- 使用方式：在本地库 pm_system 中执行本文件（Navicat / mysql 客户端 / IDE）
-- 默认登录：admin / password（BCrypt，与 Spring Security 一致）
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ---------- 用户与权限 ----------
DROP TABLE IF EXISTS `sys_oper_log`;
DROP TABLE IF EXISTS `sys_user_role`;
DROP TABLE IF EXISTS `sys_role`;
DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` bigint NOT NULL COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '登录账号',
  `password` varchar(100) NOT NULL COMMENT 'BCrypt 密码',
  `real_name` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '0禁用 1启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除 0否 1是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';

CREATE TABLE `sys_role` (
  `id` bigint NOT NULL,
  `role_name` varchar(20) NOT NULL,
  `role_code` varchar(20) NOT NULL COMMENT 'admin/manager/dev/tester',
  `permissions` text COMMENT 'JSON 权限串',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色';

CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色';

CREATE TABLE `sys_oper_log` (
  `id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `module` varchar(50) DEFAULT NULL,
  `content` text,
  `ip` varchar(50) DEFAULT NULL,
  `oper_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_oper_time` (`oper_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志';

-- ---------- 项目 ----------
DROP TABLE IF EXISTS `project_config`;
DROP TABLE IF EXISTS `project_member`;
DROP TABLE IF EXISTS `project`;

CREATE TABLE `project` (
  `id` bigint NOT NULL,
  `project_name` varchar(100) NOT NULL,
  `code` varchar(50) DEFAULT NULL,
  `description` text,
  `manager_id` bigint DEFAULT NULL,
  `status` varchar(20) NOT NULL DEFAULT '0' COMMENT '0草稿 1进行中 2归档（字符串）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目';

CREATE TABLE `project_member` (
  `id` bigint NOT NULL,
  `project_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `role_in_project` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目成员';

CREATE TABLE `project_config` (
  `id` bigint NOT NULL,
  `project_id` bigint NOT NULL,
  `workflow_config` text COMMENT 'JSON',
  `custom_fields` text COMMENT 'JSON',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目配置';

-- ---------- 任务 ----------
DROP TABLE IF EXISTS `task_history`;
DROP TABLE IF EXISTS `task_follower`;
DROP TABLE IF EXISTS `task_attachment`;
DROP TABLE IF EXISTS `task_comment`;
DROP TABLE IF EXISTS `task`;

CREATE TABLE `task` (
  `id` bigint NOT NULL,
  `title` varchar(200) NOT NULL,
  `project_id` bigint NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  `priority` tinyint DEFAULT '2' COMMENT '1低2中3高',
  `status` varchar(30) NOT NULL DEFAULT 'todo' COMMENT 'todo/doing/done/closed',
  `assignee_id` bigint DEFAULT NULL,
  `creator_id` bigint DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `description` text,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务';

CREATE TABLE `task_comment` (
  `id` bigint NOT NULL,
  `task_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `content` text NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务评论';

CREATE TABLE `task_attachment` (
  `id` bigint NOT NULL,
  `task_id` bigint NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_path` varchar(500) DEFAULT NULL,
  `file_size` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务附件';

CREATE TABLE `task_follower` (
  `id` bigint NOT NULL,
  `task_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务关注';

CREATE TABLE `task_history` (
  `id` bigint NOT NULL,
  `task_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `operate` varchar(50) DEFAULT NULL,
  `content` text,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务历史';

-- ---------- 看板 ----------
DROP TABLE IF EXISTS `board_column`;
DROP TABLE IF EXISTS `board`;

CREATE TABLE `board` (
  `id` bigint NOT NULL,
  `project_id` bigint NOT NULL,
  `board_name` varchar(100) NOT NULL,
  `type` varchar(20) NOT NULL DEFAULT 'kanban' COMMENT 'kanban/list/table',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='看板';

CREATE TABLE `board_column` (
  `id` bigint NOT NULL,
  `board_id` bigint NOT NULL,
  `column_name` varchar(100) NOT NULL,
  `order_num` int NOT NULL DEFAULT '0',
  `status_value` varchar(30) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_board_id` (`board_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='看板列';

-- ---------- 缺陷 ----------
DROP TABLE IF EXISTS `bug_history`;
DROP TABLE IF EXISTS `bug`;

CREATE TABLE `bug` (
  `id` bigint NOT NULL,
  `title` varchar(200) NOT NULL,
  `project_id` bigint NOT NULL,
  `task_id` bigint DEFAULT NULL,
  `severity` tinyint DEFAULT '2' COMMENT '1轻微2一般3严重4致命',
  `priority` tinyint DEFAULT '2' COMMENT '1-3',
  `status` varchar(30) NOT NULL DEFAULT 'new' COMMENT 'new/fixing/fixed/verified/closed',
  `reporter_id` bigint DEFAULT NULL,
  `fixer_id` bigint DEFAULT NULL,
  `reproduce_step` text,
  `environment` varchar(200) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='缺陷';

CREATE TABLE `bug_history` (
  `id` bigint NOT NULL,
  `bug_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `operate` varchar(50) DEFAULT NULL,
  `content` text,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_bug_id` (`bug_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='缺陷历史';

-- ---------- 系统 ----------
DROP TABLE IF EXISTS `organization`;
DROP TABLE IF EXISTS `system_config`;
DROP TABLE IF EXISTS `system_dict`;

CREATE TABLE `system_dict` (
  `id` bigint NOT NULL,
  `dict_type` varchar(50) NOT NULL,
  `dict_label` varchar(100) NOT NULL,
  `dict_value` varchar(100) NOT NULL,
  `sort` int NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据字典';

CREATE TABLE `system_config` (
  `id` bigint NOT NULL,
  `config_key` varchar(100) NOT NULL,
  `config_value` text,
  `remark` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置';

CREATE TABLE `organization` (
  `id` bigint NOT NULL,
  `parent_id` bigint NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL,
  `sort` int NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织架构';

-- ---------- 通知 ----------
DROP TABLE IF EXISTS `notice_template`;
DROP TABLE IF EXISTS `notification`;

CREATE TABLE `notification` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `title` varchar(200) NOT NULL,
  `content` text,
  `type` tinyint NOT NULL DEFAULT '1' COMMENT '1站内2邮件3webhook',
  `is_read` tinyint NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知';

CREATE TABLE `notice_template` (
  `id` bigint NOT NULL,
  `template_code` varchar(50) NOT NULL,
  `title` varchar(200) DEFAULT NULL,
  `content` text,
  `type` tinyint DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_code` (`template_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知模板';

SET FOREIGN_KEY_CHECKS = 1;

-- ---------- 初始数据 ----------
-- 密码为明文 password
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `email`, `phone`, `status`, `create_time`, `update_time`, `deleted`) VALUES
(1, 'admin', 'password', '系统管理员', 'admin@pm-system.local', NULL, 1, NOW(), NOW(), 0);

INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `permissions`, `create_time`, `update_time`, `deleted`) VALUES
(1, '超级管理员', 'admin', '["*:*:*"]', NOW(), NOW(), 0),
(2, '项目经理', 'manager', '[]', NOW(), NOW(), 0),
(3, '开发', 'dev', '[]', NOW(), NOW(), 0),
(4, '测试', 'tester', '[]', NOW(), NOW(), 0);

INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `deleted`) VALUES
(1, 1, 1, NOW(), NOW(), 0);

INSERT INTO `system_config` (`id`, `config_key`, `config_value`, `remark`, `create_time`, `update_time`, `deleted`) VALUES
(1, 'sys.name', 'pm-system', '系统名称', NOW(), NOW(), 0);

INSERT INTO `organization` (`id`, `parent_id`, `name`, `sort`, `create_time`, `update_time`, `deleted`) VALUES
(1, 0, '总公司', 0, NOW(), NOW(), 0);

-- 任务状态字典示例
INSERT INTO `system_dict` (`id`, `dict_type`, `dict_label`, `dict_value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES
(1, 'task_status', '待办', 'todo', 1, NOW(), NOW(), 0),
(2, 'task_status', '进行中', 'doing', 2, NOW(), NOW(), 0),
(3, 'task_status', '已完成', 'done', 3, NOW(), NOW(), 0),
(4, 'task_status', '已关闭', 'closed', 4, NOW(), NOW(), 0);
