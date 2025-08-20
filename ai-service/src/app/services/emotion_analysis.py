"""
情感分析服务
"""

import re
import jieba
from typing import Dict, List
from loguru import logger

class EmotionAnalysisService:
    """情感分析服务类"""
    
    def __init__(self):
        self.emotion_keywords = {
            "positive": ["开心", "快乐", "高兴", "兴奋", "满意", "幸福", "温暖", "喜欢", "爱"],
            "negative": ["难过", "伤心", "痛苦", "悲伤", "失望", "愤怒", "生气", "焦虑", "害怕"],
            "neutral": ["一般", "普通", "正常", "平静", "安静", "冷静"]
        }
        logger.info("情感分析服务初始化完成")
    
    def analyze_emotion(self, text: str) -> Dict:
        """分析文本情感"""
        if not text or len(text.strip()) == 0:
            return {"error": "输入文本不能为空"}
        
        # 文本预处理
        processed_text = re.sub(r'\s+', ' ', text.strip())
        
        # 情感分类
        emotion_category = self._classify_emotion(processed_text)
        
        # 情感强度评估
        intensity = self._assess_intensity(processed_text)
        
        # 情感关键词提取
        keywords = self._extract_emotion_keywords(processed_text)
        
        return {
            "text": text,
            "emotion_category": emotion_category,
            "intensity": intensity,
            "keywords": keywords,
            "suggestions": self._generate_suggestions(emotion_category, intensity)
        }
    
    def _classify_emotion(self, text: str) -> str:
        """情感分类"""
        positive_score = 0
        negative_score = 0
        
        for word in jieba.lcut(text):
            if word in self.emotion_keywords["positive"]:
                positive_score += 1
            elif word in self.emotion_keywords["negative"]:
                negative_score += 1
        
        if positive_score > negative_score:
            return "positive"
        elif negative_score > positive_score:
            return "negative"
        else:
            return "neutral"
    
    def _assess_intensity(self, text: str) -> float:
        """评估情感强度"""
        words = jieba.lcut(text)
        emotion_words = sum(1 for word in words 
                           if any(word in self.emotion_keywords[cat] 
                                 for cat in self.emotion_keywords))
        
        if len(words) == 0:
            return 0.5
        
        density = emotion_words / len(words)
        return min(1.0, density * 2 + 0.3)
    
    def _extract_emotion_keywords(self, text: str) -> List[str]:
        """提取情感关键词"""
        keywords = []
        words = jieba.lcut(text)
        
        for word in words:
            for category, word_list in self.emotion_keywords.items():
                if word in word_list and word not in keywords:
                    keywords.append(word)
        
        return keywords[:10]
    
    def _generate_suggestions(self, emotion_category: str, intensity: float) -> List[str]:
        """生成情感建议"""
        if emotion_category == "positive":
            return ["保持积极心态", "分享你的好心情", "继续享受生活"]
        elif emotion_category == "negative":
            return ["尝试转移注意力", "做一些喜欢的事情", "寻求朋友支持"]
        else:
            return ["情绪比较平稳", "可以尝试新事物", "保持平衡状态"]
