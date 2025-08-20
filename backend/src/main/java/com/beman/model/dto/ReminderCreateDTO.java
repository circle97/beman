package com.beman.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 提醒创建DTO
 */
@Data
public class ReminderCreateDTO {

    /**
     * 日程ID
     */
    @NotNull(message = "日程ID不能为空")
    private Long scheduleId;

    /**
     * 提醒类型：1-邮件，2-短信，3-推送，4-微信，5-钉钉
     */
    @NotNull(message = "提醒类型不能为空")
    private Integer type;

    /**
     * 提醒时间
     */
    @NotNull(message = "提醒时间不能为空")
    private LocalDateTime reminderTime;

    /**
     * 提前时间（分钟）
     */
    private Integer advanceMinutes = 0;

    /**
     * 提醒内容
     */
    private String content;

    /**
     * 最大重试次数
     */
    private Integer maxRetryCount = 3;
}
