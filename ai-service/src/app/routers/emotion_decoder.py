"""
情感解码器路由
Week 5核心功能的路由接口
"""

from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from typing import List, Optional, Dict
from loguru import logger

from app.services.emotion_decoder import EmotionDecoderService
from app.core.config import settings

router = APIRouter()

# 创建情感解码器服务实例
emotion_decoder_service = EmotionDecoderService()

class EmotionDecodeRequest(BaseModel):
    """情感解码请求模型"""
    text: str
    context: Optional[Dict] = None
    analysis_type: Optional[str] = "comprehensive"  # comprehensive, emotion_only, relationship_only

class EmotionDecodeResponse(BaseModel):
    """情感解码响应模型"""
    text: str
    emotion_state: Dict
    relationship_health: Dict
    suggestions: List[str]
    analysis_time: str
    confidence: float
    success: bool = True
    message: Optional[str] = None

class BatchDecodeRequest(BaseModel):
    """批量情感解码请求模型"""
    texts: List[str]
    context: Optional[Dict] = None

class BatchDecodeResponse(BaseModel):
    """批量情感解码响应模型"""
    results: List[EmotionDecodeResponse]
    total_count: int
    success_count: int
    failed_count: int

@router.post("/decode", response_model=EmotionDecodeResponse)
async def decode_emotion(request: EmotionDecodeRequest):
    """
    解码情感状态和关系健康度
    
    Args:
        request: 情感解码请求
        
    Returns:
        情感解码结果
    """
    try:
        logger.info(f"开始情感解码: {request.text[:50]}...")
        
        # 验证输入
        if not request.text or len(request.text.strip()) == 0:
            raise HTTPException(status_code=400, detail="文本内容不能为空")
        
        if len(request.text) > settings.MAX_TEXT_LENGTH:
            raise HTTPException(
                status_code=400, 
                detail=f"文本长度超过限制({settings.MAX_TEXT_LENGTH}字符)"
            )
        
        # 执行情感解码
        result = emotion_decoder_service.decode_emotion_state(request.text, request.context)
        
        # 检查是否有错误
        if "error" in result:
            raise HTTPException(status_code=500, detail=result["error"])
        
        # 构建响应
        response = EmotionDecodeResponse(
            text=result["text"],
            emotion_state=result["emotion_state"],
            relationship_health=result["relationship_health"],
            suggestions=result["suggestions"],
            analysis_time=result["analysis_time"],
            confidence=result["confidence"]
        )
        
        logger.info(f"情感解码完成: 关系健康度 {result['relationship_health']['health_level']}")
        return response
        
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"情感解码失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"情感解码失败: {str(e)}")

@router.post("/batch-decode", response_model=BatchDecodeResponse)
async def batch_decode_emotion(request: BatchDecodeRequest):
    """
    批量解码多条文本的情感状态
    
    Args:
        request: 批量情感解码请求
        
    Returns:
        批量情感解码结果
    """
    try:
        logger.info(f"开始批量情感解码: {len(request.texts)}条文本")
        
        # 验证输入
        if not request.texts:
            raise HTTPException(status_code=400, detail="文本列表不能为空")
        
        if len(request.texts) > 50:  # 限制批量处理数量
            raise HTTPException(status_code=400, detail="批量处理数量不能超过50条")
        
        # 执行批量解码
        results = []
        success_count = 0
        failed_count = 0
        
        for text in request.texts:
            try:
                result = emotion_decoder_service.decode_emotion_state(text, request.context)
                
                if "error" in result:
                    failed_count += 1
                    results.append(EmotionDecodeResponse(
                        text=text,
                        emotion_state={},
                        relationship_health={},
                        suggestions=[],
                        analysis_time="",
                        confidence=0.0,
                        success=False,
                        message=result["error"]
                    ))
                else:
                    success_count += 1
                    results.append(EmotionDecodeResponse(
                        text=result["text"],
                        emotion_state=result["emotion_state"],
                        relationship_health=result["relationship_health"],
                        suggestions=result["suggestions"],
                        analysis_time=result["analysis_time"],
                        confidence=result["confidence"]
                    ))
                    
            except Exception as e:
                failed_count += 1
                results.append(EmotionDecodeResponse(
                    text=text,
                    emotion_state={},
                    relationship_health={},
                    suggestions=[],
                    analysis_time="",
                    confidence=0.0,
                    success=False,
                    message=str(e)
                ))
        
        response = BatchDecodeResponse(
            results=results,
            total_count=len(request.texts),
            success_count=success_count,
            failed_count=failed_count
        )
        
        logger.info(f"批量情感解码完成: 成功{success_count}条, 失败{failed_count}条")
        return response
        
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"批量情感解码失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"批量情感解码失败: {str(e)}")

@router.get("/health")
async def health_check():
    """健康检查"""
    return {"status": "healthy", "service": "emotion_decoder"}

@router.get("/info")
async def get_service_info():
    """获取服务信息"""
    return {
        "service_name": "情感解码器服务",
        "version": "1.0.0",
        "max_text_length": settings.MAX_TEXT_LENGTH,
        "features": [
            "情感状态分析",
            "关系健康度评估",
            "个性化建议生成",
            "多维度关系指标",
            "智能情感解码"
        ],
        "analysis_dimensions": [
            "沟通质量",
            "信任程度",
            "相互支持",
            "情感状态",
            "关系稳定性"
        ]
    }
