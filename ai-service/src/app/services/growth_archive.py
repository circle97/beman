"""
成长档案AI服务
提供成长分析、建议生成和总结功能
"""

import jieba
import json
from datetime import datetime, timedelta
from typing import Dict, List, Optional, Any
import random


class GrowthArchiveService:
    """成长档案AI服务类"""
    
    def __init__(self):
        """初始化服务"""
        self._load_growth_patterns()
        self._load_improvement_suggestions()
        self._load_milestone_templates()
    
    def _load_growth_patterns(self):
        """加载成长模式库"""
        self.growth_patterns = {
            "communication": {
                "keywords": ["沟通", "对话", "表达", "倾听", "理解", "共情"],
                "milestones": ["首次主动沟通", "学会倾听", "表达清晰", "共情能力提升"],
                "challenges": ["沟通障碍", "表达不清", "缺乏耐心", "情绪化反应"]
            },
            "emotion": {
                "keywords": ["情绪", "感受", "压力", "焦虑", "平静", "调节"],
                "milestones": ["情绪识别", "压力管理", "情绪调节", "内心平静"],
                "challenges": ["情绪失控", "压力过大", "负面情绪", "缺乏调节方法"]
            },
            "relationship": {
                "keywords": ["关系", "信任", "亲密", "理解", "支持", "成长"],
                "milestones": ["建立信任", "深度理解", "相互支持", "共同成长"],
                "challenges": ["缺乏信任", "沟通不畅", "理解偏差", "成长不同步"]
            },
            "personal": {
                "keywords": ["目标", "计划", "执行", "反思", "改进", "成就"],
                "milestones": ["目标设定", "计划执行", "习惯养成", "目标达成"],
                "challenges": ["目标模糊", "执行不力", "缺乏坚持", "成就感不足"]
            }
        }
    
    def _load_improvement_suggestions(self):
        """加载改进建议库"""
        self.improvement_suggestions = {
            "communication": [
                "尝试使用'我'语句表达感受，避免指责对方",
                "练习主动倾听，重复对方的话确认理解",
                "在沟通前先深呼吸，保持冷静",
                "使用具体例子说明问题，避免抽象表达"
            ],
            "emotion": [
                "学习识别情绪触发点，提前预防",
                "练习深呼吸和冥想，培养内在平静",
                "建立情绪日记，记录情绪变化",
                "寻求专业帮助，学习情绪管理技巧"
            ],
            "relationship": [
                "定期进行关系回顾，分享感受和期望",
                "尝试新的共同活动，增进了解",
                "学习冲突解决技巧，避免升级",
                "建立共同目标，促进共同成长"
            ],
            "personal": [
                "设定SMART目标，确保可执行性",
                "建立每日习惯，从小事开始",
                "定期反思和调整，持续改进",
                "庆祝小成就，保持动力"
            ]
        }
    
    def analyze_growth_data(self, growth_records: List[Dict], achievements: List[Dict]) -> Dict[str, Any]:
        """分析成长数据，生成成长报告"""
        if not growth_records:
            return self._generate_empty_report()
        
        # 分析成长趋势
        trend_analysis = self._analyze_growth_trend(growth_records)
        
        # 分析成就分布
        achievement_analysis = self._analyze_achievements(achievements)
        
        # 识别成长模式
        pattern_analysis = self._identify_growth_patterns(growth_records)
        
        # 生成改进建议
        improvement_suggestions = self._generate_improvement_suggestions(
            growth_records, achievements
        )
        
        # 生成成长总结
        summary = self._generate_growth_summary(
            trend_analysis, achievement_analysis, pattern_analysis
        )
        
        return {
            "summary": summary,
            "trend_analysis": trend_analysis,
            "achievement_analysis": achievement_analysis,
            "pattern_analysis": pattern_analysis,
            "improvement_suggestions": improvement_suggestions,
            "generated_at": datetime.now().isoformat()
        }
    
    def _analyze_growth_trend(self, growth_records: List[Dict]) -> Dict[str, Any]:
        """分析成长趋势"""
        if not growth_records:
            return {"trend": "stable", "description": "暂无成长数据"}
        
        # 按时间排序
        sorted_records = sorted(growth_records, key=lambda x: x.get("recordTime", ""))
        
        # 计算分数趋势
        scores = [record.get("score", 0) for record in sorted_records]
        if len(scores) < 2:
            return {"trend": "stable", "description": "数据不足，无法分析趋势"}
        
        # 计算趋势
        score_changes = [scores[i] - scores[i-1] for i in range(1, len(scores))]
        avg_change = sum(score_changes) / len(score_changes)
        
        if avg_change > 5:
            trend = "rapid_improvement"
            description = "快速提升"
        elif avg_change > 1:
            trend = "steady_improvement"
            description = "稳步提升"
        elif avg_change > -1:
            trend = "stable"
            description = "保持稳定"
        elif avg_change > -5:
            trend = "slight_decline"
            description = "略有下降"
        else:
            trend = "decline"
            description = "需要关注"
        
        return {
            "trend": trend,
            "description": description,
            "average_change": round(avg_change, 2),
            "total_records": len(growth_records),
            "score_range": {"min": min(scores), "max": max(scores), "avg": round(sum(scores) / len(scores), 2)}
        }
    
    def _analyze_achievements(self, achievements: List[Dict]) -> Dict[str, Any]:
        """分析成就分布"""
        if not achievements:
            return {"total": 0, "distribution": {}, "description": "暂无成就"}
        
        # 按类型统计
        type_distribution = {}
        for achievement in achievements:
            achievement_type = achievement.get("achievementType", "unknown")
            type_distribution[achievement_type] = type_distribution.get(achievement_type, 0) + 1
        
        # 计算完成度
        total_achievements = len(achievements)
        max_possible = 20  # 假设每种类型最多5个成就，共4种类型
        completion_rate = (total_achievements / max_possible) * 100
        
        if completion_rate >= 80:
            level = "excellent"
            description = "成就卓越"
        elif completion_rate >= 60:
            level = "good"
            description = "成就良好"
        elif completion_rate >= 40:
            level = "fair"
            description = "成就一般"
        else:
            level = "beginner"
            description = "刚刚开始"
        
        return {
            "total": total_achievements,
            "distribution": type_distribution,
            "completion_rate": round(completion_rate, 1),
            "level": level,
            "description": description
        }
    
    def _identify_growth_patterns(self, growth_records: List[Dict]) -> Dict[str, Any]:
        """识别成长模式"""
        if not growth_records:
            return {"patterns": [], "description": "暂无成长模式数据"}
        
        # 分析各类型成长
        type_analysis = {}
        for record in growth_records:
            growth_type = record.get("growthType")
            if growth_type:
                if growth_type not in type_analysis:
                    type_analysis[growth_type] = {"count": 0, "scores": []}
                type_analysis[growth_type]["count"] += 1
                type_analysis[growth_type]["scores"].append(record.get("score", 0))
        
        # 识别强项和弱项
        strengths = []
        weaknesses = []
        for growth_type, data in type_analysis.items():
            avg_score = sum(data["scores"]) / len(data["scores"])
            if avg_score >= 80:
                strengths.append(self._get_growth_type_name(growth_type))
            elif avg_score <= 40:
                weaknesses.append(self._get_growth_type_name(growth_type))
        
        return {
            "type_analysis": type_analysis,
            "strengths": strengths,
            "weaknesses": weaknesses,
            "description": f"发现{len(strengths)}个强项，{len(weaknesses)}个需要提升的领域"
        }
    
    def _generate_improvement_suggestions(self, growth_records: List[Dict], achievements: List[Dict]) -> List[Dict]:
        """生成改进建议"""
        suggestions = []
        
        # 基于成长记录生成建议
        if growth_records:
            # 分析低分记录
            low_score_records = [r for r in growth_records if r.get("score", 0) < 60]
            for record in low_score_records[:3]:  # 最多3个建议
                growth_type = record.get("growthType")
                if growth_type:
                    area = self._get_growth_type_name(growth_type)
                    suggestion = random.choice(self.improvement_suggestions.get(
                        self._get_growth_category(growth_type), 
                        ["继续努力，保持积极心态"]
                    ))
                    suggestions.append({
                        "area": area,
                        "suggestion": suggestion,
                        "priority": "high" if record.get("score", 0) < 40 else "medium"
                    })
        
        return suggestions[:5]  # 最多5个建议
    
    def _generate_growth_summary(self, trend_analysis: Dict, achievement_analysis: Dict, pattern_analysis: Dict) -> str:
        """生成成长总结"""
        summary_parts = []
        
        # 趋势总结
        trend_desc = trend_analysis.get("description", "")
        if trend_desc == "快速提升":
            summary_parts.append("你的成长速度令人印象深刻")
        elif trend_desc == "稳步提升":
            summary_parts.append("你正在稳步前进，保持这种节奏")
        elif trend_desc == "保持稳定":
            summary_parts.append("你保持了稳定的成长状态")
        elif trend_desc in ["略有下降", "需要关注"]:
            summary_parts.append("当前遇到了一些挑战，需要调整策略")
        
        # 成就总结
        achievement_level = achievement_analysis.get("level", "")
        if achievement_level == "excellent":
            summary_parts.append("你的成就表现卓越，展现了强大的执行力")
        elif achievement_level == "good":
            summary_parts.append("你的成就表现良好，还有提升空间")
        elif achievement_level == "fair":
            summary_parts.append("你的成就表现一般，建议设定更明确的目标")
        else:
            summary_parts.append("你刚刚开始这段成长之旅，保持耐心和坚持")
        
        return "。".join(summary_parts) + "。"
    
    def _get_growth_type_name(self, growth_type: int) -> str:
        """获取成长类型名称"""
        type_mapping = {
            1: "沟通改善",
            2: "情绪管理", 
            3: "关系维护",
            4: "个人成长",
            5: "财务透明",
            6: "日程管理"
        }
        return type_mapping.get(growth_type, "未知类型")
    
    def _get_growth_category(self, growth_type: int) -> str:
        """获取成长分类"""
        if growth_type in [1, 6]:
            return "communication"
        elif growth_type == 2:
            return "emotion"
        elif growth_type == 3:
            return "relationship"
        else:
            return "personal"
    
    def get_health_check(self) -> Dict[str, Any]:
        """健康检查"""
        return {
            "status": "healthy",
            "service": "growth_archive",
            "timestamp": datetime.now().isoformat(),
            "version": "1.0.0"
        }
