#!/usr/bin/env python3
"""
沟通沙盒功能测试脚本
"""

import sys
import os

# 添加src目录到Python路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), 'src'))

from app.services.communication_sandbox import CommunicationSandboxService

def test_communication_sandbox():
    """测试沟通沙盒服务"""
    print("🧪 测试沟通沙盒服务...")
    
    try:
        # 创建服务实例
        service = CommunicationSandboxService()
        print("✅ 服务初始化成功")
        
        # 测试场景库
        print(f"📚 场景库数量: {len(service.scenario_library)}")
        for category, data in service.scenario_library.items():
            print(f"  - {data['name']}: {len(data['scenarios'])} 个场景")
        
        # 测试获取场景
        scenarios = service.get_scenario()
        print(f"🎯 总场景数: {len(scenarios['scenarios'])}")
        
        # 测试按类别获取场景
        conflict_scenarios = service.get_scenario("relationship_conflict")
        print(f"⚡ 关系冲突场景: {len(conflict_scenarios['scenarios'])} 个")
        
        # 测试按难度获取场景
        easy_scenarios = service.get_scenario(difficulty="easy")
        print(f"😊 简单场景: {len(easy_scenarios['scenarios'])} 个")
        
        # 测试对话建议生成
        if conflict_scenarios['scenarios']:
            first_scenario = conflict_scenarios['scenarios'][0]
            print(f"💬 测试场景: {first_scenario['title']}")
            
            suggestions = service.generate_dialogue_suggestions(
                first_scenario['id'], 
                "我觉得这个情况很困扰我"
            )
            
            if 'error' not in suggestions:
                print("✅ 对话建议生成成功")
                print(f"  - 情感分析: {suggestions['user_input_analysis']['dominant_emotion']}")
                print(f"  - 沟通风格: {suggestions['user_input_analysis']['dominant_style']}")
                print(f"  - 建议数量: {len(suggestions['suggestions']['immediate_response'])}")
            else:
                print(f"❌ 对话建议生成失败: {suggestions['error']}")
        
        # 测试沟通技巧练习
        skills = list(service.communication_tips.keys())
        print(f"🛠️ 可用技巧: {', '.join(skills)}")
        
        if skills:
            skill_practice = service.practice_communication_skill(skills[0])
            if 'error' not in skill_practice:
                print(f"✅ {skills[0]} 技巧练习获取成功")
                print(f"  - 技巧数量: {len(skill_practice['tips'])}")
                print(f"  - 练习数量: {len(skill_practice['practice_exercises'])}")
            else:
                print(f"❌ 技巧练习获取失败: {skill_practice['error']}")
        
        # 测试冲突解决指导
        conflict_guide = service.get_conflict_resolution_guide()
        print("🔧 冲突解决指导获取成功")
        print(f"  - 升级指导: {len(conflict_guide['escalation_guide']['warning_signs'])} 个警告信号")
        print(f"  - 解决指导: {len(conflict_guide['resolution_guide']['step_by_step_process'])} 个步骤")
        
        # 测试对话模板生成
        template = service.generate_dialogue_template("关系冲突", "生气")
        print("📝 对话模板生成成功")
        print(f"  - 开场白: {template['templates']['opening']}")
        print(f"  - 感受表达: {template['templates']['feeling_expression']}")
        
        # 测试健康检查
        health = service.get_health_check()
        print("💚 健康检查通过")
        print(f"  - 服务状态: {health['status']}")
        print(f"  - 组件数量: {health['components']}")
        
        print("\n🎉 所有测试通过！沟通沙盒服务运行正常")
        return True
        
    except Exception as e:
        print(f"❌ 测试失败: {str(e)}")
        import traceback
        traceback.print_exc()
        return False

def main():
    """主函数"""
    print("🚀 启动沟通沙盒功能测试...")
    print("=" * 50)
    
    success = test_communication_sandbox()
    
    print("=" * 50)
    if success:
        print("🎯 测试结果: 成功")
        print("💡 沟通沙盒功能已就绪，可以开始使用！")
    else:
        print("🎯 测试结果: 失败")
        print("⚠️ 请检查错误信息并修复问题")
    
    return 0 if success else 1

if __name__ == "__main__":
    exit(main())
