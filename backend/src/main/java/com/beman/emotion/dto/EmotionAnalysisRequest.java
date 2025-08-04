package com.beman.emotion.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EmotionAnalysisRequest {
    @NotBlank(message = "伴侣话语不能为空")
    private String partnerText;
} 