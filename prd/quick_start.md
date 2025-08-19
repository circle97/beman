# BeMan 快速开始开发指南

## 🚀 立即开始开发

### 第一步：选择本周任务
根据开发计划，本周应该专注于 **Week 1: 社区系统完善**

**今日推荐任务：**
- [ ] 后端：创建内容审核服务接口
- [ ] 后端：实现关键词过滤算法
- [ ] 前端：集成审核状态显示

### 第二步：环境准备
确保开发环境已就绪：

```bash
# 检查前端环境
cd frontend
npm run dev

# 检查后端环境  
cd backend
mvn spring-boot:run

# 检查数据库连接
mysql -u root -p beman
```

### 第三步：开始编码

#### 1. 创建内容审核服务
在 `backend/src/main/java/com/beman/service/` 下创建：

```java
// ContentAuditService.java
public interface ContentAuditService {
    AuditResult auditContent(String content);
    boolean isExtremeContent(String content);
    String getReplacementSuggestion(String content);
}
```

#### 2. 实现关键词过滤
在 `backend/src/main/java/com/beman/service/impl/` 下创建：

```java
// ContentAuditServiceImpl.java
@Service
public class ContentAuditServiceImpl implements ContentAuditService {
    
    private static final Set<String> EXTREME_KEYWORDS = Set.of(
        "分手", "离婚", "绝交", "拉黑", "删除"
    );
    
    @Override
    public AuditResult auditContent(String content) {
        // 实现审核逻辑
        boolean isExtreme = isExtremeContent(content);
        String suggestion = isExtreme ? getReplacementSuggestion(content) : null;
        
        return new AuditResult(isExtreme, suggestion);
    }
    
    @Override
    public boolean isExtremeContent(String content) {
        return EXTREME_KEYWORDS.stream()
            .anyMatch(content::contains);
    }
    
    @Override
    public String getReplacementSuggestion(String content) {
        return "建议先尝试沟通和改善，你试过哪些方法？";
    }
}
```

#### 3. 创建审核结果模型
在 `backend/src/main/java/com/beman/model/` 下创建：

```java
// AuditResult.java
@Data
@AllArgsConstructor
public class AuditResult {
    private boolean isExtreme;
    private String suggestion;
}
```

#### 4. 更新帖子模型
在现有的 Post 模型中添加审核状态：

```java
// Post.java 中添加字段
private Integer auditStatus; // 0:待审核 1:通过 2:拒绝
private String auditSuggestion; // 审核建议
```

#### 5. 前端集成审核状态
在 `frontend/src/views/Community.vue` 中添加：

```vue
<template>
  <!-- 在发帖表单中添加 -->
  <div v-if="auditResult" class="audit-status">
    <div v-if="auditResult.isExtreme" class="audit-warning">
      <span>⚠️ 检测到极端内容</span>
      <p>{{ auditResult.suggestion }}</p>
    </div>
  </div>
</template>

<script setup>
// 添加审核逻辑
const auditContent = async (content) => {
  try {
    const response = await api.auditContent(content);
    auditResult.value = response.data;
  } catch (error) {
    console.error('审核失败:', error);
  }
};
</script>
```

### 第四步：测试验证

#### 1. 启动服务
```bash
# 后端
cd backend && mvn spring-boot:run

# 前端
cd frontend && npm run dev
```

#### 2. 测试审核功能
- 访问社区页面
- 尝试发布包含极端关键词的帖子
- 验证审核提示是否正确显示

#### 3. 检查日志
查看后端日志，确认审核服务正常工作

### 第五步：提交代码

```bash
git add .
git commit -m "feat: 实现内容审核系统基础功能

- 添加内容审核服务接口
- 实现关键词过滤算法
- 前端集成审核状态显示
- 完成基础测试验证"
git push origin feature/content-audit
```

## 📋 今日任务清单

### 必须完成
- [ ] 内容审核服务接口
- [ ] 关键词过滤算法
- [ ] 前端审核状态显示
- [ ] 基础功能测试

### 可选完成
- [ ] 审核结果持久化
- [ ] 审核历史记录
- [ ] 管理员审核界面

## 🔍 常见问题

### Q: 关键词过滤不够准确怎么办？
A: 可以先用简单的字符串匹配，后续集成AI语义分析

### Q: 前端审核状态不显示？
A: 检查API接口是否正确调用，确认数据结构匹配

### Q: 审核服务启动失败？
A: 检查Spring Boot配置，确认依赖注入正确

## 📚 参考资料

- [Spring Boot 服务开发指南](https://spring.io/guides)
- [Vue3 组件开发最佳实践](https://vuejs.org/guide/)
- [BeMan 设计系统](prd/design.json)

## 🎯 下一步计划

完成今日任务后，明天继续：
- 反极端建议系统
- 引导性提问模板
- 高价值回答识别

---

**记住：小步快跑，快速验证，持续改进！**
