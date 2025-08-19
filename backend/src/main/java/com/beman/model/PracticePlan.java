package com.beman.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 实践计划模型
 * 用于用户标记和跟踪关系改善计划
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticePlan {
    
    /**
     * 计划ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 计划标题
     */
    private String title;
    
    /**
     * 计划描述
     */
    private String description;
    
    /**
     * 计划类型：1-沟通改善，2-情绪管理，3-关系维护，4-个人成长
     */
    private Integer type;
    
    /**
     * 计划状态：0-未开始，1-进行中，2-已完成，3-已放弃
     */
    private Integer status;
    
    /**
     * 优先级：1-低，2-中，3-高
     */
    private Integer priority;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 预计完成时间
     */
    private LocalDateTime expectedEndTime;
    
    /**
     * 实际完成时间
     */
    private LocalDateTime actualEndTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 是否公开：0-私密，1-公开
     */
    private Integer isPublic;
    
    /**
     * 标签
     */
    private String tags;
    
    /**
     * 进度百分比（0-100）
     */
    private Integer progress;
    
    /**
     * 构造函数（简化版）
     */
    public PracticePlan(Long userId, String title, String description, Integer type) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.status = 0; // 默认未开始
        this.priority = 2; // 默认中等优先级
        this.progress = 0; // 默认进度0%
        this.isPublic = 0; // 默认私密
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
}
