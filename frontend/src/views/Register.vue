<template>
  <div class="register-page">
    <div class="register-container">
      <!-- Logo和标题 -->
      <div class="register-header">
        <h1 class="register-title">BeMan</h1>
        <p class="register-subtitle">重新连接情感，理性经营关系</p>
      </div>

      <!-- 注册表单 -->
      <div class="register-form">
        <BemanCard padding="large">
          <template #header>
            <h2 class="form-title">用户注册</h2>
          </template>
          
          <form @submit.prevent="handleRegister" class="form">
            <div class="form-group">
              <label for="username" class="form-label">用户名 *</label>
              <input
                id="username"
                v-model="form.username"
                type="text"
                class="form-input"
                :class="{ 'form-input--error': errors.username }"
                placeholder="请输入用户名（3-20位字母数字组合）"
                required
              />
              <span v-if="errors.username" class="form-error">{{ errors.username }}</span>
            </div>

            <div class="form-group">
              <label for="nickname" class="form-label">昵称 *</label>
              <input
                id="nickname"
                v-model="form.nickname"
                type="text"
                class="form-input"
                :class="{ 'form-input--error': errors.nickname }"
                placeholder="请输入昵称（2-20位字符）"
                required
              />
              <span v-if="errors.nickname" class="form-error">{{ errors.nickname }}</span>
            </div>

            <div class="form-group">
              <label for="email" class="form-label">邮箱</label>
              <input
                id="email"
                v-model="form.email"
                type="email"
                class="form-input"
                :class="{ 'form-input--error': errors.email }"
                placeholder="请输入邮箱地址（可选）"
              />
              <span v-if="errors.email" class="form-error">{{ errors.email }}</span>
            </div>

            <div class="form-group">
              <label for="phone" class="form-label">手机号</label>
              <input
                id="phone"
                v-model="form.phone"
                type="tel"
                class="form-input"
                :class="{ 'form-input--error': errors.phone }"
                placeholder="请输入手机号（可选）"
              />
              <span v-if="errors.phone" class="form-error">{{ errors.phone }}</span>
            </div>

            <div class="form-group">
              <label for="password" class="form-label">密码 *</label>
              <input
                id="password"
                v-model="form.password"
                type="password"
                class="form-input"
                :class="{ 'form-input--error': errors.password }"
                placeholder="请输入密码（6-20位字符）"
                required
              />
              <span v-if="errors.password" class="form-error">{{ errors.password }}</span>
            </div>

            <div class="form-group">
              <label for="confirmPassword" class="form-label">确认密码 *</label>
              <input
                id="confirmPassword"
                v-model="form.confirmPassword"
                type="password"
                class="form-input"
                :class="{ 'form-input--error': errors.confirmPassword }"
                placeholder="请再次输入密码"
                required
              />
              <span v-if="errors.confirmPassword" class="form-error">{{ errors.confirmPassword }}</span>
            </div>

            <div class="form-group">
              <label class="form-label">用户类型 *</label>
              <div class="radio-group">
                <label class="radio-item">
                  <input
                    v-model="form.userType"
                    type="radio"
                    :value="1"
                    name="userType"
                  />
                  <span class="radio-text">实名用户</span>
                </label>
                <label class="radio-item">
                  <input
                    v-model="form.userType"
                    type="radio"
                    :value="2"
                    name="userType"
                  />
                  <span class="radio-text">匿名用户</span>
                </label>
              </div>
              <span v-if="errors.userType" class="form-error">{{ errors.userType }}</span>
            </div>

                         <div class="form-group">
               <label class="checkbox-item">
                 <input
                   v-model="agreeTerms"
                   type="checkbox"
                   required
                 />
                <span class="checkbox-text">
                  我已阅读并同意
                  <a href="#" @click.prevent="showTerms = true" class="link">用户协议</a>
                  和
                  <a href="#" @click.prevent="showPrivacy = true" class="link">隐私政策</a>
                </span>
              </label>
              <span v-if="errors.agreeTerms" class="form-error">{{ errors.agreeTerms }}</span>
            </div>

            <BemanButton
              type="primary"
              size="large"
              :loading="loading"
              :disabled="loading"
              class="form-submit"
            >
              立即注册
            </BemanButton>
          </form>

          <div class="form-footer">
            <span>已有账号？</span>
            <router-link to="/login" class="form-link">立即登录</router-link>
          </div>
        </BemanCard>
      </div>
    </div>

    <!-- 用户协议弹窗 -->
    <div v-if="showTerms" class="modal-overlay" @click="showTerms = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>用户协议</h3>
          <button class="modal-close" @click="showTerms = false">×</button>
        </div>
        <div class="modal-body">
          <h4>1. 服务说明</h4>
          <p>BeMan是一个情感关系管理平台，致力于帮助用户维护健康的关系，提供情感支持和建议。</p>
          
          <h4>2. 用户责任</h4>
          <p>用户应遵守相关法律法规，不得发布违法、有害、威胁、辱骂、骚扰、诽谤、粗俗、淫秽或其他不当内容。</p>
          
          <h4>3. 隐私保护</h4>
          <p>我们承诺保护用户隐私，不会泄露用户的个人信息，除非法律要求或获得用户明确同意。</p>
          
          <h4>4. 服务变更</h4>
          <p>我们保留随时修改或终止服务的权利，但会提前通知用户。</p>
        </div>
      </div>
    </div>

    <!-- 隐私政策弹窗 -->
    <div v-if="showPrivacy" class="modal-overlay" @click="showPrivacy = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>隐私政策</h3>
          <button class="modal-close" @click="showPrivacy = false">×</button>
        </div>
        <div class="modal-body">
          <h4>1. 信息收集</h4>
          <p>我们收集的信息包括：注册信息、使用记录、设备信息等，用于提供和改进服务。</p>
          
          <h4>2. 信息使用</h4>
          <p>收集的信息仅用于：提供服务、改善用户体验、安全防护、法律合规等目的。</p>
          
          <h4>3. 信息共享</h4>
          <p>我们不会向第三方出售、出租或共享用户个人信息，除非获得用户明确同意。</p>
          
          <h4>4. 信息安全</h4>
          <p>我们采用行业标准的安全措施保护用户信息，防止未经授权的访问、使用或泄露。</p>
        </div>
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
import type { RegisterRequest } from '../types/user'

const router = useRouter()
const userStore = useUserStore()

// 表单数据
const form = reactive<RegisterRequest & { agreeTerms: boolean }>({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: '',
  userType: 1,
  agreeTerms: false
})

// 表单状态
const loading = ref(false)
const showTerms = ref(false)
const showPrivacy = ref(false)
const agreeTerms = ref(false)

// 错误信息
const errors = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: '',
  userType: '',
  agreeTerms: ''
})

// 验证表单
const validateForm = () => {
  // 重置错误信息
  Object.keys(errors).forEach(key => {
    errors[key] = ''
  })

  let isValid = true

  // 验证用户名
  if (!form.username.trim()) {
    errors.username = '请输入用户名'
    isValid = false
  } else if (form.username.length < 3 || form.username.length > 20) {
    errors.username = '用户名长度应为3-20位'
    isValid = false
  } else if (!/^[a-zA-Z0-9_]+$/.test(form.username)) {
    errors.username = '用户名只能包含字母、数字和下划线'
    isValid = false
  }

  // 验证昵称
  if (!form.nickname.trim()) {
    errors.nickname = '请输入昵称'
    isValid = false
  } else if (form.nickname.length < 2 || form.nickname.length > 20) {
    errors.nickname = '昵称长度应为2-20位'
    isValid = false
  }

  // 验证邮箱
  if (form.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
    errors.email = '请输入有效的邮箱地址'
    isValid = false
  }

  // 验证手机号
  if (form.phone && !/^1[3-9]\d{9}$/.test(form.phone)) {
    errors.phone = '请输入有效的手机号'
    isValid = false
  }

  // 验证密码
  if (!form.password) {
    errors.password = '请输入密码'
    isValid = false
  } else if (form.password.length < 6 || form.password.length > 20) {
    errors.password = '密码长度应为6-20位'
    isValid = false
  }

  // 验证确认密码
  if (!form.confirmPassword) {
    errors.confirmPassword = '请确认密码'
    isValid = false
  } else if (form.password !== form.confirmPassword) {
    errors.confirmPassword = '两次输入的密码不一致'
    isValid = false
  }

  // 验证用户类型
  if (!form.userType) {
    errors.userType = '请选择用户类型'
    isValid = false
  }

     // 验证协议同意
   if (!agreeTerms.value) {
     errors.agreeTerms = '请阅读并同意用户协议和隐私政策'
     isValid = false
   }

  return isValid
}

// 处理注册
const handleRegister = async () => {
  if (!validateForm()) return

  loading.value = true
  try {
    const result = await userStore.register(form)
    if (result.success) {
      alert('注册成功！请登录')
      router.push('/login')
    } else {
      alert(result.message || '注册失败')
    }
  } catch (error) {
    alert('注册失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
@import '../styles/variables.scss';

.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: $spacing-md;
  background: $bg-app;
}

.register-container {
  width: 100%;
  max-width: 500px;
}

.register-header {
  text-align: center;
  margin-bottom: $spacing-xl;
}

.register-title {
  font-size: 2.5rem;
  font-weight: $font-weight-bold;
  color: $text-primary;
  margin-bottom: $spacing-sm;
}

.register-subtitle {
  font-size: 1.1rem;
  color: $text-secondary;
}

.register-form {
  .form-title {
    font-size: 1.5rem;
    font-weight: $font-weight-semibold;
    color: $text-primary;
    margin: 0;
    text-align: center;
  }

  .form {
    margin-top: $spacing-lg;
  }

  .form-group {
    margin-bottom: $spacing-md;

    .form-label {
      display: block;
      font-weight: $font-weight-medium;
      color: $text-primary;
      margin-bottom: $spacing-xs;
    }

    .form-input {
      width: 100%;
      padding: $spacing-sm $spacing-md;
      border: 2px solid $border-color;
      border-radius: $border-radius;
      font-size: 1rem;
      transition: border-color 0.3s ease;

      &:focus {
        outline: none;
        border-color: $primary-color;
      }

      &--error {
        border-color: $error-color;
      }
    }

    .form-error {
      display: block;
      color: $error-color;
      font-size: 0.875rem;
      margin-top: $spacing-xs;
    }
  }

  .radio-group {
    display: flex;
    gap: $spacing-lg;

    .radio-item {
      display: flex;
      align-items: center;
      cursor: pointer;

      input[type="radio"] {
        margin-right: $spacing-xs;
      }

      .radio-text {
        color: $text-primary;
      }
    }
  }

  .checkbox-item {
    display: flex;
    align-items: flex-start;
    cursor: pointer;

    input[type="checkbox"] {
      margin-right: $spacing-xs;
      margin-top: 2px;
    }

    .checkbox-text {
      color: $text-secondary;
      font-size: 0.875rem;
      line-height: 1.4;

      .link {
        color: $primary-color;
        text-decoration: none;

        &:hover {
          text-decoration: underline;
        }
      }
    }
  }

  .form-submit {
    width: 100%;
    margin-top: $spacing-lg;
  }

  .form-footer {
    text-align: center;
    margin-top: $spacing-lg;
    color: $text-secondary;

    .form-link {
      color: $primary-color;
      text-decoration: none;
      margin-left: $spacing-xs;

      &:hover {
        text-decoration: underline;
      }
    }
  }
}

// 弹窗样式
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: $border-radius;
  max-width: 600px;
  max-height: 80vh;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);

  .modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: $spacing-lg;
    border-bottom: 1px solid $border-color;

    h3 {
      margin: 0;
      color: $text-primary;
    }

    .modal-close {
      background: none;
      border: none;
      font-size: 1.5rem;
      cursor: pointer;
      color: $text-secondary;
      padding: 0;
      width: 30px;
      height: 30px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;

      &:hover {
        background: $bg-hover;
      }
    }
  }

  .modal-body {
    padding: $spacing-lg;
    max-height: 60vh;
    overflow-y: auto;

    h4 {
      color: $text-primary;
      margin: $spacing-md 0 $spacing-sm 0;
    }

    p {
      color: $text-secondary;
      line-height: 1.6;
      margin-bottom: $spacing-sm;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .register-container {
    max-width: 100%;
  }

  .radio-group {
    flex-direction: column;
    gap: $spacing-sm;
  }
}
</style>


