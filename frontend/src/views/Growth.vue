<template>
  <div class="growth-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>成长档案</h1>
      <p class="subtitle">记录你的成长轨迹，见证关系改善的每一步</p>
    </div>

    <!-- 统计概览 -->
    <div class="stats-overview" v-if="statistics">
      <div class="stat-card">
        <div class="stat-number">{{ statistics.totalPlans }}</div>
        <div class="stat-label">总计划数</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ statistics.completedPlans }}</div>
        <div class="stat-label">已完成</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ statistics.inProgressPlans }}</div>
        <div class="stat-label">进行中</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ Math.round(statistics.completionRate) }}%</div>
        <div class="stat-label">完成率</div>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="actions-section">
      <button class="btn-primary" @click="showCreateModal = true">
        <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
          <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
        </svg>
        创建新计划
      </button>
      <button class="btn-secondary" @click="refreshData">
        <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
          <path d="M17.65 6.35C16.2 4.9 14.21 4 12 4c-4.42 0-7.99 3.58-7.99 8s3.57 8 7.99 8c3.73 0 6.84-2.55 7.73-6h-2.08c-.82 2.33-3.04 4-5.65 4-3.31 0-6-2.69-6-6s2.69-6 6-6c1.66 0 3.14.69 4.22 1.78L13 11h7V4l-2.35 2.35z"/>
        </svg>
        刷新
      </button>
    </div>

    <!-- 计划列表 -->
    <div class="plans-section">
      <h2>我的实践计划</h2>
      
      <div v-if="loading" class="loading">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>
      
      <div v-else-if="plans.length === 0" class="empty-state">
        <svg viewBox="0 0 24 24" fill="currentColor" width="64" height="64">
          <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/>
        </svg>
        <h3>暂无实践计划</h3>
        <p>开始创建你的第一个实践计划，记录关系改善的每一步</p>
        <button class="btn-primary" @click="showCreateModal = true">创建第一个计划</button>
      </div>
      
      <div v-else class="plans-grid">
        <div 
          v-for="plan in plans" 
          :key="plan.id"
          class="plan-card"
          :class="getPlanStatusClass(plan.status)"
        >
          <div class="plan-header">
            <div class="plan-type">{{ getPlanTypeText(plan.type) }}</div>
            <div class="plan-status">{{ getPlanStatusText(plan.status) }}</div>
          </div>
          
          <h3 class="plan-title">{{ plan.title }}</h3>
          <p class="plan-description">{{ plan.description }}</p>
          
          <div class="plan-progress">
            <div class="progress-bar">
              <div 
                class="progress-fill" 
                :style="{ width: plan.progress + '%' }"
              ></div>
            </div>
            <span class="progress-text">{{ plan.progress }}%</span>
          </div>
          
          <div class="plan-meta">
            <div class="meta-item">
              <span class="label">优先级:</span>
              <span class="value" :class="getPriorityClass(plan.priority)">
                {{ getPlanPriorityText(plan.priority) }}
              </span>
            </div>
            <div class="meta-item">
              <span class="label">创建时间:</span>
              <span class="value">{{ formatDate(plan.createTime) }}</span>
            </div>
            <div v-if="plan.tags" class="meta-item">
              <span class="label">标签:</span>
              <span class="value">{{ plan.tags }}</span>
            </div>
          </div>
          
          <div class="plan-actions">
            <button 
              v-if="plan.status === 0" 
              class="btn-start"
              @click="startPlan(plan.id!)"
            >
              开始执行
            </button>
            <button 
              v-if="plan.status === 1" 
              class="btn-update"
              @click="openUpdateModal(plan)"
            >
              更新进度
            </button>
            <button 
              v-if="plan.status === 1" 
              class="btn-complete"
              @click="completePlan(plan.id!)"
            >
              标记完成
            </button>
            <button 
              class="btn-edit"
              @click="openUpdateModal(plan)"
            >
              编辑
            </button>
            <button 
              class="btn-delete"
              @click="deletePlan(plan.id!)"
            >
              删除
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 创建计划模态框 -->
    <div v-if="showCreateModal" class="modal-overlay" @click="showCreateModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>创建实践计划</h3>
          <button class="close-btn" @click="showCreateModal = false">
            <svg viewBox="0 0 24 24" fill="currentColor" width="24" height="24">
              <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
            </svg>
          </button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>计划标题 *</label>
            <input v-model="newPlan.title" type="text" placeholder="请输入计划标题" />
          </div>
          <div class="form-group">
            <label>计划描述 *</label>
            <textarea 
              v-model="newPlan.description" 
              placeholder="请描述你的计划内容..." 
              rows="4"
            ></textarea>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>计划类型 *</label>
              <select v-model="newPlan.type">
                <option value="1">沟通改善</option>
                <option value="2">情绪管理</option>
                <option value="3">关系维护</option>
                <option value="4">个人成长</option>
              </select>
            </div>
            <div class="form-group">
              <label>优先级</label>
              <select v-model="newPlan.priority">
                <option value="1">低</option>
                <option value="2">中</option>
                <option value="3">高</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label>标签</label>
            <input v-model="newPlan.tags" type="text" placeholder="用逗号分隔多个标签" />
          </div>
          <div class="form-group">
            <label>备注</label>
            <textarea v-model="newPlan.remark" placeholder="其他备注信息..." rows="2"></textarea>
          </div>
          <div class="form-group">
            <label class="checkbox-label">
              <input v-model="newPlan.isPublic" type="checkbox" :true-value="1" :false-value="0" />
              <span>公开分享（其他用户可以看到）</span>
            </label>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-secondary" @click="showCreateModal = false">取消</button>
          <button class="btn-primary" @click="createPlan" :disabled="!newPlan.title || !newPlan.description">
            创建计划
          </button>
        </div>
      </div>
    </div>

    <!-- 更新计划模态框 -->
    <div v-if="showUpdateModal && editingPlan" class="modal-overlay" @click="showUpdateModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>更新实践计划</h3>
          <button class="close-btn" @click="showUpdateModal = false">
            <svg viewBox="0 0 24 24" fill="currentColor" width="24" height="24">
              <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
            </svg>
          </button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>计划标题 *</label>
            <input v-model="editingPlan.title" type="text" placeholder="请输入计划标题" />
          </div>
          <div class="form-group">
            <label>计划描述 *</label>
            <textarea 
              v-model="editingPlan.description" 
              placeholder="请描述你的计划内容..." 
              rows="4"
            ></textarea>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>计划类型 *</label>
              <select v-model="editingPlan.type">
                <option value="1">沟通改善</option>
                <option value="2">情绪管理</option>
                <option value="3">关系维护</option>
                <option value="4">个人成长</option>
              </select>
            </div>
            <div class="form-group">
              <label>优先级</label>
              <select v-model="editingPlan.priority">
                <option value="1">低</option>
                <option value="2">中</option>
                <option value="3">高</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label>当前进度</label>
            <div class="progress-input">
              <input 
                v-model.number="editingPlan.progress" 
                type="range" 
                min="0" 
                max="100" 
                step="5"
              />
              <span class="progress-value">{{ editingPlan.progress }}%</span>
            </div>
          </div>
          <div class="form-group">
            <label>计划状态</label>
            <select v-model="editingPlan.status">
              <option value="0">未开始</option>
              <option value="1">进行中</option>
              <option value="2">已完成</option>
              <option value="3">已放弃</option>
            </select>
          </div>
          <div class="form-group">
            <label>标签</label>
            <input v-model="editingPlan.tags" type="text" placeholder="用逗号分隔多个标签" />
          </div>
          <div class="form-group">
            <label>备注</label>
            <textarea v-model="editingPlan.remark" placeholder="其他备注信息..." rows="2"></textarea>
          </div>
          <div class="form-group">
            <label class="checkbox-label">
              <input v-model="editingPlan.isPublic" type="checkbox" :true-value="1" :false-value="0" />
              <span>公开分享（其他用户可以看到）</span>
            </label>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-secondary" @click="showUpdateModal = false">取消</button>
          <button class="btn-primary" @click="updatePlan" :disabled="!editingPlan.title || !editingPlan.description">
            保存更新
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { 
  practicePlanAPI, 
  type PracticePlan, 
  type PlanStatistics,
  PLAN_STATUS,
  getPlanTypeText,
  getPlanStatusText,
  getPlanPriorityText
} from '../api/practicePlan'

// 响应式数据
const loading = ref(false)
const plans = ref<PracticePlan[]>([])
const statistics = ref<PlanStatistics | null>(null)
const showCreateModal = ref(false)
const editingPlan = ref<PracticePlan | null>(null)

// 新计划数据
const newPlan = ref<PracticePlan>({
  userId: 1, // 临时用户ID，实际应该从用户状态获取
  title: '',
  description: '',
  type: 1,
  status: 0,
  priority: 2,
  progress: 0,
  isPublic: 0
})

// 方法
const loadData = async () => {
  loading.value = true
  try {
    // 加载用户计划列表
    const plansResult = await practicePlanAPI.getUserPlans(1) // 临时用户ID
    if (plansResult.success) {
      plans.value = plansResult.data
    }
    
    // 加载统计信息
    const statsResult = await practicePlanAPI.getPlanStatistics(1) // 临时用户ID
    if (statsResult.success) {
      statistics.value = statsResult.data
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const refreshData = () => {
  loadData()
}

const createPlan = async () => {
  try {
    const result = await practicePlanAPI.createPlan(newPlan.value)
    if (result.success) {
      showCreateModal.value = false
      // 重置表单
      newPlan.value = {
        userId: 1,
        title: '',
        description: '',
        type: 1,
        status: 0,
        priority: 2,
        progress: 0,
        isPublic: 0
      }
      // 重新加载数据
      await loadData()
    }
  } catch (error) {
    console.error('创建计划失败:', error)
  }
}

const showUpdateModal = ref(false)

const openUpdateModal = (plan: PracticePlan) => {
  editingPlan.value = { ...plan }
  showUpdateModal.value = true
}

const updatePlan = async () => {
  if (!editingPlan.value) return
  
  try {
    const result = await practicePlanAPI.updatePlan(editingPlan.value)
    if (result.success) {
      showUpdateModal.value = false
      editingPlan.value = null
      // 重新加载数据
      await loadData()
    }
  } catch (error) {
    console.error('更新计划失败:', error)
  }
}

const startPlan = async (planId: number) => {
  try {
    await practicePlanAPI.updateStatus(planId, PLAN_STATUS.IN_PROGRESS)
    await loadData()
  } catch (error) {
    console.error('开始计划失败:', error)
  }
}

const completePlan = async (planId: number) => {
  try {
    await practicePlanAPI.updateStatus(planId, PLAN_STATUS.COMPLETED)
    await practicePlanAPI.updateProgress(planId, 100)
    await loadData()
  } catch (error) {
    console.error('完成计划失败:', error)
  }
}

const deletePlan = async (planId: number) => {
  if (!confirm('确定要删除这个计划吗？')) return
  
  try {
    const result = await practicePlanAPI.deletePlan(planId)
    if (result.success) {
      await loadData()
    }
  } catch (error) {
    console.error('删除计划失败:', error)
  }
}

const getPlanStatusClass = (status: number) => {
  switch (status) {
    case PLAN_STATUS.NOT_STARTED: return 'status-not-started'
    case PLAN_STATUS.IN_PROGRESS: return 'status-in-progress'
    case PLAN_STATUS.COMPLETED: return 'status-completed'
    case PLAN_STATUS.ABANDONED: return 'status-abandoned'
    default: return ''
  }
}

const getPriorityClass = (priority: number) => {
  switch (priority) {
    case 1: return 'priority-low'
    case 2: return 'priority-medium'
    case 3: return 'priority-high'
    default: return ''
  }
}

const formatDate = (dateStr: string | undefined) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
@use 'sass:color';
@import '../styles/variables.scss';

.growth-page {
  padding: $spacing-lg;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: $spacing-xl;
  
  h1 {
    font-size: $font-size-xxl;
    font-weight: $font-weight-bold;
    color: $text-primary;
    margin-bottom: $spacing-sm;
  }
  
  .subtitle {
    color: $text-secondary;
    font-size: $font-size-lg;
  }
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: $spacing-md;
  margin-bottom: $spacing-xl;
  
  .stat-card {
    background: $bg-section;
    border: 1px solid $border-card;
    border-radius: $radius-card;
    padding: $spacing-lg;
    text-align: center;
    
    .stat-number {
      font-size: $font-size-xxl;
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

.actions-section {
  display: flex;
  gap: $spacing-md;
  margin-bottom: $spacing-xl;
  justify-content: center;
}

.plans-section {
  h2 {
    font-size: $font-size-xl;
    font-weight: $font-weight-semibold;
    color: $text-primary;
    margin-bottom: $spacing-lg;
    text-align: center;
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

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
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
  
  p {
    margin-bottom: $spacing-lg;
  }
}

.plans-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: $spacing-lg;
}

.plan-card {
  background: $bg-section;
  border: 1px solid $border-card;
  border-radius: $radius-card;
  padding: $spacing-lg;
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
  
  &.status-completed {
    border-left: 4px solid $color-success;
  }
  
  &.status-in-progress {
    border-left: 4px solid $color-secondary;
  }
  
  &.status-not-started {
    border-left: 4px solid $color-info;
  }
  
  &.status-abandoned {
    border-left: 4px solid $color-warning;
  }
}

.plan-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $spacing-md;
  
  .plan-type {
    background: rgba($color-secondary, 0.1);
    color: $color-secondary;
    padding: $spacing-xs $spacing-sm;
    border-radius: 12px;
    font-size: $font-size-sm;
    font-weight: $font-weight-medium;
  }
  
  .plan-status {
    font-size: $font-size-sm;
    font-weight: $font-weight-medium;
  }
}

.plan-title {
  font-size: $font-size-lg;
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin-bottom: $spacing-sm;
}

.plan-description {
  color: $text-secondary;
  line-height: 1.6;
  margin-bottom: $spacing-md;
}

.plan-progress {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  margin-bottom: $spacing-md;
  
  .progress-bar {
    flex: 1;
    height: 8px;
    background: rgba($color-secondary, 0.2);
    border-radius: 4px;
    overflow: hidden;
    
    .progress-fill {
      height: 100%;
      background: $color-secondary;
      transition: width 0.3s ease;
    }
  }
  
  .progress-text {
    font-size: $font-size-sm;
    color: $text-secondary;
    min-width: 40px;
  }
}

.plan-meta {
  margin-bottom: $spacing-md;
  
  .meta-item {
    display: flex;
    justify-content: space-between;
    margin-bottom: $spacing-xs;
    font-size: $font-size-sm;
    
    .label {
      color: $text-secondary;
    }
    
    .value {
      color: $text-primary;
      font-weight: $font-weight-medium;
      
      &.priority-high {
        color: $color-danger;
      }
      
      &.priority-medium {
        color: $color-warning;
      }
      
      &.priority-low {
        color: $color-success;
      }
    }
  }
}

.plan-actions {
  display: flex;
  gap: $spacing-xs;
  flex-wrap: wrap;
  
  button {
    padding: $spacing-xs $spacing-sm;
    border: none;
    border-radius: $radius-button;
    font-size: $font-size-sm;
    cursor: pointer;
    transition: all 0.2s;
    
    &:hover {
      transform: translateY(-1px);
    }
  }
  
  .btn-start {
    background: $color-success;
    color: white;
    
    &:hover {
      background: color.adjust($color-success, $lightness: -10%);
    }
  }
  
  .btn-update {
    background: $color-info;
    color: white;
    
    &:hover {
      background: color.adjust($color-info, $lightness: -10%);
    }
  }
  
  .btn-complete {
    background: $color-secondary;
    color: white;
    
    &:hover {
      background: color.adjust($color-secondary, $lightness: -10%);
    }
  }
  
  .btn-edit {
    background: $color-warning;
    color: white;
    
    &:hover {
      background: color.adjust($color-warning, $lightness: -10%);
    }
  }
  
  .btn-delete {
    background: $color-danger;
    color: white;
    
    &:hover {
      background: color.adjust($color-danger, $lightness: -10%);
    }
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
  max-width: 600px;
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
  
  input, textarea, select {
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
    
    &::placeholder {
      color: $text-secondary;
    }
  }
  
  textarea {
    resize: vertical;
    min-height: 80px;
  }
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: $spacing-md;
}

.progress-input {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  
  input[type="range"] {
    flex: 1;
  }
  
  .progress-value {
    min-width: 50px;
    text-align: center;
    color: $text-secondary;
  }
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  cursor: pointer;
  
  input[type="checkbox"] {
    width: auto;
    margin: 0;
  }
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: $spacing-sm;
  padding: $spacing-lg;
  border-top: 1px solid $border-card;
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .growth-page {
    padding: $spacing-sm;
  }
  
  .stats-overview {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .plans-grid {
    grid-template-columns: 1fr;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .modal-content {
    width: 95%;
    margin: $spacing-sm;
  }
}
</style>