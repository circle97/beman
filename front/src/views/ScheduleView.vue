<template>
  <div class="schedule-view">
    <h2>日程管家</h2>
    <p>智能提醒，不错过重要时刻</p>
    
    <div class="schedule-list">
      <div class="schedule-card">
        <h3>女朋友生日</h3>
        <p>女朋友25岁生日</p>
        <div class="schedule-meta">
          <span>2024-01-15</span>
          <span>生日</span>
        </div>
      </div>
      
      <div class="schedule-card">
        <h3>恋爱纪念日</h3>
        <p>在一起2周年纪念日</p>
        <div class="schedule-meta">
          <span>2024-02-14</span>
          <span>纪念日</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 日程管理页面逻辑
</script>

<style scoped>
.schedule-view {
  text-align: center;
  padding: 20px;
}

h2 {
  font-size: 2.5rem;
  margin-bottom: 10px;
  background: linear-gradient(45deg, #ff6b6b, #4ecdc4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

p {
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 30px;
}

.schedule-list {
  max-width: 600px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.schedule-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 20px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  text-align: left;
}

.schedule-card h3 {
  color: white;
  margin-bottom: 10px;
}

.schedule-card p {
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 15px;
}

.schedule-meta {
  display: flex;
  justify-content: space-between;
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
}
</style> 