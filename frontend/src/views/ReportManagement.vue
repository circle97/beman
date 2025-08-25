<template>
  <div class="report-management-page">
    <!-- 顶部导航 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">举报管理</h1>
        <div class="header-actions">
          <button class="btn-secondary" @click="refreshReports">
            <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
              <path d="M17.65 6.35C16.2 4.9 14.21 4 12 4c-4.42 0-7.99 3.58-7.99 8s3.57 8 7.99 8c3.73 0 6.84-2.55 7.73-6h-2.08c-.82 2.33-3.04 4-5.65 4-3.31 0-6-2.69-6-6s2.69-6 6-6c1.66 0 3.14.69 4.22 1.78L13 11h7V4l-2.35 2.35z"/>
            </svg>
            刷新
          </button>
        </div>
      </div>
    </div>

    <!-- 筛选器 -->
    <div class="filter-section">
      <div class="filter-group">
        <label>状态筛选：</label>
        <select v-model="statusFilter" @change="loadReports">
          <option value="">全部状态</option>
          <option value="0">待处理</option>
          <option value="1">处理中</option>
          <option value="2">已处理</option>
          <option value="3">已驳回</option>
        </select>
      </div>
      
      <div class="filter-group">
        <label>内容类型：</label>
        <select v-model="contentTypeFilter" @change="loadReports">
          <option value="">全部类型</option>
          <option value="1">帖子</option>
          <option value="2">评论</option>
          <option value="3">用户</option>
        </select>
      </div>
      
      <div class="filter-group">
        <label>优先级：</label>
        <select v-model="priorityFilter" @change="loadReports">
          <option value="">全部优先级</option>
          <option value="1">低</option>
          <option value="2">中</option>
          <option value="3">高</option>
        </select>
      </div>
    </div>

    <!-- 举报列表 -->
    <div class="report-list">
      <div v-if="loading" class="loading">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>
      
      <div v-else-if="reports.length === 0" class="empty-state">
        <svg viewBox="0 0 24 24" fill="currentColor" width="64" height="64">
          <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
        </svg>
        <h3>暂无举报</h3>
        <p>当前没有需要处理的举报</p>
      </div>
      
      <div v-else class="reports">
        <div v-for="report in reports" :key="report.id" class="report-item">
          <BemanCard class="report-card">
            <div class="report-header">
              <div class="report-info">
                <div class="report-id">#{{ report.id }}</div>
                <div class="report-status" :class="getStatusClass(report.status)">
                  {{ getStatusText(report.status) }}
                </div>
                <div class="report-priority" :class="getPriorityClass(report.priority)">
                  {{ getPriorityText(report.priority) }}
                </div>
              </div>
              <div class="report-time">{{ formatTime(report.createTime) }}</div>
            </div>
            
            <div class="report-content">
              <div class="content-info">
                <div class="content-type">
                  <span class="label">内容类型：</span>
                  <span>{{ getContentTypeText(report.contentType) }}</span>
                </div>
                <div class="content-id">
                  <span class="label">内容ID：</span>
                  <span>{{ report.contentId }}</span>
                </div>
                <div class="content-title" v-if="report.contentTitle">
                  <span class="label">内容标题：</span>
                  <span>{{ report.contentTitle }}</span>
                </div>
              </div>
              
              <div class="reporter-info">
                <div class="reporter-name">
                  <span class="label">举报人：</span>
                  <span>{{ report.reporterName }}</span>
                </div>
                <div class="report-reason">
                  <span class="label">举报原因：</span>
                  <span>{{ getReasonText(report.reasonType) }}</span>
                </div>
                <div class="report-description" v-if="report.description">
                  <span class="label">详细描述：</span>
                  <span>{{ report.description }}</span>
                </div>
              </div>
              
              <div class="report-actions">
                <button 
                  class="btn-primary" 
                  @click="handleReport(report)"
                  v-if="report.status === 0"
                >
                  开始处理
                </button>
                <button 
                  class="btn-secondary" 
                  @click="viewReportDetail(report)"
                >
                  查看详情
                </button>
                <button 
                  class="btn-danger" 
                  @click="rejectReport(report)"
                  v-if="report.status === 0"
                >
                  驳回举报
                </button>
              </div>
            </div>
          </BemanCard>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination" v-if="total > 0">
      <button 
        class="page-btn" 
        :disabled="currentPage === 1"
        @click="changePage(currentPage - 1)"
      >
        上一页
      </button>
      <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
      <button 
        class="page-btn" 
        :disabled="currentPage === totalPages"
        @click="changePage(currentPage + 1)"
      >
        下一页
      </button>
    </div>

    <!-- 处理举报弹窗 -->
    <div v-if="showHandleModal" class="modal-overlay" @click="showHandleModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>处理举报</h3>
          <button class="close-btn" @click="showHandleModal = false">
            <svg viewBox="0 0 24 24" fill="currentColor" width="24" height="24">
              <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
            </svg>
          </button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>处理结果</label>
            <textarea 
              v-model="handleResult"
              placeholder="请输入处理结果..."
              rows="4"
              class="form-input"
            ></textarea>
          </div>
          <div class="form-group">
            <label>处理措施</label>
            <select v-model="handleAction" class="form-select">
              <option value="">请选择处理措施</option>
              <option value="warning">警告</option>
              <option value="delete">删除内容</option>
              <option value="ban">禁言用户</option>
              <option value="ban_permanent">永久封禁</option>
            </select>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-secondary" @click="showHandleModal = false">取消</button>
          <button 
            class="btn-primary" 
            @click="submitHandle"
            :disabled="!handleResult.trim() || !handleAction"
          >
            确认处理
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import BemanCard from '../components/BemanCard.vue'
import { getReportPage, handleReport as handleReportApi, rejectReport as rejectReportApi, type Report, type ReportQueryDTO } from '../api/report'

// 响应式数据
const loading = ref(false)
const reports = ref<Report[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 筛选器
const statusFilter = ref('')
const contentTypeFilter = ref('')
const priorityFilter = ref('')

// 弹窗相关
const showHandleModal = ref(false)
const currentReport = ref<Report | null>(null)
const handleResult = ref('')
const handleAction = ref('')

// 计算属性
const totalPages = computed(() => Math.ceil(total.value / pageSize.value))

// 方法
const loadReports = async () => {
  loading.value = true
  try {
    const query: ReportQueryDTO = {
      page: currentPage.value,
      size: pageSize.value,
      status: statusFilter.value ? Number(statusFilter.value) : undefined,
      contentType: contentTypeFilter.value ? Number(contentTypeFilter.value) : undefined,
      priority: priorityFilter.value ? Number(priorityFilter.value) : undefined
    }
    
    const result = await getReportPage(query)
    reports.value = result.records
    total.value = result.total
  } catch (error) {
    console.error('加载举报列表失败:', error)
  } finally {
    loading.value = false
  }
}

const refreshReports = () => {
  currentPage.value = 1
  loadReports()
}

const changePage = (page: number) => {
  currentPage.value = page
  loadReports()
}

const formatTime = (timeStr: string) => {
  const date = new Date(timeStr)
  return date.toLocaleString('zh-CN')
}

const getStatusClass = (status: number) => {
  switch (status) {
    case 0: return 'status-pending'
    case 1: return 'status-processing'
    case 2: return 'status-resolved'
    case 3: return 'status-rejected'
    default: return 'status-unknown'
  }
}

const getStatusText = (status: number) => {
  switch (status) {
    case 0: return '待处理'
    case 1: return '处理中'
    case 2: return '已处理'
    case 3: return '已驳回'
    default: return '未知'
  }
}

const getPriorityClass = (priority: number) => {
  switch (priority) {
    case 1: return 'priority-low'
    case 2: return 'priority-medium'
    case 3: return 'priority-high'
    default: return 'priority-unknown'
  }
}

const getPriorityText = (priority: number) => {
  switch (priority) {
    case 1: return '低'
    case 2: return '中'
    case 3: return '高'
    default: return '未知'
  }
}

const getContentTypeText = (contentType: number) => {
  switch (contentType) {
    case 1: return '帖子'
    case 2: return '评论'
    case 3: return '用户'
    default: return '未知'
  }
}

const getReasonText = (reasonType: number) => {
  switch (reasonType) {
    case 1: return '垃圾信息'
    case 2: return '不当内容'
    case 3: return '恶意行为'
    case 4: return '其他'
    default: return '未知'
  }
}

const handleReport = (report: Report) => {
  currentReport.value = report
  showHandleModal.value = true
  handleResult.value = ''
  handleAction.value = ''
}

const viewReportDetail = (report: Report) => {
  // TODO: 实现查看举报详情的功能
  console.log('查看举报详情:', report)
}

const rejectReport = async (report: Report) => {
  if (!confirm('确定要驳回这个举报吗？')) return
  
  try {
    await rejectReportApi(report.id)
    await loadReports()
  } catch (error) {
    console.error('驳回举报失败:', error)
  }
}

const submitHandle = async () => {
  if (!currentReport.value) return
  
  try {
    await handleReportApi(currentReport.value.id, {
      result: handleResult.value,
      action: handleAction.value
    })
    
    showHandleModal.value = false
    await loadReports()
  } catch (error) {
    console.error('处理举报失败:', error)
  }
}

onMounted(() => {
  loadReports()
})
</script>

<style lang="scss" scoped>
@import '../styles/variables.scss';

.report-management-page {
  padding: $spacing-md;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: $spacing-lg;
  
  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .page-title {
    font-size: $font-size-xxl;
    font-weight: $font-weight-bold;
    color: $text-primary;
    margin: 0;
  }
  
  .header-actions {
    display: flex;
    gap: $spacing-sm;
  }
}

.filter-section {
  display: flex;
  gap: $spacing-md;
  margin-bottom: $spacing-lg;
  padding: $spacing-md;
  background: $bg-section;
  border-radius: $radius-card;
  border: 1px solid $border-card;
  
  .filter-group {
    display: flex;
    align-items: center;
    gap: $spacing-sm;
    
    label {
      font-weight: $font-weight-medium;
      color: $text-primary;
      white-space: nowrap;
    }
    
    select {
      padding: $spacing-xs $spacing-sm;
      border: 1px solid $border-card;
      border-radius: $radius-button;
      background: $bg-app;
      color: $text-primary;
      min-width: 120px;
      
      &:focus {
        outline: none;
        border-color: $color-secondary;
      }
    }
  }
}

.report-list {
  margin-bottom: $spacing-lg;
}

.report-item {
  margin-bottom: $spacing-md;
}

.report-card {
  .report-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $spacing-md;
    padding-bottom: $spacing-sm;
    border-bottom: 1px solid $border-card;
    
    .report-info {
      display: flex;
      align-items: center;
      gap: $spacing-md;
      
      .report-id {
        font-weight: $font-weight-bold;
        color: $color-secondary;
      }
      
      .report-status {
        padding: $spacing-xs $spacing-sm;
        border-radius: 12px;
        font-size: $font-size-sm;
        font-weight: $font-weight-medium;
        
        &.status-pending {
          background: rgba($color-warning, 0.1);
          color: $color-warning;
        }
        
        &.status-processing {
          background: rgba($color-info, 0.1);
          color: $color-info;
        }
        
        &.status-resolved {
          background: rgba($color-success, 0.1);
          color: $color-success;
        }
        
        &.status-rejected {
          background: rgba($color-danger, 0.1);
          color: $color-danger;
        }
      }
      
      .report-priority {
        padding: $spacing-xs $spacing-sm;
        border-radius: 12px;
        font-size: $font-size-sm;
        font-weight: $font-weight-medium;
        
        &.priority-low {
          background: rgba($color-success, 0.1);
          color: $color-success;
        }
        
        &.priority-medium {
          background: rgba($color-warning, 0.1);
          color: $color-warning;
        }
        
        &.priority-high {
          background: rgba($color-danger, 0.1);
          color: $color-danger;
        }
      }
    }
    
    .report-time {
      font-size: $font-size-sm;
      color: $text-secondary;
    }
  }
  
  .report-content {
    .content-info,
    .reporter-info {
      margin-bottom: $spacing-md;
      
      .label {
        font-weight: $font-weight-medium;
        color: $text-primary;
        margin-right: $spacing-sm;
      }
      
      .content-title {
        margin-top: $spacing-sm;
        font-weight: $font-weight-medium;
      }
      
      .report-description {
        margin-top: $spacing-sm;
        padding: $spacing-sm;
        background: $bg-app;
        border-radius: $radius-button;
        font-style: italic;
      }
    }
    
    .report-actions {
      display: flex;
      gap: $spacing-sm;
      justify-content: flex-end;
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
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: $spacing-md;
  
  .page-btn {
    padding: $spacing-sm $spacing-md;
    background: $bg-app;
    border: 1px solid $border-card;
    color: $text-primary;
    cursor: pointer;
    border-radius: $radius-button;
    transition: all 0.2s;
    
    &:hover:not(:disabled) {
      background: $color-secondary;
      color: white;
      border-color: $color-secondary;
    }
    
    &:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }
  }
  
  .page-info {
    color: $text-secondary;
    font-weight: $font-weight-medium;
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
  
  .form-input,
  .form-select {
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
  
  .form-input {
    resize: vertical;
    min-height: 80px;
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
  .report-management-page {
    padding: $spacing-sm;
  }
  
  .filter-section {
    flex-direction: column;
    gap: $spacing-sm;
  }
  
  .report-card {
    .report-header {
      flex-direction: column;
      align-items: flex-start;
      gap: $spacing-sm;
    }
    
    .report-actions {
      flex-direction: column;
      align-items: stretch;
    }
  }
}
</style>
