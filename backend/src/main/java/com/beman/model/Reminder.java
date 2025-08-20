package com.beman.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 提醒实体类
 */
@Data
@TableName("reminder")
public class Reminder {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 日程ID
     */
    private Long scheduleId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 提醒类型：1-邮件，2-短信，3-推送，4-微信，5-钉钉
     */
    private Integer type;

    /**
     * 提醒时间
     */
    private LocalDateTime reminderTime;

    /**
     * 提前时间（分钟）
     */
    private Integer advanceMinutes;

    /**
     * 提醒内容
     */
    private String content;

    /**
     * 提醒状态：1-待发送，2-已发送，3-发送失败，4-已取消
     */
    private Integer status;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 最大重试次数
     */
    private Integer maxRetryCount;

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
