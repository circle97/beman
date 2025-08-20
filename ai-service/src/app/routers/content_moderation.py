"""
内容审核路由
"""

from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from typing import List, Optional
from loguru import logger

from app.core.config import settings

router = APIRouter()

class ContentModerationRequest(BaseModel):
    """内容审核请求模型"""
    text: str
    content_type: Optional[str] = "general"  # general, comment, post, message
    user_id: Optional[str] = None

class ContentModerationResponse(BaseModel):
    """内容审核响应模型"""
    text: str
    is_appropriate: bool
    risk_level: str  # low, medium, high, extreme
    risk_score: float
    flagged_keywords: List[str]
    suggestions: List[str]
    moderation_time: str

@router.post("/moderate", response_model=ContentModerationResponse)
async def moderate_content(request: ContentModerationRequest):
    """
    审核内容是否合适
    
    Args:
        request: 内容审核请求
        
    Returns:
        内容审核结果
    """
    try:
        logger.info(f"开始内容审核: {request.text[:50]}...")
        
        # 验证输入
        if not request.text or len(request.text.strip()) == 0:
            raise HTTPException(status_code=400, detail="文本内容不能为空")
        
        if len(request.text) > settings.MAX_TEXT_LENGTH:
            raise HTTPException(
                status_code=400, 
                detail=f"文本长度超过限制({settings.MAX_TEXT_LENGTH}字符)"
            )
        
        # 模拟内容审核逻辑（实际应该调用AI模型）
        result = await _perform_content_moderation(request.text)
        
        logger.info(f"内容审核完成: {result['risk_level']}")
        return result
        
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"内容审核失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"内容审核失败: {str(e)}")

async def _perform_content_moderation(text: str) -> ContentModerationResponse:
    """执行内容审核（模拟实现）"""
    from datetime import datetime
    
    # 定义敏感词汇库
    sensitive_keywords = [
        "暴力", "仇恨", "歧视", "极端", "自杀", "自残", "毒品", "赌博",
        "色情", "诈骗", "违法", "犯罪", "恐怖", "威胁", "骚扰"
    ]
    
    # 检查敏感词汇
    flagged_keywords = []
    risk_score = 0.0
    
    for keyword in sensitive_keywords:
        if keyword in text:
            flagged_keywords.append(keyword)
            risk_score += 0.2
    
    # 根据风险分数确定风险等级
    if risk_score >= 0.8:
        risk_level = "extreme"
        is_appropriate = False
    elif risk_score >= 0.6:
        risk_level = "high"
        is_appropriate = False
    elif risk_score >= 0.4:
        risk_level = "medium"
        is_appropriate = True
    else:
        risk_level = "low"
        is_appropriate = True
    
    # 生成建议
    suggestions = _generate_moderation_suggestions(risk_level, flagged_keywords)
    
    return ContentModerationResponse(
        text=text,
        is_appropriate=is_appropriate,
        risk_level=risk_level,
        risk_score=min(1.0, risk_score),
        flagged_keywords=flagged_keywords,
        suggestions=suggestions,
        moderation_time=datetime.now().isoformat()
    )

def _generate_moderation_suggestions(risk_level: str, flagged_keywords: List[str]) -> List[str]:
    """生成审核建议"""
    suggestions = []
    
    if risk_level == "extreme":
        suggestions.extend([
            "内容包含极端敏感信息，建议重新编辑",
            "请避免使用暴力或仇恨性语言",
            "考虑内容的积极影响"
        ])
    elif risk_level == "high":
        suggestions.extend([
            "内容风险较高，建议修改",
            "注意用词，保持友善态度",
            "避免可能引起争议的表达"
        ])
    elif risk_level == "medium":
        suggestions.extend([
            "内容基本合适，可以适当优化",
            "注意表达方式，保持礼貌"
        ])
    else:
        suggestions.append("内容合适，继续保持")
    
    return suggestions

@router.get("/health")
async def health_check():
    """健康检查"""
    return {"status": "healthy", "service": "content_moderation"}

@router.get("/info")
async def get_service_info():
    """获取服务信息"""
    return {
        "service_name": "内容审核服务",
        "version": "1.0.0",
        "max_text_length": settings.MAX_TEXT_LENGTH,
        "features": [
            "内容风险评估",
            "敏感词汇检测",
            "智能建议生成",
            "多类型内容支持"
        ]
    }
