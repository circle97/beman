package com.beman.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 成长轨迹实体类
 * 
 * @author beman
 * @since 2024-12-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("growth_trajectory")
public class GrowthTrajectory {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 记录日期
     */
    @TableField("record_date")
    private LocalDate recordDate;

    /**
     * 沟通质量评分(1-100)
     */
    @TableField("communication_score")
    private Integer communicationScore;

    /**
     * 信任程度评分(1-100)
     */
    @TableField("trust_score")
    private Integer trustScore;

    /**
     * 相互支持评分(1-100)
     */
    @TableField("support_score")
    private Integer supportScore;

    /**
     * 亲密度评分(1-100)
     */
    @TableField("intimacy_score")
    private Integer intimacyScore;

    /**
     * 综合评分(1-100)
     */
    @TableField("overall_score")
    private Integer overallScore;

    /**
     * 情绪状态
     */
    @TableField("mood_state")
    private String moodState;

    /**
     * 关系笔记
     */
    @TableField("relationship_notes")
    private String relationshipNotes;

    /**
     * 改进目标
     */
    @TableField("improvement_goals")
    private String improvementGoals;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
