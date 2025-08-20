package com.beman.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 日程创建DTO
 */
@Data
public class ScheduleCreateDTO {

    /**
     * 日程标题
     */
    @NotBlank(message = "日程标题不能为空")
    private String title;

    /**
     * 日程描述
     */
    private String description;

    /**
     * 日程类型：1-生日，2-纪念日，3-节日，4-约会，5-会议，6-其他
     */
    @NotNull(message = "日程类型不能为空")
    private Integer type;

    /**
     * 重要程度：1-低，2-中，3-高，4-紧急
     */
    @NotNull(message = "重要程度不能为空")
    private Integer priority;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 是否全天
     */
    private Boolean isAllDay = false;

    /**
     * 重复类型：0-不重复，1-每天，2-每周，3-每月，4-每年
     */
    private Integer repeatType = 0;

    /**
     * 重复结束时间
     */
    private LocalDateTime repeatEndTime;

    /**
     * 提醒设置
     */
    private List<ReminderSetting> reminderSettings;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 位置
     */
    private String location;

    /**
     * 提醒设置内部类
     */
    @Data
    public static class ReminderSetting {
        /**
         * 提醒类型：1-邮件，2-短信，3-推送，4-微信，5-钉钉
         */
        private Integer type;

        /**
         * 提前时间（分钟）
         */
        private Integer advanceMinutes;

        /**
         * 提醒内容
         */
        private String content;
    }
}
