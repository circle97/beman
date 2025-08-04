package com.beman.community.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ContentFilterService {
    
    // 禁止的极端词汇
    private static final List<String> BANNED_WORDS = Arrays.asList(
        "分手", "离婚", "女人都", "男人都", "女权", "男权", "极端", "暴力"
    );
    
    // 引导性词汇，用于替换极端表达
    private static final List<String> GUIDANCE_WORDS = Arrays.asList(
        "改善", "沟通", "理解", "包容", "成长"
    );
    
    public boolean isContentSafe(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        
        String lowerContent = content.toLowerCase();
        
        // 检查是否包含禁止词汇
        for (String bannedWord : BANNED_WORDS) {
            if (lowerContent.contains(bannedWord.toLowerCase())) {
                return false;
            }
        }
        
        return true;
    }
    
    public String getGuidanceMessage(String content) {
        if (isContentSafe(content)) {
            return null;
        }
        
        return "试试换个角度思考：我们如何改善这个情况？建议使用建设性的表达方式。";
    }
    
    public String filterContent(String content) {
        if (content == null) {
            return "";
        }
        
        String filteredContent = content;
        for (String bannedWord : BANNED_WORDS) {
            filteredContent = filteredContent.replaceAll("(?i)" + bannedWord, "***");
        }
        
        return filteredContent;
    }
} 