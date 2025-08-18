import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

/**
 * 内容审核相关API
 */
export const auditAPI = {
  /**
   * 审核内容
   * @param {string} content 待审核的内容
   * @returns {Promise} 审核结果
   */
  auditContent: async (content) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/audit/content`, {
        content: content
      })
      return response.data
    } catch (error) {
      console.error('内容审核失败:', error)
      throw error
    }
  },

  /**
   * 检查是否为极端内容
   * @param {string} content 待检查的内容
   * @returns {Promise} 检查结果
   */
  checkExtremeContent: async (content) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/audit/check-extreme`, {
        content: content
      })
      return response.data
    } catch (error) {
      console.error('极端内容检查失败:', error)
      throw error
    }
  },

  /**
   * 获取内容风险等级
   * @param {string} content 待评估的内容
   * @returns {Promise} 风险等级
   */
  getRiskLevel: async (content) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/audit/risk-level`, {
        content: content
      })
      return response.data
    } catch (error) {
      console.error('风险等级评估失败:', error)
      throw error
    }
  },

  /**
   * 获取替换建议
   * @param {string} content 内容
   * @returns {Promise} 建议内容
   */
  getSuggestion: async (content) => {
    try {
      const response = await axios.post(`${API_BASE_URL}/audit/suggestion`, {
        content: content
      })
      return response.data
    } catch (error) {
      console.error('获取建议失败:', error)
      throw error
    }
  }
}
