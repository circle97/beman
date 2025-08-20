<template>
  <div class="test-dashboard-page">
    <div class="page-header">
      <h1>共同账户看板测试页面</h1>
      <p>这个页面用于测试共同账户看板组件的功能</p>
    </div>

    <!-- 测试控制面板 -->
    <div class="test-controls">
      <h3>测试控制</h3>
      <div class="control-buttons">
        <button @click="refreshData" class="btn-primary">
          🔄 刷新数据
        </button>
        <button @click="toggleMockData" class="btn-secondary">
          {{ useMockData ? '📡 使用真实API' : '🎭 使用模拟数据' }}
        </button>
        <button @click="clearData" class="btn-secondary">
          🗑️ 清空数据
        </button>
      </div>
    </div>

    <!-- 共同账户看板 -->
    <div class="dashboard-container">
      <SharedAccountDashboard />
    </div>

    <!-- 数据状态显示 -->
    <div class="data-status">
      <h3>数据状态</h3>
      <div class="status-grid">
        <div class="status-item">
          <div class="status-label">API状态</div>
          <div class="status-value" :class="apiStatus">
            {{ apiStatusText }}
          </div>
        </div>
        <div class="status-item">
          <div class="status-label">数据加载时间</div>
          <div class="status-value">{{ lastLoadTime || '未加载' }}</div>
        </div>
        <div class="status-item">
          <div class="status-label">错误信息</div>
          <div class="status-value error" v-if="lastError">
            {{ lastError }}
          </div>
          <div class="status-value success" v-else>
            无错误
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import SharedAccountDashboard from '../components/SharedAccountDashboard.vue'

// 响应式数据
const useMockData = ref(false)
const apiStatus = ref('idle')
const apiStatusText = ref('空闲')
const lastLoadTime = ref('')
const lastError = ref('')

// 方法
const refreshData = () => {
  apiStatus.value = 'loading'
  apiStatusText.value = '加载中...'
  lastError.value = ''
  
  // 模拟API调用延迟
  setTimeout(() => {
    apiStatus.value = 'success'
    apiStatusText.value = '成功'
    lastLoadTime.value = new Date().toLocaleString('zh-CN')
  }, 1000)
}

const toggleMockData = () => {
  useMockData.value = !useMockData.value
  if (useMockData.value) {
    apiStatus.value = 'mock'
    apiStatusText.value = '使用模拟数据'
  } else {
    apiStatus.value = 'idle'
    apiStatusText.value = '空闲'
  }
}

const clearData = () => {
  apiStatus.value = 'idle'
  apiStatusText.value = '空闲'
  lastLoadTime.value = ''
  lastError.value = ''
}

onMounted(() => {
  // 页面加载完成后的初始化
  console.log('测试页面已加载')
})
</script>

<style lang="scss" scoped>
@import '../styles/variables.scss';

.test-dashboard-page {
  padding: $spacing-lg;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: $spacing-xl;
  
  h1 {
    color: $text-primary;
    margin-bottom: $spacing-sm;
  }
  
  p {
    color: $text-secondary;
    font-size: $font-size-md;
  }
}

.test-controls {
  background: $bg-section;
  border-radius: $radius-card;
  padding: $spacing-lg;
  margin-bottom: $spacing-lg;
  border: 1px solid $border-card;
  
  h3 {
    color: $text-primary;
    margin-bottom: $spacing-md;
  }
  
  .control-buttons {
    display: flex;
    gap: $spacing-md;
    flex-wrap: wrap;
  }
}

.dashboard-container {
  margin-bottom: $spacing-xl;
}

.data-status {
  background: $bg-section;
  border-radius: $radius-card;
  padding: $spacing-lg;
  border: 1px solid $border-card;
  
  h3 {
    color: $text-primary;
    margin-bottom: $spacing-md;
  }
  
  .status-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: $spacing-md;
  }
  
  .status-item {
    .status-label {
      color: $text-secondary;
      font-size: $font-size-sm;
      margin-bottom: $spacing-xs;
    }
    
    .status-value {
      font-weight: $font-weight-medium;
      padding: $spacing-sm;
      border-radius: $radius-button;
      text-align: center;
      
      &.idle {
        background: rgba($text-secondary, 0.1);
        color: $text-secondary;
      }
      
      &.loading {
        background: rgba($color-secondary, 0.1);
        color: $color-secondary;
      }
      
      &.success {
        background: rgba(#4caf50, 0.1);
        color: #4caf50;
      }
      
      &.error {
        background: rgba(#f44336, 0.1);
        color: #f44336;
      }
      
      &.mock {
        background: rgba(#ff9800, 0.1);
        color: #ff9800;
      }
    }
  }
}

.btn-primary {
  background: $color-secondary;
  color: white;
  border: none;
  padding: $spacing-sm $spacing-md;
  border-radius: $radius-button;
  cursor: pointer;
  font-size: $font-size-md;
  transition: all 0.2s;
  
  &:hover {
    background: color.adjust($color-secondary, $lightness: -10%);
  }
}

.btn-secondary {
  background: $bg-app;
  color: $text-primary;
  border: 1px solid $border-card;
  padding: $spacing-sm $spacing-md;
  border-radius: $radius-button;
  cursor: pointer;
  font-size: $font-size-md;
  transition: all 0.2s;
  
  &:hover {
    background: rgba($color-secondary, 0.1);
    border-color: $color-secondary;
  }
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .control-buttons {
    flex-direction: column;
  }
  
  .status-grid {
    grid-template-columns: 1fr;
  }
}
</style>
