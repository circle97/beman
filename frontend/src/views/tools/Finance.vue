<template>
  <div class="finance-page">
    <!-- 顶部统计卡片 -->
    <div class="stats-cards">
      <div class="stat-card income">
        <div class="stat-icon">💰</div>
        <div class="stat-content">
          <div class="stat-label">本月收入</div>
          <div class="stat-value">¥{{ stats.monthIncome || 0 }}</div>
        </div>
      </div>
      <div class="stat-card expense">
        <div class="stat-icon">💸</div>
        <div class="stat-content">
          <div class="stat-label">本月支出</div>
          <div class="stat-value">¥{{ stats.monthExpense || 0 }}</div>
        </div>
      </div>
      <div class="stat-card balance">
        <div class="stat-icon">📊</div>
        <div class="stat-content">
          <div class="stat-label">本月结余</div>
          <div class="stat-value">¥{{ stats.monthBalance || 0 }}</div>
        </div>
      </div>
    </div>

    <!-- 快速记账区域 -->
    <div class="quick-record-section">
      <h2>快速记账</h2>
      
      <!-- 语音记账 -->
      <div class="voice-record">
        <button 
          class="voice-btn" 
          :class="{ recording: isRecording }"
          @click="toggleVoiceRecord"
        >
          <svg viewBox="0 0 24 24" fill="currentColor" width="24" height="24">
            <path d="M12 14c1.66 0 3-1.34 3-3V5c0-1.66-1.34-3-3-3S9 3.34 9 5v6c0 1.66 1.34 3 3 3z"/>
            <path d="M17 11c0 2.76-2.24 5-5 5s-5-2.24-5-5H5c0 3.53 2.61 6.43 6 6.92V21h2v-3.08c3.39-.49 6-3.39 6-6.92h-2z"/>
          </svg>
          {{ isRecording ? '录音中...' : '语音记账' }}
        </button>
        <div v-if="voiceText" class="voice-result">
          <p>识别结果：{{ voiceText }}</p>
          <button class="btn-primary" @click="parseVoiceText">确认记账</button>
        </div>
      </div>

      <!-- 手动记账 -->
      <div class="manual-record">
        <button class="btn-primary" @click="showRecordModal = true">
          <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
            <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
          </svg>
          手动记账
        </button>
      </div>
    </div>

    <!-- 交易记录列表 -->
    <div class="transaction-list">
      <div class="list-header">
        <h2>交易记录</h2>
        <div class="list-actions">
          <select v-model="queryParams.type" @change="loadTransactions">
            <option value="">全部</option>
            <option value="1">收入</option>
            <option value="2">支出</option>
          </select>
        </div>
      </div>
      
      <div v-if="loading" class="loading">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>
      
      <div v-else-if="transactions.length === 0" class="empty-state">
        <svg viewBox="0 0 24 24" fill="currentColor" width="64" height="64">
          <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
        </svg>
        <h3>暂无交易记录</h3>
        <p>开始记录你的第一笔交易吧</p>
      </div>
      
      <div v-else class="transaction-items">
        <div 
          v-for="transaction in transactions" 
          :key="transaction.id"
          class="transaction-item"
          :class="transaction.type === 1 ? 'income' : 'expense'"
        >
          <div class="transaction-icon">
            {{ transaction.categoryIcon || '💰' }}
          </div>
          <div class="transaction-content">
            <div class="transaction-title">{{ transaction.description }}</div>
            <div class="transaction-category">{{ transaction.categoryName }}</div>
            <div class="transaction-time">{{ formatTime(transaction.transactionDate) }}</div>
          </div>
          <div class="transaction-amount">
            {{ transaction.type === 1 ? '+' : '-' }}¥{{ transaction.amount }}
          </div>
        </div>
      </div>
    </div>

    <!-- 记账模态框 -->
    <div v-if="showRecordModal" class="modal-overlay" @click="showRecordModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>记录交易</h3>
          <button class="close-btn" @click="showRecordModal = false">
            <svg viewBox="0 0 24 24" fill="currentColor" width="24" height="24">
              <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
            </svg>
          </button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>交易类型</label>
            <div class="type-selector">
              <button 
                class="type-btn" 
                :class="{ active: newTransaction.type === 1 }"
                @click="newTransaction.type = 1"
              >
                收入
              </button>
              <button 
                class="type-btn" 
                :class="{ active: newTransaction.type === 2 }"
                @click="newTransaction.type = 2"
              >
                支出
              </button>
            </div>
          </div>
          
          <div class="form-group">
            <label>金额</label>
            <input 
              v-model.number="newTransaction.amount" 
              type="number" 
              placeholder="请输入金额"
              step="0.01"
            />
          </div>
          
          <div class="form-group">
            <label>分类</label>
            <select v-model="newTransaction.categoryId">
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
            />
          </div>
          
          <div class="form-group">
            <label>交易日期</label>
            <input 
              v-model="newTransaction.transactionDate" 
              type="datetime-local"
            />
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-secondary" @click="showRecordModal = false">取消</button>
          <button 
            class="btn-primary" 
            @click="submitTransaction"
            :disabled="!isValidTransaction"
          >
            确认
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { 
  createTransaction, 
  getTransactionPage, 
  getFinanceStats, 
  getUserCategories,
  type Transaction,
  type TransactionCreateDTO,
  type TransactionQueryDTO,
  type FinanceStats,
  type Category
} from '../../api/finance'

// 响应式数据
const loading = ref(false)
const showRecordModal = ref(false)
const isRecording = ref(false)
const voiceText = ref('')
const transactions = ref<Transaction[]>([])
const categories = ref<Category[]>([])
const stats = ref<FinanceStats>({
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

// 查询参数
const queryParams = ref<TransactionQueryDTO>({
  page: 1,
  size: 20
})

// 新交易数据
const newTransaction = ref<TransactionCreateDTO>({
  type: 2,
  amount: 0,
  categoryId: 0,
  description: '',
  transactionDate: new Date().toISOString().slice(0, 16)
})

// 计算属性
const isValidTransaction = computed(() => {
  return newTransaction.value.amount > 0 && 
         newTransaction.value.categoryId > 0 && 
         newTransaction.value.description.trim()
})

// 方法
const loadStats = async () => {
  try {
    const data = await getFinanceStats()
    stats.value = data
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const loadTransactions = async () => {
  loading.value = true
  try {
    const pageResult = await getTransactionPage(queryParams.value)
    transactions.value = pageResult.records
  } catch (error) {
    console.error('加载交易记录失败:', error)
  } finally {
    loading.value = false
  }
}

const loadCategories = async () => {
  try {
    const incomeCategories = await getUserCategories(1)
    const expenseCategories = await getUserCategories(2)
    categories.value = [...incomeCategories, ...expenseCategories]
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

const toggleVoiceRecord = () => {
  if (isRecording.value) {
    stopVoiceRecord()
  } else {
    startVoiceRecord()
  }
}

const startVoiceRecord = () => {
  isRecording.value = true
  voiceText.value = ''
  // TODO: 实现语音录制功能
  // 这里先模拟语音识别结果
  setTimeout(() => {
    voiceText.value = '昨天买花200元'
    isRecording.value = false
  }, 2000)
}

const stopVoiceRecord = () => {
  isRecording.value = false
  // TODO: 停止语音录制
}

const parseVoiceText = () => {
  // TODO: 实现语音文本解析
  // 这里先简单处理
  const text = voiceText.value
  const amountMatch = text.match(/(\d+(?:\.\d+)?)/)
  if (amountMatch) {
    newTransaction.value.amount = parseFloat(amountMatch[1])
    newTransaction.value.description = text
    newTransaction.value.type = 2 // 默认为支出
    showRecordModal.value = true
  }
  voiceText.value = ''
}

const submitTransaction = async () => {
  try {
    await createTransaction(newTransaction.value)
    showRecordModal.value = false
    // 重置表单
    newTransaction.value = {
      type: 2,
      amount: 0,
      categoryId: 0,
      description: '',
      transactionDate: new Date().toISOString().slice(0, 16)
    }
    // 重新加载数据
    await loadStats()
    await loadTransactions()
  } catch (error) {
    console.error('提交交易失败:', error)
  }
}

const formatTime = (timeStr: string) => {
  const date = new Date(timeStr)
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString()
}

onMounted(() => {
  loadStats()
  loadTransactions()
  loadCategories()
})
</script>

<style lang="scss" scoped>
@use 'sass:color';
@import '../../styles/variables.scss';

.finance-page {
  padding: $spacing-md;
  max-width: 1200px;
  margin: 0 auto;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: $spacing-md;
  margin-bottom: $spacing-lg;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-lg;
  background: $bg-section;
  border-radius: $radius-card;
  border: 1px solid $border-card;
  
  &.income {
    border-left: 4px solid #4caf50;
  }
  
  &.expense {
    border-left: 4px solid #f44336;
  }
  
  &.balance {
    border-left: 4px solid #2196f3;
  }
  
  .stat-icon {
    font-size: 2rem;
  }
  
  .stat-content {
    .stat-label {
      font-size: $font-size-sm;
      color: $text-secondary;
      margin-bottom: $spacing-xs;
    }
    
    .stat-value {
      font-size: $font-size-xl;
      font-weight: $font-weight-bold;
      color: $text-primary;
    }
  }
}

.quick-record-section {
  margin-bottom: $spacing-lg;
  
  h2 {
    margin-bottom: $spacing-md;
    color: $text-primary;
  }
  
  .voice-record {
    margin-bottom: $spacing-md;
    
    .voice-btn {
      display: flex;
      align-items: center;
      gap: $spacing-sm;
      padding: $spacing-md $spacing-lg;
      background: $color-secondary;
      color: white;
      border: none;
      border-radius: $radius-button;
      cursor: pointer;
      transition: all 0.2s;
      
      &.recording {
        background: #f44336;
        animation: pulse 1s infinite;
      }
      
      &:hover {
        background: color.adjust($color-secondary, $lightness: -10%);
      }
    }
    
    .voice-result {
      margin-top: $spacing-md;
      padding: $spacing-md;
      background: rgba($color-secondary, 0.1);
      border-radius: $radius-card;
      
      p {
        margin-bottom: $spacing-sm;
        color: $text-primary;
      }
    }
  }
  
  .manual-record {
    .btn-primary {
      display: flex;
      align-items: center;
      gap: $spacing-sm;
    }
  }
}

.transaction-list {
  .list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $spacing-md;
    
    h2 {
      color: $text-primary;
      margin: 0;
    }
    
    select {
      padding: $spacing-sm;
      border: 1px solid $border-card;
      border-radius: $radius-button;
      background: $bg-app;
      color: $text-primary;
    }
  }
}

.transaction-items {
  display: flex;
  flex-direction: column;
  gap: $spacing-sm;
}

.transaction-item {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-md;
  background: $bg-section;
  border-radius: $radius-card;
  border: 1px solid $border-card;
  
  &.income {
    border-left: 4px solid #4caf50;
  }
  
  &.expense {
    border-left: 4px solid #f44336;
  }
  
  .transaction-icon {
    font-size: 1.5rem;
    width: 40px;
    text-align: center;
  }
  
  .transaction-content {
    flex: 1;
    
    .transaction-title {
      font-weight: $font-weight-medium;
      color: $text-primary;
      margin-bottom: $spacing-xs;
    }
    
    .transaction-category {
      font-size: $font-size-sm;
      color: $text-secondary;
      margin-bottom: $spacing-xs;
    }
    
    .transaction-time {
      font-size: $font-size-sm;
      color: $text-secondary;
    }
  }
  
  .transaction-amount {
    font-weight: $font-weight-bold;
    font-size: $font-size-lg;
    
    .income & {
      color: #4caf50;
    }
    
    .expense & {
      color: #f44336;
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
  background: $bg-section;
  border-radius: $radius-card;
  border: 1px solid $border-card;
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-lg;
  border-bottom: 1px solid $border-card;
  
  h3 {
    margin: 0;
    color: $text-primary;
  }
  
  .close-btn {
    background: none;
    border: none;
    color: $text-secondary;
    cursor: pointer;
    padding: $spacing-xs;
    border-radius: $radius-button;
    
    &:hover {
      background: rgba($color-secondary, 0.1);
      color: $color-secondary;
    }
  }
}

.modal-body {
  padding: $spacing-lg;
}

.form-group {
  margin-bottom: $spacing-md;
  
  label {
    display: block;
    margin-bottom: $spacing-xs;
    color: $text-primary;
    font-weight: $font-weight-medium;
  }
  
  input, select {
    width: 100%;
    padding: $spacing-sm;
    border: 1px solid $border-card;
    border-radius: $radius-button;
    background: $bg-app;
    color: $text-primary;
    font-size: $font-size-md;
    
    &:focus {
      outline: none;
      border-color: $color-secondary;
    }
  }
}

.type-selector {
  display: flex;
  gap: $spacing-sm;
  
  .type-btn {
    flex: 1;
    padding: $spacing-sm;
    border: 1px solid $border-card;
    background: $bg-app;
    color: $text-primary;
    border-radius: $radius-button;
    cursor: pointer;
    transition: all 0.2s;
    
    &.active {
      background: $color-secondary;
      color: white;
      border-color: $color-secondary;
    }
    
    &:hover {
      background: rgba($color-secondary, 0.1);
    }
  }
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: $spacing-sm;
  padding: $spacing-lg;
  border-top: 1px solid $border-card;
}

</style>
