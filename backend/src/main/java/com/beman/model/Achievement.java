package com.beman.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 成就勋章模型
 * 用于用户解锁和展示各种成长里程碑
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {
    
    /**
     * 成就ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 成就类型：communication-沟通达人, emotion-情绪管理, relationship-关系经营, growth-个人成长
     */
    private String achievementType;
    
    /**
     * 成就名称
     */
    private String achievementName;
    
    /**
     * 成就描述
     */
    private String description;
    
    /**
     * 勋章图标URL
     */
    private String iconUrl;
    
    /**
     * 解锁时间
     */
    private LocalDateTime unlockDate;
    
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
    public Achievement(Long userId, String achievementType, String achievementName, String description) {
        this.userId = userId;
        this.achievementType = achievementType;
        this.achievementName = achievementName;
        this.description = description;
        this.deleted = 0;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
}
