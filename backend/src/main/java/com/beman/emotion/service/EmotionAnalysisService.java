package com.beman.emotion.service;

import com.beman.emotion.dto.EmotionAnalysisRequest;
import com.beman.emotion.dto.EmotionAnalysisResponse;
import com.beman.emotion.entity.EmotionRecord;
import com.beman.emotion.repository.EmotionRecordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmotionAnalysisService {
    
    private final EmotionRecordRepository emotionRecordRepository;
    private final ObjectMapper objectMapper;
    
    @Transactional
    public EmotionAnalysisResponse analyzeEmotion(EmotionAnalysisRequest request, Long userId) {
        // 调用AI服务进行情绪分析
        Map<String, Object> analysisResult = performEmotionAnalysis(request.getPartnerText());
        
        // 保存记录
        EmotionRecord record = new EmotionRecord();
        record.setUserId(userId);
        record.setPartnerText(request.getPartnerText());
        
        try {
            record.setEmotionAnalysis(objectMapper.writeValueAsString(analysisResult));
            record.setSuggestedResponse((String) analysisResult.get("suggestedResponse"));
            record.setAvoidWords(objectMapper.writeValueAsString(analysisResult.get("avoidWords")));
            record.setActionSuggestions(objectMapper.writeValueAsString(analysisResult.get("actionSuggestions")));
        } catch (JsonProcessingException e) {
            log.error("JSON序列化失败", e);
            throw new RuntimeException("数据处理失败");
        }
        
        EmotionRecord savedRecord = emotionRecordRepository.save(record);
        return convertToResponse(savedRecord);
    }
    
    public List<EmotionAnalysisResponse> getUserRecords(Long userId) {
        List<EmotionRecord> records = emotionRecordRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return records.stream().map(this::convertToResponse).collect(java.util.stream.Collectors.toList());
    }
    
    public EmotionAnalysisResponse getRecordById(Long recordId, Long userId) {
        EmotionRecord record = emotionRecordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new RuntimeException("记录不存在"));
        return convertToResponse(record);
    }
    
    private Map<String, Object> performEmotionAnalysis(String partnerText) {
        // 简化的情绪分析逻辑，实际应该调用AI服务
        Map<String, Object> result = new HashMap<>();
        
        String lowerText = partnerText.toLowerCase();
        
        // 简单的关键词匹配
        if (lowerText.contains("累") || lowerText.contains("疲惫")) {
            result.put("emotion", "疲惫");
            result.put("need", "关心");
            result.put("confidence", 0.85);
            result.put("suggestedResponse", "辛苦了，我给你揉揉肩吧？");
            result.put("avoidWords", Arrays.asList("我也很累", "那你休息吧"));
            result.put("actionSuggestions", Arrays.asList("主动分担家务", "准备热茶", "给予拥抱"));
        } else if (lowerText.contains("不关心") || lowerText.contains("忽略")) {
            result.put("emotion", "失望");
            result.put("need", "关注");
            result.put("confidence", 0.92);
            result.put("suggestedResponse", "对不起，我最近确实忽略你了，我们聊聊吧");
            result.put("avoidWords", Arrays.asList("我哪里不关心你了", "你太敏感了"));
            result.put("actionSuggestions", Arrays.asList("安排约会", "增加陪伴时间", "表达爱意"));
        } else if (lowerText.contains("生气") || lowerText.contains("愤怒")) {
            result.put("emotion", "愤怒");
            result.put("need", "理解");
            result.put("confidence", 0.88);
            result.put("suggestedResponse", "我理解你的感受，让我们冷静下来好好谈谈");
            result.put("avoidWords", Arrays.asList("你冷静点", "别生气了"));
            result.put("actionSuggestions", Arrays.asList("承认错误", "道歉", "倾听"));
        } else {
            result.put("emotion", "中性");
            result.put("need", "陪伴");
            result.put("confidence", 0.6);
            result.put("suggestedResponse", "我在听，继续说");
            result.put("avoidWords", Arrays.asList("打断", "转移话题"));
            result.put("actionSuggestions", Arrays.asList("专注倾听", "给予回应", "表达关心"));
        }
        
        return result;
    }
    
    private EmotionAnalysisResponse convertToResponse(EmotionRecord record) {
        EmotionAnalysisResponse response = new EmotionAnalysisResponse();
        response.setId(record.getId());
        response.setPartnerText(record.getPartnerText());
        response.setSuggestedResponse(record.getSuggestedResponse());
        response.setCreatedAt(record.getCreatedAt());
        
        try {
            if (record.getEmotionAnalysis() != null) {
                Map<String, Object> analysis = objectMapper.readValue(record.getEmotionAnalysis(), new TypeReference<Map<String, Object>>() {});
                response.setEmotion((String) analysis.get("emotion"));
                response.setNeed((String) analysis.get("need"));
                response.setConfidence((Double) analysis.get("confidence"));
            }
            
            if (record.getAvoidWords() != null) {
                List<String> avoidWords = objectMapper.readValue(record.getAvoidWords(), new TypeReference<List<String>>() {});
                response.setAvoidWords(avoidWords);
            }
            
            if (record.getActionSuggestions() != null) {
                List<String> actionSuggestions = objectMapper.readValue(record.getActionSuggestions(), new TypeReference<List<String>>() {});
                response.setActionSuggestions(actionSuggestions);
            }
        } catch (JsonProcessingException e) {
            log.error("JSON反序列化失败", e);
        }
        
        return response;
    }
} 