package com.beman.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * AuditResult模型单元测试
 */
@DisplayName("AuditResult模型测试")
class AuditResultTest {

    @Test
    @DisplayName("测试默认构造函数")
    void testDefaultConstructor() {
        AuditResult result = new AuditResult();
        
        assertNotNull(result, "AuditResult对象应该被创建");
        assertFalse(result.isExtreme(), "默认应该不是极端内容");
        assertNull(result.getSuggestion(), "默认建议应该为null");
        assertEquals(0, result.getRiskLevel(), "默认风险等级应该为0");
        assertNull(result.getAuditStatus(), "默认审核状态应该为null");
        assertNull(result.getRejectReason(), "默认拒绝原因应该为null");
        assertNull(result.getCreateTime(), "默认创建时间应该为null");
    }

    @Test
    @DisplayName("测试带参数构造函数 - 极端内容")
    void testParameterizedConstructorExtreme() {
        String suggestion = "建议先尝试沟通和改善";
        AuditResult result = new AuditResult(true, suggestion);
        
        assertTrue(result.isExtreme(), "应该设置为极端内容");
        assertEquals(suggestion, result.getSuggestion(), "建议应该正确设置");
        assertEquals(2, result.getRiskLevel(), "极端内容应该是高风险");
        assertEquals(2, result.getAuditStatus(), "极端内容应该被拒绝");
        assertNotNull(result.getCreateTime(), "创建时间应该被设置");
    }

    @Test
    @DisplayName("测试带参数构造函数 - 正常内容")
    void testParameterizedConstructorNormal() {
        String suggestion = "内容正常";
        AuditResult result = new AuditResult(false, suggestion);
        
        assertFalse(result.isExtreme(), "应该设置为正常内容");
        assertEquals(suggestion, result.getSuggestion(), "建议应该正确设置");
        assertEquals(0, result.getRiskLevel(), "正常内容应该是低风险");
        assertEquals(1, result.getAuditStatus(), "正常内容应该通过");
        assertNotNull(result.getCreateTime(), "创建时间应该被设置");
    }

    @Test
    @DisplayName("测试setter和getter方法")
    void testSettersAndGetters() {
        AuditResult result = new AuditResult();
        
        // 测试所有字段的设置和获取
        result.setExtreme(true);
        result.setSuggestion("测试建议");
        result.setRiskLevel(1);
        result.setAuditStatus(1);
        result.setRejectReason("测试拒绝原因");
        result.setCreateTime(1234567890L);
        
        assertTrue(result.isExtreme(), "isExtreme应该正确设置");
        assertEquals("测试建议", result.getSuggestion(), "suggestion应该正确设置");
        assertEquals(1, result.getRiskLevel(), "riskLevel应该正确设置");
        assertEquals(1, result.getAuditStatus(), "auditStatus应该正确设置");
        assertEquals("测试拒绝原因", result.getRejectReason(), "rejectReason应该正确设置");
        assertEquals(1234567890L, result.getCreateTime(), "createTime应该正确设置");
    }

    @Test
    @DisplayName("测试equals和hashCode方法")
    void testEqualsAndHashCode() {
        AuditResult result1 = new AuditResult(true, "建议1");
        result1.setRiskLevel(2);
        result1.setAuditStatus(2);
        
        AuditResult result2 = new AuditResult(true, "建议1");
        result2.setRiskLevel(2);
        result2.setAuditStatus(2);
        
        AuditResult result3 = new AuditResult(false, "建议2");
        result3.setRiskLevel(0);
        result3.setAuditStatus(1);
        
        // 测试equals
        assertEquals(result1, result2, "相同内容的AuditResult应该相等");
        assertNotEquals(result1, result3, "不同内容的AuditResult不应该相等");
        assertNotEquals(result1, null, "AuditResult不应该等于null");
        assertNotEquals(result1, "字符串", "AuditResult不应该等于其他类型");
        
        // 测试hashCode
        assertEquals(result1.hashCode(), result2.hashCode(), "相同内容的AuditResult应该有相同的hashCode");
        assertNotEquals(result1.hashCode(), result3.hashCode(), "不同内容的AuditResult应该有不同的hashCode");
    }

    @Test
    @DisplayName("测试toString方法")
    void testToString() {
        AuditResult result = new AuditResult(true, "测试建议");
        result.setRiskLevel(2);
        result.setAuditStatus(2);
        result.setRejectReason("测试拒绝原因");
        
        String toString = result.toString();
        
        assertNotNull(toString, "toString不应该返回null");
        assertTrue(toString.contains("true"), "toString应该包含isExtreme值");
        assertTrue(toString.contains("测试建议"), "toString应该包含suggestion值");
        assertTrue(toString.contains("2"), "toString应该包含riskLevel值");
        assertTrue(toString.contains("测试拒绝原因"), "toString应该包含rejectReason值");
    }

    @Test
    @DisplayName("测试边界值")
    void testBoundaryValues() {
        AuditResult result = new AuditResult();
        
        // 测试风险等级边界值
        result.setRiskLevel(0);
        assertEquals(0, result.getRiskLevel(), "风险等级应该支持0");
        
        result.setRiskLevel(1);
        assertEquals(1, result.getRiskLevel(), "风险等级应该支持1");
        
        result.setRiskLevel(2);
        assertEquals(2, result.getRiskLevel(), "风险等级应该支持2");
        
        result.setRiskLevel(999);
        assertEquals(999, result.getRiskLevel(), "风险等级应该支持其他值");
        
        // 测试审核状态边界值
        result.setAuditStatus(0);
        assertEquals(0, result.getAuditStatus(), "审核状态应该支持0");
        
        result.setAuditStatus(1);
        assertEquals(1, result.getAuditStatus(), "审核状态应该支持1");
        
        result.setAuditStatus(2);
        assertEquals(2, result.getAuditStatus(), "审核状态应该支持2");
    }

    @Test
    @DisplayName("测试时间戳处理")
    void testTimestampHandling() {
        AuditResult result = new AuditResult();
        
        long currentTime = System.currentTimeMillis();
        result.setCreateTime(currentTime);
        
        assertEquals(currentTime, result.getCreateTime(), "时间戳应该正确设置和获取");
        
        // 测试0时间戳
        result.setCreateTime(0L);
        assertEquals(0L, result.getCreateTime(), "应该支持0时间戳");
        
        // 测试负数时间戳
        result.setCreateTime(-1000L);
        assertEquals(-1000L, result.getCreateTime(), "应该支持负数时间戳");
    }

    @Test
    @DisplayName("测试空字符串处理")
    void testEmptyStringHandling() {
        AuditResult result = new AuditResult();
        
        // 测试空字符串
        result.setSuggestion("");
        assertEquals("", result.getSuggestion(), "应该支持空字符串");
        
        result.setRejectReason("");
        assertEquals("", result.getRejectReason(), "应该支持空字符串");
        
        // 测试null值
        result.setSuggestion(null);
        assertNull(result.getSuggestion(), "应该支持null值");
        
        result.setRejectReason(null);
        assertNull(result.getRejectReason(), "应该支持null值");
    }
}
