package com.beman.schedule.dto;

import lombok.Data;
import com.beman.schedule.entity.Schedule.EventType;
import com.beman.schedule.entity.Schedule.RepeatType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class ScheduleRequest {
    @NotBlank(message = "标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "事件日期不能为空")
    private LocalDate eventDate;

    @NotNull(message = "事件类型不能为空")
    private EventType eventType;

    private List<Integer> reminderDays;

    private String giftSuggestion;

    private boolean isRepeated = false;

    private RepeatType repeatType;
} 