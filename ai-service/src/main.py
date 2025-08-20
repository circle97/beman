"""
AI服务主入口
提供NLP情感分析、内容审核、智能对话、情感解码等功能
"""

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
import uvicorn
from loguru import logger

from app.routers import emotion_analysis, content_moderation, dialogue_system, emotion_decoder
from app.core.config import settings
from app.core.exceptions import AIException

# 创建FastAPI应用
app = FastAPI(
    title="Beman AI Service",
    description="情感关系管理平台的AI服务",
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc"
)

# 配置CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.ALLOWED_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 注册路由
app.include_router(emotion_analysis.router, prefix="/api/emotion", tags=["情感分析"])
app.include_router(content_moderation.router, prefix="/api/moderation", tags=["内容审核"])
app.include_router(dialogue_system.router, prefix="/api/dialogue", tags=["智能对话"])
app.include_router(emotion_decoder.router, prefix="/api/decoder", tags=["情感解码器"])

# 全局异常处理
@app.exception_handler(AIException)
async def ai_exception_handler(request, exc):
    logger.error(f"AI服务异常: {exc.message}")
    return JSONResponse(
        status_code=exc.status_code,
        content={"error": exc.message, "type": exc.error_type}
    )

@app.exception_handler(Exception)
async def general_exception_handler(request, exc):
    logger.error(f"未处理的异常: {str(exc)}")
    return JSONResponse(
        status_code=500,
        content={"error": "内部服务器错误", "type": "internal_error"}
    )

# 健康检查
@app.get("/health")
async def health_check():
    return {"status": "healthy", "service": "beman-ai"}

# 根路径
@app.get("/")
async def root():
    return {
        "message": "Beman AI Service",
        "version": "1.0.0",
        "services": ["情感分析", "内容审核", "智能对话", "情感解码器"]
    }

if __name__ == "__main__":
    logger.info("启动Beman AI服务...")
    uvicorn.run(
        "main:app",
        host=settings.HOST,
        port=settings.PORT,
        reload=settings.DEBUG,
        log_level="info"
    )
