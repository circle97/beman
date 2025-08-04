package com.beman.emotion.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "emotion_records")
@EntityListeners(AuditingEntityListener.class)
public class EmotionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "partner_text", nullable = false, columnDefinition = "TEXT")
    private String partnerText;

    @Column(name = "emotion_analysis", columnDefinition = "JSON")
    private String emotionAnalysis;

    @Column(name = "suggested_response")
    private String suggestedResponse;

    @Column(name = "avoid_words", columnDefinition = "JSON")
    private String avoidWords;

    @Column(name = "action_suggestions", columnDefinition = "JSON")
    private String actionSuggestions;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
} 