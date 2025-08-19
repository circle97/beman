package com.beman.service;

import com.beman.model.AuditResult;
import com.beman.service.impl.ContentAuditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 内容审核服务单元测试
 */
@DisplayName("内容审核服务测试")
class ContentAuditServiceTest {

    private ContentAuditService contentAuditService;

    @BeforeEach
    void setUp() {
        contentAuditService = new ContentAuditServiceImpl();
    }

    @Test
    @DisplayName("测试极端内容识别 - 分手关键词")
    void testExtremeContentWithBreakup() {
        String content = "我要和她分手，永远不再见面！";
        boolean isExtreme = contentAuditService.isExtremeContent(content);
        assertTrue(isExtreme, "包含分手关键词应该被识别为极端内容");
    }

    @Test
    @DisplayName("测试极端内容识别 - 暴力关键词")
    void testExtremeContentWithViolence() {
        String content = "我恨你，去死吧！";
        boolean isExtreme = contentAuditService.isExtremeContent(content);
        assertTrue(isExtreme, "包含暴力关键词应该被识别为极端内容");
    }

    @Test
    @DisplayName("测试极端内容识别 - 敏感词组合")
    void testExtremeContentWithSensitiveCombination() {
        String content = "最近吵架了，感觉很生气，也很失望，想不开，活不下去了";
        boolean isExtreme = contentAuditService.isExtremeContent(content);
        assertTrue(isExtreme, "多个敏感词组合应该被识别为极端内容");
    }

    @Test
    @DisplayName("测试正常内容识别")
    void testNormalContent() {
        String content = "今天和女朋友一起看了电影，感觉很开心，关系越来越好了";
        boolean isExtreme = contentAuditService.isExtremeContent(content);
        assertFalse(isExtreme, "正常内容不应该被识别为极端内容");
    }

    @Test
    @DisplayName("测试空内容处理")
    void testEmptyContent() {
        String content = "";
        boolean isExtreme = contentAuditService.isExtremeContent(content);
        assertFalse(isExtreme, "空内容不应该被识别为极端内容");
    }

    @Test
    @DisplayName("测试null内容处理")
    void testNullContent() {
        String content = null;
        boolean isExtreme = contentAuditService.isExtremeContent(content);
        assertFalse(isExtreme, "null内容不应该被识别为极端内容");
    }

    @Test
    @DisplayName("测试风险等级评估 - 高风险")
    void testRiskLevelHigh() {
        String content = "我要和她分手，永远不再见面！";
        int riskLevel = contentAuditService.getRiskLevel(content);
        assertEquals(2, riskLevel, "极端内容应该是高风险");
    }

    @Test
    @DisplayName("测试风险等级评估 - 中风险")
    void testRiskLevelMedium() {
        String content = "最近吵架了，感觉很生气，也很失望";
        int riskLevel = contentAuditService.getRiskLevel(content);
        assertEquals(1, riskLevel, "多个敏感词应该是中风险");
    }

    @Test
    @DisplayName("测试风险等级评估 - 低风险")
    void testRiskLevelLow() {
        String content = "今天心情不太好，有点难过";
        int riskLevel = contentAuditService.getRiskLevel(content);
        assertEquals(0, riskLevel, "单个敏感词应该是低风险");
    }

    @Test
    @DisplayName("测试建议生成 - 分手场景")
    void testSuggestionForBreakup() {
        String content = "我想和她分手";
        String suggestion = contentAuditService.getReplacementSuggestion(content);
        assertNotNull(suggestion, "应该生成建议");
        assertTrue(suggestion.contains("分手") || suggestion.contains("沟通"), "建议应该与分手场景相关");
    }

    @Test
    @DisplayName("测试建议生成 - 吵架场景")
    void testSuggestionForArgument() {
        String content = "我们吵架了，我很生气";
        String suggestion = contentAuditService.getReplacementSuggestion(content);
        assertNotNull(suggestion, "应该生成建议");
        assertTrue(suggestion.contains("吵架") || suggestion.contains("冷静"), "建议应该与吵架场景相关");
    }

    @Test
    @DisplayName("测试建议生成 - 通用场景")
    void testSuggestionForGeneral() {
        String content = "关系遇到了一些问题";
        String suggestion = contentAuditService.getReplacementSuggestion(content);
        assertNotNull(suggestion, "应该生成通用建议");
    }

    @Test
    @DisplayName("测试完整内容审核 - 极端内容")
    void testFullAuditExtremeContent() {
        String content = "我要和她分手，永远不再见面！";
        AuditResult result = contentAuditService.auditContent(content);
        
        assertNotNull(result, "审核结果不应该为null");
        assertTrue(result.isExtreme(), "应该识别为极端内容");
        assertEquals(2, result.getRiskLevel(), "应该是高风险");
        assertEquals(2, result.getAuditStatus(), "应该被拒绝");
        assertNotNull(result.getSuggestion(), "应该提供建议");
        assertEquals("包含极端内容", result.getRejectReason(), "应该有拒绝原因");
    }

    @Test
    @DisplayName("测试完整内容审核 - 正常内容")
    void testFullAuditNormalContent() {
        String content = "今天和女朋友一起看了电影，感觉很开心";
        AuditResult result = contentAuditService.auditContent(content);
        
        assertNotNull(result, "审核结果不应该为null");
        assertFalse(result.isExtreme(), "不应该识别为极端内容");
        assertEquals(0, result.getRiskLevel(), "应该是低风险");
        assertEquals(1, result.getAuditStatus(), "应该通过");
        assertNull(result.getSuggestion(), "正常内容不需要建议");
        assertNull(result.getRejectReason(), "正常内容没有拒绝原因");
    }

    @Test
    @DisplayName("测试边界情况 - 单个敏感词")
    void testBoundarySingleSensitive() {
        String content = "今天有点生气";
        int riskLevel = contentAuditService.getRiskLevel(content);
        assertEquals(0, riskLevel, "单个敏感词应该是低风险");
    }

    @Test
    @DisplayName("测试边界情况 - 两个敏感词")
    void testBoundaryTwoSensitive() {
        String content = "今天吵架了，很生气";
        int riskLevel = contentAuditService.getRiskLevel(content);
        assertEquals(1, riskLevel, "两个敏感词应该是中风险");
    }

    @Test
    @DisplayName("测试边界情况 - 三个敏感词")
    void testBoundaryThreeSensitive() {
        String content = "今天吵架了，很生气，也很失望";
        int riskLevel = contentAuditService.getRiskLevel(content);
        assertEquals(2, riskLevel, "三个敏感词应该是高风险");
    }

    @Test
    @DisplayName("测试大小写不敏感")
    void testCaseInsensitive() {
        String content1 = "我要和她分手";
        String content2 = "我要和她分手";
        String content3 = "我要和她分手";
        
        boolean result1 = contentAuditService.isExtremeContent(content1);
        boolean result2 = contentAuditService.isExtremeContent(content2);
        boolean result3 = contentAuditService.isExtremeContent(content3);
        
        assertEquals(result1, result2, "大小写应该不影响结果");
        assertEquals(result2, result3, "大小写应该不影响结果");
    }

    @Test
    @DisplayName("测试特殊字符处理")
    void testSpecialCharacters() {
        String content = "我要和她分手！！！";
        boolean isExtreme = contentAuditService.isExtremeContent(content);
        assertTrue(isExtreme, "特殊字符不应该影响关键词识别");
    }

    @Test
    @DisplayName("测试长文本处理")
    void testLongText() {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longText.append("今天很开心，");
        }
        longText.append("但是我想分手");
        
        boolean isExtreme = contentAuditService.isExtremeContent(longText.toString());
        assertTrue(isExtreme, "长文本中的关键词应该被正确识别");
    }
}
