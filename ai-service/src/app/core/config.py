"""
AI服务配置管理
"""

from pydantic_settings import BaseSettings
from typing import List
import os

class Settings(BaseSettings):
    """应用配置类"""
    
    # 基础配置
    APP_NAME: str = "Beman AI Service"
    VERSION: str = "1.0.0"
    DEBUG: bool = False
    
    # 服务器配置
    HOST: str = "0.0.0.0"
    PORT: int = 8001
    
    # CORS配置
    ALLOWED_ORIGINS: List[str] = [
        "http://localhost:3000",
        "http://localhost:8080",
        "http://127.0.0.1:3000",
        "http://127.0.0.1:8080"
    ]
    
    # 数据库配置
    DATABASE_URL: str = "sqlite:///./ai_service.db"
    
    # AI模型配置
    MODEL_CACHE_DIR: str = "./models"
    MAX_TEXT_LENGTH: int = 1000
    BATCH_SIZE: int = 32
    
    # 情感分析配置
    EMOTION_MODEL_NAME: str = "bert-base-chinese"
    EMOTION_THRESHOLD: float = 0.6
    
    # 内容审核配置
    MODERATION_MODEL_NAME: str = "bert-base-chinese"
    TOXICITY_THRESHOLD: float = 0.7
    EXTREME_THRESHOLD: float = 0.8
    
    # 对话系统配置
    DIALOGUE_MODEL_NAME: str = "gpt2-chinese-cluecorpussmall"
    MAX_DIALOGUE_LENGTH: int = 200
    
    # 日志配置
    LOG_LEVEL: str = "INFO"
    LOG_FILE: str = "./logs/ai_service.log"
    
    # 缓存配置
    REDIS_URL: str = "redis://localhost:6379"
    CACHE_TTL: int = 3600  # 1小时
    
    # 安全配置
    API_KEY_HEADER: str = "X-API-Key"
    API_KEYS: List[str] = ["beman-ai-key-2024"]
    
    class Config:
        env_file = ".env"
        case_sensitive = True

# 创建全局配置实例
settings = Settings()

# 确保必要的目录存在
os.makedirs(settings.MODEL_CACHE_DIR, exist_ok=True)
os.makedirs(os.path.dirname(settings.LOG_FILE), exist_ok=True)
