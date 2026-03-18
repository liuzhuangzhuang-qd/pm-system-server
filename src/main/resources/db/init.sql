-- ============================================================
-- pm-system 数据库初始化脚本（MySQL 8）
-- 使用方式：在本地库 pm_system 中执行本文件（Navicat / mysql 客户端 / IDE）
-- 默认登录：admin / password（BCrypt，与 Spring Security 一致）
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ---------- 用户与权限 ----------
DROP TABLE IF EXISTS `sys_oper_log`;
DROP TABLE IF EXISTS `sys_route`;
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

CREATE TABLE `sys_route` (
  `id` bigint NOT NULL COMMENT '主键',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父级ID，0为顶级',
  `route_type` varchar(20) NOT NULL COMMENT 'constant/async',
  `sort` int NOT NULL DEFAULT '0',
  `path` varchar(200) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `component` varchar(255) DEFAULT NULL,
  `redirect` varchar(255) DEFAULT NULL,
  `meta_json` text COMMENT 'meta JSON',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_type_sort` (`route_type`, `sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='前端路由/目录';

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

-- ---------- 前端路由/目录（用于动态菜单/路由下发） ----------
CREATE TABLE `sys_route` (
  `id` bigint NOT NULL COMMENT '主键',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父级ID，0为顶级',
  `route_type` varchar(20) NOT NULL COMMENT 'constant/async',
  `sort` int NOT NULL DEFAULT '0',
  `path` varchar(200) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `component` varchar(255) DEFAULT NULL,
  `redirect` varchar(255) DEFAULT NULL,
  `meta_json` text COMMENT 'meta JSON',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_type_sort` (`route_type`, `sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='前端路由/目录';

INSERT INTO `sys_route` (`id`,`parent_id`,`route_type`,`sort`,`path`,`name`,`component`,`redirect`,`meta_json`,`create_time`,`update_time`,`deleted`) VALUES
-- constantRoutes
(1000,0,'constant',1,'/login','Login','views/login/index',NULL,'{\"hidden\":true}',NOW(),NOW(),0),
(1001,0,'constant',2,'/403','NoAuth','views/error/noAuth/index',NULL,'{\"hidden\":true}',NOW(),NOW(),0),
(1002,0,'constant',3,'/','Root','Layout','/dashboard','{\"title\":\"工作台\",\"icon\":\"House\",\"requiresAuth\":true}',NOW(),NOW(),0),
(1003,1002,'constant',1,'/dashboard','Dashboard','views/dashboard/index',NULL,'{\"title\":\"首页\",\"requiresAuth\":true}',NOW(),NOW(),0),

-- asyncRoutes（顶级）
(2000,0,'async',10,'/project',NULL,'Layout',NULL,'{\"title\":\"项目管理\",\"icon\":\"FolderOpened\",\"requiresAuth\":true,\"roles\":[\"ADMIN\",\"PM\"]}',NOW(),NOW(),0),
(2001,0,'async',20,'/task',NULL,'Layout',NULL,'{\"title\":\"任务管理\",\"icon\":\"List\",\"requiresAuth\":true,\"roles\":[\"ADMIN\",\"PM\",\"DEV\"]}',NOW(),NOW(),0),
(2002,0,'async',30,'/board',NULL,'Layout',NULL,'{\"title\":\"任务看板\",\"icon\":\"Collection\",\"requiresAuth\":true,\"roles\":[\"ADMIN\",\"PM\",\"DEV\"]}',NOW(),NOW(),0),
(2003,0,'async',40,'/bug',NULL,'Layout',NULL,'{\"title\":\"缺陷管理\",\"icon\":\"Warning\",\"requiresAuth\":true,\"roles\":[\"ADMIN\",\"QA\"]}',NOW(),NOW(),0),
(2004,0,'async',50,'/report',NULL,'Layout',NULL,'{\"title\":\"统计报表\",\"icon\":\"DataAnalysis\",\"requiresAuth\":true,\"roles\":[\"ADMIN\",\"PM\"]}',NOW(),NOW(),0),
(2005,0,'async',60,'/system',NULL,'Layout',NULL,'{\"title\":\"系统管理\",\"icon\":\"Setting\",\"requiresAuth\":true,\"roles\":[\"ADMIN\"]}',NOW(),NOW(),0),
(2006,0,'async',70,'/auth',NULL,'Layout',NULL,'{\"title\":\"权限管理\",\"icon\":\"UserFilled\",\"requiresAuth\":true,\"roles\":[\"ADMIN\"]}',NOW(),NOW(),0),
(2007,0,'async',80,'/notice',NULL,'Layout',NULL,'{\"title\":\"通知消息\",\"icon\":\"Bell\",\"requiresAuth\":true,\"roles\":[\"ADMIN\",\"PM\",\"DEV\",\"QA\"]}',NOW(),NOW(),0),

-- project children
(2100,2000,'async',1,'list','ProjectList','views/project/projectList/index',NULL,'{\"title\":\"项目列表\"}',NOW(),NOW(),0),
(2101,2000,'async',2,'detail/:id','ProjectDetail','views/project/projectDetail/index',NULL,'{\"title\":\"项目详情\",\"hidden\":true}',NOW(),NOW(),0),
(2102,2000,'async',3,'config','ProjectConfig','views/project/projectConfig/index',NULL,'{\"title\":\"项目配置\"}',NOW(),NOW(),0),

-- task children
(2200,2001,'async',1,'list','TaskList','views/task/taskList/index',NULL,'{\"title\":\"任务列表\"}',NOW(),NOW(),0),
(2201,2001,'async',2,'detail/:id','TaskDetail','views/task/taskDetail/index',NULL,'{\"title\":\"任务详情\",\"hidden\":true}',NOW(),NOW(),0),
(2202,2001,'async',3,'create','TaskCreate','views/task/taskCreate/index',NULL,'{\"title\":\"创建任务\"}',NOW(),NOW(),0),

-- board children
(2300,2002,'async',1,'kanban','Kanban','views/board/kanban/index',NULL,'{\"title\":\"看板\"}',NOW(),NOW(),0),

-- bug children
(2400,2003,'async',1,'list','BugList','views/bug/bugList/index',NULL,'{\"title\":\"缺陷列表\"}',NOW(),NOW(),0),
(2401,2003,'async',2,'detail/:id','BugDetail','views/bug/bugDetail/index',NULL,'{\"title\":\"缺陷详情\",\"hidden\":true}',NOW(),NOW(),0),
(2402,2003,'async',3,'create','BugCreate','views/bug/bugCreate/index',NULL,'{\"title\":\"缺陷提报\"}',NOW(),NOW(),0),

-- report children
(2500,2004,'async',1,'project','ProjectReport','views/report/projectReport/index',NULL,'{\"title\":\"项目进度报表\"}',NOW(),NOW(),0),
(2501,2004,'async',2,'task','TaskReport','views/report/taskReport/index',NULL,'{\"title\":\"任务完成率报表\"}',NOW(),NOW(),0),
(2502,2004,'async',3,'bug','BugReport','views/report/bugReport/index',NULL,'{\"title\":\"缺陷分布报表\"}',NOW(),NOW(),0),

-- system children
(2600,2005,'async',1,'theme','ThemeSettings','views/system/themeSettings/index',NULL,'{\"title\":\"系统主题\"}',NOW(),NOW(),0),
(2601,2005,'async',2,'dict','DictList','views/system/dictList/index',NULL,'{\"title\":\"数据字典\"}',NOW(),NOW(),0),
(2602,2005,'async',3,'config','ConfigList','views/system/configList/index',NULL,'{\"title\":\"系统配置\"}',NOW(),NOW(),0),
(2603,2005,'async',4,'org','OrgList','views/system/orgList/index',NULL,'{\"title\":\"组织机构\"}',NOW(),NOW(),0),

-- auth children
(2700,2006,'async',1,'user','UserList','views/auth/userList/index',NULL,'{\"title\":\"用户管理\"}',NOW(),NOW(),0),
(2701,2006,'async',2,'role','RoleList','views/auth/roleList/index',NULL,'{\"title\":\"角色管理\"}',NOW(),NOW(),0),

-- notice children
(2800,2007,'async',1,'list','NoticeList','views/notice/noticeList/index',NULL,'{\"title\":\"通知列表\"}',NOW(),NOW(),0);
