// 用户类型定义
export interface User {
  id: number
  username: string
  nickname: string
  avatar?: string
  email?: string
  phone?: string
  status: number
  userType: number // 1-实名用户，2-匿名用户
  privacyLevel: number // 0-公开，1-仅好友，2-完全私密
  anonymousId?: string
  lastLoginTime?: string
  createTime: string
  updateTime: string
}

// 登录请求参数
export interface LoginRequest {
  username: string
  password: string
  loginType?: number
  deviceId?: string
}

// 注册请求参数
export interface RegisterRequest {
  username: string
  password: string
  confirmPassword: string
  nickname: string
  email?: string
  phone?: string
  userType?: number
}

// 登录响应
export interface LoginResponse {
  token: string
  tokenName: string
}

// API响应格式
export interface ApiResponse<T = any> {
  code: number
  message: string
  data?: T
  timestamp: number
} 