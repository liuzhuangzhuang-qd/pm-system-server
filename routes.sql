-- 前端路由/目录初始化（MySQL 8）
-- 用途：把前端 router.ts 的 constantRoutes + asyncRoutes 落库，供后端接口下发

DROP TABLE IF EXISTS `sys_route`;

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
(1000,0,'constant',1,'/login','Login','views/login/Login',NULL,'{\"hidden\":true}',NOW(),NOW(),0),
(1001,0,'constant',2,'/403','NoAuth','views/error/NoAuth',NULL,'{\"hidden\":true}',NOW(),NOW(),0),
(1002,0,'constant',3,'/','Root','Layout','/dashboard','{\"title\":\"工作台\",\"icon\":\"House\",\"requiresAuth\":true}',NOW(),NOW(),0),
(1003,1002,'constant',1,'/dashboard','Dashboard','views/dashboard/Dashboard',NULL,'{\"title\":\"首页\",\"requiresAuth\":true}',NOW(),NOW(),0),

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
(2100,2000,'async',1,'list','ProjectList','views/project/ProjectList',NULL,'{\"title\":\"项目列表\"}',NOW(),NOW(),0),
(2101,2000,'async',2,'detail/:id','ProjectDetail','views/project/ProjectDetail',NULL,'{\"title\":\"项目详情\",\"hidden\":true}',NOW(),NOW(),0),
(2102,2000,'async',3,'config','ProjectConfig','views/project/ProjectConfig',NULL,'{\"title\":\"项目配置\"}',NOW(),NOW(),0),

-- task children
(2200,2001,'async',1,'list','TaskList','views/task/TaskList',NULL,'{\"title\":\"任务列表\"}',NOW(),NOW(),0),
(2201,2001,'async',2,'detail/:id','TaskDetail','views/task/TaskDetail',NULL,'{\"title\":\"任务详情\",\"hidden\":true}',NOW(),NOW(),0),
(2202,2001,'async',3,'create','TaskCreate','views/task/TaskCreate',NULL,'{\"title\":\"创建任务\"}',NOW(),NOW(),0),

-- board children
(2300,2002,'async',1,'kanban','Kanban','views/board/Kanban',NULL,'{\"title\":\"看板\"}',NOW(),NOW(),0),

-- bug children
(2400,2003,'async',1,'list','BugList','views/bug/BugList',NULL,'{\"title\":\"缺陷列表\"}',NOW(),NOW(),0),
(2401,2003,'async',2,'detail/:id','BugDetail','views/bug/BugDetail',NULL,'{\"title\":\"缺陷详情\",\"hidden\":true}',NOW(),NOW(),0),
(2402,2003,'async',3,'create','BugCreate','views/bug/BugCreate',NULL,'{\"title\":\"缺陷提报\"}',NOW(),NOW(),0),

-- report children
(2500,2004,'async',1,'project','ProjectReport','views/report/ProjectReport',NULL,'{\"title\":\"项目进度报表\"}',NOW(),NOW(),0),
(2501,2004,'async',2,'task','TaskReport','views/report/TaskReport',NULL,'{\"title\":\"任务完成率报表\"}',NOW(),NOW(),0),
(2502,2004,'async',3,'bug','BugReport','views/report/BugReport',NULL,'{\"title\":\"缺陷分布报表\"}',NOW(),NOW(),0),

-- system children
(2600,2005,'async',1,'theme','ThemeSettings','views/system/ThemeSettings',NULL,'{\"title\":\"系统主题\"}',NOW(),NOW(),0),
(2601,2005,'async',2,'dict','DictList','views/system/DictList',NULL,'{\"title\":\"数据字典\"}',NOW(),NOW(),0),
(2602,2005,'async',3,'config','ConfigList','views/system/ConfigList',NULL,'{\"title\":\"系统配置\"}',NOW(),NOW(),0),
(2603,2005,'async',4,'org','OrgList','views/system/OrgList',NULL,'{\"title\":\"组织机构\"}',NOW(),NOW(),0),

-- auth children
(2700,2006,'async',1,'user','UserList','views/auth/UserList',NULL,'{\"title\":\"用户管理\"}',NOW(),NOW(),0),
(2701,2006,'async',2,'role','RoleList','views/auth/RoleList',NULL,'{\"title\":\"角色管理\"}',NOW(),NOW(),0),

-- notice children
(2800,2007,'async',1,'list','NoticeList','views/notice/NoticeList',NULL,'{\"title\":\"通知列表\"}',NOW(),NOW(),0);

