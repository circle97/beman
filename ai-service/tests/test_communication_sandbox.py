import pytest
from app.services.communication_sandbox import CommunicationSandboxService

class TestCommunicationSandboxService:
    """沟通沙盒服务测试类"""
    
    @pytest.fixture
    def service(self):
        """创建服务实例"""
        return CommunicationSandboxService()
    
    def test_service_initialization(self, service):
        """测试服务初始化"""
        assert service is not None
        assert hasattr(service, 'scenario_library')
        assert hasattr(service, 'conflict_patterns')
        assert hasattr(service, 'communication_tips')
        assert hasattr(service, 'dialogue_templates')
    
    def test_scenario_library_structure(self, service):
        """测试场景库结构"""
        library = service.scenario_library
        
        # 检查主要类别
        assert "relationship_conflict" in library
        assert "emotional_support" in library
        assert "communication_improvement" in library
        
        # 检查场景结构
        for category in library.values():
            assert "name" in category
            assert "description" in category
            assert "scenarios" in category
            assert isinstance(category["scenarios"], list)
            
            for scenario in category["scenarios"]:
                assert "id" in scenario
                assert "title" in scenario
                assert "description" in scenario
                assert "context" in scenario
                assert "difficulty" in scenario
                assert "tags" in scenario
    
    def test_get_scenarios_all(self, service):
        """测试获取所有场景"""
        result = service.get_scenario()
        
        assert "category" in result
        assert "description" in result
        assert "scenarios" in result
        assert result["category"] == "所有场景"
        assert len(result["scenarios"]) > 0
    
    def test_get_scenarios_by_category(self, service):
        """测试按类别获取场景"""
        result = service.get_scenario("relationship_conflict")
        
        assert result["category"] == "关系冲突场景"
        assert "关系冲突场景" in result["description"]
        assert len(result["scenarios"]) > 0
        
        # 检查场景内容
        for scenario in result["scenarios"]:
            assert scenario["id"].startswith("rc_")
    
    def test_get_scenarios_by_difficulty(self, service):
        """测试按难度获取场景"""
        result = service.get_scenario(difficulty="easy")
        
        assert "scenarios" in result
        for scenario in result["scenarios"]:
            assert scenario["difficulty"] == "easy"
    
    def test_get_scenarios_by_category_and_difficulty(self, service):
        """测试按类别和难度获取场景"""
        result = service.get_scenario("emotional_support", "easy")
        
        assert result["category"] == "情感支持场景"
        for scenario in result["scenarios"]:
            assert scenario["difficulty"] == "easy"
    
    def test_generate_dialogue_suggestions(self, service):
        """测试生成对话建议"""
        result = service.generate_dialogue_suggestions("rc_001", "我觉得家务分配不公平")
        
        assert "scenario" in result
        assert "user_input_analysis" in result
        assert "suggestions" in result
        assert "next_steps" in result
        
        # 检查场景信息
        assert result["scenario"]["id"] == "rc_001"
        assert "家务" in result["scenario"]["tags"]
        
        # 检查分析结果
        analysis = result["user_input_analysis"]
        assert "words" in analysis
        assert "emotion_analysis" in analysis
        assert "style_analysis" in analysis
        assert "dominant_emotion" in analysis
        assert "dominant_style" in analysis
        
        # 检查建议结构
        suggestions = result["suggestions"]
        assert "immediate_response" in suggestions
        assert "communication_style" in suggestions
        assert "conflict_resolution" in suggestions
        assert "long_term_strategies" in suggestions
    
    def test_generate_dialogue_suggestions_invalid_scenario(self, service):
        """测试无效场景ID"""
        result = service.generate_dialogue_suggestions("invalid_id", "测试输入")
        
        assert "error" in result
        assert result["error"] == "场景不存在"
    
    def test_practice_communication_skill(self, service):
        """测试沟通技巧练习"""
        result = service.practice_communication_skill("active_listening")
        
        assert "skill_type" in result
        assert "tips" in result
        assert "practice_exercises" in result
        assert "daily_goal" in result
        assert "progress_tracking" in result
        
        assert result["skill_type"] == "active_listening"
        assert len(result["tips"]) > 0
        assert len(result["practice_exercises"]) > 0
    
    def test_practice_communication_skill_invalid_type(self, service):
        """测试无效技巧类型"""
        result = service.practice_communication_skill("invalid_skill")
        
        assert "error" in result
        assert result["error"] == "技巧类型不存在"
    
    def test_get_conflict_resolution_guide(self, service):
        """测试获取冲突解决指导"""
        result = service.get_conflict_resolution_guide()
        
        assert "escalation_guide" in result
        assert "resolution_guide" in result
        assert "general_tips" in result
        
        # 检查升级指导
        escalation = result["escalation_guide"]
        assert "warning_signs" in escalation
        assert "immediate_actions" in escalation
        assert "prevention_tips" in escalation
        
        # 检查解决指导
        resolution = result["resolution_guide"]
        assert "effective_patterns" in resolution
        assert "step_by_step_process" in resolution
        assert "communication_tools" in resolution
    
    def test_get_conflict_resolution_guide_escalation(self, service):
        """测试获取冲突升级指导"""
        result = service.get_conflict_resolution_guide("escalation")
        
        assert "warning_signs" in result
        assert "immediate_actions" in result
        assert "prevention_tips" in result
        assert "escalation_guide" not in result  # 不应该包含嵌套结构
    
    def test_get_conflict_resolution_guide_resolution(self, service):
        """测试获取冲突解决指导"""
        result = service.get_conflict_resolution_guide("resolution")
        
        assert "effective_patterns" in result
        assert "step_by_step_process" in result
        assert "communication_tools" in result
        assert "resolution_guide" not in result  # 不应该包含嵌套结构
    
    def test_generate_dialogue_template(self, service):
        """测试生成对话模板"""
        result = service.generate_dialogue_template("关系冲突", "生气")
        
        assert "situation" in result
        assert "emotion" in result
        assert "templates" in result
        assert "usage_tips" in result
        
        assert result["situation"] == "关系冲突"
        assert result["emotion"] == "生气"
        
        templates = result["templates"]
        assert "opening" in templates
        assert "feeling_expression" in templates
        assert "understanding" in templates
        assert "resolution" in templates
        
        assert len(result["usage_tips"]) > 0
    
    def test_health_check(self, service):
        """测试健康检查"""
        result = service.get_health_check()
        
        assert "service" in result
        assert "status" in result
        assert "timestamp" in result
        assert "components" in result
        
        assert result["service"] == "CommunicationSandboxService"
        assert result["status"] == "healthy"
        
        components = result["components"]
        assert "scenario_library" in components
        assert "conflict_patterns" in components
        assert "communication_tips" in components
        assert "dialogue_templates" in components
        
        # 检查组件数量
        assert components["scenario_library"] > 0
        assert components["conflict_patterns"] > 0
        assert components["communication_tips"] > 0
        assert components["dialogue_templates"] > 0
    
    def test_analyze_user_input(self, service):
        """测试用户输入分析"""
        # 测试正面情感
        result = service._analyze_user_input("我觉得这个建议很好，我很满意")
        
        assert "words" in result
        assert "emotion_analysis" in result
        assert "style_analysis" in result
        assert "dominant_emotion" in result
        assert "dominant_style" in result
        
        # 检查情感分析
        emotion_analysis = result["emotion_analysis"]
        assert "positive" in emotion_analysis
        assert "negative" in emotion_analysis
        assert "neutral" in emotion_analysis
        
        # 检查风格分析
        style_analysis = result["style_analysis"]
        assert "assertive" in style_analysis
        assert "passive" in style_analysis
        assert "aggressive" in style_analysis
    
    def test_generate_suggestions(self, service):
        """测试建议生成"""
        scenario = {
            "id": "test_001",
            "title": "测试场景",
            "tags": ["家务", "分工"]
        }
        
        analysis = {
            "dominant_emotion": "negative",
            "dominant_style": "aggressive"
        }
        
        result = service._generate_suggestions(scenario, analysis)
        
        assert "immediate_response" in result
        assert "communication_style" in result
        assert "conflict_resolution" in result
        assert "long_term_strategies" in result
        
        # 检查根据情感生成的建议
        assert len(result["immediate_response"]) > 0
        
        # 检查根据风格生成的建议
        assert len(result["communication_style"]) > 0
        
        # 检查根据场景标签生成的建议
        assert len(result["conflict_resolution"]) > 0
        
        # 检查长期策略
        assert len(result["long_term_strategies"]) > 0
    
    def test_suggest_next_steps(self, service):
        """测试下一步建议"""
        # 测试负面情感
        analysis = {"dominant_emotion": "negative", "dominant_style": "neutral"}
        result = service._suggest_next_steps(analysis)
        
        assert isinstance(result, list)
        assert len(result) > 0
        assert "先处理情绪" in result[0]
        
        # 测试攻击性风格
        analysis = {"dominant_emotion": "neutral", "dominant_style": "aggressive"}
        result = service._suggest_next_steps(analysis)
        
        assert isinstance(result, list)
        assert len(result) > 0
        assert "练习使用更温和的表达方式" in result[1]  # 第二个建议
    
    def test_select_template(self, service):
        """测试模板选择"""
        # 测试冲突场景
        result = service._select_template("opening_statements", "关系冲突")
        assert result is not None
        assert len(result) > 0
        
        # 测试支持场景
        result = service._select_template("feeling_expressions", "情感支持")
        assert result is not None
        assert len(result) > 0
        
        # 测试不存在的模板类型
        result = service._select_template("nonexistent", "测试")
        assert result == "请根据具体情况表达"

if __name__ == "__main__":
    pytest.main([__file__, "-v"])
