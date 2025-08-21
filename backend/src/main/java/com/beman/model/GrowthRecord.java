package com.beman.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 成长记录模型
 * 用于记录用户的成长轨迹和里程碑
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrowthRecord {
    
    /**
     * 记录ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 成长类型：1-沟通改善，2-情绪管理，3-关系维护，4-个人成长，5-财务透明，6-日程管理
     */
    private Integer growthType;
    
    /**
     * 成长标题
     */
    private String title;
    
    /**
     * 成长描述
     */
    private String description;
    
    /**
     * 成长分数（0-100）
     */
    private Integer score;
    
    /**
     * 成长状态：1-开始，2-进行中，3-完成，4-反思
     */
    private Integer status;
    
    /**
     * 相关标签
     */
    private String tags;
    
    /**
     * 记录时间
     */
    private LocalDateTime recordTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 是否删除：0-未删除，1-已删除
     */
    private Integer deleted;
    
    /**
     * 构造函数（简化版）
     */
    public GrowthRecord(Long userId, Integer growthType, String title, String description, Integer score) {
        this.userId = userId;
        this.growthType = growthType;
        this.title = title;
        this.description = description;
        this.score = score;
        this.status = 1; // 默认开始状态
        this.recordTime = LocalDateTime.now();
        this.deleted = 0;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
}
