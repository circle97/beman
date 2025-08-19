package com.beman.service;

import com.beman.model.AuditResult;

/**
 * 内容审核服务接口
 * 负责社区内容的审核、关键词过滤和极端倾向识别
 */
public interface ContentAuditService {
    
    /**
     * 审核内容
     * @param content 待审核的内容
     * @return 审核结果
     */
    AuditResult auditContent(String content);
    
    /**
     * 判断是否为极端内容
     * @param content 待判断的内容
     * @return true表示极端内容，false表示正常内容
     */
    boolean isExtremeContent(String content);
    
    /**
     * 获取替换建议
     * @param content 极端内容
     * @return 引导性建议
     */
    String getReplacementSuggestion(String content);
    
    /**
     * 获取内容风险等级
     * @param content 待评估内容
     * @return 风险等级：0-低风险，1-中风险，2-高风险
     */
    int getRiskLevel(String content);
}
