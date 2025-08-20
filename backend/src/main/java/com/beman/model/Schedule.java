package com.beman.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 日程实体类
 */
@Data
@TableName("schedule")
public class Schedule {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 日程标题
     */
    private String title;

    /**
     * 日程描述
     */
    private String description;

    /**
     * 日程类型：1-生日，2-纪念日，3-节日，4-约会，5-会议，6-其他
     */
    private Integer type;

    /**
     * 重要程度：1-低，2-中，3-高，4-紧急
     */
    private Integer priority;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 是否全天
     */
    private Boolean isAllDay;

    /**
     * 重复类型：0-不重复，1-每天，2-每周，3-每月，4-每年
     */
    private Integer repeatType;

    /**
     * 重复结束时间
     */
    private LocalDateTime repeatEndTime;

    /**
     * 提醒设置：JSON格式，包含提前时间等
     */
    private String reminderSettings;

    /**
     * 标签：JSON格式，用于分类和搜索
     */
    private String tags;

    /**
     * 位置
     */
    private String location;

    /**
     * 状态：1-正常，2-已完成，3-已取消
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer deleted;
}
