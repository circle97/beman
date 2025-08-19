<template>
  <div class="shared-account-dashboard">
    <div class="dashboard-header">
      <h3>共同账户看板</h3>
      <div class="period-selector">
        <select v-model="selectedPeriod" @change="loadDashboardData">
          <option value="week">本周</option>
          <option value="month">本月</option>
          <option value="quarter">本季度</option>
          <option value="year">本年</option>
        </select>
      </div>
    </div>

    <!-- 消费占比图表 -->
    <div class="chart-section">
      <h4>消费占比分析</h4>
      <div class="chart-container">
        <div class="pie-chart">
          <div class="chart-legend">
            <div class="legend-item" v-for="item in expenseCategories" :key="item.name">
              <div class="legend-color" :style="{ backgroundColor: item.color }"></div>
              <span class="legend-label">{{ item.name }}</span>
              <span class="legend-value">¥{{ item.amount }}</span>
              <span class="legend-percentage">{{ item.percentage }}%</span>
            </div>
          </div>
          <div class="pie-visual">
            <div class="pie-slice" 
                 v-for="(item, index) in expenseCategories" 
                 :key="item.name"
                 :style="getPieSliceStyle(item, index)">
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 共同支出统计 -->
    <div class="shared-expense-section">
      <h4>共同支出统计</h4>
      <div class="stats-grid">
        <div class="stat-item">
          <div class="stat-number">¥{{ sharedStats.totalShared }}</div>
          <div class="stat-label">共同支出总额</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">¥{{ sharedStats.averagePerPerson }}</div>
          <div class="stat-label">人均支出</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ sharedStats.sharedCount }}</div>
          <div class="stat-label">共同支出笔数</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ sharedStats.participantCount }}</div>
          <div class="stat-label">参与人数</div>
        </div>
      </div>
    </div>

    <!-- 共同支出列表 -->
    <div class="shared-transactions">
      <h4>共同支出记录</h4>
      <div v-if="sharedTransactions.length === 0" class="empty-state">
        <p>暂无共同支出记录</p>
      </div>
      <div v-else class="transaction-list">
        <div v-for="transaction in sharedTransactions" 
             :key="transaction.id" 
             class="transaction-item">
          <div class="transaction-info">
            <div class="transaction-title">{{ transaction.description }}</div>
            <div class="transaction-meta">
              <span class="category">{{ transaction.categoryName }}</span>
              <span class="date">{{ formatDate(transaction.transactionDate) }}</span>
            </div>
          </div>
          <div class="transaction-amount">
            <span class="amount">¥{{ transaction.amount }}</span>
            <span class="shared-badge">共同</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 消费趋势 -->
    <div class="trend-section">
      <h4>消费趋势</h4>
      <div class="trend-chart">
        <div class="trend-line" v-for="(point, index) in trendData" :key="index">
          <div class="trend-point" :style="getTrendPointStyle(point, index)"></div>
          <div v-if="index < trendData.length - 1" class="trend-connector"></div>
        </div>
        <div class="trend-labels">
          <span v-for="label in trendLabels" :key="label" class="trend-label">{{ label }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'

interface ExpenseCategory {
  name: string
  amount: number
  percentage: number
  color: string
}

interface SharedStats {
  totalShared: number
  averagePerPerson: number
  sharedCount: number
  participantCount: number
}

interface SharedTransaction {
  id: number
  description: string
  amount: number
  categoryName: string
  transactionDate: string
}

// 响应式数据
const selectedPeriod = ref('month')
const expenseCategories = ref<ExpenseCategory[]>([])
const sharedStats = ref<SharedStats>({
  totalShared: 0,
  averagePerPerson: 0,
  sharedCount: 0,
  participantCount: 0
})
const sharedTransactions = ref<SharedTransaction[]>([])
const trendData = ref<number[]>([])

// 计算属性
const trendLabels = computed(() => {
  const labels = []
  const now = new Date()
  
  switch (selectedPeriod.value) {
    case 'week':
      for (let i = 6; i >= 0; i--) {
        const date = new Date(now)
        date.setDate(date.getDate() - i)
        labels.push(date.toLocaleDateString('zh-CN', { weekday: 'short' }))
      }
      break
    case 'month':
      for (let i = 3; i >= 0; i--) {
        const date = new Date(now)
        date.setDate(date.getDate() - i * 7)
        labels.push(`第${Math.ceil((now.getDate() - i * 7) / 7)}周`)
      }
      break
    case 'quarter':
      for (let i = 2; i >= 0; i--) {
        labels.push(`第${3 - i}个月`)
      }
      break
    case 'year':
      for (let i = 3; i >= 0; i--) {
        const month = now.getMonth() - i * 3
        labels.push(`${month > 0 ? month : month + 12}月`)
      }
      break
  }
  
  return labels
})

// 方法
const loadDashboardData = async () => {
  // 模拟加载数据
  await loadExpenseCategories()
  await loadSharedStats()
  await loadSharedTransactions()
  await loadTrendData()
}

const loadExpenseCategories = async () => {
  // 模拟数据
  const mockData = [
    { name: '餐饮', amount: 1200, percentage: 35, color: '#FF6B6B' },
    { name: '交通', amount: 800, percentage: 23, color: '#4ECDC4' },
    { name: '购物', amount: 600, percentage: 17, color: '#45B7D1' },
    { name: '娱乐', amount: 400, percentage: 12, color: '#96CEB4' },
    { name: '其他', amount: 500, percentage: 13, color: '#FFEAA7' }
  ]
  
  expenseCategories.value = mockData
}

const loadSharedStats = async () => {
  // 模拟数据
  sharedStats.value = {
    totalShared: 3500,
    averagePerPerson: 1750,
    sharedCount: 15,
    participantCount: 2
  }
}

const loadSharedTransactions = async () => {
  // 模拟数据
  sharedTransactions.value = [
    {
      id: 1,
      description: '周末聚餐',
      amount: 200,
      categoryName: '餐饮',
      transactionDate: '2024-01-15'
    },
    {
      id: 2,
      description: '电影票',
      amount: 120,
      categoryName: '娱乐',
      transactionDate: '2024-01-14'
    },
    {
      id: 3,
      description: '超市购物',
      amount: 300,
      categoryName: '购物',
      transactionDate: '2024-01-13'
    }
  ]
}

const loadTrendData = async () => {
  // 模拟趋势数据
  const baseAmount = 1000
  trendData.value = Array.from({ length: 4 }, () => 
    baseAmount + Math.random() * 500
  )
}

const getPieSliceStyle = (item: ExpenseCategory, index: number) => {
  const total = expenseCategories.value.reduce((sum, cat) => sum + cat.percentage, 0)
  let startAngle = 0
  
  for (let i = 0; i < index; i++) {
    startAngle += (expenseCategories.value[i].percentage / total) * 360
  }
  
  const angle = (item.percentage / total) * 360
  
  return {
    transform: `rotate(${startAngle}deg)`,
    background: `conic-gradient(${item.color} 0deg, ${item.color} ${angle}deg, transparent ${angle}deg)`
  }
}

const getTrendPointStyle = (value: number, index: number) => {
  const maxValue = Math.max(...trendData.value)
  const height = (value / maxValue) * 100
  
  return {
    height: `${height}%`,
    left: `${(index / (trendData.value.length - 1)) * 100}%`
  }
}

const formatDate = (dateStr: string) => {
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

onMounted(() => {
  loadDashboardData()
})
</script>

<style lang="scss" scoped>
@use 'sass:color';
@import '../styles/variables.scss';

.shared-account-dashboard {
  background: $bg-section;
  border-radius: $radius-card;
  padding: $spacing-lg;
  border: 1px solid $border-card;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $spacing-lg;
  
  h3 {
    margin: 0;
    color: $text-primary;
  }
  
  .period-selector select {
    padding: $spacing-xs $spacing-sm;
    border: 1px solid $border-card;
    border-radius: $radius-button;
    background: $bg-app;
    color: $text-primary;
  }
}

.chart-section {
  margin-bottom: $spacing-xl;
  
  h4 {
    color: $text-primary;
    margin-bottom: $spacing-md;
  }
}

.chart-container {
  display: flex;
  align-items: center;
  gap: $spacing-xl;
}

.pie-chart {
  display: flex;
  align-items: center;
  gap: $spacing-xl;
}

.pie-visual {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  position: relative;
  overflow: hidden;
}

.pie-slice {
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.chart-legend {
  .legend-item {
    display: flex;
    align-items: center;
    gap: $spacing-sm;
    margin-bottom: $spacing-xs;
    
    .legend-color {
      width: 16px;
      height: 16px;
      border-radius: 50%;
    }
    
    .legend-label {
      color: $text-primary;
      min-width: 60px;
    }
    
    .legend-value {
      color: $text-secondary;
      font-size: $font-size-sm;
      min-width: 60px;
    }
    
    .legend-percentage {
      color: $color-secondary;
      font-weight: $font-weight-medium;
      min-width: 40px;
    }
  }
}

.shared-expense-section {
  margin-bottom: $spacing-xl;
  
  h4 {
    color: $text-primary;
    margin-bottom: $spacing-md;
  }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: $spacing-md;
}

.stat-item {
  text-align: center;
  padding: $spacing-md;
  background: $bg-app;
  border-radius: $radius-card;
  border: 1px solid $border-card;
  
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

.shared-transactions {
  margin-bottom: $spacing-xl;
  
  h4 {
    color: $text-primary;
    margin-bottom: $spacing-md;
  }
}

.empty-state {
  text-align: center;
  padding: $spacing-lg;
  color: $text-secondary;
}

.transaction-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
}

.transaction-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-md;
  background: $bg-app;
  border-radius: $radius-card;
  border: 1px solid $border-card;
}

.transaction-info {
  .transaction-title {
    color: $text-primary;
    font-weight: $font-weight-medium;
    margin-bottom: $spacing-xs;
  }
  
  .transaction-meta {
    display: flex;
    gap: $spacing-md;
    font-size: $font-size-sm;
    color: $text-secondary;
  }
}

.transaction-amount {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  
  .amount {
    font-weight: $font-weight-bold;
    color: $text-primary;
  }
  
  .shared-badge {
    background: $color-secondary;
    color: white;
    padding: $spacing-xs $spacing-sm;
    border-radius: 12px;
    font-size: $font-size-xs;
  }
}

.trend-section {
  h4 {
    color: $text-primary;
    margin-bottom: $spacing-md;
  }
}

.trend-chart {
  position: relative;
  height: 200px;
  background: $bg-app;
  border-radius: $radius-card;
  border: 1px solid $border-card;
  padding: $spacing-md;
}

.trend-line {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.trend-point {
  position: absolute;
  width: 12px;
  height: 12px;
  background: $color-secondary;
  border-radius: 50%;
  transform: translateX(-50%);
  bottom: 0;
}

.trend-connector {
  position: absolute;
  width: 2px;
  height: 100%;
  background: $color-secondary;
  opacity: 0.3;
  left: 50%;
  transform: translateX(-50%);
}

.trend-labels {
  position: absolute;
  bottom: -30px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
}

.trend-label {
  font-size: $font-size-xs;
  color: $text-secondary;
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .chart-container {
    flex-direction: column;
    gap: $spacing-md;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .trend-labels {
    font-size: $font-size-xs;
  }
}
</style>
