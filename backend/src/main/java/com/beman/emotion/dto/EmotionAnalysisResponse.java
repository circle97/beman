package com.beman.emotion.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EmotionAnalysisResponse {
    private Long id;
    private String partnerText;
    private String emotion;
    private String need;
    private double confidence;
    private String suggestedResponse;
    private List<String> avoidWords;
    private List<String> actionSuggestions;
    private LocalDateTime createdAt;
} 