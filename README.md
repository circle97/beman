# BeMan - 男性情感成长与伴侣关系经营工具

## 项目简介

BeMan 是一个专为男性用户设计的情感成长与伴侣关系经营工具，通过**情感互助社区+关系管理工具**的一站式解决方案，帮助男性提升情感认知能力、改善伴侣关系、避免极端行为。

## 核心功能

### 1. 男性互助社区（Boys Help Boys）
- **匿名树洞**：支持纯文字/语音吐槽，默认隐藏用户ID
- **经验共享**：分类标签（如#挽回经验# #冷战化解# #日常经营#）
- **反极端建议机制**：屏蔽极端关键词，引导建设性沟通
- **行动见证系统**：用户可标记实践计划，形成成长轨迹

### 2. 关系管理工具（理性经营四支柱）
- **财务透明化**：语音快速记账、账单同步、共同账户看板
- **日程管家**：智能识别重要日期、分级提醒、礼物推荐
- **情绪解码器**：AI情绪分析、应对建议库
- **沟通沙盒**：对话模拟、小作文助手

### 3. 成长档案与成就
- **实践计划**：制定和跟踪关系改善计划
- **成就勋章**：解锁各种成长里程碑
- **成长曲线**：可视化展示关系健康指数

## 技术栈

### 前端
- **Vue 3** + **TypeScript** + **Vite**
- **Vue Router** - 路由管理
- **Pinia** - 状态管理
- **Axios** - HTTP请求
- **SASS/SCSS** - 样式预处理
- **设计系统** - 严格遵循 design.json 规范

### 后端
- **Java 1.8** + **Spring Boot 2.7.x**
- **sa-token** - 认证与权限管理
- **MyBatis-Plus** - ORM框架
- **Redis** - 缓存和会话管理
- **MySQL** - 数据库

### AI服务（计划中）
- **Python 3** + **FastAPI**
- **NLP情感分析** - 情绪解码
- **内容审核** - 关键词过滤、极端倾向识别
- **推荐算法** - 礼物推荐、内容推荐

## 项目结构

```
beman/
├── frontend/          # 前端工程
│   ├── src/
│   │   ├── components/    # 通用组件
│   │   ├── views/         # 页面组件
│   │   ├── stores/        # 状态管理
│   │   ├── api/           # API接口
│   │   ├── styles/        # 样式文件
│   │   └── utils/         # 工具函数
│   └── package.json
├── backend/           # 后端工程
│   ├── src/main/java/com/beman/
│   │   ├── controller/    # 控制器
│   │   ├── service/       # 业务逻辑
│   │   ├── model/         # 数据模型
│   │   ├── mapper/        # 数据访问
│   │   └── config/        # 配置类
│   └── pom.xml
├── prd/              # 产品文档
│   ├── beman.md      # 产品需求文档
│   ├── detail.md     # 技术实现方案
│   └── design.json   # UI设计系统
└── ui/               # UI设计稿
```

## 快速开始

### 环境要求
- Node.js 16+
- Java 1.8+
- MySQL 5.7+
- Redis 6.0+

### 1. 克隆项目
```bash
git clone <repository-url>
cd beman
```

### 2. 数据库初始化
```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE beman DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 执行初始化SQL
mysql -u root -p beman < backend/src/main/resources/db/init.sql
```

### 3. 配置后端
```bash
cd backend
# 修改 application.yml 中的数据库和Redis配置
```

### 4. 启动后端
```bash
cd backend
mvn spring-boot:run
```

### 5. 启动前端
```bash
cd frontend
npm install
npm run dev
```

### 6. 访问应用
- 前端: http://localhost:5173
- 后端API: http://localhost:8080

## 开发指南

### 前端开发
- 所有组件样式严格遵循 `prd/design.json` 设计规范
- 使用 TypeScript 进行类型检查
- 组件命名采用 BEM 方法论
- 状态管理使用 Pinia

### 后端开发
- 遵循 RESTful API 设计规范
- 使用 MyBatis-Plus 进行数据库操作
- 统一异常处理和响应格式
- 接口文档使用 Swagger

### 代码规范
- 前端：ESLint + Prettier
- 后端：Checkstyle + SpotBugs
- 提交信息：Conventional Commits

## 部署

### 开发环境
```bash
# 前端
cd frontend && npm run build
# 后端
cd backend && mvn clean package
```

### 生产环境
- 使用 Docker 容器化部署
- Nginx 反向代理
- 数据库主从复制
- Redis 集群

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

- 项目维护者：[Your Name]
- 邮箱：[your.email@example.com]
- 项目地址：[https://github.com/your-username/beman]

---

**改变从认知开始，成长用行动证明** 