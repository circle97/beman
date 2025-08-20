<template>
  <div class="schedule-page">
    <div class="page-header">
      <h1>日程管家</h1>
      <p>智能管理你的重要日程，不错过每一个重要时刻</p>
    </div>

    <div class="stats-overview">
      <div class="stat-card">
        <div class="stat-icon">📅</div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.todayCount }}</div>
          <div class="stat-label">今日日程</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">⚠️</div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.highPriorityCount }}</div>
          <div class="stat-label">重要日程</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">✅</div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.completedCount }}</div>
          <div class="stat-label">已完成</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">⏰</div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.pendingCount }}</div>
          <div class="stat-label">待办中</div>
        </div>
      </div>
    </div>

    <div class="quick-actions">
      <button class="btn-primary" @click="showCreateModal = true">
        <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
          <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
        </svg>
        新建日程
      </button>
      
      <button class="btn-secondary" @click="showReminderManager = !showReminderManager">
        <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
          <path d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.89 2 2 2zm6-6v-5c0-3.07-1.64-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.63 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z"/>
        </svg>
        {{ showReminderManager ? '隐藏提醒' : '管理提醒' }}
      </button>
      
      <button class="btn-secondary" @click="showSmartRecognition = true">
        <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
          <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/>
        </svg>
        智能识别
      </button>
      
      <button class="btn-secondary" @click="showGiftRecommendations = true">
        <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
          <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
        </svg>
        礼物推荐
      </button>
      
      <button class="btn-secondary" @click="loadSchedules">
        <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
          <path d="M17.65 6.35A7.958 7.958 0 0012 4c-4.42 0-7.99 3.58-7.99 8s3.57 8 7.99 8c3.73 0 6.84-2.55 7.73-6h-2.08A5.99 5.99 0 0112 18c-3.31 0-6-2.69-6-6s2.69-6 6-6c1.66 0 3.14.69 4.22 1.78L13 11h7V4l-2.35 2.35z"/>
        </svg>
        刷新数据
      </button>
    </div>

    <!-- 智能识别模态框 -->
    <div v-if="showSmartRecognition" class="modal-overlay" @click="showSmartRecognition = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>智能识别重要日期</h3>
          <button class="modal-close" @click="showSmartRecognition = false">×</button>
        </div>
        <div class="modal-body">
          <div class="input-group">
            <label>输入文本内容：</label>
            <textarea 
              v-model="recognitionText" 
              placeholder="例如：明天是我女朋友的生日，我们约好晚上7点一起吃饭..."
              rows="4"
            ></textarea>
          </div>
          <div class="recognition-examples">
            <h4>识别示例：</h4>
            <ul>
              <li>生日：明天是我女朋友的生日</li>
              <li>纪念日：下个月是我们结婚一周年</li>
              <li>约会：明天晚上7点一起看电影</li>
              <li>会议：明天上午9点开项目讨论会</li>
            </ul>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-secondary" @click="showSmartRecognition = false">取消</button>
          <button class="btn-primary" @click="identifyImportantDates" :disabled="!recognitionText.trim()">
            开始识别
          </button>
        </div>
      </div>
    </div>

    <!-- 礼物推荐模态框 -->
    <div v-if="showGiftRecommendations" class="modal-overlay" @click="showGiftRecommendations = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>礼物推荐</h3>
          <button class="modal-close" @click="showGiftRecommendations = false">×</button>
        </div>
        <div class="modal-body">
          <div class="gift-filters">
            <div class="input-group">
              <label>目标年龄：</label>
              <select v-model="giftFilters.targetAge">
                <option value="">不限</option>
                <option value="18">18岁以下</option>
                <option value="25">18-25岁</option>
                <option value="35">26-35岁</option>
                <option value="45">36-45岁</option>
                <option value="55">46-55岁</option>
                <option value="65">55岁以上</option>
              </select>
            </div>
            <div class="input-group">
              <label>目标性别：</label>
              <select v-model="giftFilters.targetGender">
                <option value="">不限</option>
                <option value="1">男性</option>
                <option value="2">女性</option>
              </select>
            </div>
            <div class="input-group">
              <label>场合：</label>
              <select v-model="giftFilters.occasion">
                <option value="">不限</option>
                <option value="生日">生日</option>
                <option value="纪念日">纪念日</option>
                <option value="节日">节日</option>
                <option value="约会">约会</option>
              </select>
            </div>
            <div class="input-group">
              <label>预算上限：</label>
              <input type="number" v-model="giftFilters.maxBudget" placeholder="输入金额">
            </div>
          </div>
          
          <div v-if="giftRecommendations.length > 0" class="gift-list">
            <h4>推荐结果：</h4>
            <div class="gift-items">
              <div v-for="gift in giftRecommendations" :key="gift.id" class="gift-item">
                <div class="gift-image">
                  <img v-if="gift.imageUrl" :src="gift.imageUrl" :alt="gift.name">
                  <div v-else class="gift-placeholder">🎁</div>
                </div>
                <div class="gift-info">
                  <h5>{{ gift.name }}</h5>
                  <p>{{ gift.description }}</p>
                  <div class="gift-meta">
                    <span class="gift-price">¥{{ gift.minPrice }} - ¥{{ gift.maxPrice }}</span>
                    <span class="gift-rating">⭐ {{ gift.rating }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-secondary" @click="showGiftRecommendations = false">关闭</button>
          <button class="btn-primary" @click="searchGiftRecommendations">搜索推荐</button>
        </div>
      </div>
    </div>

    <!-- 提醒管理区域 -->
    <div v-if="showReminderManager" class="reminder-section">
      <ReminderManager />
    </div>

    <div class="schedule-list">
      <div class="list-header">
        <h2>我的日程</h2>
        <div class="list-filters">
          <select @change="loadSchedules">
            <option value="">全部类型</option>
            <option value="1">生日</option>
            <option value="2">纪念日</option>
            <option value="3">节日</option>
            <option value="4">约会</option>
            <option value="5">会议</option>
            <option value="6">其他</option>
          </select>
          <select @change="loadSchedules">
            <option value="">全部优先级</option>
            <option value="1">低</option>
            <option value="2">中</option>
            <option value="3">高</option>
            <option value="4">紧急</option>
          </select>
        </div>
      </div>

      <div v-if="loading" class="loading">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>

      <div v-else-if="schedules.length === 0" class="empty-state">
        <svg viewBox="0 0 24 24" fill="currentColor" width="64" height="64">
          <path d="M19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V8h14v11zM7 10h5v5H7z"/>
        </svg>
        <h3>暂无日程</h3>
        <p>开始创建你的第一个日程吧</p>
      </div>

      <div v-else class="schedule-items">
        <div v-for="schedule in schedules" :key="schedule.id" class="schedule-item">
          <div class="schedule-header">
            <div class="schedule-title">{{ schedule.title }}</div>
            <div class="schedule-actions">
              <button @click="() => {}" class="action-btn edit">编辑</button>
              <button @click="() => {}" class="action-btn delete">删除</button>
            </div>
          </div>
          
          <div class="schedule-content">
            <div class="schedule-description">{{ schedule.description }}</div>
            <div class="schedule-meta">
              <span class="schedule-type">{{ getTypeName(schedule.type) }}</span>
              <span class="schedule-priority" :class="`priority-${schedule.priority}`">
                {{ getPriorityName(schedule.priority) }}
              </span>
              <span class="schedule-time">{{ formatTime(schedule.startTime) }}</span>
              <span v-if="schedule.location" class="schedule-location">📍 {{ schedule.location }}</span>
            </div>
          </div>

          <div class="schedule-footer">
            <div class="schedule-status">
              <span v-if="schedule.status === 1" class="status-pending">进行中</span>
              <span v-else-if="schedule.status === 2" class="status-completed">已完成</span>
              <span v-else-if="schedule.status === 3" class="status-cancelled">已取消</span>
            </div>
            
            <div class="schedule-actions">
              <button v-if="schedule.status === 1" @click="() => {}" class="action-btn complete">
                完成
              </button>
              <button v-if="schedule.status === 1" @click="() => {}" class="action-btn cancel">
                取消
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { 
  getSchedulePage, 
  getScheduleStats,
  createTieredReminders,
  getReminderConfig,
  sendTieredReminders,
  getGiftRecommendations,
  getGiftRecommendationsByOccasion,
  type Schedule,
  type ScheduleStats,
  type Reminder,
  type GiftRecommendation
} from '../../api/schedule'
import ReminderManager from '../../components/ReminderManager.vue'

// 响应式数据
const loading = ref(false)
const schedules = ref<Schedule[]>([])
const stats = ref<ScheduleStats>({
  todayCount: 0,
  weekCount: 0,
  monthCount: 0,
  completedCount: 0,
  pendingCount: 0,
  highPriorityCount: 0
})

// 模态框状态
const showCreateModal = ref(false)
const showReminderManager = ref(false)
const showSmartRecognition = ref(false)
const showGiftRecommendations = ref(false)

// 识别文本
const recognitionText = ref('')

// 礼物推荐过滤器
const giftFilters = ref({
  targetAge: '',
  targetGender: '',
  occasion: '',
  maxBudget: ''
})

// 礼物推荐结果
const giftRecommendations = ref<any[]>([])

// 查询参数
const queryParams = ref({
  page: 1,
  size: 20
})

// 方法
const loadSchedules = async () => {
  loading.value = true
  try {
    const pageResult = await getSchedulePage(queryParams.value)
    schedules.value = pageResult.records
  } catch (error) {
    console.error('加载日程失败:', error)
  } finally {
    loading.value = false
  }
}

const loadStats = async () => {
  try {
    const data = await getScheduleStats()
    stats.value = data
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const getTypeName = (type: number) => {
  const typeNames = ['', '生日', '纪念日', '节日', '约会', '会议', '其他']
  return typeNames[type] || '未知'
}

const getPriorityName = (priority: number) => {
  const priorityNames = ['', '低', '中', '高', '紧急']
  return priorityNames[priority] || '未知'
}

const formatTime = (timeStr: string) => {
  const date = new Date(timeStr)
  return date.toLocaleString('zh-CN')
}

const identifyImportantDates = async () => {
  try {
    const text = recognitionText.value.trim();
    if (!text) {
      alert('请输入要识别的文本内容');
      return;
    }

    // 调用后端API进行智能识别
    const schedules = await identifyImportantDatesFromAPI(text);
    
    if (schedules && schedules.length > 0) {
      alert(`识别成功！识别到 ${schedules.length} 个重要日期。`);
      // 可以在这里将识别到的日程添加到列表中
      await loadSchedules(); // 重新加载日程列表
    } else {
      alert('未识别到重要日期，请尝试其他表述方式。');
    }
    
    showSmartRecognition.value = false;
    recognitionText.value = '';
    
  } catch (error) {
    console.error('智能识别失败:', error);
    alert('智能识别失败，请稍后重试。');
  }
}

const identifyImportantDatesFromAPI = async (text: string): Promise<Schedule[]> => {
  try {
    // 这里应该调用后端的智能识别API
    // 由于后端API还没有完全实现，我们先使用模拟数据
    const mockSchedules: Schedule[] = [];
    
    // 生日识别
    if (text.toLowerCase().includes('生日')) {
      mockSchedules.push({
        id: Date.now(),
        userId: 1,
        title: '生日提醒',
        description: `识别到的生日：${text}`,
        type: 1,
        priority: 3,
        startTime: new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString(),
        endTime: new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString(),
        isAllDay: true,
        repeatType: 4,
        repeatEndTime: '',
        reminderSettings: '',
        tags: '',
        location: '',
        status: 1,
        createTime: new Date().toISOString(),
        updateTime: new Date().toISOString()
      });
    }
    
    // 约会识别
    if (text.toLowerCase().includes('约会') || text.toLowerCase().includes('吃饭') || text.toLowerCase().includes('看电影')) {
      mockSchedules.push({
        id: Date.now() + 1,
        userId: 1,
        title: '约会提醒',
        description: `识别到的约会：${text}`,
        type: 4,
        priority: 2,
        startTime: new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString(),
        endTime: new Date(Date.now() + 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000).toISOString(),
        isAllDay: false,
        repeatType: 0,
        repeatEndTime: '',
        reminderSettings: '',
        tags: '',
        location: '',
        status: 1,
        createTime: new Date().toISOString(),
        updateTime: new Date().toISOString()
      });
    }
    
    return mockSchedules;
    
  } catch (error) {
    console.error('API调用失败:', error);
    return [];
  }
}

const searchGiftRecommendations = async () => {
  try {
    const params = {
      targetAge: giftFilters.value.targetAge ? parseInt(giftFilters.value.targetAge) : undefined,
      targetGender: giftFilters.value.targetGender ? parseInt(giftFilters.value.targetGender) : undefined,
      occasions: giftFilters.value.occasion ? [giftFilters.value.occasion] : undefined,
      maxBudget: giftFilters.value.maxBudget ? parseFloat(giftFilters.value.maxBudget) : undefined,
      limit: 10
    };

    // 调用后端API获取礼物推荐
    const recommendations = await getGiftRecommendations(params);
    
    if (recommendations && recommendations.length > 0) {
      giftRecommendations.value = recommendations;
    } else {
      // 如果没有推荐结果，显示模拟数据
      giftRecommendations.value = [
        {
          id: 1,
          name: '鲜花',
          description: '美丽的玫瑰花束，适合各种场合',
          imageUrl: '',
          minPrice: 50,
          maxPrice: 100,
          minAge: 18,
          maxAge: 65,
          gender: 2,
          rating: 4.5,
          reason: '经典礼物，表达爱意',
          purchaseUrl: '',
          occasions: '生日,纪念日',
          categories: '鲜花',
          tags: '浪漫,温馨',
          status: 1,
          createTime: new Date().toISOString(),
          updateTime: new Date().toISOString()
        },
        {
          id: 2,
          name: '巧克力',
          description: '精致的巧克力礼盒，甜蜜的选择',
          imageUrl: '',
          minPrice: 30,
          maxPrice: 80,
          minAge: 18,
          maxAge: 65,
          gender: 2,
          rating: 4.0,
          reason: '甜蜜美味，适合分享',
          purchaseUrl: '',
          occasions: '生日,约会',
          categories: '食品',
          tags: '甜蜜,美味',
          status: 1,
          createTime: new Date().toISOString(),
          updateTime: new Date().toISOString()
        },
        {
          id: 3,
          name: '香水',
          description: '优雅的女士香水，彰显品味',
          imageUrl: '',
          minPrice: 100,
          maxPrice: 200,
          minAge: 25,
          maxAge: 55,
          gender: 2,
          rating: 4.8,
          reason: '高端礼品，体现品味',
          purchaseUrl: '',
          occasions: '生日,纪念日,节日',
          categories: '美妆',
          tags: '高端,优雅',
          status: 1,
          createTime: new Date().toISOString(),
          updateTime: new Date().toISOString()
        }
      ];
    }
    
  } catch (error) {
    console.error('获取礼物推荐失败:', error);
    alert('获取礼物推荐失败，请稍后重试。');
  }
}

onMounted(() => {
  loadSchedules()
  loadStats()
})
</script>

<style lang="scss" scoped>
@import '../../styles/variables.scss';

.schedule-page {
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

.stats-overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: $spacing-md;
  margin-bottom: $spacing-xl;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-lg;
  background: $bg-section;
  border-radius: $radius-card;
  border: 1px solid $border-card;
  
  .stat-icon {
    font-size: 2rem;
  }
  
  .stat-content {
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
}

.quick-actions {
  display: flex;
  gap: $spacing-md;
  margin-bottom: $spacing-xl;
  flex-wrap: wrap;
}

.btn-primary {
  background: $color-secondary;
  color: white;
  border: none;
  padding: $spacing-sm $spacing-md;
  border-radius: $radius-button;
  cursor: pointer;
  transition: all 0.2s;
  
  &:hover {
    background: color.adjust($color-secondary, $lightness: -10%);
  }
}

.btn-secondary {
  background: $bg-app;
  color: $text-primary;
  border: 1px solid $border-card;
  padding: $spacing-sm $spacing-md;
  border-radius: $radius-button;
  cursor: pointer;
  transition: all 0.2s;
  
  &:hover {
    background: rgba($color-secondary, 0.1);
    border-color: $color-secondary;
  }
}

.schedule-list {
  .list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $spacing-lg;
    
    h2 {
      color: $text-primary;
      margin: 0;
    }
    
    .list-filters {
      display: flex;
      gap: $spacing-sm;
      
      select {
        padding: $spacing-sm;
        border: 1px solid $border-card;
        border-radius: $radius-button;
        background: $bg-app;
        color: $text-primary;
      }
    }
  }
}

.schedule-items {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.schedule-item {
  background: $bg-section;
  border-radius: $radius-card;
  border: 1px solid $border-card;
  padding: $spacing-lg;
  
  .schedule-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $spacing-md;
    
    .schedule-title {
      font-size: $font-size-lg;
      font-weight: $font-weight-bold;
      color: $text-primary;
    }
    
    .schedule-actions {
      display: flex;
      gap: $spacing-sm;
    }
  }
  
  .schedule-content {
    margin-bottom: $spacing-md;
    
    .schedule-description {
      color: $text-secondary;
      margin-bottom: $spacing-sm;
    }
    
    .schedule-meta {
      display: flex;
      gap: $spacing-md;
      flex-wrap: wrap;
      
      .schedule-type {
        background: rgba($color-secondary, 0.1);
        color: $color-secondary;
        padding: $spacing-xs $spacing-sm;
        border-radius: 12px;
        font-size: $font-size-xs;
      }
      
      .schedule-priority {
        padding: $spacing-xs $spacing-sm;
        border-radius: 12px;
        font-size: $font-size-xs;
        font-weight: $font-weight-medium;
        
        &.priority-1 { background: rgba(#4caf50, 0.1); color: #4caf50; }
        &.priority-2 { background: rgba(#ff9800, 0.1); color: #ff9800; }
        &.priority-3 { background: rgba(#f44336, 0.1); color: #f44336; }
        &.priority-4 { background: rgba(#9c27b0, 0.1); color: #9c27b0; }
      }
      
      .schedule-time {
        color: $text-secondary;
        font-size: $font-size-sm;
      }
      
      .schedule-location {
        color: $text-secondary;
        font-size: $font-size-sm;
      }
    }
  }
  
  .schedule-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .schedule-status {
      .status-pending { color: #ff9800; }
      .status-completed { color: #4caf50; }
      .status-cancelled { color: #f44336; }
    }
    
    .schedule-actions {
      display: flex;
      gap: $spacing-sm;
    }
  }
}

.action-btn {
  padding: $spacing-xs $spacing-sm;
  border: none;
  border-radius: $radius-button;
  cursor: pointer;
  font-size: $font-size-sm;
  transition: all 0.2s;
  
  &.edit {
    background: rgba($color-secondary, 0.1);
    color: $color-secondary;
    
    &:hover {
      background: rgba($color-secondary, 0.2);
    }
  }
  
  &.delete {
    background: rgba(#f44336, 0.1);
    color: #f44336;
    
    &:hover {
      background: rgba(#f44336, 0.2);
    }
  }
  
  &.complete {
    background: rgba(#4caf50, 0.1);
    color: #4caf50;
    
    &:hover {
      background: rgba(#4caf50, 0.2);
    }
  }
  
  &.cancel {
    background: rgba(#f44336, 0.1);
    color: #f44336;
    
    &:hover {
      background: rgba(#f44336, 0.2);
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

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@media (max-width: $breakpoint-md) {
  .stats-overview {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .quick-actions {
    flex-direction: column;
  }
  
  .schedule-meta {
    flex-direction: column;
    gap: $spacing-sm;
  }
}

.reminder-section {
  margin-bottom: $spacing-xl;
  border: 1px solid $border-card;
  border-radius: $radius-card;
  overflow: hidden;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: $bg-app;
  border-radius: $radius-card;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  width: 90%;
  max-width: 600px;
  max-height: 90%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-md;
  border-bottom: 1px solid $border-card;
  background: $bg-section;
  
  h3 {
    margin: 0;
    color: $text-primary;
  }
  
  .modal-close {
    background: none;
    border: none;
    font-size: $font-size-xl;
    cursor: pointer;
    color: $text-secondary;
    transition: color 0.2s;
    
    &:hover {
      color: $color-secondary;
    }
  }
}

.modal-body {
  padding: $spacing-md;
  overflow-y: auto;
  flex-grow: 1;
}

.input-group {
  margin-bottom: $spacing-md;
  
  label {
    display: block;
    margin-bottom: $spacing-xs;
    color: $text-primary;
    font-weight: $font-weight-medium;
  }
  
  input[type="text"],
  input[type="number"],
  select,
  textarea {
    width: 100%;
    padding: $spacing-sm;
    border: 1px solid $border-card;
    border-radius: $radius-button;
    background: $bg-input;
    color: $text-primary;
    font-size: $font-size-sm;
  }
  
  textarea {
    min-height: 80px;
    resize: vertical;
  }
}

.recognition-examples {
  margin-top: $spacing-md;
  padding-top: $spacing-md;
  border-top: 1px solid $border-card;
  
  h4 {
    margin-bottom: $spacing-xs;
    color: $text-primary;
  }
  
  ul {
    list-style: none;
    padding: 0;
    margin: 0;
    
    li {
      color: $text-secondary;
      font-size: $font-size-sm;
      margin-bottom: $spacing-xs;
    }
  }
}

.gift-filters {
  margin-bottom: $spacing-md;
  padding-bottom: $spacing-md;
  border-bottom: 1px solid $border-card;
  
  .input-group {
    margin-bottom: $spacing-sm;
  }
}

.gift-list {
  margin-top: $spacing-md;
  padding-top: $spacing-md;
  border-top: 1px solid $border-card;
  
  h4 {
    margin-bottom: $spacing-xs;
    color: $text-primary;
  }
  
  .gift-items {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: $spacing-md;
  }
  
  .gift-item {
    display: flex;
    align-items: center;
    gap: $spacing-md;
    background: $bg-section;
    border-radius: $radius-card;
    border: 1px solid $border-card;
    padding: $spacing-md;
  }
  
  .gift-image {
    width: 60px;
    height: 60px;
    overflow: hidden;
    border-radius: $radius-card;
    flex-shrink: 0;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    
    .gift-placeholder {
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      background: $bg-input;
      color: $text-secondary;
      font-size: 2rem;
    }
  }
  
  .gift-info {
    flex-grow: 1;
    
    h5 {
      margin: 0 0 $spacing-xs 0;
      color: $text-primary;
      font-size: $font-size-md;
    }
    
    p {
      color: $text-secondary;
      font-size: $font-size-sm;
      margin-bottom: $spacing-xs;
    }
    
    .gift-meta {
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: $text-secondary;
      font-size: $font-size-xs;
    }
  }
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: $spacing-sm;
  padding: $spacing-md;
  border-top: 1px solid $border-card;
  background: $bg-section;
  
  .btn-secondary {
    background: $bg-app;
    color: $text-primary;
    border: 1px solid $border-card;
    padding: $spacing-sm $spacing-md;
    border-radius: $radius-button;
    cursor: pointer;
    transition: all 0.2s;
    
    &:hover {
      background: rgba($color-secondary, 0.1);
      border-color: $color-secondary;
    }
  }
  
  .btn-primary {
    background: $color-secondary;
    color: white;
    border: none;
    padding: $spacing-sm $spacing-md;
    border-radius: $radius-button;
    cursor: pointer;
    transition: all 0.2s;
    
    &:hover {
      background: color.adjust($color-secondary, $lightness: -10%);
    }
    &:disabled {
      background: $bg-input;
      color: $text-secondary;
      cursor: not-allowed;
    }
  }
}
</style>


