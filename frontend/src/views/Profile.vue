<template>
  <div class="profile-page">
    <!-- 顶部导航 -->
    <div class="profile-header">
      <div class="header-content">
        <h1 class="page-title">个人中心</h1>
        <div class="header-actions">
          <button class="btn-secondary" @click="showEditModal = true">
            <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
              <path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/>
            </svg>
            编辑资料
          </button>
        </div>
      </div>
    </div>

    <div class="profile-content">
      <!-- 个人信息卡片 -->
      <div class="profile-card">
        <div class="profile-avatar">
          <img v-if="user?.avatar" :src="user.avatar" :alt="user?.nickname">
          <div v-else class="avatar-placeholder">
            {{ user?.nickname?.charAt(0) || '用' }}
          </div>
        </div>
        <div class="profile-info">
          <h2 class="profile-name">{{ user?.nickname || '未设置昵称' }}</h2>
          <p class="profile-username">@{{ user?.username || 'unknown' }}</p>
          <p class="profile-type">
            <span class="type-badge" :class="userTypeClass">
              {{ userTypeText }}
            </span>
          </p>
          <p class="profile-join-time">加入时间：{{ formatTime(user?.createTime) }}</p>
        </div>
      </div>

      <!-- 功能导航 -->
      <div class="profile-nav">
        <div class="nav-section">
          <h3>账户管理</h3>
          <div class="nav-items">
            <div class="nav-item" @click="activeTab = 'account'">
              <svg viewBox="0 0 24 24" fill="currentColor" width="24" height="24">
                <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
              </svg>
              <span>账户设置</span>
            </div>
            <div class="nav-item" @click="activeTab = 'security'">
              <svg viewBox="0 0 24 24" fill="currentColor" width="24" height="24">
                <path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zM9 6c0-1.66 1.34-3 3-3s3 1.34 3 3v2H9V6zm3 15c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2z"/>
              </svg>
              <span>安全设置</span>
            </div>
            <div class="nav-item" @click="activeTab = 'privacy'">
              <svg viewBox="0 0 24 24" fill="currentColor" width="24" height="24">
                <path d="M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4zm0 10.99h7c-.53 4.12-3.28 7.79-7 8.94V12H5V6.3l7-3.11v8.8z"/>
              </svg>
              <span>隐私设置</span>
            </div>
          </div>
        </div>

        <div class="nav-section">
          <h3>偏好设置</h3>
          <div class="nav-items">
            <div class="nav-item" @click="activeTab = 'notifications'">
              <svg viewBox="0 0 24 24" fill="currentColor" width="24" height="24">
                <path d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.89 2 2 2zm6-6v-5c0-3.07-1.64-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.63 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z"/>
              </svg>
              <span>通知设置</span>
            </div>
            <div class="nav-item" @click="activeTab = 'appearance'">
              <svg viewBox="0 0 24 24" fill="currentColor" width="24" height="24">
                <path d="M12 3c-4.97 0-9 4.03-9 9s4.03 9 9 9c.83 0 1.5-.67 1.5-1.5 0-.39-.15-.74-.39-1.01-.23-.26-.38-.61-.38-.99 0-.83.67-1.5 1.5-1.5H16c2.76 0 5-2.24 5-5 0-4.42-4.03-8-9-8zm-5.5 9c-.83 0-1.5-.67-1.5-1.5S5.67 9 6.5 9 8 9.67 8 10.5 7.33 12 6.5 12zm3-4C8.67 8 8 7.33 8 6.5S8.67 5 9.5 5s1.5.67 1.5 1.5S10.33 8 9.5 8zm5 0c-.83 0-1.5-.67-1.5-1.5S13.67 5 14.5 5s1.5.67 1.5 1.5S15.33 8 14.5 8zm3 4c-.83 0-1.5-.67-1.5-1.5S16.67 9 17.5 9s1.5.67 1.5 1.5-.67 1.5-1.5 1.5z"/>
              </svg>
              <span>外观设置</span>
            </div>
          </div>
        </div>

        <div class="nav-section">
          <h3>数据管理</h3>
          <div class="nav-items">
            <div class="nav-item" @click="activeTab = 'data'">
              <svg viewBox="0 0 24 24" fill="currentColor" width="24" height="24">
                <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/>
              </svg>
              <span>数据管理</span>
            </div>
          </div>
        </div>

        <div class="nav-section">
          <h3>帮助支持</h3>
          <div class="nav-items">
            <div class="nav-item" @click="activeTab = 'help'">
              <svg viewBox="0 0 24 24" fill="currentColor" width="24" height="24">
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 17h-2v-2h2v2zm2.07-7.75l-.9.92C13.45 12.9 13 13.5 13 15h-2v-.5c0-1.1.45-2.1 1.17-2.83l1.24-1.26c.37-.36.59-.86.59-1.41 0-1.1-.9-2-2-2s-2 .9-2 2H8c0-2.21 1.79-4 4-4s4 1.79 4 4c0 .88-.36 1.68-.93 2.25z"/>
              </svg>
              <span>帮助与支持</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 内容区域 -->
      <div class="profile-content-area">
        <!-- 账户设置 -->
        <div v-if="activeTab === 'account'" class="tab-content">
          <h3>账户设置</h3>
          <div class="setting-group">
            <label class="setting-label">用户名</label>
            <div class="setting-value">{{ user?.username }}</div>
            <button class="btn-secondary" @click="showEditModal = true">修改</button>
          </div>
          <div class="setting-group">
            <label class="setting-label">昵称</label>
            <div class="setting-value">{{ user?.nickname || '未设置' }}</div>
            <button class="btn-secondary" @click="showEditModal = true">修改</button>
          </div>
          <div class="setting-group">
            <label class="setting-label">邮箱</label>
            <div class="setting-value">{{ user?.email || '未设置' }}</div>
            <button class="btn-secondary" @click="showEditModal = true">修改</button>
          </div>
          <div class="setting-group">
            <label class="setting-label">手机号</label>
            <div class="setting-value">{{ user?.phone || '未设置' }}</div>
            <button class="btn-secondary" @click="showEditModal = true">修改</button>
          </div>
        </div>

        <!-- 安全设置 -->
        <div v-if="activeTab === 'security'" class="tab-content">
          <h3>安全设置</h3>
          <div class="setting-group">
            <label class="setting-label">密码</label>
            <div class="setting-value">••••••••</div>
            <button class="btn-secondary" @click="showPasswordModal = true">修改密码</button>
          </div>
          <div class="setting-group">
            <label class="setting-label">登录设备</label>
            <div class="setting-value">当前设备</div>
            <button class="btn-secondary" @click="showDevicesModal = true">查看设备</button>
          </div>
        </div>

        <!-- 隐私设置 -->
        <div v-if="activeTab === 'privacy'" class="tab-content">
          <h3>隐私设置</h3>
          <div class="setting-group">
            <label class="setting-label">隐私级别</label>
            <div class="setting-value">
              <select v-model="privacyLevel" class="privacy-select">
                <option :value="0">公开</option>
                <option :value="1">仅好友可见</option>
                <option :value="2">完全私密</option>
              </select>
            </div>
            <button class="btn-primary" @click="updatePrivacy">保存设置</button>
          </div>
        </div>

        <!-- 通知设置 -->
        <div v-if="activeTab === 'notifications'" class="tab-content">
          <h3>通知设置</h3>
          <div class="setting-group">
            <label class="setting-label">邮件通知</label>
            <div class="setting-value">
              <label class="switch">
                <input type="checkbox" v-model="notificationSettings.email">
                <span class="slider"></span>
              </label>
            </div>
          </div>
          <div class="setting-group">
            <label class="setting-label">短信通知</label>
            <div class="setting-value">
              <label class="switch">
                <input type="checkbox" v-model="notificationSettings.sms">
                <span class="slider"></span>
              </label>
            </div>
          </div>
        </div>

        <!-- 外观设置 -->
        <div v-if="activeTab === 'appearance'" class="tab-content">
          <h3>外观设置</h3>
          <div class="setting-group">
            <label class="setting-label">主题模式</label>
            <div class="setting-value">
              <select v-model="appearanceSettings.theme" class="theme-select">
                <option value="light">浅色模式</option>
                <option value="dark">深色模式</option>
                <option value="auto">跟随系统</option>
              </select>
            </div>
            <button class="btn-primary" @click="updateAppearance">保存设置</button>
          </div>
          <div class="setting-group">
            <label class="setting-label">字体大小</label>
            <div class="setting-value">
              <select v-model="appearanceSettings.fontSize" class="font-size-select">
                <option value="small">小</option>
                <option value="medium">中</option>
                <option value="large">大</option>
              </select>
            </div>
            <button class="btn-primary" @click="updateAppearance">保存设置</button>
          </div>
        </div>

        <!-- 数据管理 -->
        <div v-if="activeTab === 'data'" class="tab-content">
          <h3>数据管理</h3>
          <div class="setting-group">
            <label class="setting-label">数据导出</label>
            <div class="setting-value">导出所有个人数据</div>
            <button class="btn-secondary" @click="exportData">导出数据</button>
          </div>
          <div class="setting-group">
            <label class="setting-label">数据备份</label>
            <div class="setting-value">自动备份到云端</div>
            <button class="btn-secondary" @click="backupData">立即备份</button>
          </div>
          <div class="setting-group">
            <label class="setting-label">数据清理</label>
            <div class="setting-value">清理缓存和临时文件</div>
            <button class="btn-secondary" @click="clearCache">清理缓存</button>
          </div>
        </div>

        <!-- 帮助与支持 -->
        <div v-if="activeTab === 'help'" class="tab-content">
          <h3>帮助与支持</h3>
          <div class="help-section">
            <h4>常见问题</h4>
            <div class="faq-item" v-for="faq in faqList" :key="faq.id">
              <div class="faq-question" @click="toggleFaq(faq.id)">
                {{ faq.question }}
                <span class="faq-toggle">{{ faq.expanded ? '−' : '+' }}</span>
              </div>
              <div v-if="faq.expanded" class="faq-answer">
                {{ faq.answer }}
              </div>
            </div>
          </div>
          <div class="help-section">
            <h4>联系我们</h4>
            <p>如果您需要帮助，请通过以下方式联系我们：</p>
            <div class="contact-info">
              <div class="contact-item">
                <span class="contact-label">邮箱：</span>
                <span class="contact-value">support@beman.com</span>
              </div>
              <div class="contact-item">
                <span class="contact-label">客服热线：</span>
                <span class="contact-value">400-123-4567</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 系统设置 -->
        <div v-if="activeTab === 'system'" class="tab-content">
          <h3>系统设置</h3>
          <div class="setting-group">
            <label class="setting-label">语言设置</label>
            <div class="setting-value">
              <select v-model="systemSettings.language" class="language-select">
                <option value="zh-CN">简体中文</option>
                <option value="en-US">English</option>
                <option value="ja-JP">日本語</option>
              </select>
            </div>
            <button class="btn-primary" @click="updateSystemSettings">保存设置</button>
          </div>
          <div class="setting-group">
            <label class="setting-label">时区设置</label>
            <div class="setting-value">
              <select v-model="systemSettings.timezone" class="timezone-select">
                <option value="Asia/Shanghai">中国标准时间 (UTC+8)</option>
                <option value="America/New_York">美国东部时间 (UTC-5)</option>
                <option value="Europe/London">格林威治时间 (UTC+0)</option>
              </select>
            </div>
            <button class="btn-primary" @click="updateSystemSettings">保存设置</button>
          </div>
          <div class="setting-group">
            <label class="setting-label">自动更新</label>
            <div class="setting-value">
              <label class="switch">
                <input type="checkbox" v-model="systemSettings.autoUpdate">
                <span class="slider"></span>
              </label>
            </div>
          </div>
          <div class="setting-group">
            <label class="setting-label">调试模式</label>
            <div class="setting-value">
              <label class="switch">
                <input type="checkbox" v-model="systemSettings.debugMode">
                <span class="slider"></span>
              </label>
          </div>
        </div>
      </div>
    </div>

    <!-- 编辑资料弹窗 -->
    <div v-if="showEditModal" class="modal-overlay" @click="showEditModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>编辑个人资料</h3>
          <button class="modal-close" @click="showEditModal = false">×</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>昵称</label>
            <input v-model="editForm.nickname" type="text" class="form-input">
          </div>
          <div class="form-group">
            <label>邮箱</label>
            <input v-model="editForm.email" type="email" class="form-input">
          </div>
          <div class="form-group">
            <label>手机号</label>
            <input v-model="editForm.phone" type="tel" class="form-input">
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-secondary" @click="showEditModal = false">取消</button>
          <button class="btn-primary" @click="saveProfile">保存</button>
        </div>
      </div>
    </div>

    <!-- 修改密码弹窗 -->
    <div v-if="showPasswordModal" class="modal-overlay" @click="showPasswordModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>修改密码</h3>
          <button class="modal-close" @click="showPasswordModal = false">×</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>当前密码</label>
            <input v-model="passwordForm.oldPassword" type="password" class="form-input">
          </div>
          <div class="form-group">
            <label>新密码</label>
            <input v-model="passwordForm.newPassword" type="password" class="form-input">
          </div>
          <div class="form-group">
            <label>确认新密码</label>
            <input v-model="passwordForm.confirmPassword" type="password" class="form-input">
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-secondary" @click="showPasswordModal = false">取消</button>
          <button class="btn-primary" @click="changePassword">确认修改</button>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import type { User } from '../types/user'

const userStore = useUserStore()

// 响应式数据
const activeTab = ref('account')
const showEditModal = ref(false)
const showPasswordModal = ref(false)
const privacyLevel = ref(0)
const notificationSettings = reactive({
  email: true,
  sms: false
})
const appearanceSettings = reactive({
  theme: 'light',
  fontSize: 'medium'
})

// 系统设置
const systemSettings = reactive({
  language: 'zh-CN',
  timezone: 'Asia/Shanghai',
  autoUpdate: true,
  debugMode: false
})

// FAQ数据
const faqList = ref([
  {
    id: 1,
    question: '如何修改密码？',
    answer: '在安全设置中点击"修改密码"按钮，输入当前密码和新密码即可完成修改。',
    expanded: false
  },
  {
    id: 2,
    question: '如何保护我的隐私？',
    answer: '在隐私设置中可以调整隐私级别，选择公开、仅好友可见或完全私密。',
    expanded: false
  },
  {
    id: 3,
    question: '如何导出我的数据？',
    answer: '在数据管理中选择"导出数据"，系统会生成包含您所有数据的文件供下载。',
    expanded: false
  },
  {
    id: 4,
    question: '忘记密码怎么办？',
    answer: '可以通过邮箱或手机号进行密码重置，如果仍有问题请联系客服。',
    expanded: false
  }
])

// 编辑表单
const editForm = reactive({
  nickname: '',
  email: '',
  phone: ''
})

// 密码表单
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 计算属性
const user = computed(() => userStore.user)
const userTypeText = computed(() => {
  if (!user.value) return '未知'
  return user.value.userType === 1 ? '实名用户' : '匿名用户'
})
const userTypeClass = computed(() => {
  if (!user.value) return 'type-unknown'
  return user.value.userType === 1 ? 'type-real' : 'type-anonymous'
})

// 方法
const formatTime = (time: string | undefined) => {
  if (!time) return '未知'
  return new Date(time).toLocaleDateString('zh-CN')
}

const updatePrivacy = async () => {
  try {
    // TODO: 调用API更新隐私设置
    alert('隐私设置已更新')
  } catch (error) {
    alert('更新失败，请重试')
  }
}

const updateAppearance = async () => {
  try {
    // TODO: 调用API更新外观设置
    alert('外观设置已更新')
  } catch (error) {
    alert('更新失败，请重试')
  }
}

const saveProfile = async () => {
  try {
    // TODO: 调用API保存个人资料
    alert('个人资料已保存')
    showEditModal.value = false
  } catch (error) {
    alert('保存失败，请重试')
  }
}

const changePassword = async () => {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    alert('两次输入的新密码不一致')
    return
  }
  
  try {
    // TODO: 调用API修改密码
    alert('密码修改成功')
    showPasswordModal.value = false
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
  } catch (error) {
    alert('密码修改失败，请重试')
  }
}

// 数据管理方法
const exportData = async () => {
  try {
    // TODO: 调用API导出数据
    alert('数据导出成功，文件已下载到您的设备')
  } catch (error) {
    alert('数据导出失败，请重试')
  }
}

const backupData = async () => {
  try {
    // TODO: 调用API备份数据
    alert('数据备份成功，已保存到云端')
  } catch (error) {
    alert('数据备份失败，请重试')
  }
}

const clearCache = async () => {
  try {
    // TODO: 调用API清理缓存
    alert('缓存清理成功')
  } catch (error) {
    alert('缓存清理失败，请重试')
  }
}

// FAQ相关方法
const toggleFaq = (id: number) => {
  const faq = faqList.value.find(item => item.id === id)
  if (faq) {
    faq.expanded = !faq.expanded
  }
}

// 系统设置方法
const updateSystemSettings = async () => {
  try {
    // TODO: 调用API更新系统设置
    alert('系统设置已更新')
  } catch (error) {
    alert('更新失败，请重试')
  }
}

// 生命周期
onMounted(async () => {
  if (!user.value) {
    await userStore.fetchUserInfo()
  }
  if (user.value) {
    privacyLevel.value = user.value.privacyLevel
    editForm.nickname = user.value.nickname || ''
    editForm.email = user.value.email || ''
    editForm.phone = user.value.phone || ''
  }
})
</script>

<style lang="scss" scoped>
@import '../styles/variables.scss';

.profile-page {
  min-height: 100vh;
  background: $bg-app;
}

.profile-header {
  background: white;
  border-bottom: 1px solid $border-color;
  padding: $spacing-lg 0;

  .header-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 $spacing-lg;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .page-title {
      margin: 0;
      color: $text-primary;
      font-size: 1.75rem;
      font-weight: $font-weight-bold;
    }

    .header-actions {
      .btn-secondary {
        display: flex;
        align-items: center;
        gap: $spacing-xs;
        padding: $spacing-sm $spacing-md;
        background: $bg-secondary;
        color: $text-primary;
        border: 1px solid $border-color;
        border-radius: $border-radius;
        cursor: pointer;
        transition: all 0.3s ease;

        &:hover {
          background: $bg-hover;
        }
      }
    }
  }
}

.profile-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: $spacing-lg;
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: $spacing-xl;

  @media (max-width: 768px) {
    grid-template-columns: 1fr;
    gap: $spacing-lg;
  }
}

.profile-card {
  background: white;
  border-radius: $border-radius;
  padding: $spacing-xl;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .profile-avatar {
    margin-bottom: $spacing-lg;

    img {
      width: 80px;
      height: 80px;
      border-radius: 50%;
      object-fit: cover;
    }

    .avatar-placeholder {
      width: 80px;
      height: 80px;
      border-radius: 50%;
      background: $primary-color;
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 2rem;
      font-weight: $font-weight-bold;
      margin: 0 auto;
    }
  }

  .profile-info {
    .profile-name {
      margin: 0 0 $spacing-xs 0;
      color: $text-primary;
      font-size: 1.5rem;
      font-weight: $font-weight-bold;
    }

    .profile-username {
      margin: 0 0 $spacing-sm 0;
      color: $text-secondary;
      font-size: 0.9rem;
    }

    .profile-type {
      margin: 0 0 $spacing-sm 0;

      .type-badge {
        display: inline-block;
        padding: $spacing-xs $spacing-sm;
        border-radius: 20px;
        font-size: 0.8rem;
        font-weight: $font-weight-medium;

        &.type-real {
          background: $success-color;
          color: white;
        }

        &.type-anonymous {
          background: $warning-color;
          color: white;
        }

        &.type-unknown {
          background: $text-secondary;
          color: white;
        }
      }
    }

    .profile-join-time {
      margin: 0;
      color: $text-secondary;
      font-size: 0.8rem;
    }
  }
}

.profile-nav {
  .nav-section {
    margin-bottom: $spacing-xl;

    h3 {
      margin: 0 0 $spacing-md 0;
      color: $text-primary;
      font-size: 1.1rem;
      font-weight: $font-weight-semibold;
    }

    .nav-items {
      .nav-item {
        display: flex;
        align-items: center;
        gap: $spacing-sm;
        padding: $spacing-md;
        background: white;
        border-radius: $border-radius;
        margin-bottom: $spacing-sm;
        cursor: pointer;
        transition: all 0.3s ease;

        &:hover {
          background: $bg-hover;
          transform: translateX(5px);
        }

        svg {
          color: $primary-color;
        }

        span {
          color: $text-primary;
          font-weight: $font-weight-medium;
        }
      }
    }
  }
}

.profile-content-area {
  background: white;
  border-radius: $border-radius;
  padding: $spacing-xl;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .tab-content {
    h3 {
      margin: 0 0 $spacing-lg 0;
      color: $text-primary;
      font-size: 1.3rem;
      font-weight: $font-weight-semibold;
    }

    .setting-group {
      display: flex;
      align-items: center;
      padding: $spacing-md 0;
      border-bottom: 1px solid $border-color;

      &:last-child {
        border-bottom: none;
      }

      .setting-label {
        width: 120px;
        font-weight: $font-weight-medium;
        color: $text-primary;
      }

      .setting-value {
        flex: 1;
        color: $text-secondary;

        .privacy-select,
        .theme-select {
          padding: $spacing-xs $spacing-sm;
          border: 1px solid $border-color;
          border-radius: $border-radius;
          background: white;
        }
      }

      button {
        margin-left: $spacing-md;
      }
    }
  }
}

// 开关样式
.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 24px;

  input {
    opacity: 0;
    width: 0;
    height: 0;

    &:checked + .slider {
      background-color: $primary-color;
    }

    &:checked + .slider:before {
      transform: translateX(26px);
    }
  }

  .slider {
    position: absolute;
    cursor: pointer;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #ccc;
    transition: 0.4s;
    border-radius: 24px;

    &:before {
      position: absolute;
      content: "";
      height: 16px;
      width: 16px;
      left: 4px;
      bottom: 4px;
      background-color: white;
      transition: 0.4s;
      border-radius: 50%;
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
  max-width: 500px;
  width: 90%;
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

    .form-group {
      margin-bottom: $spacing-md;

      label {
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
      }
    }
  }

  .modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: $spacing-md;
    padding: $spacing-lg;
    border-top: 1px solid $border-color;
  }
}

// 按钮样式
.btn-primary {
  padding: $spacing-sm $spacing-md;
  background: $primary-color;
  color: white;
  border: none;
  border-radius: $border-radius;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s ease;

  &:hover {
    background: darken($primary-color, 10%);
  }
}

.btn-secondary {
  padding: $spacing-sm $spacing-md;
  background: $bg-secondary;
  color: $text-primary;
  border: 1px solid $border-color;
  border-radius: $border-radius;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s ease;

  &:hover {
    background: $bg-hover;
  }
}

// 帮助与支持样式
.help-section {
  margin-bottom: $spacing-xl;

  h4 {
    color: $text-primary;
    margin: 0 0 $spacing-md 0;
    font-size: 1.1rem;
    font-weight: $font-weight-semibold;
  }

  .faq-item {
    margin-bottom: $spacing-sm;
    border: 1px solid $border-color;
    border-radius: $border-radius;
    overflow: hidden;

    .faq-question {
      padding: $spacing-md;
      background: $bg-secondary;
      cursor: pointer;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-weight: $font-weight-medium;
      color: $text-primary;
      transition: background-color 0.3s ease;

      &:hover {
        background: $bg-hover;
      }

      .faq-toggle {
        font-size: 1.2rem;
        font-weight: $font-weight-bold;
        color: $primary-color;
      }
    }

    .faq-answer {
      padding: $spacing-md;
      background: white;
      color: $text-secondary;
      line-height: 1.6;
      border-top: 1px solid $border-color;
    }
  }

  .contact-info {
    background: $bg-secondary;
    padding: $spacing-md;
    border-radius: $border-radius;

    .contact-item {
      display: flex;
      margin-bottom: $spacing-sm;

      &:last-child {
        margin-bottom: 0;
      }

      .contact-label {
        font-weight: $font-weight-medium;
        color: $text-primary;
        width: 80px;
      }

      .contact-value {
        color: $primary-color;
        font-weight: $font-weight-medium;
      }
    }
  }
}

// 字体大小选择器样式
.font-size-select {
  padding: $spacing-xs $spacing-sm;
  border: 1px solid $border-color;
  border-radius: $border-radius;
  background: white;
  min-width: 100px;
}

// 语言和时区选择器样式
.language-select,
.timezone-select {
  padding: $spacing-xs $spacing-sm;
  border: 1px solid $border-color;
  border-radius: $border-radius;
  background: white;
  min-width: 150px;
}

// 响应式设计
@media (max-width: 768px) {
  .profile-content {
    grid-template-columns: 1fr;
    gap: $spacing-lg;
  }

  .profile-nav {
    order: 2;
  }

  .profile-content-area {
    order: 1;
  }

  .setting-group {
    flex-direction: column;
    align-items: flex-start;
    gap: $spacing-sm;

    button {
      margin-left: 0;
      margin-top: $spacing-sm;
    }
  }

  .faq-question {
    font-size: 0.9rem;
  }

  .contact-info {
    .contact-item {
      flex-direction: column;
      gap: $spacing-xs;

      .contact-label {
        width: auto;
      }
    }
  }
}
</style>