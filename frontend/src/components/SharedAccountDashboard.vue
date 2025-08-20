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

    <!-- 财务概览 -->
    <div class="overview-section">
      <h4>财务概览</h4>
      <div class="overview-grid">
        <div class="overview-item income">
          <div class="overview-icon">💰</div>
          <div class="overview-content">
            <div class="overview-label">本月收入</div>
            <div class="overview-value">¥{{ formatAmount(financeStats.monthIncome) }}</div>
          </div>
        </div>
        <div class="overview-item expense">
          <div class="overview-icon">💸</div>
          <div class="overview-content">
            <div class="overview-label">本月支出</div>
            <div class="overview-value">¥{{ formatAmount(financeStats.monthExpense) }}</div>
          </div>
        </div>
        <div class="overview-item balance">
          <div class="overview-icon">⚖️</div>
          <div class="overview-content">
            <div class="overview-label">本月结余</div>
            <div class="overview-value" :class="{ negative: financeStats.monthBalance < 0 }">
              ¥{{ formatAmount(financeStats.monthBalance) }}
            </div>
          </div>
        </div>
        <div class="overview-item year">
          <div class="overview-icon">📊</div>
          <div class="overview-content">
            <div class="overview-label">本年结余</div>
            <div class="overview-value" :class="{ negative: financeStats.yearBalance < 0 }">
              ¥{{ formatAmount(financeStats.yearBalance) }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 消费占比图表 -->
    <div class="chart-section">
      <h4>消费分类占比</h4>
      <div class="chart-container">
        <div class="pie-chart">
          <div class="chart-legend">
            <div class="legend-item" v-for="item in expenseCategories" :key="item.categoryId">
              <div class="legend-color" :style="{ backgroundColor: item.categoryColor || getDefaultColor(item.categoryId) }"></div>
              <span class="legend-label">{{ item.categoryName }}</span>
              <span class="legend-value">¥{{ formatAmount(item.amount) }}</span>
              <span class="legend-percentage">{{ item.percentage.toFixed(1) }}%</span>
            </div>
          </div>
          <div class="pie-visual">
            <div class="pie-slice" 
                 v-for="(item, index) in expenseCategories" 
                 :key="item.categoryId"
                 :style="getPieSliceStyle(item, index)">
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 支付方式统计 -->
    <div class="payment-method-section" v-if="financeStats.paymentMethodStats && financeStats.paymentMethodStats.length > 0">
      <h4>支付方式统计</h4>
      <div class="payment-stats">
        <div class="payment-item" v-for="item in financeStats.paymentMethodStats" :key="item.paymentMethod">
          <div class="payment-info">
            <div class="payment-name">{{ item.paymentMethodName }}</div>
            <div class="payment-percentage">{{ item.percentage.toFixed(1) }}%</div>
          </div>
          <div class="payment-bar">
            <div class="payment-fill" :style="{ width: item.percentage + '%' }"></div>
          </div>
          <div class="payment-amount">¥{{ formatAmount(item.amount) }}</div>
        </div>
      </div>
    </div>

    <!-- 消费趋势 -->
    <div class="trend-section" v-if="financeStats.weeklyExpenseTrend && financeStats.weeklyExpenseTrend.length > 0">
      <h4>最近7天支出趋势</h4>
      <div class="trend-chart">
        <div class="trend-line">
          <div class="trend-point" 
               v-for="(point, index) in financeStats.weeklyExpenseTrend" 
               :key="index"
               :style="getTrendPointStyle(point.amount, index)">
          </div>
          <svg class="trend-svg" viewBox="0 0 100 100" preserveAspectRatio="none">
            <polyline 
              :points="getTrendLinePoints()" 
              fill="none" 
              stroke="var(--color-secondary)" 
              stroke-width="2"
              opacity="0.6">
            </polyline>
          </svg>
        </div>
        <div class="trend-labels">
          <span v-for="(point, index) in financeStats.weeklyExpenseTrend" 
                :key="index" 
                class="trend-label">
            {{ formatTrendDate(point.date) }}
          </span>
        </div>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>

    <!-- 错误状态 -->
    <div v-if="error" class="error-state">
      <p>{{ error }}</p>
      <button @click="loadDashboardData" class="retry-button">重试</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { getFinanceStats, FinanceStats } from '../api/finance'

// 响应式数据
const selectedPeriod = ref('month')
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
const loading = ref(false)
const error = ref('')

// 计算属性
const expenseCategories = computed(() => {
  return financeStats.value.expenseCategoryStats || []
})

// 方法
const loadDashboardData = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const stats = await getFinanceStats()
    financeStats.value = stats
  } catch (err: any) {
    error.value = err.message || '加载数据失败'
    console.error('Failed to load finance stats:', err)
  } finally {
    loading.value = false
  }
}

const getPieSliceStyle = (item: any, index: number) => {
  if (expenseCategories.value.length === 0) return {}
  
  const total = expenseCategories.value.reduce((sum, cat) => sum + cat.percentage, 0)
  let startAngle = 0
  
  for (let i = 0; i < index; i++) {
    startAngle += (expenseCategories.value[i].percentage / total) * 360
  }
  
  const angle = (item.percentage / total) * 360
  
  return {
    transform: `rotate(${startAngle}deg)`,
    background: `conic-gradient(${item.categoryColor || getDefaultColor(item.categoryId)} 0deg, ${item.categoryColor || getDefaultColor(item.categoryId)} ${angle}deg, transparent ${angle}deg)`
  }
}

const getDefaultColor = (categoryId: number) => {
  const colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7', '#DDA0DD', '#98D8C8', '#F7DC6F']
  return colors[categoryId % colors.length]
}

const getTrendPointStyle = (amount: number, index: number) => {
  if (financeStats.value.weeklyExpenseTrend.length === 0) return {}
  
  const maxAmount = Math.max(...financeStats.value.weeklyExpenseTrend.map(p => p.amount))
  const height = maxAmount > 0 ? (amount / maxAmount) * 100 : 0
  
  return {
    height: `${height}%`,
    left: `${(index / (financeStats.value.weeklyExpenseTrend.length - 1)) * 100}%`
  }
}

const getTrendLinePoints = () => {
  if (financeStats.value.weeklyExpenseTrend.length === 0) return ''
  
  const maxAmount = Math.max(...financeStats.value.weeklyExpenseTrend.map(p => p.amount))
  
  return financeStats.value.weeklyExpenseTrend.map((point, index) => {
    const x = (index / (financeStats.value.weeklyExpenseTrend.length - 1)) * 100
    const y = maxAmount > 0 ? 100 - (point.amount / maxAmount) * 100 : 100
    return `${x},${y}`
  }).join(' ')
}

const formatAmount = (amount: number) => {
  return amount.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const formatTrendDate = (dateStr: string) => {
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' })
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

.overview-section {
  margin-bottom: $spacing-xl;
  
  h4 {
    color: $text-primary;
    margin-bottom: $spacing-md;
  }
}

.overview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: $spacing-md;
}

.overview-item {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-md;
  background: $bg-app;
  border-radius: $radius-card;
  border: 1px solid $border-card;
  
  .overview-icon {
    font-size: 2rem;
  }
  
  .overview-content {
    flex: 1;
    
    .overview-label {
      color: $text-secondary;
      font-size: $font-size-sm;
      margin-bottom: $spacing-xs;
    }
    
    .overview-value {
      font-size: $font-size-lg;
      font-weight: $font-weight-bold;
      color: $text-primary;
      
      &.negative {
        color: #ff4757;
      }
    }
  }
  
  &.income .overview-value {
    color: #2ed573;
  }
  
  &.expense .overview-value {
    color: #ff4757;
  }
  
  &.balance .overview-value {
    color: #3742fa;
  }
  
  &.year .overview-value {
    color: #ffa502;
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

.payment-method-section {
  margin-bottom: $spacing-xl;
  
  h4 {
    color: $text-primary;
    margin-bottom: $spacing-md;
  }
}

.payment-stats {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
}

.payment-item {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-sm;
  background: $bg-app;
  border-radius: $radius-card;
  border: 1px solid $border-card;
  
  .payment-info {
    display: flex;
    flex-direction: column;
    min-width: 120px;
    
    .payment-name {
      color: $text-primary;
      font-weight: $font-weight-medium;
    }
    
    .payment-percentage {
      color: $color-secondary;
      font-size: $font-size-sm;
    }
  }
  
  .payment-bar {
    flex: 1;
    height: 8px;
    background: $border-card;
    border-radius: 4px;
    overflow: hidden;
    
    .payment-fill {
      height: 100%;
      background: $color-secondary;
      border-radius: 4px;
      transition: width 0.3s ease;
    }
  }
  
  .payment-amount {
    color: $text-primary;
    font-weight: $font-weight-medium;
    min-width: 80px;
    text-align: right;
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
  position: relative;
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
  z-index: 2;
}

.trend-svg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
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

.loading-state, .error-state {
  text-align: center;
  padding: $spacing-xl;
  color: $text-secondary;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid $border-card;
  border-top: 4px solid $color-secondary;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto $spacing-md;
}

.retry-button {
  background: $color-secondary;
  color: white;
  border: none;
  padding: $spacing-sm $spacing-md;
  border-radius: $radius-button;
  cursor: pointer;
  margin-top: $spacing-sm;
  
  &:hover {
    opacity: 0.9;
  }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .chart-container {
    flex-direction: column;
    gap: $spacing-md;
  }
  
  .overview-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .trend-labels {
    font-size: $font-size-xs;
  }
}
</style>
