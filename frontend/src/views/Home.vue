<template>
  <div class="home-page">
    <!-- 顶部欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <h1 class="welcome-title">欢迎回来，{{ displayName }}</h1>
        <p class="welcome-subtitle">今天也要努力经营关系哦</p>
      </div>
    </div>

    <!-- 快速操作区域 -->
    <div class="quick-actions">
      <h2 class="section-title">快速操作</h2>
      <div class="action-grid">
        <BemanCard 
          v-for="action in quickActions" 
          :key="action.id"
          class="action-card"
          clickable
          @click="handleActionClick(action)"
        >
          <div class="action-content">
            <div class="action-icon">
              <component :is="action.icon" />
            </div>
            <h3 class="action-title">{{ action.title }}</h3>
            <p class="action-desc">{{ action.description }}</p>
          </div>
        </BemanCard>
      </div>
    </div>

    <!-- 财务概览 -->
    <div class="finance-overview">
      <h2 class="section-title">财务概览</h2>
      <div class="finance-grid">
        <BemanCard class="finance-card">
          <div class="finance-item">
            <div class="finance-icon income">
              <svg viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
              </svg>
            </div>
            <div class="finance-content">
              <div class="finance-label">本月收入</div>
              <div class="finance-amount income">¥{{ formatAmount(financeStats.monthIncome) }}</div>
            </div>
          </div>
        </BemanCard>
        
        <BemanCard class="finance-card">
          <div class="finance-item">
            <div class="finance-icon expense">
              <svg viewBox="0 0 24 24" fill="currentColor">
                <path d="M19 13H5v-2h14v2z"/>
              </svg>
            </div>
            <div class="finance-content">
              <div class="finance-label">本月支出</div>
              <div class="finance-amount expense">¥{{ formatAmount(financeStats.monthExpense) }}</div>
            </div>
          </div>
        </BemanCard>
        
        <BemanCard class="finance-card">
          <div class="finance-item">
            <div class="finance-icon balance">
              <svg viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/>
              </svg>
            </div>
            <div class="finance-content">
              <div class="finance-label">本月结余</div>
              <div class="finance-amount" :class="financeStats.monthBalance >= 0 ? 'income' : 'expense'">
                ¥{{ formatAmount(financeStats.monthBalance) }}
              </div>
            </div>
          </div>
        </BemanCard>
      </div>
    </div>

    <!-- 快速记账 -->
    <div class="quick-accounting">
      <h2 class="section-title">快速记账</h2>
      <BemanCard>
        <div class="accounting-form">
          <div class="form-row">
            <div class="form-group">
              <label>类型</label>
              <div class="type-buttons">
                <button 
                  class="type-btn" 
                  :class="{ 'active': newTransaction.type === 1 }"
                  @click="newTransaction.type = 1"
                >
                  收入
                </button>
                <button 
                  class="type-btn" 
                  :class="{ 'active': newTransaction.type === 2 }"
                  @click="newTransaction.type = 2"
                >
                  支出
                </button>
              </div>
            </div>
            <div class="form-group">
              <label>金额</label>
              <input 
                v-model="newTransaction.amount" 
                type="number" 
                placeholder="0.00" 
                class="amount-input"
              />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>分类</label>
              <select v-model="newTransaction.categoryId" class="category-select">
                <option value="">请选择分类</option>
                <option 
                  v-for="category in categories" 
                  :key="category.id" 
                  :value="category.id"
                >
                  {{ category.name }}
                </option>
              </select>
            </div>
            <div class="form-group">
              <label>描述</label>
              <input 
                v-model="newTransaction.description" 
                type="text" 
                placeholder="请输入描述" 
                class="description-input"
              />
            </div>
          </div>
          <div class="form-actions">
            <button 
              class="btn-primary" 
              @click="submitTransaction"
              :disabled="!canSubmit"
            >
              记录
            </button>
          </div>
        </div>
      </BemanCard>
    </div>

    <!-- 今日任务 -->
    <div class="daily-tasks">
      <h2 class="section-title">今日任务</h2>
      <BemanCard>
        <div class="task-list">
          <div 
            v-for="task in dailyTasks" 
            :key="task.id"
            class="task-item"
            :class="{ 'task-item--completed': task.completed }"
          >
            <div class="task-checkbox" @click="toggleTask(task)">
              <div class="checkbox" :class="{ 'checkbox--checked': task.completed }">
                <svg v-if="task.completed" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/>
                </svg>
              </div>
            </div>
            <div class="task-content">
              <h4 class="task-title">{{ task.title }}</h4>
              <p class="task-desc">{{ task.description }}</p>
            </div>
          </div>
        </div>
      </BemanCard>
    </div>

    <!-- 成长进度 -->
    <div class="growth-progress">
      <h2 class="section-title">成长进度</h2>
      <BemanCard>
        <div class="progress-stats">
          <div class="progress-item">
            <div class="progress-number">{{ stats.completedTasks }}</div>
            <div class="progress-label">已完成任务</div>
          </div>
          <div class="progress-item">
            <div class="progress-number">{{ stats.achievements }}</div>
            <div class="progress-label">获得勋章</div>
          </div>
          <div class="progress-item">
            <div class="progress-number">{{ stats.streakDays }}</div>
            <div class="progress-label">连续天数</div>
          </div>
        </div>
      </BemanCard>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, h } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import BemanCard from '../components/BemanCard.vue'
import { getFinanceStats, getUserCategories, createTransaction, type FinanceStats, type Category, type TransactionCreateDTO } from '../api/finance'

const router = useRouter()
const userStore = useUserStore()

// 显示名称
const displayName = computed(() => {
  return userStore.displayName || '用户'
})

// 快速操作
const quickActions = ref([
  {
    id: 1,
    title: '记录心情',
    description: '记录今天的心情变化',
    icon: 'MoodIcon',
    path: '/tools/emotion'
  },
  {
    id: 2,
    title: '记账',
    description: '记录今天的支出',
    icon: 'FinanceIcon',
    path: '/tools/finance'
  },
  {
    id: 3,
    title: '查看日程',
    description: '查看重要日期提醒',
    icon: 'ScheduleIcon',
    path: '/tools/schedule'
  },
  {
    id: 4,
    title: '社区交流',
    description: '与其他用户交流经验',
    icon: 'CommunityIcon',
    path: '/community'
  }
])

// 今日任务
const dailyTasks = ref([
  {
    id: 1,
    title: '夸赞伴侣',
    description: '今天夸赞她的穿搭或工作',
    completed: false
  },
  {
    id: 2,
    title: '记录对话',
    description: '记录一次有意义的对话',
    completed: false
  },
  {
    id: 3,
    title: '制定计划',
    description: '制定一个关系改善计划',
    completed: false
  }
])

// 统计数据
const stats = ref({
  completedTasks: 12,
  achievements: 3,
  streakDays: 7
})

// 财务数据
const financeStats = ref<FinanceStats>({
  monthIncome: 0,
  monthExpense: 0,
  monthBalance: 0,
  yearIncome: 0,
  yearExpense: 0,
  yearBalance: 0,
  totalAssets: 0,
  totalLiabilities: 0,
  netWorth: 0,
  expenseCategoryStats: [],
  incomeCategoryStats: [],
  weeklyExpenseTrend: [],
  weeklyIncomeTrend: [],
  paymentMethodStats: []
})

// 分类列表
const categories = ref<Category[]>([])

// 新交易记录
const newTransaction = ref<TransactionCreateDTO>({
  type: 2, // 默认支出
  amount: 0,
  categoryId: 0,
  description: ''
})

// 是否可以提交
const canSubmit = computed(() => {
  return newTransaction.value.amount > 0 && 
         newTransaction.value.categoryId > 0 && 
         newTransaction.value.description.trim()
})

// 处理操作点击
const handleActionClick = (action: any) => {
  router.push(action.path)
}

// 切换任务状态
const toggleTask = (task: any) => {
  task.completed = !task.completed
  // 这里可以调用API更新任务状态
}

// 格式化金额
const formatAmount = (amount: number) => {
  return amount.toFixed(2)
}

// 加载财务数据
const loadFinanceData = async () => {
  try {
    const stats = await getFinanceStats()
    financeStats.value = stats
  } catch (error) {
    console.error('加载财务数据失败:', error)
  }
}

// 加载分类数据
const loadCategories = async () => {
  try {
    const categoryList = await getUserCategories(2) // 默认加载支出分类
    categories.value = categoryList
  } catch (error) {
    console.error('加载分类数据失败:', error)
  }
}

// 提交交易记录
const submitTransaction = async () => {
  try {
    await createTransaction(newTransaction.value)
    // 重置表单
    newTransaction.value = {
      type: 2,
      amount: 0,
      categoryId: 0,
      description: ''
    }
    // 重新加载财务数据
    await loadFinanceData()
    alert('记录成功！')
  } catch (error) {
    console.error('记录失败:', error)
    alert('记录失败，请重试')
  }
}

// 图标组件
const MoodIcon = () => h('svg', { viewBox: '0 0 24 24', fill: 'currentColor' }, [
  h('path', { d: 'M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm3.5-9c.83 0 1.5-.67 1.5-1.5S16.33 8 15.5 8 14 8.67 14 9.5s.67 1.5 1.5 1.5zm-7 0c.83 0 1.5-.67 1.5-1.5S9.33 8 8.5 8 7 8.67 7 9.5 7.67 11 8.5 11zm3.5 6.5c2.33 0 4.31-1.46 5.11-3.5H6.89c.8 2.04 2.78 3.5 5.11 3.5z' })
])

const FinanceIcon = () => h('svg', { viewBox: '0 0 24 24', fill: 'currentColor' }, [
  h('path', { d: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z' })
])

const ScheduleIcon = () => h('svg', { viewBox: '0 0 24 24', fill: 'currentColor' }, [
  h('path', { d: 'M19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V8h14v11zM7 10h5v5H7z' })
])

const CommunityIcon = () => h('svg', { viewBox: '0 0 24 24', fill: 'currentColor' }, [
  h('path', { d: 'M16 4c0-1.11.89-2 2-2s2 .89 2 2-.89 2-2 2-2-.89-2-2zm4 18v-6h2.5l-2.54-7.63A1.5 1.5 0 0 0 18.54 8H17c-.8 0-1.54.37-2.01 1l-1.7 2.26V15h-1.5v6H20zM12.5 11.5c.83 0 1.5-.67 1.5-1.5s-.67-1.5-1.5-1.5S11 9.17 11 10s.67 1.5 1.5 1.5zM5.5 6c1.11 0 2-.89 2-2s-.89-2-2-2-2 .89-2 2 .89 2 2 2zm2 16v-7H9V9c0-1.1-.9-2-2-2H4c-1.1 0-2 .9-2 2v6h1.5v7h4z' })
])

onMounted(() => {
  // 页面加载时的初始化逻辑
  loadFinanceData()
  loadCategories()
})
</script>

<style lang="scss" scoped>
@import '../styles/variables.scss';

.home-page {
  padding: $spacing-md;
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-section {
  margin-bottom: $spacing-xl;
}

.welcome-content {
  text-align: center;
}

.welcome-title {
  font-size: $font-size-xxl;
  font-weight: $font-weight-bold;
  color: $text-primary;
  margin-bottom: $spacing-sm;
}

.welcome-subtitle {
  font-size: $font-size-md;
  color: $text-secondary;
  margin: 0;
}

.section-title {
  font-size: $font-size-xl;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin-bottom: $spacing-md;
}

.quick-actions {
  margin-bottom: $spacing-xl;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: $spacing-md;
}

.action-card {
  .action-content {
    text-align: center;
    padding: $spacing-md;
  }
}

.action-icon {
  width: 48px;
  height: 48px;
  margin: 0 auto $spacing-md;
  color: $color-secondary;
  
  svg {
    width: 100%;
    height: 100%;
  }
}

.action-title {
  font-size: $font-size-lg;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin-bottom: $spacing-xs;
}

.action-desc {
  font-size: $font-size-sm;
  color: $text-secondary;
  margin: 0;
}

// 财务概览样式
.finance-overview {
  margin-bottom: $spacing-xl;
}

.finance-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: $spacing-md;
}

.finance-card {
  .finance-item {
    display: flex;
    align-items: center;
    gap: $spacing-md;
  }
}

.finance-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  
  &.income {
    background: #4caf50;
  }
  
  &.expense {
    background: #f44336;
  }
  
  &.balance {
    background: #2196f3;
  }
  
  svg {
    width: 24px;
    height: 24px;
  }
}

.finance-content {
  flex: 1;
}

.finance-label {
  font-size: $font-size-sm;
  color: $text-secondary;
  margin-bottom: $spacing-xs;
}

.finance-amount {
  font-size: $font-size-xl;
  font-weight: $font-weight-bold;
  
  &.income {
    color: #4caf50;
  }
  
  &.expense {
    color: #f44336;
  }
}

// 快速记账样式
.quick-accounting {
  margin-bottom: $spacing-xl;
}

.accounting-form {
  .form-row {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: $spacing-md;
    margin-bottom: $spacing-md;
    
    @media (max-width: $breakpoint-sm) {
      grid-template-columns: 1fr;
    }
  }
}

.form-group {
  label {
    display: block;
    font-size: $font-size-sm;
    font-weight: $font-weight-medium;
    color: $text-primary;
    margin-bottom: $spacing-xs;
  }
}

.type-buttons {
  display: flex;
  gap: $spacing-xs;
}

.type-btn {
  flex: 1;
  padding: $spacing-sm;
  border: 1px solid $border-card;
  border-radius: $radius-button;
  background: $bg-section;
  color: $text-secondary;
  cursor: pointer;
  transition: all 0.2s;
  
  &.active {
    background: $color-secondary;
    color: white;
    border-color: $color-secondary;
  }
}

.amount-input,
.category-select,
.description-input {
  width: 100%;
  padding: $spacing-sm;
  border: 1px solid $border-card;
  border-radius: $radius-button;
  background: $bg-section;
  color: $text-primary;
  font-size: $font-size-md;
  
  &:focus {
    outline: none;
    border-color: $color-secondary;
  }
}

.form-actions {
  text-align: center;
  margin-top: $spacing-lg;
}

.daily-tasks {
  margin-bottom: $spacing-xl;
}

.task-list {
  .task-item {
    display: flex;
    align-items: flex-start;
    padding: $spacing-md 0;
    border-bottom: 1px solid $border-card;
    
    &:last-child {
      border-bottom: none;
    }
    
    &--completed {
      opacity: 0.6;
      
      .task-title {
        text-decoration: line-through;
      }
    }
  }
}

.task-checkbox {
  margin-right: $spacing-md;
  cursor: pointer;
}

.checkbox {
  width: 20px;
  height: 20px;
  border: 2px solid $border-card;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  
  &--checked {
    background: $color-secondary;
    border-color: $color-secondary;
    color: white;
  }
  
  svg {
    width: 14px;
    height: 14px;
  }
}

.task-content {
  flex: 1;
}

.task-title {
  font-size: $font-size-md;
  font-weight: $font-weight-medium;
  color: $text-primary;
  margin-bottom: $spacing-xs;
}

.task-desc {
  font-size: $font-size-sm;
  color: $text-secondary;
  margin: 0;
}

.growth-progress {
  margin-bottom: $spacing-xl;
}

.progress-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: $spacing-lg;
  text-align: center;
}

.progress-item {
  .progress-number {
    font-size: 2rem;
    font-weight: $font-weight-bold;
    color: $color-secondary;
    margin-bottom: $spacing-xs;
  }
  
  .progress-label {
    font-size: $font-size-sm;
    color: $text-secondary;
  }
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .home-page {
    padding: $spacing-sm;
  }
  
  .action-grid {
    grid-template-columns: 1fr;
  }
  
  .finance-grid {
    grid-template-columns: 1fr;
  }
  
  .progress-stats {
    grid-template-columns: 1fr;
    gap: $spacing-md;
  }
}
</style> 