## 后端接口文档（根目录生成）

### 通用说明
- **Base URL**: `http://localhost:8080`
- **统一返回**: `Result<T>`：`code` / `msg` / `data`
- **分页返回**: `Result<PageResult<T>>`：`data.total` / `data.records`
- **鉴权（JWT）**：
  - `POST /api/auth/login`、`POST /api/auth/refresh` **不需要** token
  - 其它 `/api/**` 接口默认需要 Header：`Authorization: Bearer <token>`

---

### 健康检查
- **GET** `/api/health`
  - **响应**：`OK`（纯文本）

---

### 认证（/api/auth）

#### 登录
- **POST** `/api/auth/login`
- **Body(JSON)**：`{ "username": string, "password": string }`（明文比对）
- **Resp**：`Result<LoginResult>`，`data.token` 为 JWT 字符串

#### 刷新 token
- **POST** `/api/auth/refresh?token=xxx`
- **Resp**：`Result<LoginResult>`

#### 登出
- **POST** `/api/auth/logout`
- **Resp**：`Result<Void>`

#### 当前登录用户信息
- **GET** `/api/auth/user-info`
- **Header**：`Authorization: Bearer <token>`
- **Resp**：`Result<SysUser>`（`password` 返回为 `null`）

#### 前端路由/目录下发（动态菜单）
- **GET** `/api/auth/routes`
- **Header**：`Authorization: Bearer <token>`
- **Resp**：`Result<{ constantRoutes: Route[], asyncRoutes: Route[] }>`
- **过滤规则**：`asyncRoutes` 按当前用户角色过滤（库表 `sys_user_role` → `sys_role.role_code`）
  - **映射**：`admin`→超级管理员（不过滤）、`manager`→`PM`、`dev`→`DEV`、`tester`→`QA`
  - 若路由 `meta.roles` 存在，则需与用户角色有交集才返回（子路由全无权限则父级一并剔除）

---

### 用户（/api/users）
- **GET** `/api/users/{id}`：获取用户
- **POST** `/api/users`：创建用户（Body：`SysUser`）
- **PUT** `/api/users/{id}`：更新用户（Body：`SysUser`）
- **DELETE** `/api/users/{id}`：删除用户
- **GET** `/api/users?pageNo=1&pageSize=10`：分页列表
  - **Resp**：`Result<PageResult<SysUser>>`

---

### 角色（/api/roles）
- **POST** `/api/roles`：新增角色（Body：`SysRole`）
- **PUT** `/api/roles/{id}`：修改角色（Body：`SysRole`）
- **GET** `/api/roles/{id}`：角色详情
- **GET** `/api/roles?pageNo=1&pageSize=10`：分页列表
  - **Resp**：`Result<PageResult<SysRole>>`

---

### 项目（/api/projects）
- **GET** `/api/projects/{id}`
- **POST** `/api/projects`
- **PUT** `/api/projects/{id}`
- **DELETE** `/api/projects/{id}`
- **GET** `/api/projects?pageNo=1&pageSize=10`
  - **Resp**：`Result<PageResult<Project>>`

---

### 项目成员（/api/project-members）
- **POST** `/api/project-members`：添加成员（Body：`ProjectMember`）
- **DELETE** `/api/project-members/{id}`：移除成员
- **GET** `/api/project-members?projectId=xxx&pageNo=1&pageSize=10`
  - **Resp**：`Result<PageResult<ProjectMember>>`

---

### 项目配置（/api/project-config）
- **GET** `/api/project-config/{projectId}`：获取配置
- **POST** `/api/project-config`：保存/更新配置（按 `projectId` 唯一）

---

### 任务（/api/tasks）
- **GET** `/api/tasks/{id}`
- **POST** `/api/tasks`
- **PUT** `/api/tasks/{id}`
- **DELETE** `/api/tasks/{id}`
- **GET** `/api/tasks?projectId=xxx&pageNo=1&pageSize=10`
  - `projectId` 可选
  - **Resp**：`Result<PageResult<Task>>`

---

### 任务评论（/api/task-comments）
- **POST** `/api/task-comments`
- **DELETE** `/api/task-comments/{id}`
- **GET** `/api/task-comments?taskId=xxx&pageNo=1&pageSize=10`
  - **Resp**：`Result<PageResult<TaskComment>>`

---

### 任务附件（/api/task-attachments）
- **POST** `/api/task-attachments`
- **DELETE** `/api/task-attachments/{id}`
- **GET** `/api/task-attachments?taskId=xxx&pageNo=1&pageSize=10`
  - **Resp**：`Result<PageResult<TaskAttachment>>`

---

### 任务关注人（/api/task-followers）
- **POST** `/api/task-followers`
- **DELETE** `/api/task-followers/{id}`
- **GET** `/api/task-followers?taskId=xxx&pageNo=1&pageSize=10`
  - **Resp**：`Result<PageResult<TaskFollower>>`

---

### 任务历史（/api/task-history）
- **GET** `/api/task-history?taskId=xxx&pageNo=1&pageSize=10`（按时间倒序）
  - **Resp**：`Result<PageResult<TaskHistory>>`

