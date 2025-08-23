package com.beman.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 关系里程碑实体类
 * 
 * @author beman
 * @since 2024-12-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("relationship_milestone")
public class RelationshipMilestone {

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
     * 里程碑类型
     */
    @TableField("milestone_type")
    private String milestoneType;

    /**
     * 里程碑标题
     */
    @TableField("title")
    private String title;

    /**
     * 里程碑描述
     */
    @TableField("description")
    private String description;

    /**
     * 里程碑日期
     */
    @TableField("milestone_date")
    private LocalDate milestoneDate;

    /**
     * 地点
     */
    @TableField("location")
    private String location;

    /**
     * 情感评分(1-10)
     */
    @TableField("emotion_score")
    private Integer emotionScore;

    /**
     * 照片URL数组
     */
    @TableField("photos")
    private List<String> photos;

    /**
     * 标签数组
     */
    @TableField("tags")
    private List<String> tags;

    /**
     * 是否公开：0-私密，1-公开
     */
    @TableField("is_public")
    private Integer isPublic;

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
