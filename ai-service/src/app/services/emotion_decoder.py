"""
情感解码器服务
Week 5核心功能：情感状态分析、关系健康度评估、个性化建议生成
"""

import re
import jieba
from typing import Dict, List, Optional
from datetime import datetime
from loguru import logger

class EmotionDecoderService:
    """情感解码器服务类"""
    
    def __init__(self):
        """初始化情感解码器服务"""
        self.emotion_patterns = self._load_emotion_patterns()
        self.relationship_indicators = self._load_relationship_indicators()
        self.suggestion_templates = self._load_suggestion_templates()
        logger.info("情感解码器服务初始化完成")
    
    def _load_emotion_patterns(self) -> Dict[str, List[str]]:
        """加载情感模式库"""
        return {
            "positive_emotions": ["开心", "快乐", "高兴", "满意", "幸福", "温暖", "喜欢", "爱"],
            "negative_emotions": ["难过", "伤心", "痛苦", "悲伤", "失望", "愤怒", "焦虑", "害怕"],
            "relationship_emotions": ["信任", "理解", "包容", "支持", "亲密", "安全", "归属"],
            "communication_patterns": ["沟通", "交流", "倾听", "表达", "分享", "理解", "共情"]
        }
    
    def _load_relationship_indicators(self) -> Dict[str, Dict]:
        """加载关系健康度指标"""
        return {
            "communication": {
                "positive": ["主动沟通", "倾听理解", "表达清晰"],
                "negative": ["沟通困难", "表达不清", "缺乏倾听"],
                "weight": 0.3
            },
            "trust": {
                "positive": ["相互信任", "诚实透明", "承诺履行"],
                "negative": ["怀疑猜忌", "隐瞒欺骗", "承诺失信"],
                "weight": 0.25
            },
            "support": {
                "positive": ["相互支持", "关心照顾", "共同成长"],
                "negative": ["缺乏支持", "冷漠忽视", "各自为政"],
                "weight": 0.2
            }
        }
    
    def _load_suggestion_templates(self) -> Dict[str, List[str]]:
        """加载建议模板库"""
        return {
            "communication_improvement": [
                "建议每天安排固定的沟通时间，分享彼此的感受和想法",
                "学习使用'我感受'的表达方式，避免指责性语言"
            ],
            "trust_building": [
                "保持诚实透明，及时分享重要信息",
                "履行承诺，如果无法完成要提前沟通"
            ],
            "emotional_support": [
                "关注对方的情绪变化，主动询问和关心",
                "在对方遇到困难时提供情感支持"
            ]
        }
    
    def decode_emotion_state(self, text: str, context: Optional[Dict] = None) -> Dict:
        """解码情感状态"""
        try:
            if not text or len(text.strip()) == 0:
                return {"error": "输入文本不能为空"}
            
            # 文本预处理
            processed_text = self._preprocess_text(text)
            
            # 情感状态分析
            emotion_analysis = self._analyze_emotion_state(processed_text)
            
            # 关系健康度评估
            relationship_health = self._assess_relationship_health(processed_text, context)
            
            # 个性化建议生成
            suggestions = self._generate_personalized_suggestions(emotion_analysis, relationship_health)
            
            return {
                "text": text,
                "emotion_state": emotion_analysis,
                "relationship_health": relationship_health,
                "suggestions": suggestions,
                "analysis_time": datetime.now().isoformat(),
                "confidence": self._calculate_confidence(processed_text, context)
            }
            
        except Exception as e:
            logger.error(f"情感解码失败: {str(e)}")
            return {"error": f"情感解码失败: {str(e)}"}
    
    def _preprocess_text(self, text: str) -> str:
        """文本预处理"""
        text = re.sub(r'\s+', ' ', text.strip())
        text = re.sub(r'[^\u4e00-\u9fa5a-zA-Z0-9\s，。！？；：""''（）【】]', '', text)
        return text
    
    def _analyze_emotion_state(self, text: str) -> Dict:
        """分析情感状态"""
        words = jieba.lcut(text)
        
        # 计算各类情感得分
        emotion_scores = {}
        for category, word_list in self.emotion_patterns.items():
            score = sum(1 for word in words if word in word_list)
            emotion_scores[category] = score
        
        # 确定主要情感状态
        primary_emotion = max(emotion_scores.items(), key=lambda x: x[1])[0]
        
        # 计算情感强度
        total_emotion_words = sum(emotion_scores.values())
        intensity = min(1.0, total_emotion_words / len(words) * 3) if words else 0.5
        
        return {
            "primary_emotion": primary_emotion,
            "emotion_scores": emotion_scores,
            "intensity": intensity,
            "emotion_keywords": self._extract_emotion_keywords(words)
        }
    
    def _assess_relationship_health(self, text: str, context: Optional[Dict] = None) -> Dict:
        """评估关系健康度"""
        words = jieba.lcut(text)
        
        # 计算各维度得分
        dimension_scores = {}
        total_score = 0
        
        for dimension, config in self.relationship_indicators.items():
            positive_score = sum(1 for word in words if word in config["positive"])
            negative_score = sum(1 for word in words if word in config["negative"])
            
            # 计算维度得分（0-100）
            dimension_score = max(0, min(100, (positive_score - negative_score) * 10 + 50))
            dimension_scores[dimension] = {
                "score": dimension_score,
                "positive_indicators": [word for word in words if word in config["positive"]],
                "negative_indicators": [word for word in words if word in config["negative"]],
                "weight": config["weight"]
            }
            
            # 加权总分
            total_score += dimension_score * config["weight"]
        
        # 确定关系健康等级
        health_level = self._determine_health_level(total_score)
        
        return {
            "overall_score": round(total_score, 2),
            "health_level": health_level,
            "dimension_scores": dimension_scores,
            "strengths": self._identify_strengths(dimension_scores),
            "weaknesses": self._identify_weaknesses(dimension_scores)
        }
    
    def _generate_personalized_suggestions(self, emotion_state: Dict, relationship_health: Dict) -> List[str]:
        """生成个性化建议"""
        suggestions = []
        
        # 基于情感状态的建议
        if emotion_state["primary_emotion"] == "negative_emotions":
            suggestions.extend([
                "当前情绪状态偏负面，建议先调整自己的心情",
                "可以尝试深呼吸、冥想或运动来缓解负面情绪"
            ])
        
        # 基于关系健康度的建议
        if relationship_health["health_level"] == "poor":
            suggestions.extend([
                "关系健康度较低，建议寻求专业咨询师的帮助",
                "与伴侣进行深入的沟通，了解彼此的需求和期望"
            ])
        
        # 基于具体维度的建议
        for dimension, data in relationship_health["dimension_scores"].items():
            if data["score"] < 60 and dimension in self.suggestion_templates:
                suggestions.extend(self.suggestion_templates[dimension][:1])
        
        return suggestions[:5]
    
    def _extract_emotion_keywords(self, words: List[str]) -> List[str]:
        """提取情感关键词"""
        keywords = []
        for category, word_list in self.emotion_patterns.items():
            for word in words:
                if word in word_list and word not in keywords:
                    keywords.append(word)
        return keywords[:10]
    
    def _determine_health_level(self, score: float) -> str:
        """确定健康等级"""
        if score >= 80:
            return "excellent"
        elif score >= 70:
            return "good"
        elif score >= 60:
            return "fair"
        elif score >= 50:
            return "poor"
        else:
            return "critical"
    
    def _identify_strengths(self, dimension_scores: Dict) -> List[str]:
        """识别关系优势"""
        strengths = []
        for dimension, data in dimension_scores.items():
            if data["score"] >= 80:
                strengths.append(f"{dimension}: 表现优秀")
        return strengths
    
    def _identify_weaknesses(self, dimension_scores: Dict) -> List[str]:
        """识别关系弱点"""
        weaknesses = []
        for dimension, data in dimension_scores.items():
            if data["score"] < 60:
                weaknesses.append(f"{dimension}: 需要改善")
        return weaknesses
    
    def _calculate_confidence(self, text: str, context: Optional[Dict] = None) -> float:
        """计算分析置信度"""
        words = jieba.lcut(text)
        emotion_words = sum(1 for word in words 
                           if any(word in word_list 
                                 for word_list in self.emotion_patterns.values()))
        
        if len(words) == 0:
            return 0.0
        
        density = emotion_words / len(words)
        length_factor = min(1.0, len(text) / 200)
        
        confidence = (density * 0.7 + length_factor * 0.3)
        return min(1.0, confidence)
