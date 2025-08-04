# BeMan Backend 启动指南

## 环境要求

1. **Java 8** 或更高版本
2. **Maven 3.6+**
3. **MySQL 5.7+** 或 **MySQL 8.0+**

## 快速启动

### 方法1：使用批处理文件（推荐）

```bash
# 在 backend 目录下运行
.\start.bat
```

### 方法2：手动启动

```bash
# 设置环境变量
set JAVA_HOME=D:\software\jdk\install
set MAVEN_HOME=D:\software\maven\apache-maven-3.8.6
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

# 编译项目
mvn clean install

# 启动应用
mvn spring-boot:run
```

## 数据库配置

确保 MySQL 服务正在运行，并创建数据库：

```sql
CREATE DATABASE beman CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 数据库连接信息
- **主机**: localhost:3306
- **数据库**: beman
- **用户名**: root
- **密码**: Lq@986455353

## 应用信息

- **端口**: 8080
- **上下文路径**: /api
- **访问地址**: http://localhost:8080/api

## 常见问题

### 1. JAVA_HOME 错误
确保 JAVA_HOME 指向正确的 JDK 安装目录，包含 bin 子目录。

### 2. 数据库连接失败
- 检查 MySQL 服务是否运行
- 确认数据库用户名和密码正确
- 确保数据库 `beman` 已创建

### 3. 端口占用
如果 8080 端口被占用，可以修改 `application.yml` 中的 `server.port` 配置。

## API 文档

启动成功后，可以访问以下接口：
- 用户认证: POST /api/auth/login
- 财务账单: GET /api/finance/bills
- 账单汇总: GET /api/finance/bills/summary 