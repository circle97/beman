<template>
  <div class="test-reminder-page">
    <div class="page-header">
      <h1>提醒管理测试页面</h1>
      <p>测试提醒管理组件的功能</p>
    </div>

    <div class="test-section">
      <h2>提醒管理组件测试</h2>
      <ReminderManager />
    </div>

    <div class="test-section">
      <h2>提醒设置组件测试</h2>
      <ReminderSettings v-model="reminderSettings" />
    </div>

    <div class="test-info">
      <h3>测试说明</h3>
      <ul>
        <li>上方显示完整的提醒管理界面</li>
        <li>下方显示提醒设置组件</li>
        <li>检查样式是否正确显示</li>
        <li>检查组件是否正常工作</li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import ReminderManager from '../../components/ReminderManager.vue'
import ReminderSettings, { type ReminderSetting } from '../../components/ReminderSettings.vue'

const reminderSettings = ref<ReminderSetting[]>([
  {
    type: 1,
    advanceMinutes: 30,
    content: '测试提醒内容'
  }
])
</script>

<style scoped lang="scss">
@import '../../styles/variables.scss';

.test-reminder-page {
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

.test-section {
  margin-bottom: $spacing-xl;
  
  h2 {
    color: $color-primary;
    margin-bottom: $spacing-lg;
    border-bottom: 1px solid $border-card;
    padding-bottom: $spacing-sm;
  }
}

.test-info {
  background: $bg-section;
  border: 1px solid $border-card;
  border-radius: $radius-card;
  padding: $spacing-lg;
  
  h3 {
    color: $color-secondary;
    margin-bottom: $spacing-md;
  }
  
  ul {
    color: $text-secondary;
    line-height: 1.6;
    
    li {
      margin-bottom: $spacing-xs;
    }
  }
}
</style>
