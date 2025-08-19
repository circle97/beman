package com.beman.service.impl;

import com.beman.model.AuditResult;
import com.beman.service.ContentAuditService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 内容审核服务实现类
 */
@Service
public class ContentAuditServiceImpl implements ContentAuditService {
    
    /**
     * 极端关键词集合
     */
    private static final Set<String> EXTREME_KEYWORDS = new HashSet<>(Arrays.asList(
        "分手", "离婚", "绝交", "拉黑", "删除", "断绝关系", "永不相见",
        "恨你", "讨厌你", "恶心", "滚蛋", "去死", "自杀", "自残",
        "报复", "报复社会", "杀人", "放火", "爆炸", "恐怖袭击"
    ));
    
    /**
     * 敏感关键词集合
     */
    private static final Set<String> SENSITIVE_KEYWORDS = new HashSet<>(Arrays.asList(
        "吵架", "冷战", "不理你", "生气", "愤怒", "失望", "绝望",
        "痛苦", "难过", "伤心", "崩溃", "想不开", "活不下去"
    ));
    
    /**
     * 引导性建议模板 - 按场景分类
     */
    private static final Map<String, String[]> SUGGESTION_TEMPLATES;
    
    static {
        SUGGESTION_TEMPLATES = new HashMap<>();
        
        SUGGESTION_TEMPLATES.put("分手", new String[]{
            "分手是一个重大决定，你确定已经尝试了所有可能的解决方案吗？",
            "在做出最终决定前，也许可以尝试一次深入的沟通，你觉得呢？",
            "关系中的问题往往需要双方共同努力，你希望对方如何配合？"
        });
        
        SUGGESTION_TEMPLATES.put("吵架", new String[]{
            "吵架后冷静下来，你觉得问题的根源是什么？",
            "也许可以尝试用'我'开头的表达方式，比如'我感觉...'",
            "每次冲突都是了解对方的机会，你从这次吵架中学到了什么？"
        });
        
        SUGGESTION_TEMPLATES.put("冷战", new String[]{
            "冷战往往让问题变得更复杂，你愿意主动打破僵局吗？",
            "也许可以先从一个小话题开始，比如分享今天的有趣事情？",
            "关系需要温度，你觉得如何能让彼此重新连接？"
        });
        
        SUGGESTION_TEMPLATES.put("失望", new String[]{
            "失望的感觉很难受，你愿意分享具体是什么让你失望吗？",
            "也许可以尝试表达你的期望，让对方知道你的需求？",
            "失望往往源于期望，你觉得你的期望合理吗？"
        });
        
        SUGGESTION_TEMPLATES.put("愤怒", new String[]{
            "愤怒时容易说出伤人的话，你愿意先冷静一下吗？",
            "也许可以尝试理解愤怒背后的原因，你觉得是什么触发了你的愤怒？",
            "愤怒往往掩盖了更深层的情绪，你真正想要的是什么？"
        });
    }
    
    @Override
    public AuditResult auditContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return new AuditResult(false, "内容不能为空");
        }
        
        // 判断是否为极端内容
        boolean isExtreme = isExtremeContent(content);
        
        // 获取风险等级
        int riskLevel = getRiskLevel(content);
        
        // 获取建议
        String suggestion = isExtreme ? getReplacementSuggestion(content) : null;
        
        // 创建审核结果
        AuditResult result = new AuditResult(isExtreme, suggestion);
        result.setRiskLevel(riskLevel);
        result.setAuditStatus(isExtreme ? 2 : 1); // 2-拒绝，1-通过
        result.setRejectReason(isExtreme ? "包含极端内容" : null);
        
        return result;
    }
    
    @Override
    public boolean isExtremeContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        
        String lowerContent = content.toLowerCase();
        
        // 检查极端关键词
        for (String keyword : EXTREME_KEYWORDS) {
            if (lowerContent.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        
        // 检查敏感词组合（多个敏感词同时出现可能表示极端倾向）
        int sensitiveCount = 0;
        for (String keyword : SENSITIVE_KEYWORDS) {
            if (lowerContent.contains(keyword.toLowerCase())) {
                sensitiveCount++;
                if (sensitiveCount >= 3) { // 3个以上敏感词视为极端内容
                    return true;
                }
            }
        }
        
        return false;
    }
    
    @Override
    public String getReplacementSuggestion(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "请提供具体内容以便给出建议";
        }
        
        // 根据内容中的关键词选择合适的建议模板
        String lowerContent = content.toLowerCase();
        
        // 优先匹配具体场景
        for (Map.Entry<String, String[]> entry : SUGGESTION_TEMPLATES.entrySet()) {
            if (lowerContent.contains(entry.getKey().toLowerCase())) {
                String[] templates = entry.getValue();
                int templateIndex = Math.abs(content.hashCode()) % templates.length;
                return templates[templateIndex];
            }
        }
        
        // 如果没有匹配到具体场景，返回通用建议
        String[] generalTemplates = {
            "建议先尝试沟通和改善，你试过哪些方法？",
            "关系遇到困难时，冷静思考往往能找到解决方案，你觉得问题出在哪里？",
            "每个人都会遇到挫折，重要的是如何面对，你愿意分享更多细节吗？",
            "也许换个角度思考会有不同的发现，你觉得对方是怎么想的？",
            "关系需要双方共同努力，你希望对方怎么做？"
        };
        
        int templateIndex = Math.abs(content.hashCode()) % generalTemplates.length;
        return generalTemplates[templateIndex];
    }
    
    @Override
    public int getRiskLevel(String content) {
        if (content == null || content.trim().isEmpty()) {
            return 0;
        }
        
        if (isExtremeContent(content)) {
            return 2; // 高风险
        }
        
        // 检查敏感词数量
        String lowerContent = content.toLowerCase();
        int sensitiveCount = 0;
        for (String keyword : SENSITIVE_KEYWORDS) {
            if (lowerContent.contains(keyword.toLowerCase())) {
                sensitiveCount++;
            }
        }
        
        if (sensitiveCount >= 2) {
            return 1; // 中风险
        }
        
        return 0; // 低风险
    }
}
