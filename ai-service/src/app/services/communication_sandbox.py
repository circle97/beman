import jieba
import re
from typing import List, Dict, Any, Optional
from datetime import datetime
import json
import os

class CommunicationSandboxService:
    """
    沟通沙盒服务类
    提供沟通场景模拟、对话建议生成、冲突解决指导等功能
    """
    
    def __init__(self):
        self.scenario_library = self._load_scenario_library()
        self.conflict_patterns = self._load_conflict_patterns()
        self.communication_tips = self._load_communication_tips()
        self.dialogue_templates = self._load_dialogue_templates()
        
    def _load_scenario_library(self) -> Dict[str, Any]:
        """加载沟通场景库"""
        return {
            "relationship_conflict": {
                "name": "关系冲突场景",
                "description": "处理日常关系中的冲突和分歧",
                "scenarios": [
                    {
                        "id": "rc_001",
                        "title": "家务分工分歧",
                        "description": "关于家务分工的意见不一致",
                        "context": "你和伴侣在家务分工上产生了分歧，你觉得分配不公平",
                        "difficulty": "medium",
                        "tags": ["家务", "分工", "公平性"]
                    },
                    {
                        "id": "rc_002", 
                        "title": "消费观念差异",
                        "description": "在消费和理财方面的观念冲突",
                        "context": "你想买一件心仪已久的物品，但伴侣认为太贵了",
                        "difficulty": "hard",
                        "tags": ["消费", "理财", "价值观"]
                    },
                    {
                        "id": "rc_003",
                        "title": "时间安排冲突",
                        "description": "个人时间和共同时间的平衡问题",
                        "context": "你想安排一些个人时间，但伴侣希望多些共同活动",
                        "difficulty": "medium",
                        "tags": ["时间管理", "个人空间", "共同活动"]
                    }
                ]
            },
            "emotional_support": {
                "name": "情感支持场景",
                "description": "在对方情绪低落时提供支持和安慰",
                "scenarios": [
                    {
                        "id": "es_001",
                        "title": "工作压力安慰",
                        "description": "伴侣工作压力大，需要情感支持",
                        "context": "伴侣最近工作压力很大，情绪低落，需要你的支持",
                        "difficulty": "easy",
                        "tags": ["工作压力", "情感支持", "安慰"]
                    },
                    {
                        "id": "es_002",
                        "title": "家庭矛盾处理",
                        "description": "帮助处理家庭内部矛盾",
                        "context": "伴侣与家人产生了矛盾，心情不好",
                        "difficulty": "hard",
                        "tags": ["家庭矛盾", "情感支持", "调解"]
                    }
                ]
            },
            "communication_improvement": {
                "name": "沟通改善场景",
                "description": "改善日常沟通方式和效果",
                "scenarios": [
                    {
                        "id": "ci_001",
                        "title": "表达需求练习",
                        "description": "练习如何清晰表达自己的需求",
                        "context": "你需要练习如何向伴侣清晰表达自己的需求和感受",
                        "difficulty": "easy",
                        "tags": ["表达", "需求", "感受"]
                    },
                    {
                        "id": "ci_002",
                        "title": "倾听技巧训练",
                        "description": "提高倾听和理解能力",
                        "context": "你想提高倾听伴侣的能力，更好地理解对方",
                        "difficulty": "medium",
                        "tags": ["倾听", "理解", "共情"]
                    }
                ]
            }
        }
    
    def _load_conflict_patterns(self) -> Dict[str, Any]:
        """加载冲突模式库"""
        return {
            "escalation_patterns": [
                "指责性语言",
                "人身攻击", 
                "翻旧账",
                "情绪化表达",
                "回避问题"
            ],
            "resolution_patterns": [
                "冷静分析",
                "换位思考",
                "寻找共同点",
                "妥协让步",
                "寻求第三方帮助"
            ],
            "prevention_strategies": [
                "定期沟通",
                "情绪管理",
                "边界设定",
                "共同目标",
                "感恩练习"
            ]
        }
    
    def _load_communication_tips(self) -> Dict[str, List[str]]:
        """加载沟通技巧库"""
        return {
            "active_listening": [
                "保持眼神接触",
                "不打断对方",
                "重复关键信息",
                "表达理解和同情",
                "提问澄清"
            ],
            "nonviolent_communication": [
                "观察事实",
                "表达感受",
                "说明需求",
                "提出请求"
            ],
            "conflict_resolution": [
                "保持冷静",
                "寻找共同点",
                "避免指责",
                "寻求解决方案",
                "达成共识"
            ],
            "emotional_expression": [
                "使用'我'语句",
                "具体描述感受",
                "避免绝对化语言",
                "表达期望而非要求"
            ]
        }
    
    def _load_dialogue_templates(self) -> Dict[str, List[str]]:
        """加载对话模板库"""
        return {
            "opening_statements": [
                "我想和你谈谈关于...的事情",
                "我注意到最近...",
                "我觉得我们可能需要讨论一下...",
                "我想了解你的想法关于..."
            ],
            "feeling_expressions": [
                "当...发生时，我感到...",
                "我觉得...因为...",
                "我担心...",
                "我希望..."
            ],
            "understanding_questions": [
                "你能告诉我更多关于...吗？",
                "我理解得对吗？",
                "你的意思是...？",
                "你觉得...怎么样？"
            ],
            "resolution_suggestions": [
                "我们能不能一起想想办法？",
                "你觉得有什么好的解决方案吗？",
                "我们能不能找个折中的办法？",
                "你觉得什么时候再谈比较好？"
            ]
        }
    
    def get_scenario(self, category: str = None, difficulty: str = None) -> Dict[str, Any]:
        """获取沟通场景"""
        if category and category in self.scenario_library:
            scenarios = self.scenario_library[category]["scenarios"]
            if difficulty:
                scenarios = [s for s in scenarios if s["difficulty"] == difficulty]
            return {
                "category": self.scenario_library[category]["name"],
                "description": self.scenario_library[category]["description"],
                "scenarios": scenarios
            }
        
        # 返回所有场景
        all_scenarios = []
        for cat_key, cat_data in self.scenario_library.items():
            all_scenarios.extend(cat_data["scenarios"])
        
        if difficulty:
            all_scenarios = [s for s in all_scenarios if s["difficulty"] == difficulty]
        
        return {
            "category": "所有场景",
            "description": "完整的沟通场景库",
            "scenarios": all_scenarios
        }
    
    def generate_dialogue_suggestions(self, scenario_id: str, user_input: str) -> Dict[str, Any]:
        """生成对话建议"""
        # 找到对应场景
        scenario = None
        for cat_data in self.scenario_library.values():
            for s in cat_data["scenarios"]:
                if s["id"] == scenario_id:
                    scenario = s
                    break
            if scenario:
                break
        
        if not scenario:
            return {"error": "场景不存在"}
        
        # 分析用户输入
        analysis = self._analyze_user_input(user_input)
        
        # 生成建议
        suggestions = self._generate_suggestions(scenario, analysis)
        
        return {
            "scenario": scenario,
            "user_input_analysis": analysis,
            "suggestions": suggestions,
            "next_steps": self._suggest_next_steps(analysis)
        }
    
    def _analyze_user_input(self, text: str) -> Dict[str, Any]:
        """分析用户输入"""
        # 分词
        words = list(jieba.cut(text))
        
        # 情感分析
        emotion_keywords = {
            "positive": ["好", "棒", "喜欢", "开心", "满意", "理解", "同意"],
            "negative": ["不好", "糟糕", "讨厌", "生气", "失望", "不理解", "不同意"],
            "neutral": ["觉得", "认为", "可能", "也许", "一般"]
        }
        
        emotion_score = {"positive": 0, "negative": 0, "neutral": 0}
        for word in words:
            for emotion, keywords in emotion_keywords.items():
                if word in keywords:
                    emotion_score[emotion] += 1
        
        # 沟通风格分析
        style_indicators = {
            "assertive": ["我", "我觉得", "我认为", "我希望"],
            "passive": ["你", "你觉得", "你认为", "你希望"],
            "aggressive": ["必须", "应该", "总是", "从不"]
        }
        
        style_score = {"assertive": 0, "passive": 0, "aggressive": 0}
        for word in words:
            for style, indicators in style_indicators.items():
                if word in indicators:
                    style_score[style] += 1
        
        return {
            "words": words,
            "emotion_analysis": emotion_score,
            "style_analysis": style_score,
            "dominant_emotion": max(emotion_score, key=emotion_score.get),
            "dominant_style": max(style_score, key=style_score.get) if any(style_score.values()) else "neutral"
        }
    
    def _generate_suggestions(self, scenario: Dict, analysis: Dict) -> Dict[str, Any]:
        """生成具体建议"""
        suggestions = {
            "immediate_response": [],
            "communication_style": [],
            "conflict_resolution": [],
            "long_term_strategies": []
        }
        
        # 根据情感分析生成建议
        if analysis["dominant_emotion"] == "negative":
            suggestions["immediate_response"].extend([
                "先深呼吸，保持冷静",
                "表达你理解对方的感受",
                "避免立即反驳或辩解"
            ])
        
        # 根据沟通风格生成建议
        if analysis["dominant_style"] == "aggressive":
            suggestions["communication_style"].extend([
                "使用'我'语句而非'你'语句",
                "具体描述情况而非概括性判断",
                "表达感受而非指责"
            ])
        elif analysis["dominant_style"] == "passive":
            suggestions["communication_style"].extend([
                "勇敢表达自己的感受和需求",
                "使用肯定的语言",
                "不要害怕表达不同意见"
            ])
        
        # 根据场景生成具体建议
        if "家务" in scenario["tags"]:
            suggestions["conflict_resolution"].extend([
                "列出所有家务项目",
                "根据各自时间和能力分配",
                "定期重新评估和调整"
            ])
        elif "消费" in scenario["tags"]:
            suggestions["conflict_resolution"].extend([
                "制定共同的预算计划",
                "区分必要支出和可选支出",
                "为个人爱好预留空间"
            ])
        
        # 长期策略
        suggestions["long_term_strategies"].extend([
            "建立定期沟通机制",
            "学习情绪管理技巧",
            "培养共同兴趣爱好",
            "定期回顾和庆祝关系进展"
        ])
        
        return suggestions
    
    def _suggest_next_steps(self, analysis: Dict) -> List[str]:
        """建议下一步行动"""
        next_steps = []
        
        if analysis["dominant_emotion"] == "negative":
            next_steps.append("先处理情绪，等冷静后再继续对话")
        
        if analysis["dominant_style"] == "aggressive":
            next_steps.append("练习使用更温和的表达方式")
        
        next_steps.extend([
            "安排一个专门的时间进行深入讨论",
            "准备具体的例子和感受描述",
            "考虑寻求专业咨询师的帮助"
        ])
        
        return next_steps
    
    def practice_communication_skill(self, skill_type: str) -> Dict[str, Any]:
        """练习特定沟通技巧"""
        if skill_type not in self.communication_tips:
            return {"error": "技巧类型不存在"}
        
        tips = self.communication_tips[skill_type]
        
        # 生成练习建议
        practice_exercises = {
            "active_listening": [
                "找一个朋友，练习5分钟不打断的倾听",
                "重复对方说的关键信息",
                "用肢体语言表达你在认真听"
            ],
            "nonviolent_communication": [
                "选择一个日常情况，练习四步表达法",
                "记录你的观察、感受、需求和请求",
                "与信任的人练习这些表达方式"
            ],
            "conflict_resolution": [
                "回忆一个冲突，分析可以改进的地方",
                "练习'我们'思维而非'我对你错'",
                "寻找冲突中的共同目标"
            ],
            "emotional_expression": [
                "每天记录3个感受",
                "练习用'我'语句表达",
                "避免使用'总是'、'从不'等绝对化词汇"
            ]
        }
        
        return {
            "skill_type": skill_type,
            "tips": tips,
            "practice_exercises": practice_exercises.get(skill_type, []),
            "daily_goal": "每天练习15-20分钟",
            "progress_tracking": "记录练习效果和感受"
        }
    
    def get_conflict_resolution_guide(self, conflict_type: str = None) -> Dict[str, Any]:
        """获取冲突解决指导"""
        if conflict_type == "escalation":
            return {
                "warning_signs": self.conflict_patterns["escalation_patterns"],
                "immediate_actions": [
                    "暂停对话，深呼吸",
                    "使用'暂停'信号",
                    "各自冷静5-10分钟",
                    "重新开始对话"
                ],
                "prevention_tips": self.conflict_patterns["prevention_strategies"]
            }
        elif conflict_type == "resolution":
            return {
                "effective_patterns": self.conflict_patterns["resolution_patterns"],
                "step_by_step_process": [
                    "确认问题",
                    "表达各自观点",
                    "寻找共同点",
                    "提出解决方案",
                    "达成共识",
                    "制定行动计划"
                ],
                "communication_tools": self.communication_tips["conflict_resolution"]
            }
        else:
            return {
                "escalation_guide": self.get_conflict_resolution_guide("escalation"),
                "resolution_guide": self.get_conflict_resolution_guide("resolution"),
                "general_tips": [
                    "冲突是关系发展的正常部分",
                    "关键在于如何处理冲突",
                    "每次冲突都是学习的机会",
                    "保持对关系的长期视角"
                ]
            }
    
    def generate_dialogue_template(self, situation: str, emotion: str) -> Dict[str, Any]:
        """生成对话模板"""
        templates = {
            "opening": self._select_template("opening_statements", situation),
            "feeling_expression": self._select_template("feeling_expressions", emotion),
            "understanding": self._select_template("understanding_questions", situation),
            "resolution": self._select_template("resolution_suggestions", situation)
        }
        
        return {
            "situation": situation,
            "emotion": emotion,
            "templates": templates,
            "usage_tips": [
                "根据具体情况调整语言",
                "保持真诚和开放的态度",
                "给对方足够的回应时间",
                "注意非语言沟通"
            ]
        }
    
    def _select_template(self, template_type: str, context: str) -> str:
        """选择合适的模板"""
        templates = self.dialogue_templates.get(template_type, [])
        if not templates:
            return "请根据具体情况表达"
        
        # 简单的上下文匹配
        if "冲突" in context or "分歧" in context:
            return templates[0] if templates else "我想和你谈谈这个问题"
        elif "支持" in context or "安慰" in context:
            return templates[1] if len(templates) > 1 else "我想了解你的感受"
        else:
            return templates[0] if templates else "我想和你沟通一下"
    
    def get_health_check(self) -> Dict[str, Any]:
        """健康检查"""
        return {
            "service": "CommunicationSandboxService",
            "status": "healthy",
            "timestamp": datetime.now().isoformat(),
            "components": {
                "scenario_library": len(self.scenario_library),
                "conflict_patterns": len(self.conflict_patterns),
                "communication_tips": len(self.communication_tips),
                "dialogue_templates": len(self.dialogue_templates)
            }
        }
