package com.beman.schedule.dto;

import lombok.Data;
import com.beman.schedule.entity.Schedule.EventType;
import com.beman.schedule.entity.Schedule.RepeatType;
import com.beman.schedule.entity.Schedule.ScheduleStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScheduleResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate eventDate;
    private EventType eventType;
    private List<Integer> reminderDays;
    private String giftSuggestion;
    private boolean isRepeated;
    private RepeatType repeatType;
    private ScheduleStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 计算字段
    private long daysUntilEvent;
    private boolean isUpcoming;
} 