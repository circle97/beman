import request from '../utils/request'
import type { 
  User, 
  LoginRequest, 
  RegisterRequest, 
  LoginResponse, 
  ApiResponse 
} from '../types/user'

// 用户API接口
export const userApi = {
  // 用户注册
  register: (data: RegisterRequest): Promise<ApiResponse<User>> => {
    return request.post('/api/user/register', data)
  },

  // 用户登录
  login: (data: LoginRequest): Promise<ApiResponse<LoginResponse>> => {
    return request.post('/api/user/login', data)
  },

  // 匿名登录
  anonymousLogin: (deviceId: string): Promise<ApiResponse<LoginResponse>> => {
    return request.post('/api/user/anonymous-login', null, {
      params: { deviceId }
    })
  },

  // 获取用户信息
  getUserInfo: (): Promise<ApiResponse<User>> => {
    return request.get('/api/user/info')
  },

  // 用户登出
  logout: (): Promise<ApiResponse<void>> => {
    return request.post('/api/user/logout')
  },

  // 检查登录状态
  checkLogin: (): Promise<ApiResponse<boolean>> => {
    return request.get('/api/user/check-login')
  }
} 