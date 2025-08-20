"""
AI服务异常处理
"""

from fastapi import HTTPException
from typing import Optional

class AIException(Exception):
    """AI服务基础异常类"""
    
    def __init__(
        self, 
        message: str, 
        error_type: str = "ai_error",
        status_code: int = 500
    ):
        self.message = message
        self.error_type = error_type
        self.status_code = status_code
        super().__init__(self.message)

class ModelLoadException(AIException):
    """模型加载异常"""
    
    def __init__(self, model_name: str, message: str = None):
        self.model_name = model_name
        msg = message or f"模型 {model_name} 加载失败"
        super().__init__(msg, "model_load_error", 500)

class TextProcessingException(AIException):
    """文本处理异常"""
    
    def __init__(self, message: str = "文本处理失败"):
        super().__init__(message, "text_processing_error", 400)

class EmotionAnalysisException(AIException):
    """情感分析异常"""
    
    def __init__(self, message: str = "情感分析失败"):
        super().__init__(message, "emotion_analysis_error", 500)

class ContentModerationException(AIException):
    """内容审核异常"""
    
    def __init__(self, message: str = "内容审核失败"):
        super().__init__(message, "content_moderation_error", 500)

class DialogueException(AIException):
    """对话系统异常"""
    
    def __init__(self, message: str = "对话生成失败"):
        super().__init__(message, "dialogue_error", 500)

class ValidationException(AIException):
    """数据验证异常"""
    
    def __init__(self, message: str = "数据验证失败"):
        super().__init__(message, "validation_error", 400)

class RateLimitException(AIException):
    """速率限制异常"""
    
    def __init__(self, message: str = "请求过于频繁"):
        super().__init__(message, "rate_limit_error", 429)

class AuthenticationException(AIException):
    """认证异常"""
    
    def __init__(self, message: str = "认证失败"):
        super().__init__(message, "authentication_error", 401)

class ResourceNotFoundException(AIException):
    """资源未找到异常"""
    
    def __init__(self, resource: str, message: str = None):
        msg = message or f"资源 {resource} 未找到"
        super().__init__(msg, "resource_not_found", 404)
