<template>
  <div class="login-page">
    <div class="login-container">
      <!-- Logo和标题 -->
      <div class="login-header">
        <h1 class="login-title">BeMan</h1>
        <p class="login-subtitle">重新连接情感，理性经营关系</p>
      </div>

      <!-- 登录表单 -->
      <div class="login-form">
        <BemanCard padding="large">
          <template #header>
            <h2 class="form-title">登录</h2>
          </template>
          
          <form @submit.prevent="handleLogin" class="form">
            <div class="form-group">
              <label for="username" class="form-label">用户名/邮箱/手机号</label>
              <input
                id="username"
                v-model="form.username"
                type="text"
                class="form-input"
                :class="{ 'form-input--error': errors.username }"
                placeholder="请输入用户名"
                required
              />
              <span v-if="errors.username" class="form-error">{{ errors.username }}</span>
            </div>

            <div class="form-group">
              <label for="password" class="form-label">密码</label>
              <input
                id="password"
                v-model="form.password"
                type="password"
                class="form-input"
                :class="{ 'form-input--error': errors.password }"
                placeholder="请输入密码"
                required
              />
              <span v-if="errors.password" class="form-error">{{ errors.password }}</span>
            </div>

            <BemanButton
              type="primary"
              size="large"
              :loading="loading"
              :disabled="loading"
              class="form-submit"
            >
              登录
            </BemanButton>
          </form>

          <div class="form-divider">
            <span>或</span>
          </div>

          <BemanButton
            type="secondary"
            size="large"
            :loading="anonymousLoading"
            :disabled="anonymousLoading"
            @click="handleAnonymousLogin"
            class="anonymous-btn"
          >
            匿名登录
          </BemanButton>

          <div class="form-footer">
            <span>还没有账号？</span>
            <router-link to="/register" class="form-link">立即注册</router-link>
          </div>
        </BemanCard>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import BemanButton from '../components/BemanButton.vue'
import BemanCard from '../components/BemanCard.vue'

const router = useRouter()
const userStore = useUserStore()

// 表单数据
const form = reactive({
  username: '',
  password: ''
})

// 错误信息
const errors = reactive({
  username: '',
  password: ''
})

// 加载状态
const loading = ref(false)
const anonymousLoading = ref(false)

// 生成设备ID
const generateDeviceId = () => {
  return 'device_' + Math.random().toString(36).substr(2, 9)
}

// 验证表单
const validateForm = () => {
  errors.username = ''
  errors.password = ''

  if (!form.username.trim()) {
    errors.username = '请输入用户名'
    return false
  }

  if (!form.password.trim()) {
    errors.password = '请输入密码'
    return false
  }

  if (form.password.length < 6) {
    errors.password = '密码长度不能少于6位'
    return false
  }

  return true
}

// 处理登录
const handleLogin = async () => {
  if (!validateForm()) return

  loading.value = true
  try {
    const result = await userStore.login(form.username, form.password)
    if (result.success) {
      router.push('/home')
    } else {
      alert(result.message || '登录失败')
    }
  } catch (error) {
    alert('登录失败，请重试')
  } finally {
    loading.value = false
  }
}

// 处理匿名登录
const handleAnonymousLogin = async () => {
  anonymousLoading.value = true
  try {
    const deviceId = generateDeviceId()
    const result = await userStore.anonymousLogin(deviceId)
    if (result.success) {
      router.push('/home')
    } else {
      alert(result.message || '匿名登录失败')
    }
  } catch (error) {
    alert('匿名登录失败，请重试')
  } finally {
    anonymousLoading.value = false
  }
}
</script>

<style lang="scss" scoped>
@import '../styles/variables.scss';

.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: $spacing-md;
  background: $bg-app;
}

.login-container {
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: $spacing-xl;
}

.login-title {
  font-size: 2.5rem;
  font-weight: $font-weight-bold;
  color: $text-primary;
  margin-bottom: $spacing-sm;
  background: $bg-button-primary;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.login-subtitle {
  font-size: $font-size-md;
  color: $text-secondary;
  margin: 0;
}

.login-form {
  .beman-card {
    margin-bottom: $spacing-lg;
  }
}

.form-title {
  font-size: $font-size-xl;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin: 0;
  text-align: center;
}

.form {
  margin-bottom: $spacing-lg;
}

.form-group {
  margin-bottom: $spacing-md;
}

.form-label {
  display: block;
  font-size: $font-size-sm;
  font-weight: $font-weight-medium;
  color: $text-primary;
  margin-bottom: $spacing-xs;
}

.form-input {
  width: 100%;
  padding: $spacing-sm $spacing-md;
  border: 1px solid $border-card;
  border-radius: $radius-button;
  background: $bg-section;
  color: $text-primary;
  font-size: $font-size-md;
  transition: border-color 0.2s;
  
  &::placeholder {
    color: $text-secondary;
  }
  
  &:focus {
    outline: none;
    border-color: $color-secondary;
  }
  
  &--error {
    border-color: #ff4757;
  }
}

.form-error {
  display: block;
  font-size: $font-size-xs;
  color: #ff4757;
  margin-top: $spacing-xs;
}

.form-submit {
  width: 100%;
  margin-top: $spacing-md;
}

.form-divider {
  position: relative;
  text-align: center;
  margin: $spacing-lg 0;
  
  &::before {
    content: '';
    position: absolute;
    top: 50%;
    left: 0;
    right: 0;
    height: 1px;
    background: $border-card;
  }
  
  span {
    background: $bg-card;
    padding: 0 $spacing-md;
    color: $text-secondary;
    font-size: $font-size-sm;
  }
}

.anonymous-btn {
  width: 100%;
  margin-bottom: $spacing-lg;
}

.form-footer {
  text-align: center;
  font-size: $font-size-sm;
  color: $text-secondary;
}

.form-link {
  color: $color-secondary;
  text-decoration: none;
  margin-left: $spacing-xs;
  
  &:hover {
    text-decoration: underline;
  }
}

// 响应式设计
@media (max-width: $breakpoint-sm) {
  .login-page {
    padding: $spacing-sm;
  }
  
  .login-title {
    font-size: 2rem;
  }
}
</style> 