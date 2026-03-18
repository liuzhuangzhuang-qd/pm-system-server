## 后端接口文档（对前端对接）

### 通用说明
- **基础地址**: `http://localhost:8080`
- **鉴权**: 通过 JWT 进行无状态认证：
  - 登录/刷新接口（`/api/auth/login`、`/api/auth/refresh`、`/api/health`）无需携带 token；
  - 其它业务接口默认需要在 Header 中携带 `Authorization: Bearer <token>`。
- **统一返回结构**: 大部分接口返回 `Result<T>` 或 `PageResult<T>`，字段如下：
  - `Result<T>`: `code`(int), `msg`(string), `data`(T)
  - `PageResult<T>`: `total`(long), `records`(T[])

---

### 健康检查

- **GET** `/api/health`
  - 功能: 服务存活检查
  - 请求体: 无
  - 响应: 文本 `"OK"`

---

### 认证与登录（AuthController）

- **POST** `/api/auth/login`
  - 功能: 用户登录（明文密码比对），返回 JWT token，后续访问其它接口需在 Header 中带上 `Authorization: Bearer <token>`
  - 请求体(JSON):
    - `username` (string) 用户名
    - `password` (string) 明文密码
  - 响应: `Result<LoginResult>`
    - `data.token` (string)

- **POST** `/api/auth/refresh`
  - 功能: 刷新 token
  - 查询参数:
    - `token` (string) 旧的 token
  - 响应: `Result<LoginResult>`，结构同登录

- **POST** `/api/auth/logout`
  - 功能: 登出（目前后端仅返回成功，前端删除本地 token 即可）
  - 请求体: 无
  - 响应: `Result<Void>`

- **GET** `/api/auth/user-info`
  - 功能: 获取当前登录用户信息（需 JWT）
  - Header: `Authorization: Bearer <token>`
  - 响应: `Result<SysUser>`（`password` 字段为 `null`，不返回明文密码）

- **GET** `/api/auth/routes`
  - 功能: 下发前端路由目录（`constantRoutes` + `asyncRoutes`），需 JWT
  - **asyncRoutes 按当前用户角色过滤**：`sys_user_role` → `sys_role.role_code` 映射为前端 `meta.roles`（`admin`→超级管理员看全部；`manager`→`PM`，`dev`→`DEV`，`tester`→`QA`）；仅当用户角色与路由 `meta.roles` 有交集时保留该段及子路由

---

### 用户管理（UserController）

基路径: `/api/users`

- **GET** `/api/users/{id}`
  - 功能: 根据 ID 获取用户
  - 路径参数:
    - `id` (long) 用户ID
  - 响应: `Result<SysUser>`

- **POST** `/api/users`
  - 功能: 新增用户
  - 请求体(JSON) `SysUser`:
    - `username` (string)
    - `password` (string) 明文密码
    - `realName` (string, 可选)
    - `email` (string, 可选)
    - `phone` (string, 可选)
    - `status` (int, 可选)
  - 响应: `Result<Boolean>` (`true` 表示成功)

- **PUT** `/api/users/{id}`
  - 功能: 修改用户
  - 路径参数:
    - `id` (long)
  - 请求体(JSON) `SysUser` 同上（会将 `id` 设置为路径中的 id）
  - 响应: `Result<Boolean>`

- **DELETE** `/api/users/{id}`
  - 功能: 删除用户
  - 路径参数:
    - `id` (long)
  - 响应: `Result<Boolean>`

- **GET** `/api/users`
  - 功能: 分页查询用户
  - 查询参数:
    - `pageNo` (long, 默认 1)
    - `pageSize` (long, 默认 10)
  - 响应: `Result<PageResult<SysUser>>`

---

### 项目管理（ProjectController）

基路径: `/api/projects`

- **GET** `/api/projects/{id}`
  - 功能: 根据 ID 获取项目
  - 路径参数: `id` (long)
  - 响应: `Result<Project>`

- **POST** `/api/projects`
  - 功能: 新增项目
  - 请求体(JSON) `Project`
  - 响应: `Result<Boolean>`

- **PUT** `/api/projects/{id}`
  - 功能: 修改项目
  - 路径参数: `id` (long)
  - 请求体(JSON) `Project`（会设置 id）
  - 响应: `Result<Boolean>`

- **DELETE** `/api/projects/{id}`
  - 功能: 删除项目
  - 路径参数: `id` (long)
  - 响应: `Result<Boolean>`

- **GET** `/api/projects`
  - 功能: 分页查询项目
  - 查询参数:
    - `pageNo` (long, 默认 1)
    - `pageSize` (long, 默认 10)
  - 响应: `Result<PageResult<Project>>`

---

### 项目成员（ProjectMemberController）

基路径: `/api/project-members`

- **POST** `/api/project-members`
  - 功能: 添加项目成员
  - 请求体(JSON) `ProjectMember`:
    - `projectId` (long)
    - `userId` (long)
    - `roleInProject` (string, 可选)
  - 响应: `Result<Boolean>`

- **DELETE** `/api/project-members/{id}`
  - 功能: 移除项目成员
  - 路径参数: `id` (long)
  - 响应: `Result<Boolean>`

- **GET** `/api/project-members`
  - 功能: 分页查询项目成员
  - 查询参数:
    - `projectId` (long, 必填)
    - `pageNo` (long, 默认 1)
    - `pageSize` (long, 默认 10)
  - 响应: `Result<PageResult<ProjectMember>>`

---

### 项目配置（ProjectConfigController）

基路径: `/api/project-config`

- **GET** `/api/project-config/{projectId}`
  - 功能: 获取项目配置
  - 路径参数: `projectId` (long)
  - 响应: `Result<ProjectConfig>`（可能为 null）

- **POST** `/api/project-config`
  - 功能: 保存/更新项目配置（按 `projectId` 唯一）
  - 请求体(JSON) `ProjectConfig`:
    - `projectId` (long)
    - `workflowConfig` (string, JSON 字符串)
    - `customFields` (string, JSON 字符串)
  - 响应: `Result<Boolean>`

---

### 任务（TaskController）

基路径: `/api/tasks`

- **GET** `/api/tasks/{id}`
  - 功能: 根据 ID 获取任务
  - 路径参数: `id` (long)
  - 响应: `Result<Task>`

- **POST** `/api/tasks`
  - 功能: 新增任务
  - 请求体(JSON) `Task`
  - 响应: `Result<Boolean>`

- **PUT** `/api/tasks/{id}`
  - 功能: 修改任务
  - 路径参数: `id` (long)
  - 请求体(JSON) `Task`（会设置 id）
  - 响应: `Result<Boolean>`

- **DELETE** `/api/tasks/{id}`
  - 功能: 删除任务
  - 路径参数: `id` (long)
  - 响应: `Result<Boolean>`

- **GET** `/api/tasks`
  - 功能: 分页查询任务，可按项目过滤
  - 查询参数:
    - `projectId` (long, 可选)
    - `pageNo` (long, 默认 1)
    - `pageSize` (long, 默认 10)
  - 响应: `Result<PageResult<Task>>`

---

### 任务评论（TaskCommentController）

基路径: `/api/task-comments`

- **POST** `/api/task-comments`
  - 功能: 新增评论
  - 请求体(JSON) `TaskComment`:
    - `taskId` (long)
    - `userId` (long)
    - `content` (string)
  - 响应: `Result<Boolean>`

- **DELETE** `/api/task-comments/{id}`
  - 功能: 删除评论
  - 路径参数: `id` (long)
  - 响应: `Result<Boolean>`

- **GET** `/api/task-comments`
  - 功能: 分页查询任务评论
  - 查询参数:
    - `taskId` (long, 必填)
    - `pageNo` (long, 默认 1)
    - `pageSize` (long, 默认 10)
  - 响应: `Result<PageResult<TaskComment>>`

---

### 任务附件（TaskAttachmentController）

基路径: `/api/task-attachments`

- **POST** `/api/task-attachments`
  - 功能: 新增附件记录（仅元数据，实际上传流程需前后端约定）
  - 请求体(JSON) `TaskAttachment`:
    - `taskId` (long)
    - `fileName` (string)
    - `filePath` (string)
    - `fileSize` (long)
  - 响应: `Result<Boolean>`

- **DELETE** `/api/task-attachments/{id}`
  - 功能: 删除附件记录
  - 路径参数: `id` (long)
  - 响应: `Result<Boolean>`

- **GET** `/api/task-attachments`
  - 功能: 分页查询任务附件
  - 查询参数:
    - `taskId` (long, 必填)
    - `pageNo` (long, 默认 1)
    - `pageSize` (long, 默认 10)
  - 响应: `Result<PageResult<TaskAttachment>>`

---

### 任务关注人（TaskFollowerController）

基路径: `/api/task-followers`

- **POST** `/api/task-followers`
  - 功能: 添加关注人
  - 请求体(JSON) `TaskFollower`:
    - `taskId` (long)
    - `userId` (long)
  - 响应: `Result<Boolean>`

- **DELETE** `/api/task-followers/{id}`
  - 功能: 删除关注人
  - 路径参数: `id` (long)
  - 响应: `Result<Boolean>`

- **GET** `/api/task-followers`
  - 功能: 分页查询任务关注人
  - 查询参数:
    - `taskId` (long, 必填)
    - `pageNo` (long, 默认 1)
    - `pageSize` (long, 默认 10)
  - 响应: `Result<PageResult<TaskFollower>>`

---

### 任务历史（TaskHistoryController）

基路径: `/api/task-history`

- **GET** `/api/task-history`
  - 功能: 分页查询任务操作历史，按时间倒序
  - 查询参数:
    - `taskId` (long, 必填)
    - `pageNo` (long, 默认 1)
    - `pageSize` (long, 默认 10)
  - 响应: `Result<PageResult<TaskHistory>>`

