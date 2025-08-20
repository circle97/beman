"""
智能对话系统路由
"""

from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from typing import List, Optional, Dict
from loguru import logger

from app.core.config import settings

router = APIRouter()

class DialogueRequest(BaseModel):
    """对话请求模型"""
    message: str
    context: Optional[List[Dict[str, str]]] = []  # 对话历史
    user_id: Optional[str] = None
    dialogue_type: Optional[str] = "general"  # general, emotional_support, advice

class DialogueResponse(BaseModel):
    """对话响应模型"""
    message: str
    response: str
    response_type: str  # answer, question, suggestion, comfort
    confidence: float
    follow_up_questions: List[str]
    emotional_tone: str  # supportive, neutral, encouraging, empathetic

class DialogueContext(BaseModel):
    """对话上下文模型"""
    role: str  # user, assistant
    content: str
    timestamp: str

@router.post("/chat", response_model=DialogueResponse)
async def chat_with_ai(request: DialogueRequest):
    """
    与AI进行对话
    
    Args:
        request: 对话请求
        
    Returns:
        对话响应
    """
    try:
        logger.info(f"开始AI对话: {request.message[:50]}...")
        
        # 验证输入
        if not request.message or len(request.message.strip()) == 0:
            raise HTTPException(status_code=400, detail="消息内容不能为空")
        
        if len(request.message) > settings.MAX_DIALOGUE_LENGTH:
            raise HTTPException(
                status_code=400, 
                detail=f"消息长度超过限制({settings.MAX_DIALOGUE_LENGTH}字符)"
            )
        
        # 执行对话生成
        result = await _generate_dialogue_response(
            request.message, 
            request.context, 
            request.dialogue_type
        )
        
        logger.info(f"AI对话完成: {result['response_type']}")
        return result
        
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"AI对话失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"AI对话失败: {str(e)}")

async def _generate_dialogue_response(
    message: str, 
    context: List[Dict[str, str]], 
    dialogue_type: str
) -> DialogueResponse:
    """生成对话响应（模拟实现）"""
    from datetime import datetime
    
    # 根据对话类型和内容生成响应
    if dialogue_type == "emotional_support":
        response, response_type, emotional_tone = _generate_emotional_support(message)
    elif dialogue_type == "advice":
        response, response_type, emotional_tone = _generate_advice(message)
    else:
        response, response_type, emotional_tone = _generate_general_response(message)
    
    # 生成后续问题
    follow_up_questions = _generate_follow_up_questions(message, response_type)
    
    # 计算置信度（模拟）
    confidence = _calculate_confidence(message, context)
    
    return DialogueResponse(
        message=message,
        response=response,
        response_type=response_type,
        confidence=confidence,
        follow_up_questions=follow_up_questions,
        emotional_tone=emotional_tone
    )

def _generate_emotional_support(message: str) -> tuple:
    """生成情感支持响应"""
    # 检测情感关键词
    positive_words = ["开心", "快乐", "高兴", "满意", "幸福"]
    negative_words = ["难过", "伤心", "痛苦", "失望", "焦虑"]
    
    has_positive = any(word in message for word in positive_words)
    has_negative = any(word in message for word in negative_words)
    
    if has_positive:
        response = "很高兴听到你心情不错！继续保持这种积极的心态，生活会更美好。"
        response_type = "comfort"
        emotional_tone = "encouraging"
    elif has_negative:
        response = "我理解你现在的心情不太好。每个人都会遇到困难，这是正常的。记住，你并不孤单，我会一直在这里支持你。"
        response_type = "comfort"
        emotional_tone = "empathetic"
    else:
        response = "我在这里倾听你的心声。无论你想分享什么，我都会认真对待。"
        response_type = "answer"
        emotional_tone = "supportive"
    
    return response, response_type, emotional_tone

def _generate_advice(message: str) -> tuple:
    """生成建议响应"""
    # 根据消息内容生成建议
    if "关系" in message or "感情" in message:
        response = "在感情关系中，沟通和理解是关键。建议你：1. 保持开放的心态 2. 学会倾听对方 3. 表达自己的感受 4. 寻求共同点"
        response_type = "suggestion"
        emotional_tone = "supportive"
    elif "工作" in message or "学习" in message:
        response = "面对工作和学习的挑战，建议你：1. 制定明确的目标 2. 分解任务步骤 3. 保持专注和耐心 4. 适当休息调整"
        response_type = "suggestion"
        emotional_tone = "encouraging"
    else:
        response = "基于你的情况，我建议你：1. 先冷静分析问题 2. 列出可能的解决方案 3. 评估每个方案的利弊 4. 选择最适合的行动"
        response_type = "suggestion"
        emotional_tone = "neutral"
    
    return response, response_type, emotional_tone

def _generate_general_response(message: str) -> tuple:
    """生成通用响应"""
    # 简单的关键词匹配
    if "你好" in message or "hello" in message.lower():
        response = "你好！很高兴和你聊天。我是你的AI助手，有什么可以帮助你的吗？"
        response_type = "answer"
        emotional_tone = "friendly"
    elif "谢谢" in message or "感谢" in message:
        response = "不客气！能帮助到你是我的荣幸。如果还有其他问题，随时可以问我。"
        response_type = "answer"
        emotional_tone = "supportive"
    elif "再见" in message or "拜拜" in message:
        response = "再见！希望我们的对话对你有帮助。记得保持好心情，期待下次和你聊天！"
        response_type = "answer"
        emotional_tone = "warm"
    else:
        response = "我理解你的意思。这是一个很有趣的话题，能告诉我更多细节吗？"
        response_type = "question"
        emotional_tone = "neutral"
    
    return response, response_type, emotional_tone

def _generate_follow_up_questions(message: str, response_type: str) -> List[str]:
    """生成后续问题"""
    questions = []
    
    if response_type == "comfort":
        questions.extend([
            "你现在感觉怎么样？",
            "有什么具体的事情让你有这样的感受吗？",
            "你希望我如何帮助你？"
        ])
    elif response_type == "suggestion":
        questions.extend([
            "你觉得这些建议怎么样？",
            "有什么地方需要我进一步解释吗？",
            "你准备如何行动？"
        ])
    elif response_type == "question":
        questions.extend([
            "能举个例子吗？",
            "这对你来说意味着什么？",
            "你希望达到什么目标？"
        ])
    else:
        questions.extend([
            "还有什么想聊的吗？",
            "有其他问题需要帮助吗？"
        ])
    
    return questions[:3]  # 最多返回3个问题

def _calculate_confidence(message: str, context: List[Dict[str, str]]) -> float:
    """计算响应置信度（模拟）"""
    # 基于消息长度和上下文长度计算
    message_length = len(message)
    context_length = len(context)
    
    # 基础置信度
    base_confidence = 0.7
    
    # 根据消息长度调整
    if message_length > 50:
        base_confidence += 0.1
    elif message_length < 10:
        base_confidence -= 0.1
    
    # 根据上下文调整
    if context_length > 0:
        base_confidence += 0.1
    
    return min(1.0, max(0.0, base_confidence))

@router.get("/health")
async def health_check():
    """健康检查"""
    return {"status": "healthy", "service": "dialogue_system"}

@router.get("/info")
async def get_service_info():
    """获取服务信息"""
    return {
        "service_name": "智能对话系统",
        "version": "1.0.0",
        "max_dialogue_length": settings.MAX_DIALOGUE_LENGTH,
        "features": [
            "智能对话生成",
            "情感支持对话",
            "个性化建议",
            "上下文理解",
            "多轮对话支持"
        ]
    }
