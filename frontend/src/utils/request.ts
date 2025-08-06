import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { useUserStore } from '../stores/user'

// 创建axios实例
const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    // 添加token到请求头
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers = {
        ...config.headers,
        'Authorization': `Bearer ${userStore.token}`
      }
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response
    
    // 如果响应成功，直接返回数据
    if (data.code === 200) {
      return data
    }
    
    // 处理业务错误
    if (data.code === 401) {
      // token过期或无效，清除用户信息并跳转到登录页
      const userStore = useUserStore()
      userStore.logout()
      window.location.href = '/login'
    }
    
    return Promise.reject(new Error(data.message || '请求失败'))
  },
  (error) => {
    // 处理网络错误
    if (error.response) {
      const { status, data } = error.response
      
      switch (status) {
        case 401:
          // 未授权，跳转到登录页
          const userStore = useUserStore()
          userStore.logout()
          window.location.href = '/login'
          break
        case 403:
          console.error('权限不足')
          break
        case 404:
          console.error('请求的资源不存在')
          break
        case 500:
          console.error('服务器内部错误')
          break
        default:
          console.error(`请求失败: ${status}`)
      }
      
      return Promise.reject(new Error(data?.message || '网络错误'))
    } else if (error.request) {
      // 请求已发出但没有收到响应
      console.error('网络连接失败')
      return Promise.reject(new Error('网络连接失败'))
    } else {
      // 请求配置出错
      console.error('请求配置错误:', error.message)
      return Promise.reject(error)
    }
  }
)

export default request 