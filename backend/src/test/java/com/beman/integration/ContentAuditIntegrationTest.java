package com.beman.integration;

import com.beman.model.AuditResult;
import com.beman.service.ContentAuditService;
import com.beman.service.impl.ContentAuditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 内容审核系统集成测试
 * 测试整个内容审核流程
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("内容审核系统集成测试")
class ContentAuditIntegrationTest {

    private ContentAuditService contentAuditService;

    @BeforeEach
    void setUp() {
        contentAuditService = new ContentAuditServiceImpl();
    }

    @Test
    @DisplayName("集成测试 - 完整的内容审核流程")
    void testCompleteAuditFlow() {
        // 测试用例1：极端内容
        String extremeContent = "我要和她分手，永远不再见面！";
        AuditResult extremeResult = contentAuditService.auditContent(extremeContent);
        
        assertNotNull(extremeResult, "极端内容审核结果不应该为null");
        assertTrue(extremeResult.isExtreme(), "应该识别为极端内容");
        assertEquals(2, extremeResult.getRiskLevel(), "应该是高风险");
        assertEquals(2, extremeResult.getAuditStatus(), "应该被拒绝");
        assertNotNull(extremeResult.getSuggestion(), "应该提供建议");
        assertEquals("包含极端内容", extremeResult.getRejectReason(), "应该有拒绝原因");
        
        // 验证建议内容
        String suggestion = extremeResult.getSuggestion();
        assertTrue(suggestion.contains("分手") || suggestion.contains("沟通"), 
                  "建议应该与分手场景相关");
        
        // 测试用例2：敏感内容组合
        String sensitiveContent = "最近吵架了，感觉很生气，也很失望，想不开";
        AuditResult sensitiveResult = contentAuditService.auditContent(sensitiveContent);
        
        assertNotNull(sensitiveResult, "敏感内容审核结果不应该为null");
        assertTrue(sensitiveResult.isExtreme(), "多个敏感词应该被识别为极端内容");
        assertEquals(2, sensitiveResult.getRiskLevel(), "应该是高风险");
        
        // 测试用例3：正常内容
        String normalContent = "今天和女朋友一起看了电影，感觉很开心，关系越来越好了";
        AuditResult normalResult = contentAuditService.auditContent(normalContent);
        
        assertNotNull(normalResult, "正常内容审核结果不应该为null");
        assertFalse(normalResult.isExtreme(), "不应该识别为极端内容");
        assertEquals(0, normalResult.getRiskLevel(), "应该是低风险");
        assertEquals(1, normalResult.getAuditStatus(), "应该通过");
        assertNull(normalResult.getSuggestion(), "正常内容不需要建议");
        assertNull(normalResult.getRejectReason(), "正常内容没有拒绝原因");
    }

    @Test
    @DisplayName("集成测试 - 风险等级评估准确性")
    void testRiskLevelAccuracy() {
        // 测试低风险：单个敏感词
        String lowRiskContent = "今天有点生气";
        int lowRiskLevel = contentAuditService.getRiskLevel(lowRiskContent);
        assertEquals(0, lowRiskLevel, "单个敏感词应该是低风险");
        
        // 测试中风险：两个敏感词
        String mediumRiskContent = "今天吵架了，很生气";
        int mediumRiskLevel = contentAuditService.getRiskLevel(mediumRiskContent);
        assertEquals(1, mediumRiskLevel, "两个敏感词应该是中风险");
        
        // 测试高风险：三个敏感词
        String highRiskContent = "今天吵架了，很生气，也很失望";
        int highRiskLevel = contentAuditService.getRiskLevel(highRiskContent);
        assertEquals(2, highRiskLevel, "三个敏感词应该是高风险");
        
        // 测试高风险：极端关键词
        String extremeRiskContent = "我要和她分手";
        int extremeRiskLevel = contentAuditService.getRiskLevel(extremeRiskContent);
        assertEquals(2, extremeRiskLevel, "极端关键词应该是高风险");
    }

    @Test
    @DisplayName("集成测试 - 建议生成质量")
    void testSuggestionQuality() {
        // 测试分手场景建议
        String breakupContent = "我想和她分手";
        String breakupSuggestion = contentAuditService.getReplacementSuggestion(breakupContent);
        
        assertNotNull(breakupSuggestion, "分手场景应该生成建议");
        assertTrue(breakupSuggestion.contains("分手") || breakupSuggestion.contains("沟通") || 
                  breakupSuggestion.contains("决定"), "建议应该与分手场景相关");
        
        // 测试吵架场景建议
        String argumentContent = "我们吵架了，我很生气";
        String argumentSuggestion = contentAuditService.getReplacementSuggestion(argumentContent);
        
        assertNotNull(argumentSuggestion, "吵架场景应该生成建议");
        assertTrue(argumentSuggestion.contains("吵架") || argumentSuggestion.contains("冷静") || 
                  argumentSuggestion.contains("根源"), "建议应该与吵架场景相关");
        
        // 测试通用场景建议
        String generalContent = "关系遇到了一些问题";
        String generalSuggestion = contentAuditService.getReplacementSuggestion(generalContent);
        
        assertNotNull(generalSuggestion, "通用场景应该生成建议");
        assertTrue(generalSuggestion.contains("沟通") || generalSuggestion.contains("思考") || 
                  generalSuggestion.contains("努力"), "建议应该包含通用指导");
    }

    @Test
    @DisplayName("集成测试 - 边界情况处理")
    void testBoundaryConditions() {
        // 测试空内容
        String emptyContent = "";
        AuditResult emptyResult = contentAuditService.auditContent(emptyContent);
        assertFalse(emptyResult.isExtreme(), "空内容不应该被识别为极端内容");
        
        // 测试null内容
        String nullContent = null;
        AuditResult nullResult = contentAuditService.auditContent(nullContent);
        assertFalse(nullResult.isExtreme(), "null内容不应该被识别为极端内容");
        
        // 测试超短内容
        String shortContent = "好";
        AuditResult shortResult = contentAuditService.auditContent(shortContent);
        assertFalse(shortResult.isExtreme(), "超短内容不应该被识别为极端内容");
        
        // 测试超长内容
        StringBuilder longContent = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longContent.append("今天很开心，");
        }
        longContent.append("但是我想分手");
        
        AuditResult longResult = contentAuditService.auditContent(longContent.toString());
        assertTrue(longResult.isExtreme(), "长文本中的关键词应该被正确识别");
    }

    @Test
    @DisplayName("集成测试 - 性能表现")
    void testPerformance() {
        String testContent = "今天和女朋友一起看了电影，感觉很开心，关系越来越好了";
        
        // 测试多次调用的性能
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            contentAuditService.auditContent(testContent);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // 100次调用应该在合理时间内完成（比如1秒内）
        assertTrue(duration < 1000, "100次调用应该在1秒内完成，实际耗时: " + duration + "ms");
        
        // 测试极端内容的性能
        String extremeContent = "我要和她分手，永远不再见面！";
        startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            contentAuditService.auditContent(extremeContent);
        }
        
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        
        assertTrue(duration < 1000, "100次极端内容审核应该在1秒内完成，实际耗时: " + duration + "ms");
    }

    @Test
    @DisplayName("集成测试 - 一致性验证")
    void testConsistency() {
        String testContent = "我要和她分手";
        
        // 多次调用应该返回一致的结果
        AuditResult result1 = contentAuditService.auditContent(testContent);
        AuditResult result2 = contentAuditService.auditContent(testContent);
        AuditResult result3 = contentAuditService.auditContent(testContent);
        
        // 验证结果一致性
        assertEquals(result1.isExtreme(), result2.isExtreme(), "多次调用结果应该一致");
        assertEquals(result2.isExtreme(), result3.isExtreme(), "多次调用结果应该一致");
        assertEquals(result1.getRiskLevel(), result2.getRiskLevel(), "风险等级应该一致");
        assertEquals(result2.getRiskLevel(), result3.getRiskLevel(), "风险等级应该一致");
        assertEquals(result1.getAuditStatus(), result2.getAuditStatus(), "审核状态应该一致");
        assertEquals(result2.getAuditStatus(), result3.getAuditStatus(), "审核状态应该一致");
        
        // 验证建议内容一致性（相同内容应该返回相同建议）
        assertEquals(result1.getSuggestion(), result2.getSuggestion(), "建议内容应该一致");
        assertEquals(result2.getSuggestion(), result3.getSuggestion(), "建议内容应该一致");
    }
}
