package com.beman.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 关系目标实体类
 * 
 * @author beman
 * @since 2024-12-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("relationship_goal")
public class RelationshipGoal {

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
     * 目标类型
     */
    @TableField("goal_type")
    private String goalType;

    /**
     * 目标标题
     */
    @TableField("title")
    private String title;

    /**
     * 目标描述
     */
    @TableField("description")
    private String description;

    /**
     * 目标完成日期
     */
    @TableField("target_date")
    private LocalDate targetDate;

    /**
     * 优先级：1-高，2-中，3-低
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 状态：0-进行中，1-已完成，2-已延期，3-已放弃
     */
    @TableField("status")
    private Integer status;

    /**
     * 进度(0-100)
     */
    @TableField("progress")
    private Integer progress;

    /**
     * 子目标里程碑
     */
    @TableField("milestones")
    private List<String> milestones;

    /**
     * 完成奖励
     */
    @TableField("reward")
    private String reward;

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
