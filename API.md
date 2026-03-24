## 后端接口文档（根目录生成）

### 通用说明
- **Base URL**: `http://localhost:8080`
- **统一返回**: `Result<T>`
  - `code`(int), `msg`(string), `data`(T)
- **分页返回**: `Result<PageResult<T>>`
  - `data.total`(long), `data.list`(T[])
- **鉴权（JWT）**：
  - `POST /api/auth/createLoginToken`、`POST /api/auth/updateLoginToken`、`GET /api/getHealth` **不需要** token
  - 其它 `/api/**` 接口需要 Header：`Authorization: Bearer <token>`
- **请求体**：
  - 所有 `POST/PUT` 且带 JSON body 的接口：建议 `Content-Type: application/json`
  - 更新时间/逻辑删除字段（`id/createTime/updateTime/deleted`）通常可不传（由服务端/ORM处理）
- **时间格式**：`LocalDateTime` JSON 建议使用 `yyyy-MM-dd HH:mm:ss`（与 `application.yml` 的 `jackson.date-format` 一致）

---

### 健康检查
- **GET** `/api/getHealth`
  - **响应**：`OK`（纯文本）

---

### 认证（/api/auth）

#### 登录
- **POST** `/api/auth/createLoginToken`
- **Body(JSON)**：
  - `username` (string, required)
  - `password` (string, required)（明文比对）
- **Resp**：`Result<LoginResult>`
  - `data.token` 为 JWT 字符串

#### 刷新 token
- **POST** `/api/auth/updateLoginToken?token=xxx`
- **Query**：
  - `token` (string, required) 旧 token
- **Resp**：`Result<LoginResult>`

#### 登出
- **POST** `/api/auth/deleteLoginToken`
- **Body**：无
- **Resp**：`Result<Void>`

#### 当前登录用户信息
- **GET** `/api/auth/getUserInfo`
- **Header**：`Authorization: Bearer <token>`
- **Body/Query**：无
- **Resp**：`Result<SysUser>`
  - `password` 字段返回为 `null`

#### 前端路由/目录下发（动态菜单）
- **GET** `/api/auth/getRoutes`
- **Header**：`Authorization: Bearer <token>`
- **Body/Query**：无
- **Resp**：`Result<{ constantRoutes: Route[], asyncRoutes: Route[] }>`
- **过滤规则**：`asyncRoutes` 按当前用户角色过滤
  - 库表 `sys_user_role` → `sys_role.role_code`
  - 映射：`admin`→超级管理员（不过滤）、`manager`→`PM`、`dev`→`DEV`、`tester`→`QA`
  - 若路由 `meta.roles` 存在：需与用户角色有交集才返回（子路由全无权限则父级一并剔除）

---

### 前端路由 CRUD（/api/routes）

说明：对 `sys_route` 表的管理接口，用于增删改查前端路由/菜单配置。

- **GET** `/api/routes/getRouteById/{id}`
  - **Path**：`id` (long, required)
  - **Header**：`Authorization: Bearer <token>`
  - **Resp**：`Result<SysRoute>`

- **GET** `/api/routes/getRouteList?routeType=async&pageNo=1&pageSize=10&parentId=0`
  - **Query（可选）**：
    - `routeType` (string) 可选：`constant` / `async`
    - `parentId` (long) 可选：父级路由 ID（0 为顶级）
    - `pageNo` (long, default 1)（当前 tree 展示不做分页）
    - `pageSize` (long, default 10)（当前 tree 展示不做分页）
  - **Header**：`Authorization: Bearer <token>`
  - **Resp**：`Result<{ constantRoutes: Route[], asyncRoutes: Route[] }>`

- **POST** `/api/routes/createRoute`
  - **Body(JSON)**：`SysRoute`
  - **Header**：`Authorization: Bearer <token>`
  - **Resp**：`Result<Boolean>`

- **PUT** `/api/routes/updateRoute/{id}`
  - **Path**：`id` (long, required)（服务端会 `route.setId(id)`）
  - **Body(JSON)**：`SysRoute`（字段同新增）
  - **Header**：`Authorization: Bearer <token>`
  - **Resp**：`Result<Boolean>`

- **DELETE** `/api/routes/deleteRoute/{id}`
  - **Path**：`id` (long, required)
  - **Header**：`Authorization: Bearer <token>`
  - **Resp**：`Result<Boolean>`

---

### 用户（/api/users）

- **GET** `/api/users/getUserById/{id}`
  - **Path**：`id` (long, required)
  - **Resp**：`Result<SysUser>`

- **POST** `/api/users/createUser`
  - **Body(JSON)**：`SysUser`
    - `username` (string, required)
    - `password` (string, required)（明文）
    - `realName` (string, optional)
    - `email` (string, optional)
    - `phone` (string, optional)
    - `status` (int, optional)
  - **Resp**：`Result<Boolean>`

- **PUT** `/api/users/updateUser/{id}`
  - **Path**：`id` (long, required)（服务端会 `user.setId(id)`）
  - **Body(JSON)**：`SysUser`（字段同创建；`id` 可不传）
  - **Resp**：`Result<Boolean>`

- **DELETE** `/api/users/deleteUser/{id}`
  - **Path**：`id` (long, required)
  - **Resp**：`Result<Boolean>`

- **GET** `/api/users/getUserList?pageNo=1&pageSize=10`
  - **Query**：
    - `pageNo` (long, default 1)
    - `pageSize` (long, default 10)
  - **Resp**：`Result<PageResult<SysUser>>`

---

### 角色（/api/roles）

- **POST** `/api/roles/createRole`
  - **Body(JSON)**：`SysRole`
    - `roleName` (string, required)
    - `roleCode` (string, required)（如 `admin/manager/dev/tester`）
    - `permissions` (string, optional)（JSON 字符串）
  - **Resp**：`Result<Boolean>`

- **PUT** `/api/roles/updateRole/{id}`
  - **Path**：`id` (long, required)（服务端会 `role.setId(id)`）
  - **Body(JSON)**：`SysRole`（字段同创建；`id` 可不传）
  - **Resp**：`Result<Boolean>`

- **GET** `/api/roles/getRoleById/{id}`
  - **Path**：`id` (long, required)
  - **Resp**：`Result<SysRole>`

- **GET** `/api/roles/getRoleList?pageNo=1&pageSize=10`
  - **Query**：
    - `pageNo` (long, default 1)
    - `pageSize` (long, default 10)
  - **Resp**：`Result<PageResult<SysRole>>`

---

### 项目（/api/projects）

- **GET** `/api/projects/getProjectById/{id}`
  - **Path**：`id` (long, required)
  - **Resp**：`Result<Project>`

- **POST** `/api/projects/createProject`
  - **Body(JSON)**：`Project`
    - `projectName` (string, required)
    - `code` (string, optional)
    - `description` (string, optional)
    - `managerId` (long, optional)
    - `status` (string, optional)（0草稿/1进行中/2归档）
  - **Resp**：`Result<Boolean>`

- **PUT** `/api/projects/updateProject/{id}`
  - **Path**：`id` (long, required)（服务端会 `project.setId(id)`）
  - **Body(JSON)**：`Project`（同创建；`id` 可不传）
  - **Resp**：`Result<Boolean>`

- **DELETE** `/api/projects/deleteProject/{id}`
  - **Path**：`id` (long, required)
  - **Resp**：`Result<Boolean>`

- **GET** `/api/projects/getProjectList?pageNo=1&pageSize=10`
  - **Query**：`pageNo`、`pageSize`（默认同上）
  - **Resp**：`Result<PageResult<Project>>`

---

### 项目成员（/api/project-members）

- **POST** `/api/project-members/createProjectMember`
  - **Body(JSON)**：`ProjectMember`
    - `projectId` (long, required)
    - `userId` (long, required)
    - `roleInProject` (string, optional)
  - **Resp**：`Result<Boolean>`

- **DELETE** `/api/project-members/deleteProjectMember/{id}`
  - **Path**：`id` (long, required)
  - **Resp**：`Result<Boolean>`

- **GET** `/api/project-members/getProjectMemberList?projectId=xxx&pageNo=1&pageSize=10`
  - **Query**：
    - `projectId` (long, required)
    - `pageNo` (long, default 1)
    - `pageSize` (long, default 10)
  - **Resp**：`Result<PageResult<ProjectMember>>`

---

### 项目配置（/api/project-config）

- **GET** `/api/project-config/getProjectConfig/{projectId}`
  - **Path**：`projectId` (long, required)
  - **Resp**：`Result<ProjectConfig>`（可能为 null）

- **POST** `/api/project-config/saveProjectConfig`
  - **Body(JSON)**：`ProjectConfig`
    - `projectId` (long, required)
    - `workflowConfig` (string, required/optional，按前端约定传 JSON 字符串)
    - `customFields` (string, required/optional，按前端约定传 JSON 字符串)
  - **Resp**：`Result<Boolean>`

---

### 任务（/api/tasks）

- **GET** `/api/tasks/getTaskById/{id}`
  - **Path**：`id` (long, required)
  - **Resp**：`Result<Task>`

- **POST** `/api/tasks/createTask`
  - **Body(JSON)**：`Task`
    - `title` (string, required)
    - `projectId` (long, required)
    - `parentId` (long, optional)
    - `priority` (int, optional)（1低/2中/3高）
    - `status` (string, optional)（todo/doing/done/closed）
    - `assigneeId` (long, optional)
    - `creatorId` (long, optional)
    - `startTime` (string, optional) `yyyy-MM-dd HH:mm:ss`
    - `endTime` (string, optional) `yyyy-MM-dd HH:mm:ss`
    - `description` (string, optional)
  - **Resp**：`Result<Boolean>`

- **PUT** `/api/tasks/updateTask/{id}`
  - **Path**：`id` (long, required)（服务端会 `task.setId(id)`）
  - **Body(JSON)**：`Task`（同创建；`id` 可不传）
  - **Resp**：`Result<Boolean>`

- **DELETE** `/api/tasks/deleteTask/{id}`
  - **Path**：`id` (long, required)
  - **Resp**：`Result<Boolean>`

- **GET** `/api/tasks/getTaskList?projectId=xxx&pageNo=1&pageSize=10`
  - **Query**：
    - `projectId` (long, optional)
    - `pageNo` (long, default 1)
    - `pageSize` (long, default 10)
  - **Resp**：`Result<PageResult<Task>>`

---

### 任务评论（/api/task-comments）

- **POST** `/api/task-comments/createTaskComment`
  - **Body(JSON)**：`TaskComment`
    - `taskId` (long, required)
    - `userId` (long, required)
    - `content` (string, required)
  - **Resp**：`Result<Boolean>`

- **DELETE** `/api/task-comments/deleteTaskComment/{id}`
  - **Path**：`id` (long, required)
  - **Resp**：`Result<Boolean>`

- **GET** `/api/task-comments/getTaskCommentList?taskId=xxx&pageNo=1&pageSize=10`
  - **Query**：
    - `taskId` (long, required)
    - `pageNo` (long, default 1)
    - `pageSize` (long, default 10)
  - **Resp**：`Result<PageResult<TaskComment>>`

---

### 任务附件（/api/task-attachments）

- **POST** `/api/task-attachments/createTaskAttachment`
  - **Body(JSON)**：`TaskAttachment`
    - `taskId` (long, required)
    - `fileName` (string, optional)
    - `filePath` (string, optional)
    - `fileSize` (long, optional)
  - **Resp**：`Result<Boolean>`

- **DELETE** `/api/task-attachments/deleteTaskAttachment/{id}`
  - **Path**：`id` (long, required)
  - **Resp**：`Result<Boolean>`

- **GET** `/api/task-attachments/getTaskAttachmentList?taskId=xxx&pageNo=1&pageSize=10`
  - **Query**：
    - `taskId` (long, required)
    - `pageNo` (long, default 1)
    - `pageSize` (long, default 10)
  - **Resp**：`Result<PageResult<TaskAttachment>>`

---

### 任务关注人（/api/task-followers）

- **POST** `/api/task-followers/createTaskFollower`
  - **Body(JSON)**：`TaskFollower`
    - `taskId` (long, required)
    - `userId` (long, required)
  - **Resp**：`Result<Boolean>`

- **DELETE** `/api/task-followers/deleteTaskFollower/{id}`
  - **Path**：`id` (long, required)
  - **Resp**：`Result<Boolean>`

- **GET** `/api/task-followers/getTaskFollowerList?taskId=xxx&pageNo=1&pageSize=10`
  - **Query**：
    - `taskId` (long, required)
    - `pageNo` (long, default 1)
    - `pageSize` (long, default 10)
  - **Resp**：`Result<PageResult<TaskFollower>>`

---

### 任务历史（/api/task-history）

- **GET** `/api/task-history/getTaskHistoryList?taskId=xxx&pageNo=1&pageSize=10`
  - **Query**：
    - `taskId` (long, required)
    - `pageNo` (long, default 1)
    - `pageSize` (long, default 10)
  - **Resp**：`Result<PageResult<TaskHistory>>`

---

### 数据字典（/api/dict）

- **POST** `/api/dict/createDictionary`（新增字典项）
  - **Body(JSON)**：`Dictionary`
    - `dictType` (string, required) 字典类型，如 `task_status`
    - `dictLabel` (string, required) 字典标签（展示用）
    - `dictValue` (string, required) 字典值（存储/代码用）
    - `sort` (int, optional) 排序，升序，默认 0
  - **Resp**：`Result<Boolean>`

- **PUT** `/api/dict/updateDictionary/{id}`（修改字典项）
  - **Path**：`id` (long, required)
  - **Body(JSON)**：`Dictionary`（字段同新增；`id` 可不传，以 Path 为准）
  - **Resp**：`Result<Boolean>`

- **GET** `/api/dict/getDictionaryById/{id}`（查询单条）
  - **Path**：`id` (long, required)
  - **Resp**：`Result<Dictionary>`

- **DELETE** `/api/dict/deleteDictionary/{id}`（删除字典项）
  - **Path**：`id` (long, required)
  - **Resp**：`Result<Boolean>`

- **GET** `/api/dict/getDictionaryList?dictType=xxx&pageNo=1&pageSize=10`（分页查询列表，可按类型过滤）
  - **Query**：
    - `dictType` (string, optional) 按字典类型过滤
    - `pageNo` (long, default 1)
    - `pageSize` (long, default 10)
  - **Resp**：`Result<PageResult<Dictionary>>`
  - **说明**：列表按 `dictType`、`sort` 升序

---

### 旧 URL -> 新 URL 对照表（前端迁移）

#### 认证与健康检查
- `GET /api/health` -> `GET /api/getHealth`
- `POST /api/auth/login` -> `POST /api/auth/createLoginToken`
- `POST /api/auth/refresh` -> `POST /api/auth/updateLoginToken`
- `POST /api/auth/logout` -> `POST /api/auth/deleteLoginToken`
- `GET /api/auth/user-info` -> `GET /api/auth/getUserInfo`
- `GET /api/auth/routes` -> `GET /api/auth/getRoutes`

#### 用户
- `GET /api/users/{id}` -> `GET /api/users/getUserById/{id}`
- `POST /api/users` -> `POST /api/users/createUser`
- `PUT /api/users/{id}` -> `PUT /api/users/updateUser/{id}`
- `DELETE /api/users/{id}` -> `DELETE /api/users/deleteUser/{id}`
- `GET /api/users?pageNo=1&pageSize=10` -> `GET /api/users/getUserList?pageNo=1&pageSize=10`

#### 角色
- `POST /api/roles` -> `POST /api/roles/createRole`
- `PUT /api/roles/{id}` -> `PUT /api/roles/updateRole/{id}`
- `GET /api/roles/{id}` -> `GET /api/roles/getRoleById/{id}`
- `GET /api/roles?pageNo=1&pageSize=10` -> `GET /api/roles/getRoleList?pageNo=1&pageSize=10`

#### 项目
- `GET /api/projects/{id}` -> `GET /api/projects/getProjectById/{id}`
- `POST /api/projects` -> `POST /api/projects/createProject`
- `PUT /api/projects/{id}` -> `PUT /api/projects/updateProject/{id}`
- `DELETE /api/projects/{id}` -> `DELETE /api/projects/deleteProject/{id}`
- `GET /api/projects?pageNo=1&pageSize=10` -> `GET /api/projects/getProjectList?pageNo=1&pageSize=10`

#### 项目成员
- `POST /api/project-members` -> `POST /api/project-members/createProjectMember`
- `DELETE /api/project-members/{id}` -> `DELETE /api/project-members/deleteProjectMember/{id}`
- `GET /api/project-members?projectId=xxx&pageNo=1&pageSize=10` -> `GET /api/project-members/getProjectMemberList?projectId=xxx&pageNo=1&pageSize=10`

#### 项目配置
- `GET /api/project-config/{projectId}` -> `GET /api/project-config/getProjectConfig/{projectId}`
- `POST /api/project-config` -> `POST /api/project-config/saveProjectConfig`

#### 任务
- `GET /api/tasks/{id}` -> `GET /api/tasks/getTaskById/{id}`
- `POST /api/tasks` -> `POST /api/tasks/createTask`
- `PUT /api/tasks/{id}` -> `PUT /api/tasks/updateTask/{id}`
- `DELETE /api/tasks/{id}` -> `DELETE /api/tasks/deleteTask/{id}`
- `GET /api/tasks?projectId=xxx&pageNo=1&pageSize=10` -> `GET /api/tasks/getTaskList?projectId=xxx&pageNo=1&pageSize=10`

#### 任务评论
- `POST /api/task-comments` -> `POST /api/task-comments/createTaskComment`
- `DELETE /api/task-comments/{id}` -> `DELETE /api/task-comments/deleteTaskComment/{id}`
- `GET /api/task-comments?taskId=xxx&pageNo=1&pageSize=10` -> `GET /api/task-comments/getTaskCommentList?taskId=xxx&pageNo=1&pageSize=10`

#### 任务附件
- `POST /api/task-attachments` -> `POST /api/task-attachments/createTaskAttachment`
- `DELETE /api/task-attachments/{id}` -> `DELETE /api/task-attachments/deleteTaskAttachment/{id}`
- `GET /api/task-attachments?taskId=xxx&pageNo=1&pageSize=10` -> `GET /api/task-attachments/getTaskAttachmentList?taskId=xxx&pageNo=1&pageSize=10`

#### 任务关注人
- `POST /api/task-followers` -> `POST /api/task-followers/createTaskFollower`
- `DELETE /api/task-followers/{id}` -> `DELETE /api/task-followers/deleteTaskFollower/{id}`
- `GET /api/task-followers?taskId=xxx&pageNo=1&pageSize=10` -> `GET /api/task-followers/getTaskFollowerList?taskId=xxx&pageNo=1&pageSize=10`

#### 任务历史
- `GET /api/task-history?taskId=xxx&pageNo=1&pageSize=10` -> `GET /api/task-history/getTaskHistoryList?taskId=xxx&pageNo=1&pageSize=10`

#### 数据字典
- `POST /api/dict` -> `POST /api/dict/createDictionary`
- `PUT /api/dict/{id}` -> `PUT /api/dict/updateDictionary/{id}`
- `GET /api/dict/{id}` -> `GET /api/dict/getDictionaryById/{id}`
- `DELETE /api/dict/{id}` -> `DELETE /api/dict/deleteDictionary/{id}`
- `GET /api/dict?dictType=xxx&pageNo=1&pageSize=10` -> `GET /api/dict/getDictionaryList?dictType=xxx&pageNo=1&pageSize=10`

