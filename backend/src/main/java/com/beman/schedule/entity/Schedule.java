package com.beman.schedule.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "schedules")
@EntityListeners(AuditingEntityListener.class)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "event_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(name = "reminder_days", columnDefinition = "JSON")
    private String reminderDays;

    @Column(name = "gift_suggestion")
    private String giftSuggestion;

    @Column(name = "is_repeated")
    private boolean isRepeated = false;

    @Column(name = "repeat_type")
    @Enumerated(EnumType.STRING)
    private RepeatType repeatType;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status = ScheduleStatus.ACTIVE;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum EventType {
        BIRTHDAY, ANNIVERSARY, HOLIDAY, CUSTOM
    }

    public enum RepeatType {
        YEARLY, MONTHLY, WEEKLY
    }

    public enum ScheduleStatus {
        ACTIVE, COMPLETED, CANCELLED
    }
} 