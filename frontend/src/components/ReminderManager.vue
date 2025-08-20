<template>
  <div class="reminder-manager">
    <!-- 统计概览 -->
    <div class="stats-overview">
      <div class="stat-card">
        <div class="stat-icon">⏰</div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.pendingCount }}</div>
          <div class="stat-label">待发送</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">✅</div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.sentCount }}</div>
          <div class="stat-label">已发送</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">❌</div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.failedCount }}</div>
          <div class="stat-label">发送失败</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">📊</div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.todaySentCount }}</div>
          <div class="stat-label">今日发送</div>
        </div>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="action-bar">
      <button class="btn-primary" @click="showCreateModal = true">
        <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
          <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
        </svg>
        新建提醒
      </button>
      <button class="btn-secondary" @click="loadReminders">
        <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
          <path d="M17.65 6.35A7.958 7.958 0 0012 4c-4.42 0-7.99 3.58-7.99 8s3.57 8 7.99 8c3.73 0 6.84-2.55 7.73-6h-2.08A5.99 5.99 0 0112 18c-3.31 0-6-2.69-6-6s2.69-6 6-6c1.66 0 3.14.69 4.22 1.78L13 11h7V4l-2.35 2.35z"/>
        </svg>
        刷新
      </button>
    </div>

    <!-- 提醒列表 -->
    <div class="reminder-list">
      <div class="list-header">
        <h3>提醒列表 ({{ reminders.length }})</h3>
      </div>
      
      <div v-if="loading" class="loading">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>

      <div v-else-if="reminders.length === 0" class="empty-state">
        <svg viewBox="0 0 24 24" fill="currentColor" width="64" height="64">
          <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
        </svg>
        <h3>暂无提醒</h3>
        <p>开始创建你的第一个提醒吧</p>
      </div>

      <div v-else class="reminder-items">
        <div v-for="reminder in reminders" :key="reminder.id" class="reminder-item">
          <div class="reminder-header">
            <div class="reminder-type">
              <span :class="`type-badge type-${reminder.type}`">
                {{ getTypeName(reminder.type) }}
              </span>
            </div>
            <div class="reminder-actions">
              <button @click="editReminder(reminder)" class="action-btn edit">编辑</button>
              <button @click="deleteReminder(reminder.id)" class="action-btn delete">删除</button>
            </div>
          </div>

          <div class="reminder-content">
            <div class="reminder-text">{{ reminder.content || '无内容' }}</div>
            <div class="reminder-meta">
              <span class="reminder-time">
                <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
                  <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                </svg>
                {{ formatTime(reminder.reminderTime) }}
              </span>
            </div>
          </div>

          <div class="reminder-footer">
            <div class="reminder-status">
              <span :class="`status-badge status-${reminder.status}`">
                {{ getStatusName(reminder.status) }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import {
  getReminderList,
  getReminderStats,
  type Reminder,
  type ReminderStats
} from '../api/reminder'

// 响应式数据
const loading = ref(false)
const reminders = ref<Reminder[]>([])
const stats = ref<ReminderStats>({
  pendingCount: 0,
  sentCount: 0,
  failedCount: 0,
  todaySentCount: 0,
  weekSentCount: 0
})

// 模态框状态
const showCreateModal = ref(false)

// 方法
const loadReminders = async () => {
  loading.value = true
  try {
    reminders.value = await getReminderList({})
  } catch (error) {
    console.error('加载提醒失败:', error)
  } finally {
    loading.value = false
  }
}

const loadStats = async () => {
  try {
    stats.value = await getReminderStats()
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const editReminder = (reminder: Reminder) => {
  console.log('编辑提醒:', reminder)
}

const deleteReminder = async (id: number) => {
  if (confirm('确定要删除这个提醒吗？')) {
    console.log('删除提醒:', id)
  }
}

const getTypeName = (type: number) => {
  const typeNames = ['', '邮件', '短信', '推送', '微信', '钉钉']
  return typeNames[type] || '未知'
}

const getStatusName = (status: number) => {
  const statusNames = ['', '待发送', '已发送', '发送失败', '已取消']
  return statusNames[status] || '未知'
}

const formatTime = (timeStr: string) => {
  const date = new Date(timeStr)
  return date.toLocaleString('zh-CN')
}

onMounted(() => {
  loadReminders()
  loadStats()
})
</script>

<style scoped lang="scss">
@import '../styles/variables.scss';

.reminder-manager {
  padding: $spacing-lg;
  background-color: $bg-app;
  color: $text-primary;
  min-height: 100vh;
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: $spacing-md;
  margin-bottom: $spacing-xl;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-lg;
  background: $bg-section;
  border-radius: $radius-card;
  border: 1px solid $border-card;
  box-shadow: $shadow-card;
  
  .stat-icon {
    font-size: 2rem;
  }
  
  .stat-content {
    .stat-number {
      font-size: $font-size-xl;
      font-weight: $font-weight-bold;
      color: $color-secondary;
      margin-bottom: $spacing-xs;
    }
    
    .stat-label {
      color: $text-secondary;
      font-size: $font-size-sm;
    }
  }
}

.action-bar {
  display: flex;
  gap: $spacing-md;
  margin-bottom: $spacing-lg;
  flex-wrap: wrap;
}

.btn-primary {
  background: $color-secondary;
  color: white;
  border: none;
  padding: $spacing-sm $spacing-md;
  border-radius: $radius-button;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  
  &:hover {
    background: darken($color-secondary, 10%);
  }
}

.btn-secondary {
  background: $bg-app;
  color: $text-primary;
  border: 1px solid $border-card;
  padding: $spacing-sm $spacing-md;
  border-radius: $radius-button;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  
  &:hover {
    background: rgba($color-secondary, 0.1);
    border-color: $color-secondary;
  }
}

.reminder-list {
  background: $bg-section;
  border-radius: $radius-card;
  border: 1px solid $border-card;
  box-shadow: $shadow-card;
  
  .list-header {
    padding: $spacing-lg;
    border-bottom: 1px solid $border-card;
    
    h3 {
      margin: 0;
      color: $color-primary;
    }
  }
}

.loading {
  text-align: center;
  padding: $spacing-xl;
  color: $text-secondary;
  
  .loading-spinner {
    width: 40px;
    height: 40px;
    border: 3px solid $border-card;
    border-top: 3px solid $color-secondary;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin: 0 auto $spacing-md;
  }
}

.empty-state {
  text-align: center;
  padding: $spacing-xl;
  color: $text-secondary;
  
  svg {
    margin-bottom: $spacing-md;
    opacity: 0.5;
  }
  
  h3 {
    color: $text-primary;
    margin-bottom: $spacing-sm;
  }
}

.reminder-items {
  padding: $spacing-lg;
}

.reminder-item {
  background: $bg-app;
  border-radius: $radius-card;
  border: 1px solid $border-card;
  padding: $spacing-lg;
  margin-bottom: $spacing-md;
  box-shadow: $shadow-item;
  transition: all 0.2s;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: $shadow-hover;
  }
}

.reminder-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $spacing-md;
}

.type-badge {
  padding: $spacing-xs $spacing-sm;
  border-radius: $radius-tag;
  font-size: $font-size-xs;
  font-weight: $font-weight-medium;
  
  &.type-1 { background: rgba(#2196f3, 0.1); color: #2196f3; }
  &.type-2 { background: rgba(#4caf50, 0.1); color: #4caf50; }
  &.type-3 { background: rgba(#ff9800, 0.1); color: #ff9800; }
  &.type-4 { background: rgba(#9c27b0, 0.1); color: #9c27b0; }
  &.type-5 { background: rgba(#f44336, 0.1); color: #f44336; }
}

.reminder-content {
  margin-bottom: $spacing-md;
  
  .reminder-text {
    color: $text-primary;
    margin-bottom: $spacing-sm;
    font-size: $font-size-md;
  }
  
  .reminder-meta {
    display: flex;
    gap: $spacing-md;
    flex-wrap: wrap;
    font-size: $font-size-sm;
    color: $text-secondary;
    
    span {
      display: flex;
      align-items: center;
      gap: $spacing-xs;
    }
  }
}

.reminder-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.status-badge {
  padding: $spacing-xs $spacing-sm;
  border-radius: $radius-tag;
  font-size: $font-size-xs;
  font-weight: $font-weight-medium;
  
  &.status-1 { background: rgba(#ff9800, 0.1); color: #ff9800; }
  &.status-2 { background: rgba(#4caf50, 0.1); color: #4caf50; }
  &.status-3 { background: rgba(#f44336, 0.1); color: #f44336; }
  &.status-4 { background: rgba(#9e9e9e, 0.1); color: #9e9e9e; }
}

.reminder-actions {
  display: flex;
  gap: $spacing-xs;
}

.action-btn {
  padding: $spacing-xs $spacing-sm;
  border: none;
  border-radius: $radius-button;
  cursor: pointer;
  font-size: $font-size-sm;
  transition: all 0.2s;
  
  &.edit {
    background: rgba($color-secondary, 0.1);
    color: $color-secondary;
    
    &:hover {
      background: rgba($color-secondary, 0.2);
    }
  }
  
  &.delete {
    background: rgba(#f44336, 0.1);
    color: #f44336;
    
    &:hover {
      background: rgba(#f44336, 0.2);
    }
  }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@media (max-width: $breakpoint-md) {
  .stats-overview {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .action-bar {
    flex-direction: column;
  }
  
  .reminder-header {
    flex-direction: column;
    align-items: flex-start;
    gap: $spacing-sm;
  }
  
  .reminder-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: $spacing-sm;
  }
}
</style>
