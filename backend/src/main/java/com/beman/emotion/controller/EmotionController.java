package com.beman.emotion.controller;

import com.beman.common.ApiResponse;
import com.beman.emotion.dto.EmotionAnalysisRequest;
import com.beman.emotion.dto.EmotionAnalysisResponse;
import com.beman.emotion.service.EmotionAnalysisService;
import com.beman.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/emotion")
@RequiredArgsConstructor
public class EmotionController {
    
    private final EmotionAnalysisService emotionAnalysisService;
    
    @PostMapping("/analyze")
    public ApiResponse<EmotionAnalysisResponse> analyzeEmotion(@Valid @RequestBody EmotionAnalysisRequest request, @CurrentUser Long userId) {
        EmotionAnalysisResponse response = emotionAnalysisService.analyzeEmotion(request, userId);
        return ApiResponse.success(response);
    }
    
    @GetMapping("/records")
    public ApiResponse<List<EmotionAnalysisResponse>> getUserRecords(@CurrentUser Long userId) {
        List<EmotionAnalysisResponse> records = emotionAnalysisService.getUserRecords(userId);
        return ApiResponse.success(records);
    }
    
    @GetMapping("/records/{recordId}")
    public ApiResponse<EmotionAnalysisResponse> getRecordById(@PathVariable Long recordId, @CurrentUser Long userId) {
        EmotionAnalysisResponse record = emotionAnalysisService.getRecordById(recordId, userId);
        return ApiResponse.success(record);
    }
} 