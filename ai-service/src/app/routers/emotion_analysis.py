"""
情感分析路由
"""

from fastapi import APIRouter, HTTPException, Depends
from pydantic import BaseModel
from typing import List, Optional
from loguru import logger

from app.services.emotion_analysis import EmotionAnalysisService
from app.core.config import settings

router = APIRouter()

# 创建情感分析服务实例
emotion_service = EmotionAnalysisService()

class EmotionAnalysisRequest(BaseModel):
    """情感分析请求模型"""
    text: str
    language: Optional[str] = "zh"
    detailed: Optional[bool] = False

class EmotionAnalysisResponse(BaseModel):
    """情感分析响应模型"""
    text: str
    emotion_category: str
    intensity: float
    keywords: List[str]
    suggestions: List[str]
    success: bool = True
    message: Optional[str] = None

class BatchEmotionRequest(BaseModel):
    """批量情感分析请求模型"""
    texts: List[str]
    language: Optional[str] = "zh"

class BatchEmotionResponse(BaseModel):
    """批量情感分析响应模型"""
    results: List[EmotionAnalysisResponse]
    total_count: int
    success_count: int
    failed_count: int

@router.post("/analyze", response_model=EmotionAnalysisResponse)
async def analyze_emotion(request: EmotionAnalysisRequest):
    """
    分析单条文本的情感
    
    Args:
        request: 情感分析请求
        
    Returns:
        情感分析结果
    """
    try:
        logger.info(f"开始情感分析: {request.text[:50]}...")
        
        # 验证输入
        if not request.text or len(request.text.strip()) == 0:
            raise HTTPException(status_code=400, detail="文本内容不能为空")
        
        if len(request.text) > settings.MAX_TEXT_LENGTH:
            raise HTTPException(
                status_code=400, 
                detail=f"文本长度超过限制({settings.MAX_TEXT_LENGTH}字符)"
            )
        
        # 执行情感分析
        result = emotion_service.analyze_emotion(request.text)
        
        # 检查是否有错误
        if "error" in result:
            raise HTTPException(status_code=500, detail=result["error"])
        
        # 构建响应
        response = EmotionAnalysisResponse(
            text=result["text"],
            emotion_category=result["emotion_category"],
            intensity=result["intensity"],
            keywords=result["keywords"],
            suggestions=result["suggestions"]
        )
        
        logger.info(f"情感分析完成: {result['emotion_category']}")
        return response
        
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"情感分析失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"情感分析失败: {str(e)}")

@router.post("/batch-analyze", response_model=BatchEmotionResponse)
async def batch_analyze_emotion(request: BatchEmotionRequest):
    """
    批量分析多条文本的情感
    
    Args:
        request: 批量情感分析请求
        
    Returns:
        批量情感分析结果
    """
    try:
        logger.info(f"开始批量情感分析: {len(request.texts)}条文本")
        
        # 验证输入
        if not request.texts:
            raise HTTPException(status_code=400, detail="文本列表不能为空")
        
        if len(request.texts) > 100:  # 限制批量处理数量
            raise HTTPException(status_code=400, detail="批量处理数量不能超过100条")
        
        # 执行批量分析
        results = []
        success_count = 0
        failed_count = 0
        
        for text in request.texts:
            try:
                result = emotion_service.analyze_emotion(text)
                
                if "error" in result:
                    failed_count += 1
                    results.append(EmotionAnalysisResponse(
                        text=text,
                        emotion_category="unknown",
                        intensity=0.0,
                        keywords=[],
                        suggestions=[],
                        success=False,
                        message=result["error"]
                    ))
                else:
                    success_count += 1
                    results.append(EmotionAnalysisResponse(
                        text=result["text"],
                        emotion_category=result["emotion_category"],
                        intensity=result["intensity"],
                        keywords=result["keywords"],
                        suggestions=result["suggestions"]
                    ))
                    
            except Exception as e:
                failed_count += 1
                results.append(EmotionAnalysisResponse(
                    text=text,
                    emotion_category="unknown",
                    intensity=0.0,
                    keywords=[],
                    suggestions=[],
                    success=False,
                    message=str(e)
                ))
        
        response = BatchEmotionResponse(
            results=results,
            total_count=len(request.texts),
            success_count=success_count,
            failed_count=failed_count
        )
        
        logger.info(f"批量情感分析完成: 成功{success_count}条, 失败{failed_count}条")
        return response
        
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"批量情感分析失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"批量情感分析失败: {str(e)}")

@router.get("/health")
async def health_check():
    """健康检查"""
    return {"status": "healthy", "service": "emotion_analysis"}

@router.get("/info")
async def get_service_info():
    """获取服务信息"""
    return {
        "service_name": "情感分析服务",
        "version": "1.0.0",
        "supported_languages": ["zh", "en"],
        "max_text_length": settings.MAX_TEXT_LENGTH,
        "features": [
            "情感分类",
            "情感强度评估", 
            "情感关键词提取",
            "个性化建议生成"
        ]
    }
