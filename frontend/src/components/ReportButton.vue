<template>
  <div class="report-button">
    <button
      class="btn-report"
      @click="showReportModal = true"
      :disabled="isLoading"
    >
      <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
        <path d="M14,2H6A2,2 0 0,0 4,4V20A2,2 0 0,0 6,22H18A2,2 0 0,0 20,20V8L14,2M18,20H6V4H13V9H18V20Z"/>
      </svg>
      <span>举报</span>
    </button>

    <!-- 举报弹窗 -->
    <div v-if="showReportModal" class="report-modal-overlay" @click="closeModal">
      <div class="report-modal" @click.stop>
        <div class="modal-header">
          <h3>举报内容</h3>
          <button class="btn-close" @click="closeModal">
            <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
              <path d="M19,6.41L17.59,5L12,10.59L6.41,5L5,6.41L10.59,12L5,17.59L6.41,19L12,13.41L17.59,19L19,17.59L13.41,12L19,6.41Z"/>
            </svg>
          </button>
        </div>

        <div class="modal-body">
          <div class="form-group">
            <label>举报原因</label>
            <select v-model="reportData.reasonType" class="form-select">
              <option value="">请选择举报原因</option>
              <option value="1">垃圾信息</option>
              <option value="2">不当内容</option>
              <option value="3">恶意行为</option>
              <option value="4">版权问题</option>
              <option value="5">其他</option>
            </select>
          </div>

          <div class="form-group">
            <label>详细描述</label>
            <textarea
              v-model="reportData.description"
              class="form-textarea"
              placeholder="请详细描述举报原因..."
              rows="4"
            ></textarea>
          </div>

          <div class="form-group">
            <label>证据（可选）</label>
            <input
              v-model="reportData.evidence"
              type="text"
              class="form-input"
              placeholder="图片链接或其他证据"
            />
          </div>
        </div>

        <div class="modal-footer">
          <button class="btn-secondary" @click="closeModal">取消</button>
          <button
            class="btn-primary"
            @click="submitReport"
            :disabled="!canSubmit || isSubmitting"
          >
            {{ isSubmitting ? '提交中...' : '提交举报' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { createReport } from '../api/report'

interface Props {
  contentType: number // 1-帖子，2-评论，3-用户，4-其他
  contentId: number
}

const props = defineProps<Props>()

const emit = defineEmits<{
  (e: 'report-submitted'): void
}>()

const showReportModal = ref(false)
const isLoading = ref(false)
const isSubmitting = ref(false)

const reportData = ref({
  contentType: props.contentType,
  contentId: props.contentId,
  reasonType: '',
  description: '',
  evidence: ''
})

// 检查是否可以提交
const canSubmit = computed(() => {
  return reportData.value.reasonType && 
         reportData.value.description.trim().length >= 10
})

// 关闭弹窗
const closeModal = () => {
  showReportModal.value = false
  resetForm()
}

// 重置表单
const resetForm = () => {
  reportData.value = {
    contentType: props.contentType,
    contentId: props.contentId,
    reasonType: '',
    description: '',
    evidence: ''
  }
}

// 提交举报
const submitReport = async () => {
  if (!canSubmit.value || isSubmitting.value) return
  
  isSubmitting.value = true
  try {
    await createReport({
      contentType: Number(reportData.value.contentType),
      contentId: reportData.value.contentId,
      reasonType: Number(reportData.value.reasonType),
      description: reportData.value.description.trim(),
      evidence: reportData.value.evidence.trim() || undefined
    })
    
    // 提交成功
    closeModal()
    emit('report-submitted')
    
    // 显示成功提示
    alert('举报提交成功，我们会尽快处理')
  } catch (error) {
    console.error('提交举报失败:', error)
    alert('举报提交失败，请稍后重试')
  } finally {
    isSubmitting.value = false
  }
}
</script>

<style lang="scss" scoped>
@import '../styles/variables.scss';

.report-button {
  display: inline-block;
}

.btn-report {
  display: inline-flex;
  align-items: center;
  gap: $spacing-xs;
  padding: $spacing-xs $spacing-sm;
  border: 1px solid $border-button;
  background: $bg-button;
  color: $text-secondary;
  border-radius: $radius-button;
  font-size: $font-size-sm;
  cursor: pointer;
  transition: all 0.2s;
  
  &:hover:not(:disabled) {
    background: $bg-button-hover;
    color: $text-primary;
    border-color: $color-primary;
  }
  
  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
  
  svg {
    width: 16px;
    height: 16px;
  }
}

// 弹窗样式
.report-modal-overlay {
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

.report-modal {
  background: $bg-card;
  border-radius: $radius-card;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-lg;
  border-bottom: 1px solid $border-divider;
  
  h3 {
    margin: 0;
    color: $text-primary;
    font-size: $font-size-lg;
  }
}

.btn-close {
  background: none;
  border: none;
  color: $text-secondary;
  cursor: pointer;
  padding: $spacing-xs;
  border-radius: $radius-button;
  
  &:hover {
    background: $bg-button-hover;
    color: $text-primary;
  }
  
  svg {
    width: 20px;
    height: 20px;
  }
}

.modal-body {
  padding: $spacing-lg;
}

.form-group {
  margin-bottom: $spacing-lg;
  
  label {
    display: block;
    margin-bottom: $spacing-xs;
    color: $text-primary;
    font-weight: 500;
  }
}

.form-select,
.form-input,
.form-textarea {
  width: 100%;
  padding: $spacing-sm;
  border: 1px solid $border-input;
  border-radius: $radius-input;
  background: $bg-input;
  color: $text-primary;
  font-size: $font-size-base;
  
  &:focus {
    outline: none;
    border-color: $color-primary;
  }
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: $spacing-sm;
  padding: $spacing-lg;
  border-top: 1px solid $border-divider;
}

.btn-secondary,
.btn-primary {
  padding: $spacing-sm $spacing-md;
  border: 1px solid;
  border-radius: $radius-button;
  font-size: $font-size-base;
  cursor: pointer;
  transition: all 0.2s;
  
  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
}

.btn-secondary {
  background: $bg-button;
  color: $text-secondary;
  border-color: $border-button;
  
  &:hover:not(:disabled) {
    background: $bg-button-hover;
    color: $text-primary;
  }
}

.btn-primary {
  background: $color-primary;
  color: white;
  border-color: $color-primary;
  
  &:hover:not(:disabled) {
    background: darken($color-primary, 10%);
    border-color: darken($color-primary, 10%);
  }
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .report-modal {
    width: 95%;
    margin: $spacing-md;
  }
  
  .modal-header,
  .modal-body,
  .modal-footer {
    padding: $spacing-md;
  }
}
</style>
