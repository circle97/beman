import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

export interface PracticePlan {
  id?: number
  userId: number
  title: string
  description: string
  type: number // 1-沟通改善，2-情绪管理，3-关系维护，4-个人成长
  status: number // 0-未开始，1-进行中，2-已完成，3-已放弃
  priority: number // 1-低，2-中，3-高
  startTime?: string
  expectedEndTime?: string
  actualEndTime?: string
  createTime?: string
  updateTime?: string
  remark?: string
  isPublic: number // 0-私密，1-公开
  tags?: string
  progress: number // 0-100
}

export interface PlanStatistics {
  totalPlans: number
  completedPlans: number
  inProgressPlans: number
  abandonedPlans: number
  completionRate: number
}

/**
 * 实践计划相关API
 */
export const practicePlanAPI = {
  /**
   * 创建实践计划
   */
  createPlan: async (plan: PracticePlan) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/practice-plan/create`, plan)
      return response.data
    } catch (error) {
      console.error('创建实践计划失败:', error)
      throw error
    }
  },

  /**
   * 更新实践计划
   */
  updatePlan: async (plan: PracticePlan) => {
    try {
      const response = await axios.put(`${API_BASE_URL}/practice-plan/update`, plan)
      return response.data
    } catch (error) {
      console.error('更新实践计划失败:', error)
      throw error
    }
  },

  /**
   * 获取用户的实践计划列表
   */
  getUserPlans: async (userId: number) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/practice-plan/user/${userId}`)
      return response.data
    } catch (error) {
      console.error('获取实践计划列表失败:', error)
      throw error
    }
  },

  /**
   * 获取实践计划详情
   */
  getPlanById: async (planId: number) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/practice-plan/${planId}`)
      return response.data
    } catch (error) {
      console.error('获取实践计划详情失败:', error)
      throw error
    }
  },

  /**
   * 删除实践计划
   */
  deletePlan: async (planId: number) => {
    try {
      const response = await axios.delete(`${API_BASE_URL}/practice-plan/${planId}`)
      return response.data
    } catch (error) {
      console.error('删除实践计划失败:', error)
      throw error
    }
  },

  /**
   * 更新计划进度
   */
  updateProgress: async (planId: number, progress: number) => {
    try {
      const response = await axios.put(`${API_BASE_URL}/practice-plan/${planId}/progress`, {
        progress: progress
      })
      return response.data
    } catch (error) {
      console.error('更新计划进度失败:', error)
      throw error
    }
  },

  /**
   * 更新计划状态
   */
  updateStatus: async (planId: number, status: number) => {
    try {
      const response = await axios.put(`${API_BASE_URL}/practice-plan/${planId}/status`, {
        status: status
      })
      return response.data
    } catch (error) {
      console.error('更新计划状态失败:', error)
      throw error
    }
  },

  /**
   * 获取用户公开的计划列表
   */
  getPublicPlans: async (userId: number) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/practice-plan/user/${userId}/public`)
      return response.data
    } catch (error) {
      console.error('获取公开计划列表失败:', error)
      throw error
    }
  },

  /**
   * 获取计划统计信息
   */
  getPlanStatistics: async (userId: number) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/practice-plan/user/${userId}/statistics`)
      return response.data
    } catch (error) {
      console.error('获取计划统计信息失败:', error)
      throw error
    }
  }
}

/**
 * 计划类型常量
 */
export const PLAN_TYPES = {
  COMMUNICATION: 1, // 沟通改善
  EMOTION: 2,       // 情绪管理
  RELATIONSHIP: 3,  // 关系维护
  PERSONAL: 4       // 个人成长
}

/**
 * 计划状态常量
 */
export const PLAN_STATUS = {
  NOT_STARTED: 0,   // 未开始
  IN_PROGRESS: 1,   // 进行中
  COMPLETED: 2,     // 已完成
  ABANDONED: 3      // 已放弃
}

/**
 * 计划优先级常量
 */
export const PLAN_PRIORITY = {
  LOW: 1,           // 低
  MEDIUM: 2,        // 中
  HIGH: 3           // 高
}

/**
 * 获取计划类型文本
 */
export const getPlanTypeText = (type: number): string => {
  switch (type) {
    case PLAN_TYPES.COMMUNICATION: return '沟通改善'
    case PLAN_TYPES.EMOTION: return '情绪管理'
    case PLAN_TYPES.RELATIONSHIP: return '关系维护'
    case PLAN_TYPES.PERSONAL: return '个人成长'
    default: return '未知类型'
  }
}

/**
 * 获取计划状态文本
 */
export const getPlanStatusText = (status: number): string => {
  switch (status) {
    case PLAN_STATUS.NOT_STARTED: return '未开始'
    case PLAN_STATUS.IN_PROGRESS: return '进行中'
    case PLAN_STATUS.COMPLETED: return '已完成'
    case PLAN_STATUS.ABANDONED: return '已放弃'
    default: return '未知状态'
  }
}

/**
 * 获取计划优先级文本
 */
export const getPlanPriorityText = (priority: number): string => {
  switch (priority) {
    case PLAN_PRIORITY.LOW: return '低'
    case PLAN_PRIORITY.MEDIUM: return '中'
    case PLAN_PRIORITY.HIGH: return '高'
    default: return '未知'
  }
}
