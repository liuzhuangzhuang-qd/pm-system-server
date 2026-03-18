# pm-system 轻量级项目管理系统后端

基于 **Spring Boot 3 + MySQL 8 + MyBatis-Plus + Spring Security + JWT** 的轻量级项目管理系统后端，适用于 10～50 人研发团队。

---

## 一、技术栈与结构

- **后端技术**
  - Spring Boot 3.x
  - Spring Security + JWT
  - MyBatis-Plus
  - MySQL 8
- **包结构**
  - `com.pm.common`：基础实体、统一返回、异常、工具
  - `com.pm.config`：MyBatis-Plus、Security、密码、填充配置
  - `com.pm.modules.auth`：用户、角色、登录鉴权、操作日志
  - `com.pm.modules.project`：项目、成员、项目配置
  - `com.pm.modules.task`：任务、子任务、评论、附件、关注、历史
  - `com.pm.modules.board`：看板、看板列
  - `com.pm.modules.bug`：缺陷与缺陷历史
  - `com.pm.modules.report`：统计报表
  - `com.pm.modules.system`：数据字典、系统配置、组织架构
  - `com.pm.modules.notice`：通知与模板

---

## 二、环境准备

1. **JDK**：17+
2. **MySQL**：8.x（建议）
3. **Maven**：3.8+

数据库创建：

```sql
CREATE DATABASE pm_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pm_system;
```

**一键初始化（推荐）**：在 `pm_system` 库中执行脚本：

`src/main/resources/db/init.sql`

该脚本会：建全库表（含 `deleted` 逻辑删除字段）、插入管理员、角色、用户角色关联及少量字典/配置。

- **默认登录**：用户名 `admin`，密码 **`password`**（BCrypt，与 `BCryptPasswordEncoder` 一致）
- 脚本含 `DROP TABLE IF EXISTS`，**仅适用于空库或允许重建的开发环境**；生产环境请改为增量迁移。

---

## 三、配置说明

### 本地环境变量文件（推荐）

1. 复制 `config/env.properties.example` 为 **`config/env.properties`**
2. 修改其中的 `PM_DB_PASSWORD` 等项（与系统环境变量同名，优先级：环境变量 > 该文件 > application.yml 默认值）

启动类工作目录需为**项目根目录**（IDE 默认如此），以便加载 `./config/env.properties`。

Docker / Shell 可选用根目录 **`.env.example`** 复制为 **`.env`**（变量名同上）。

---

修改 `src/main/resources/application.yml` 中数据库默认值（未使用 env 文件或未设环境变量时生效）：

```yaml
spring:
  datasource:
    url: ${PM_DB_URL:jdbc:mysql://localhost:3306/pm_system?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai}
    username: ${PM_DB_USERNAME:root}
    password: ${PM_DB_PASSWORD:root}
```

JWT 配置（可自定义）：

```yaml
pm:
  security:
    jwt-secret: "pm-system-secret-key-change-me-please" # 建议替换为更复杂的 Base64 字符串
    jwt-expire-seconds: 7200
```

---

## 四、启动项目（本地）

### 入口类

- 主类：**`com.pm.PmApplication`**（`src/main/java/com/pm/PmApplication.java`）
- 不要用「Current File」运行 `application.yml` / `init.sql`，会提示不可运行；应运行 **`PmApplication`**。

### IntelliJ / Cursor 运行配置

1. **导入 Maven 项目**：打开根目录 **`pom.xml`**，选择作为 Maven 项目加载（等待依赖下载完成）。
2. **运行**：打开 `PmApplication.java`，点击 `main` 左侧绿色运行按钮；或 **Run → Edit Configurations → + → Spring Boot**，Main class 填 **`com.pm.PmApplication`**，Working directory 选 **项目根目录**（以便读取 `config/env.properties`）。
3. **JDK**：使用 **17**。

---

## 四（续）、命令行启动（需已安装 Maven）

在项目根目录执行：

```bash
mvn spring-boot:run
```

默认端口：`8080`

---

## 五、使用 Docker 启动（推荐）

项目根目录已经提供：

- `Dockerfile`：后端服务镜像构建
- `docker-compose.yml`：一键启动 `MySQL 8 + pm-system` 后端

### 1. 构建并启动

在项目根目录执行：

```bash
docker compose up -d
```

这会：

- 启动 `mysql` 容器（数据库为 `pm_system`，账号 `root/root`）
- 构建并启动 `pm-system` 容器，端口映射：
  - 后端：`http://localhost:8080`
  - MySQL：`localhost:3306`

### 2. 关闭容器

```bash
docker compose down
```

> 提示：`pm-mysql-data` 使用 Docker Volume 持久化 MySQL 数据，`docker compose down` 不会丢失数据，除非同时执行 `docker volume rm pm-mysql-data`。

---

## 五、初始化与登录

1. 若已执行 **`src/main/resources/db/init.sql`**，可跳过手工建表；直接使用账号 **`admin` / `password`** 登录。
2. 若未执行 init，需自行建表并插入用户：`password` 列须为 BCrypt 密文（可用 `new BCryptPasswordEncoder().encode("明文")` 生成），且表需含 `deleted` 字段（默认 0）。

调用登录接口：

- **URL**：`POST /api/auth/login`
- **Body(JSON)**（与 init 脚本一致时用 `password`）：

```json
{
  "username": "admin",
  "password": "password"
}
```

- 返回示例：

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "token": "xxx.yyy.zzz"
  }
}
```

之后请求其他接口时在 Header 中携带：

```http
Authorization: Bearer <token>
```

---

## 六、统一返回与分页

- 统一返回结构：`Result<T>`

```json
{
  "code": 0,
  "msg": "success",
  "data": { }
}
```

- 分页返回结构：`PageResult<T>`

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "total": 100,
    "list": [ ]
  }
}
```

---

## 七、主要模块接口概览

### 1. auth 用户与权限

- 登录：`POST /api/auth/login`
- 刷新 token：`POST /api/auth/refresh?token=xxx`
- 登出：`POST /api/auth/logout`
- 用户 CRUD & 分页：`/api/users`

### 2. project 项目

- 项目 CRUD & 分页：`/api/projects`
- 项目成员：
  - 增加成员：`POST /api/project-members`
  - 删除成员：`DELETE /api/project-members/{id}`
  - 成员列表（按项目）：`GET /api/project-members?projectId=xxx`
- 项目配置：
  - 获取配置：`GET /api/project-config/{projectId}`
  - 保存配置：`POST /api/project-config`

### 3. task 任务

- 任务 CRUD & 列表（可按项目过滤）：`/api/tasks`
- 评论：`/api/task-comments`
- 附件：`/api/task-attachments`
- 关注人：`/api/task-followers`
- 任务历史：`/api/task-history`

### 4. board 看板

- 看板 CRUD：`/api/boards`
- 列配置、排序与状态值：`/api/board-columns`

### 5. bug 缺陷

- 缺陷 CRUD & 列表：`/api/bugs`
- 缺陷历史：`/api/bug-history`

### 6. system 系统管理

- 数据字典：`/api/system/dicts`
- 系统配置：`/api/system/configs`
- 组织架构：`/api/system/orgs`

### 7. notice 通知

- 通知列表 / 标记已读：`/api/notifications`
- 通知模板 CRUD：`/api/notice-templates`

### 8. report 报表（示例）

- 项目进度统计：`GET /api/reports/project-progress`
- 任务完成率：`GET /api/reports/task-completion`
- 缺陷分布：`GET /api/reports/bug-distribution`
- 人员工作量：`GET /api/reports/user-workload`

---

## 八、注意事项

- 所有非 `/api/auth/login`、`/api/auth/refresh` 的接口默认需要携带 JWT。
- 实体统一继承 `BaseEntity`，带有：
  - `id`、`createTime`、`updateTime`、`deleted`（逻辑删除）
- MyBatis-Plus 启用了分页插件，分页参数默认为：
  - `pageNo`（默认 1）、`pageSize`（默认 10）
- 状态字段均使用字符串或规范的枚举值，避免魔法数字。

