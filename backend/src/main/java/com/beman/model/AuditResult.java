package com.beman.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 内容审核结果模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditResult {
    
    /**
     * 是否为极端内容
     */
    private boolean isExtreme;
    
    /**
     * 审核建议
     */
    private String suggestion;
    
    /**
     * 风险等级：0-低风险，1-中风险，2-高风险
     */
    private int riskLevel;
    
    /**
     * 审核状态：0-待审核，1-通过，2-拒绝
     */
    private Integer auditStatus;
    
    /**
     * 拒绝原因
     */
    private String rejectReason;
    
    /**
     * 创建时间
     */
    private Long createTime;
    
    /**
     * 构造函数（简化版）
     */
    public AuditResult(boolean isExtreme, String suggestion) {
        this.isExtreme = isExtreme;
        this.suggestion = suggestion;
        this.riskLevel = isExtreme ? 2 : 0;
        this.auditStatus = isExtreme ? 2 : 1;
        this.createTime = System.currentTimeMillis();
    }
}
