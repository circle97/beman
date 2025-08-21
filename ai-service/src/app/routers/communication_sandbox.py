from fastapi import APIRouter, HTTPException, Query
from pydantic import BaseModel
from typing import Optional, List, Dict, Any
from ..services.communication_sandbox import CommunicationSandboxService

router = APIRouter(prefix="/api/sandbox", tags=["沟通沙盒"])
sandbox_service = CommunicationSandboxService()

# 请求模型
class DialogueRequest(BaseModel):
    scenario_id: str
    user_input: str

class SkillPracticeRequest(BaseModel):
    skill_type: str

class TemplateRequest(BaseModel):
    situation: str
    emotion: str

class ConflictGuideRequest(BaseModel):
    conflict_type: Optional[str] = None

# 响应模型
class ScenarioResponse(BaseModel):
    category: str
    description: str
    scenarios: List[Dict[str, Any]]

class DialogueSuggestionResponse(BaseModel):
    scenario: Dict[str, Any]
    user_input_analysis: Dict[str, Any]
    suggestions: Dict[str, Any]
    next_steps: List[str]

class SkillPracticeResponse(BaseModel):
    skill_type: str
    tips: List[str]
    practice_exercises: List[str]
    daily_goal: str
    progress_tracking: str

class ConflictGuideResponse(BaseModel):
    warning_signs: Optional[List[str]] = None
    immediate_actions: Optional[List[str]] = None
    prevention_tips: Optional[List[str]] = None
    effective_patterns: Optional[List[str]] = None
    step_by_step_process: Optional[List[str]] = None
    communication_tools: Optional[List[str]] = None
    general_tips: Optional[List[str]] = None

class DialogueTemplateResponse(BaseModel):
    situation: str
    emotion: str
    templates: Dict[str, str]
    usage_tips: List[str]

class HealthCheckResponse(BaseModel):
    service: str
    status: str
    timestamp: str
    components: Dict[str, int]

@router.get("/scenarios", response_model=ScenarioResponse)
async def get_scenarios(
    category: Optional[str] = Query(None, description="场景类别"),
    difficulty: Optional[str] = Query(None, description="难度级别: easy, medium, hard")
):
    """
    获取沟通场景
    
    - **category**: 场景类别 (relationship_conflict, emotional_support, communication_improvement)
    - **difficulty**: 难度级别 (easy, medium, hard)
    """
    try:
        result = sandbox_service.get_scenario(category, difficulty)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"获取场景失败: {str(e)}")

@router.post("/dialogue-suggestions", response_model=DialogueSuggestionResponse)
async def generate_dialogue_suggestions(request: DialogueRequest):
    """
    生成对话建议
    
    - **scenario_id**: 场景ID
    - **user_input**: 用户输入内容
    """
    try:
        result = sandbox_service.generate_dialogue_suggestions(
            request.scenario_id, 
            request.user_input
        )
        
        if "error" in result:
            raise HTTPException(status_code=400, detail=result["error"])
            
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"生成对话建议失败: {str(e)}")

@router.post("/practice-skill", response_model=SkillPracticeResponse)
async def practice_communication_skill(request: SkillPracticeRequest):
    """
    练习沟通技巧
    
    - **skill_type**: 技巧类型 (active_listening, nonviolent_communication, conflict_resolution, emotional_expression)
    """
    try:
        result = sandbox_service.practice_communication_skill(request.skill_type)
        
        if "error" in result:
            raise HTTPException(status_code=400, detail=result["error"])
            
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"获取练习内容失败: {str(e)}")

@router.post("/conflict-guide", response_model=ConflictGuideResponse)
async def get_conflict_resolution_guide(request: ConflictGuideRequest):
    """
    获取冲突解决指导
    
    - **conflict_type**: 冲突类型 (escalation, resolution)
    """
    try:
        result = sandbox_service.get_conflict_resolution_guide(request.conflict_type)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"获取冲突解决指导失败: {str(e)}")

@router.post("/dialogue-template", response_model=DialogueTemplateResponse)
async def generate_dialogue_template(request: TemplateRequest):
    """
    生成对话模板
    
    - **situation**: 情况描述
    - **emotion**: 情感状态
    """
    try:
        result = sandbox_service.generate_dialogue_template(
            request.situation, 
            request.emotion
        )
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"生成对话模板失败: {str(e)}")

@router.get("/skills", response_model=List[str])
async def get_available_skills():
    """
    获取可用的沟通技巧类型
    """
    try:
        skills = list(sandbox_service.communication_tips.keys())
        return skills
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"获取技能列表失败: {str(e)}")

@router.get("/categories", response_model=List[str])
async def get_scenario_categories():
    """
    获取场景类别列表
    """
    try:
        categories = list(sandbox_service.scenario_library.keys())
        return categories
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"获取类别列表失败: {str(e)}")

@router.get("/health", response_model=HealthCheckResponse)
async def health_check():
    """
    健康检查
    """
    try:
        result = sandbox_service.get_health_check()
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"健康检查失败: {str(e)}")

@router.get("/info")
async def get_service_info():
    """
    获取服务信息
    """
    return {
        "service": "CommunicationSandboxService",
        "version": "1.0.0",
        "description": "沟通沙盒服务 - 提供沟通场景模拟、对话建议生成、冲突解决指导等功能",
        "features": [
            "沟通场景模拟",
            "对话建议生成", 
            "冲突解决指导",
            "沟通技巧训练",
            "对话模板生成"
        ],
        "endpoints": [
            "GET /scenarios - 获取沟通场景",
            "POST /dialogue-suggestions - 生成对话建议",
            "POST /practice-skill - 练习沟通技巧",
            "POST /conflict-guide - 获取冲突解决指导",
            "POST /dialogue-template - 生成对话模板",
            "GET /skills - 获取可用技巧",
            "GET /categories - 获取场景类别",
            "GET /health - 健康检查",
            "GET /info - 服务信息"
        ]
    }
