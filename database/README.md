# FlowStack 数据库初始化

本目录包含完整的数据库初始化脚本，包含所有 DDL（表结构）和 DML（初始数据）。

> **注意**：这是一个普通目录，不是 Maven 模块，仅用于存放 SQL 脚本文件。

## 文件说明

- **init.sql** - 完整的数据库初始化脚本（包含所有表结构和初始数据）

## 使用方法

### 方法一：命令行执行（推荐）

```bash
mysql -u root -p < init.sql
```

### 方法二：MySQL 客户端执行

```sql
source /path/to/init.sql;
```

### 方法三：使用 MySQL Workbench 或其他工具

直接打开并执行 `init.sql` 文件即可。

## 脚本内容

脚本包含以下内容：

1. **创建所有数据库**
   - flowstack_auth
   - flowstack_project
   - flowstack_task
   - flowstack_notification
   - flowstack_file
   - flowstack_search
   - flowstack_analytics
   - flowstack_user

2. **User Service 数据库**
   - teams 表（团队表）
   - users 表（用户表）
   - 初始团队数据（5个团队）
   - 初始用户数据（10个用户）

3. **Project Service 数据库**
   - projects 表（项目表，包含所有新字段）
   - project_teams 表（项目-团队关联表）
   - project_members 表（项目-成员关联表）
   - project_tags 表（项目标签表）
   - 初始项目数据（2个项目）
   - 初始关联数据

## 注意事项

- 所有外键约束已移除，由应用层保证数据一致性
- 执行前请确保 MySQL 服务已启动
- 建议在测试环境先执行验证
- 如果数据库已存在，脚本会跳过创建（使用 `CREATE DATABASE IF NOT EXISTS`）
- 如果表已存在，脚本会跳过创建（使用 `CREATE TABLE IF NOT EXISTS`）
- 初始化数据使用 `ON DUPLICATE KEY UPDATE`，重复执行不会报错
- 脚本可以安全地重复执行，不会导致数据重复或错误

## 项目表字段说明

projects 表包含以下字段：
- 基础字段：id, name, description, owner_id, status, deadline
- 新增字段：start_date（开始日期）, priority（优先级）, starred（收藏）, archived（归档）, cover_image（封面图片）
- 统计字段：members（成员数）, progress（进度）
- 时间字段：created_at, updated_at
