# 数据库结构图

## 表关系图

```
┌─────────────────┐         ┌─────────────────┐
│      users      │         │      bills      │
├─────────────────┤         ├─────────────────┤
│ id (PK)         │◄────────│ user_id (FK)    │
│ username (UK)   │         │ id (PK)         │
│ password        │         │ amount          │
│ phone (UK)      │         │ type            │
│ email           │         │ date            │
│ nickname        │         │ category        │
│ avatar          │         │ description     │
│ relationship_   │         │ is_shared       │
│ status          │         │ shared_percent  │
│ created_at      │         │ created_at      │
│ updated_at      │         │ updated_at      │
│ last_login_at   │         └─────────────────┘
│ is_active       │
└─────────────────┘
```

## 字段详细说明

### users 表字段

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 用户唯一标识 |
| username | VARCHAR(50) | NOT NULL, UNIQUE | 用户名，登录用 |
| password | VARCHAR(255) | NOT NULL | 密码，BCrypt加密 |
| phone | VARCHAR(20) | NOT NULL, UNIQUE | 手机号，登录用 |
| email | VARCHAR(100) | NULL | 邮箱地址 |
| nickname | VARCHAR(50) | NULL | 显示昵称 |
| avatar | VARCHAR(255) | NULL | 头像图片URL |
| relationship_status | ENUM | NULL | 感情状态枚举 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE | 更新时间 |
| last_login_at | TIMESTAMP | NULL | 最后登录时间 |
| is_active | BOOLEAN | DEFAULT TRUE | 账号是否激活 |

### bills 表字段

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 账单唯一标识 |
| user_id | BIGINT | NOT NULL, FOREIGN KEY | 关联用户ID |
| amount | DECIMAL(10,2) | NOT NULL | 账单金额 |
| type | ENUM('INCOME','EXPENSE') | NOT NULL | 收入或支出 |
| date | DATE | NOT NULL | 账单发生日期 |
| category | VARCHAR(50) | NOT NULL | 账单分类 |
| description | TEXT | NULL | 账单描述 |
| is_shared | BOOLEAN | DEFAULT FALSE | 是否与伴侣共享 |
| shared_percentage | INT | DEFAULT 50 | 共享百分比 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

## 枚举值说明

### relationship_status 枚举
- `SINGLE`: 单身
- `IN_RELATIONSHIP`: 恋爱中
- `MARRIED`: 已婚
- `DIVORCED`: 离异

### type 枚举
- `INCOME`: 收入
- `EXPENSE`: 支出

## 索引策略

### 性能优化索引
1. **用户表索引**
   - `username`: 登录查询优化
   - `phone`: 手机号登录优化
   - `email`: 邮箱查询优化
   - `created_at`: 时间范围查询优化

2. **账单表索引**
   - `user_id`: 用户账单查询优化
   - `date`: 日期范围查询优化
   - `type`: 收入/支出分类查询优化
   - `category`: 分类统计查询优化
   - `user_id + date`: 用户特定日期账单查询优化

## 数据完整性

### 外键约束
- `bills.user_id` → `users.id` (CASCADE DELETE)
  - 删除用户时自动删除该用户的所有账单

### 唯一约束
- `users.username`: 用户名唯一
- `users.phone`: 手机号唯一

### 非空约束
- 用户表：`username`, `password`, `phone`
- 账单表：`user_id`, `amount`, `type`, `date`, `category`

## 扩展建议

### 未来可能的表扩展
1. **categories 表**: 账单分类管理
2. **partners 表**: 伴侣关系管理
3. **shared_bills 表**: 共享账单详细记录
4. **budgets 表**: 预算管理
5. **goals 表**: 财务目标管理
6. **emotions 表**: 情感记录
7. **relationship_logs 表**: 关系日志

### 性能优化建议
1. 考虑分表策略：按时间分表存储历史账单
2. 添加缓存层：Redis缓存热点数据
3. 定期归档：将旧数据归档到历史表
4. 监控慢查询：定期分析SQL执行计划 