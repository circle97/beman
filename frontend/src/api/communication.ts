import axios from 'axios'

const AI_SERVICE_BASE_URL = 'http://localhost:8000/api/sandbox'

/**
 * 沟通沙盒相关API
 */
export const communicationAPI = {
  /**
   * 获取沟通场景
   * @param category 场景类别
   * @param difficulty 难度级别
   * @returns Promise<ScenarioResponse>
   */
  getScenarios: async (category?: string, difficulty?: string) => {
    try {
      const params = new URLSearchParams()
      if (category) params.append('category', category)
      if (difficulty) params.append('difficulty', difficulty)
      
      const response = await axios.get(`${AI_SERVICE_BASE_URL}/scenarios?${params}`)
      return response.data
    } catch (error) {
      console.error('获取场景失败:', error)
      throw error
    }
  },

  /**
   * 生成对话建议
   * @param scenarioId 场景ID
   * @param userInput 用户输入
   * @returns Promise<DialogueSuggestionResponse>
   */
  generateDialogueSuggestions: async (scenarioId: string, userInput: string) => {
    try {
      const response = await axios.post(`${AI_SERVICE_BASE_URL}/dialogue-suggestions`, {
        scenario_id: scenarioId,
        user_input: userInput
      })
      return response.data
    } catch (error) {
      console.error('生成对话建议失败:', error)
      throw error
    }
  },

  /**
   * 练习沟通技巧
   * @param skillType 技巧类型
   * @returns Promise<SkillPracticeResponse>
   */
  practiceSkill: async (skillType: string) => {
    try {
      const response = await axios.post(`${AI_SERVICE_BASE_URL}/practice-skill`, {
        skill_type: skillType
      })
      return response.data
    } catch (error) {
      console.error('获取练习内容失败:', error)
      throw error
    }
  },

  /**
   * 获取冲突解决指导
   * @param conflictType 冲突类型
   * @returns Promise<ConflictGuideResponse>
   */
  getConflictGuide: async (conflictType?: string) => {
    try {
      const response = await axios.post(`${AI_SERVICE_BASE_URL}/conflict-guide`, {
        conflict_type: conflictType
      })
      return response.data
    } catch (error) {
      console.error('获取冲突解决指导失败:', error)
      throw error
    }
  },

  /**
   * 生成对话模板
   * @param situation 情况描述
   * @param emotion 情感状态
   * @returns Promise<DialogueTemplateResponse>
   */
  generateDialogueTemplate: async (situation: string, emotion: string) => {
    try {
      const response = await axios.post(`${AI_SERVICE_BASE_URL}/dialogue-template`, {
        situation,
        emotion
      })
      return response.data
    } catch (error) {
      console.error('生成对话模板失败:', error)
      throw error
    }
  },

  /**
   * 获取可用技巧列表
   * @returns Promise<string[]>
   */
  getAvailableSkills: async () => {
    try {
      const response = await axios.get(`${AI_SERVICE_BASE_URL}/skills`)
      return response.data
    } catch (error) {
      console.error('获取技巧列表失败:', error)
      throw error
    }
  },

  /**
   * 获取场景类别列表
   * @returns Promise<string[]>
   */
  getScenarioCategories: async () => {
    try {
      const response = await axios.get(`${AI_SERVICE_BASE_URL}/categories`)
      return response.data
    } catch (error) {
      console.error('获取类别列表失败:', error)
      throw error
    }
  },

  /**
   * 健康检查
   * @returns Promise<HealthCheckResponse>
   */
  healthCheck: async () => {
    try {
      const response = await axios.get(`${AI_SERVICE_BASE_URL}/health`)
      return response.data
    } catch (error) {
      console.error('健康检查失败:', error)
      throw error
    }
  }
}

// 类型定义
export interface Scenario {
  id: string
  title: string
  description: string
  context: string
  difficulty: string
  tags: string[]
}

export interface ScenarioResponse {
  category: string
  description: string
  scenarios: Scenario[]
}

export interface DialogueSuggestionResponse {
  scenario: any
  user_input_analysis: any
  suggestions: any
  next_steps: string[]
}

export interface SkillPracticeResponse {
  skill_type: string
  tips: string[]
  practice_exercises: string[]
  daily_goal: string
  progress_tracking: string
}

export interface ConflictGuideResponse {
  warning_signs?: string[]
  immediate_actions?: string[]
  prevention_tips?: string[]
  effective_patterns?: string[]
  step_by_step_process?: string[]
  communication_tools?: string[]
  general_tips?: string[]
}

export interface DialogueTemplateResponse {
  situation: string
  emotion: string
  templates: Record<string, string>
  usage_tips: string[]
}

export interface HealthCheckResponse {
  service: string
  status: string
  timestamp: string
  components: Record<string, number>
}
