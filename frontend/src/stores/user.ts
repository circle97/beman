import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User } from '../types/user'
import { userApi } from '../api/user'

export const useUserStore = defineStore('user', () => {
  // 状态
  const user = ref<User | null>(null)
  const token = ref<string | null>(localStorage.getItem('beman-token'))
  const isLoggedIn = ref<boolean>(!!token.value)

  // 计算属性
  const isAnonymous = computed(() => user.value?.userType === 2)
  const displayName = computed(() => {
    if (!user.value) return ''
    return isAnonymous.value ? user.value.anonymousId : user.value.nickname
  })

  // 方法
  const setToken = (newToken: string) => {
    token.value = newToken
    isLoggedIn.value = true
    localStorage.setItem('beman-token', newToken)
  }

  const setUser = (userData: User) => {
    user.value = userData
  }

  const login = async (username: string, password: string) => {
    try {
      const response = await userApi.login({ username, password })
      if (response.code === 200 && response.data) {
        setToken(response.data.token)
        await fetchUserInfo()
        return { success: true }
      }
      return { success: false, message: response.message }
    } catch (error) {
      return { success: false, message: '登录失败' }
    }
  }

  const anonymousLogin = async (deviceId: string) => {
    try {
      const response = await userApi.anonymousLogin(deviceId)
      if (response.code === 200 && response.data) {
        setToken(response.data.token)
        await fetchUserInfo()
        return { success: true }
      }
      return { success: false, message: response.message }
    } catch (error) {
      return { success: false, message: '匿名登录失败' }
    }
  }

  const register = async (userData: any) => {
    try {
      const response = await userApi.register(userData)
      if (response.code === 200) {
        return { success: true, message: response.message }
      }
      return { success: false, message: response.message }
    } catch (error) {
      return { success: false, message: '注册失败' }
    }
  }

  const fetchUserInfo = async () => {
    try {
      const response = await userApi.getUserInfo()
      if (response.code === 200 && response.data) {
        setUser(response.data)
      }
    } catch (error) {
      console.error('获取用户信息失败:', error)
    }
  }

  const logout = () => {
    user.value = null
    token.value = null
    isLoggedIn.value = false
    localStorage.removeItem('beman-token')
  }

  const checkLoginStatus = async () => {
    try {
      const response = await userApi.checkLogin()
      if (response.code === 200 && response.data) {
        isLoggedIn.value = true
        if (!user.value) {
          await fetchUserInfo()
        }
      } else {
        logout()
      }
    } catch (error) {
      logout()
    }
  }

  return {
    // 状态
    user,
    token,
    isLoggedIn,
    
    // 计算属性
    isAnonymous,
    displayName,
    
    // 方法
    setToken,
    setUser,
    login,
    anonymousLogin,
    register,
    fetchUserInfo,
    logout,
    checkLoginStatus
  }
}) 