# 数据库初始化脚本

## 文件说明

- `init.sql`: 完整的数据库初始化脚本，包含创建数据库、表和初始数据

## 使用方法

### 1. 使用MySQL命令行
```bash
mysql -u root -p < init.sql
```

### 2. 使用MySQL Workbench或其他客户端
1. 打开 `init.sql` 文件
2. 执行整个脚本

### 3. 分步执行（推荐用于生产环境）

#### 第一步：创建数据库
```sql
CREATE DATABASE IF NOT EXISTS beman DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 第二步：创建表
```sql
USE beman;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    -- 表结构见 init.sql
);

-- 创建账单表
CREATE TABLE IF NOT EXISTS bills (
    -- 表结构见 init.sql
);
```

## 数据库结构

### users 表（用户表）
- `id`: 用户ID，主键，自增
- `username`: 用户名，唯一
- `password`: 密码（加密存储）
- `phone`: 手机号，唯一
- `email`: 邮箱
- `nickname`: 昵称
- `avatar`: 头像URL
- `relationship_status`: 感情状态（单身/恋爱中/已婚/离异）
- `created_at`: 创建时间
- `updated_at`: 更新时间
- `last_login_at`: 最后登录时间
- `is_active`: 是否激活

### bills 表（账单表）
- `id`: 账单ID，主键，自增
- `user_id`: 用户ID，外键关联users表
- `amount`: 金额
- `type`: 账单类型（收入/支出）
- `date`: 账单日期
- `category`: 分类
- `description`: 描述
- `is_shared`: 是否共享
- `shared_percentage`: 共享百分比
- `created_at`: 创建时间
- `updated_at`: 更新时间

## 索引说明

### users 表索引
- `idx_username`: 用户名索引
- `idx_phone`: 手机号索引
- `idx_email`: 邮箱索引
- `idx_created_at`: 创建时间索引

### bills 表索引
- `idx_user_id`: 用户ID索引
- `idx_date`: 日期索引
- `idx_type`: 类型索引
- `idx_category`: 分类索引
- `idx_user_date`: 用户ID和日期复合索引
- `idx_created_at`: 创建时间索引

## 注意事项

1. 脚本使用 `utf8mb4` 字符集，支持完整的Unicode字符
2. 所有表都使用 `InnoDB` 引擎，支持事务和外键
3. 密码字段预留255字符长度，支持各种加密算法
4. 外键设置了级联删除，删除用户时会自动删除相关账单
5. 时间字段使用 `TIMESTAMP` 类型，自动处理时区转换
6. 初始数据中的密码是BCrypt加密的 "123456"

## 测试账号

脚本会创建两个测试账号：
- 用户名：`admin`，密码：`123456`
- 用户名：`test_user`，密码：`123456` 