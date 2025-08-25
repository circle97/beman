# Beman项目单元测试指南

## 概述

本文档描述了Beman项目的单元测试结构、运行方法和测试覆盖范围。

## 测试结构

### 测试目录结构
```
src/test/
├── java/com/beman/
│   ├── service/           # Service层单元测试
│   │   ├── CommentServiceTest.java
│   │   ├── UserFollowServiceTest.java
│   │   ├── ReportServiceTest.java
│   │   ├── PostServiceTest.java
│   │   ├── UserServiceTest.java
│   │   └── ContentAuditServiceTest.java
│   ├── controller/        # Controller层测试
│   ├── integration/       # 集成测试
│   └── model/            # 模型测试
└── resources/
    └── application-test.yml  # 测试环境配置
```

## 已完成的测试

### 1. CommentServiceTest
- ✅ 评论创建测试
- ✅ 评论查询测试
- ✅ 评论更新测试
- ✅ 评论删除测试
- ✅ 评论计数测试
- ✅ 异常情况测试

### 2. UserFollowServiceTest
- ✅ 用户关注测试
- ✅ 取消关注测试
- ✅ 关注状态查询测试
- ✅ 关注列表查询测试
- ✅ 粉丝列表查询测试
- ✅ 互相关注测试
- ✅ 异常情况测试

### 3. ReportServiceTest
- ✅ 举报创建测试
- ✅ 举报查询测试
- ✅ 举报处理测试
- ✅ 举报拒绝测试
- ✅ 批量处理测试
- ✅ 统计查询测试
- ✅ 异常情况测试

### 4. PostServiceTest
- ✅ 帖子创建测试
- ✅ 帖子查询测试
- ✅ 帖子更新测试
- ✅ 帖子删除测试
- ✅ 点赞功能测试
- ✅ 搜索功能测试
- ✅ 异常情况测试

### 5. UserServiceTest
- ✅ 用户注册测试
- ✅ 用户登录测试
- ✅ 用户信息更新测试
- ✅ 密码修改测试
- ✅ 用户查询测试
- ✅ 用户管理测试
- ✅ 异常情况测试

### 6. ContentAuditServiceTest
- ✅ 内容审核测试
- ✅ 风险等级评估测试
- ✅ 审核结果查询测试
- ✅ 批量审核测试
- ✅ 统计查询测试
- ✅ 异常情况测试

## 运行测试

### 方法1：使用批处理脚本（推荐）
```bash
# Windows
run-unit-tests.bat

# Linux/Mac
./run-unit-tests.sh
```

### 方法2：使用Maven命令
```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=CommentServiceTest

# 运行特定测试方法
mvn test -Dtest=CommentServiceTest#testCreateComment_Success

# 生成测试覆盖率报告
mvn jacoco:report
```

### 方法3：使用IDE
在IDE中右键点击测试类或方法，选择"Run Test"即可。

## 测试配置

### 测试环境配置
- 使用H2内存数据库进行测试
- 启用SQL日志输出
- 配置测试专用日志级别

### 测试数据
- 使用Mock对象模拟外部依赖
- 每个测试方法都有独立的测试数据
- 测试数据在`@BeforeEach`方法中初始化

## 测试覆盖率

### 目标覆盖率
- 行覆盖率：≥ 80%
- 分支覆盖率：≥ 70%
- 方法覆盖率：≥ 85%

### 查看覆盖率报告
测试完成后，覆盖率报告位于：
```
target/site/jacoco/index.html
```

## 测试最佳实践

### 1. 测试命名规范
- 测试方法名：`test[方法名]_[场景描述]`
- 例如：`testCreateComment_Success`、`testCreateComment_UserNotFound`

### 2. 测试结构
每个测试方法应包含：
- 准备测试数据（Arrange）
- 执行被测试方法（Act）
- 验证测试结果（Assert）

### 3. Mock使用
- 使用`@Mock`注解创建Mock对象
- 使用`@InjectMocks`注入被测试对象
- 使用`when().thenReturn()`设置Mock行为

### 4. 异常测试
- 使用`assertThrows()`测试异常情况
- 验证异常类型和消息

## 常见问题

### 1. 测试失败
- 检查测试数据是否正确
- 验证Mock对象配置
- 查看测试日志输出

### 2. 编译错误
- 确保所有依赖都已正确导入
- 检查测试类是否继承了正确的基类
- 验证测试方法签名

### 3. 数据库连接问题
- 确保H2数据库依赖已添加
- 检查测试配置文件是否正确

## 扩展测试

### 添加新的测试类
1. 在对应包下创建测试类
2. 继承适当的测试基类
3. 添加必要的Mock对象
4. 编写测试方法

### 添加新的测试方法
1. 使用`@Test`注解标记测试方法
2. 遵循AAA模式（Arrange-Act-Assert）
3. 添加适当的断言
4. 验证Mock对象调用

## 持续集成

### CI/CD配置
- 测试在每次代码提交时自动运行
- 测试失败会阻止代码合并
- 生成测试报告和覆盖率报告

### 质量门禁
- 测试通过率：100%
- 代码覆盖率：≥ 80%
- 无测试失败

## 联系方式

如有测试相关问题，请联系开发团队或查看项目文档。
