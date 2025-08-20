package com.beman.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 提醒查询DTO
 */
@Data
public class ReminderQueryDTO {

    /**
     * 页码
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer size = 20;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 日程ID
     */
    private Long scheduleId;

    /**
     * 提醒类型
     */
    private Integer type;

    /**
     * 提醒状态：1-待发送，2-已发送，3-发送失败，4-已取消
     */
    private Integer status;

    /**
     * 提醒时间范围：开始
     */
    private LocalDateTime reminderTimeFrom;

    /**
     * 提醒时间范围：结束
     */
    private LocalDateTime reminderTimeTo;

    /**
     * 是否包含已取消的提醒
     */
    private Boolean includeCancelled = false;
}
