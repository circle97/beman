<template>
  <div class="reminder-settings">
    <div class="settings-header">
      <h4>提醒设置</h4>
      <button type="button" class="btn-add" @click="addReminder">
        <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
          <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
        </svg>
        添加提醒
      </button>
    </div>

    <div v-if="reminders.length === 0" class="no-reminders">
      <p>暂无提醒设置</p>
    </div>

    <div v-else class="reminder-list">
      <div v-for="(reminder, index) in reminders" :key="index" class="reminder-item">
        <div class="reminder-row">
          <div class="reminder-type">
            <label>类型:</label>
            <select v-model="reminder.type">
              <option value="1">邮件</option>
              <option value="2">短信</option>
              <option value="3">推送</option>
              <option value="4">微信</option>
              <option value="5">钉钉</option>
            </select>
          </div>

          <div class="reminder-time">
            <label>提前时间:</label>
            <div class="time-input">
              <input 
                v-model="reminder.advanceMinutes" 
                type="number" 
                min="0" 
                placeholder="0"
                class="minutes-input"
              >
              <span class="time-unit">分钟</span>
            </div>
          </div>

          <div class="reminder-content">
            <label>内容:</label>
            <input 
              v-model="reminder.content" 
              type="text" 
              placeholder="提醒内容（可选）"
              class="content-input"
            >
          </div>

          <button 
            type="button" 
            class="btn-remove" 
            @click="removeReminder(index)"
            title="删除提醒"
          >
            <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
              <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
            </svg>
          </button>
        </div>

        <div class="reminder-preview">
          <span class="preview-text">
            将在日程开始前 {{ reminder.advanceMinutes || 0 }} 分钟发送{{ getTypeName(reminder.type) }}提醒
          </span>
        </div>
      </div>
    </div>

    <div class="settings-help">
      <p class="help-text">
        💡 提示：可以设置多个不同类型的提醒，系统会在指定时间自动发送
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'

export interface ReminderSetting {
  type: number
  advanceMinutes: number
  content: string
}

interface Props {
  modelValue: ReminderSetting[]
}

interface Emits {
  (e: 'update:modelValue', value: ReminderSetting[]): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const reminders = ref<ReminderSetting[]>([...props.modelValue])

// 监听内部数据变化，同步到父组件
watch(reminders, (newValue) => {
  emit('update:modelValue', newValue)
}, { deep: true })

// 监听父组件数据变化，同步到内部
watch(() => props.modelValue, (newValue) => {
  reminders.value = [...newValue]
}, { deep: true })

const addReminder = () => {
  reminders.value.push({
    type: 1,
    advanceMinutes: 0,
    content: ''
  })
}

const removeReminder = (index: number) => {
  reminders.value.splice(index, 1)
}

const getTypeName = (type: number) => {
  const typeNames = ['', '邮件', '短信', '推送', '微信', '钉钉']
  return typeNames[type] || '未知'
}
</script>

<style scoped lang="scss">
@import '../styles/variables.scss';

.reminder-settings {
  border: 1px solid $border-card;
  border-radius: $radius-card;
  padding: $spacing-lg;
  background: $bg-app;
}

.settings-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $spacing-lg;
  
  h4 {
    margin: 0;
    color: $color-primary;
    font-size: $font-size-lg;
  }
}

.btn-add {
  background: $color-secondary;
  color: white;
  border: none;
  padding: $spacing-xs $spacing-sm;
  border-radius: $radius-button;
  cursor: pointer;
  font-size: $font-size-sm;
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  transition: all 0.2s;
  
  &:hover {
    background: darken($color-secondary, 10%);
  }
}

.no-reminders {
  text-align: center;
  padding: $spacing-lg;
  color: $text-secondary;
  font-style: italic;
}

.reminder-list {
  margin-bottom: $spacing-lg;
}

.reminder-item {
  background: $bg-section;
  border: 1px solid $border-card;
  border-radius: $radius-card;
  padding: $spacing-md;
  margin-bottom: $spacing-md;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.reminder-row {
  display: grid;
  grid-template-columns: 1fr 1fr 2fr auto;
  gap: $spacing-md;
  align-items: end;
  margin-bottom: $spacing-sm;
  
  @media (max-width: $breakpoint-md) {
    grid-template-columns: 1fr;
    gap: $spacing-sm;
  }
}

.reminder-type, .reminder-time, .reminder-content {
  label {
    display: block;
    margin-bottom: $spacing-xs;
    font-size: $font-size-sm;
    color: $text-secondary;
    font-weight: $font-weight-medium;
  }
  
  select, input {
    width: 100%;
    padding: $spacing-xs $spacing-sm;
    border: 1px solid $border-card;
    border-radius: $radius-button;
    background: $bg-app;
    color: $text-primary;
    font-size: $font-size-sm;
    
    &:focus {
      outline: none;
      border-color: $color-secondary;
    }
  }
}

.time-input {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  
  .minutes-input {
    flex: 1;
  }
  
  .time-unit {
    color: $text-secondary;
    font-size: $font-size-sm;
    white-space: nowrap;
  }
}

.content-input {
  width: 100%;
}

.btn-remove {
  background: rgba(#f44336, 0.1);
  color: #f44336;
  border: none;
  padding: $spacing-xs;
  border-radius: $radius-button;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  
  &:hover {
    background: rgba(#f44336, 0.2);
  }
}

.reminder-preview {
  padding: $spacing-sm;
  background: rgba($color-secondary, 0.05);
  border-radius: $radius-tag;
  
  .preview-text {
    font-size: $font-size-sm;
    color: $text-secondary;
  }
}

.settings-help {
  border-top: 1px solid $border-card;
  padding-top: $spacing-md;
  
  .help-text {
    margin: 0;
    font-size: $font-size-sm;
    color: $text-secondary;
    text-align: center;
  }
}
</style>
