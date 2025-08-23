<template>
  <div class="growth-archive">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>🌱 成长档案</h1>
      <p class="subtitle">记录关系里程碑，追踪成长轨迹</p>
      
      <!-- 快速开始指南 -->
      <div class="quick-start-guide">
        <div class="guide-tabs">
          <button 
            v-for="guide in quickGuides" 
            :key="guide.key"
            :class="['guide-tab', { active: activeGuide === guide.key }]"
            @click="activeGuide = guide.key"
          >
            {{ guide.label }}
          </button>
        </div>
        
        <div v-if="activeGuide" class="guide-content">
          <div class="guide-header">
            <h3>{{ getActiveGuide().title }}</h3>
            <button @click="activeGuide = ''" class="close-guide-btn">×</button>
          </div>
          <div class="guide-steps">
            <div v-for="(step, index) in getActiveGuide().steps" :key="index" class="guide-step">
              <span class="step-number">{{ index + 1 }}</span>
              <p>{{ step }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 功能导航 -->
    <div class="nav-tabs">
      <button 
        v-for="tab in tabs" 
        :key="tab.key"
        :class="['nav-tab', { active: activeTab === tab.key }]"
        @click="activeTab = tab.key"
      >
        {{ tab.label }}
      </button>
    </div>

    <!-- 里程碑管理模块 -->
    <div v-if="activeTab === 'milestones'" class="tab-content">
      <div class="milestones-section">
        <div class="section-header">
          <h2>关系里程碑</h2>
          <div class="header-actions">
            <div class="search-filter">
              <input 
                v-model="milestoneSearch" 
                type="text" 
                placeholder="搜索里程碑..." 
                class="search-input"
              />
              <select v-model="milestoneTypeFilter" class="filter-select">
                <option value="">所有类型</option>
                <option v-for="type in milestoneTypes" :key="type.value" :value="type.value">
                  {{ type.label }}
                </option>
              </select>
            </div>
            <button @click="showMilestoneForm = true" class="add-btn">
              ➕ 添加里程碑
            </button>
          </div>
        </div>

        <!-- 里程碑列表 -->
        <div class="milestones-grid">
          <div 
            v-for="milestone in filteredMilestones" 
            :key="milestone.id" 
            class="milestone-card"
            @click="selectMilestone(milestone)"
          >
            <div class="milestone-icon" :class="milestone.milestoneType">
              {{ getMilestoneIcon(milestone.milestoneType) }}
            </div>
            <div class="milestone-content">
              <h3>{{ milestone.title }}</h3>
              <p>{{ milestone.description }}</p>
              <div class="milestone-meta">
                <span class="date">{{ formatDate(milestone.milestoneDate) }}</span>
                <span class="location" v-if="milestone.location">{{ milestone.location }}</span>
                <span class="emotion-score" v-if="milestone.emotionScore">
                  💖 {{ milestone.emotionScore }}/10
                </span>
              </div>
              <div class="milestone-tags" v-if="milestone.tags && milestone.tags.length">
                <span v-for="tag in milestone.tags" :key="tag" class="tag">
                  {{ tag }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- 里程碑表单 -->
        <div v-if="showMilestoneForm" class="milestone-form-overlay">
          <div class="milestone-form">
            <div class="form-header">
              <h3>{{ editingMilestone ? '编辑里程碑' : '添加里程碑' }}</h3>
              <button @click="closeMilestoneForm" class="close-btn">×</button>
            </div>
            
            <form @submit.prevent="saveMilestone">
              <div class="form-group">
                <label>里程碑类型</label>
                <select v-model="milestoneForm.milestoneType" required>
                  <option value="">选择类型</option>
                  <option v-for="type in milestoneTypes" :key="type.value" :value="type.value">
                    {{ type.label }}
                  </option>
                </select>
              </div>
              
              <div class="form-group">
                <label>标题</label>
                <input v-model="milestoneForm.title" type="text" required placeholder="里程碑标题" />
              </div>
              
              <div class="form-group">
                <label>描述</label>
                <textarea v-model="milestoneForm.description" rows="3" placeholder="里程碑描述"></textarea>
              </div>
              
              <div class="form-group">
                <label>日期</label>
                <input v-model="milestoneForm.milestoneDate" type="date" required />
              </div>
              
              <div class="form-group">
                <label>地点</label>
                <input v-model="milestoneForm.location" type="text" placeholder="地点（可选）" />
              </div>
              
              <div class="form-group">
                <label>情感评分 (1-10)</label>
                <input v-model="milestoneForm.emotionScore" type="number" min="1" max="10" />
              </div>
              
              <div class="form-group">
                <label>标签</label>
                <input v-model="milestoneForm.tagInput" type="text" placeholder="输入标签，用逗号分隔" />
              </div>
              
              <div class="form-actions">
                <button type="button" @click="closeMilestoneForm" class="cancel-btn">取消</button>
                <button type="submit" class="save-btn">保存</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- 成长轨迹模块 -->
    <div v-if="activeTab === 'trajectory'" class="tab-content">
      <div class="trajectory-section">
        <h2>成长轨迹</h2>
        
        <!-- 轨迹记录表单 -->
        <div class="trajectory-form">
          <h3>记录今日成长</h3>
          <form @submit.prevent="saveTrajectory">
            <div class="score-inputs">
              <div class="score-group">
                <label>沟通质量 (1-100)</label>
                <input v-model="trajectoryForm.communicationScore" type="number" min="1" max="100" />
              </div>
              
              <div class="score-group">
                <label>信任程度 (1-100)</label>
                <input v-model="trajectoryForm.trustScore" type="number" min="1" max="100" />
              </div>
              
              <div class="score-group">
                <label>相互支持 (1-100)</label>
                <input v-model="trajectoryForm.supportScore" type="number" min="1" max="100" />
              </div>
              
              <div class="score-group">
                <label>亲密度 (1-100)</label>
                <input v-model="trajectoryForm.intimacyScore" type="number" min="1" max="100" />
              </div>
            </div>
            
            <div class="form-group">
              <label>情绪状态</label>
              <select v-model="trajectoryForm.moodState">
                <option value="">选择情绪状态</option>
                <option v-for="mood in moodStates" :key="mood.value" :value="mood.value">
                  {{ mood.label }}
                </option>
              </select>
            </div>
            
            <div class="form-group">
              <label>关系笔记</label>
              <textarea v-model="trajectoryForm.relationshipNotes" rows="3" placeholder="记录今天的关系感受..."></textarea>
            </div>
            
            <div class="form-group">
              <label>改进目标</label>
              <textarea v-model="trajectoryForm.improvementGoals" rows="2" placeholder="写下需要改进的地方..."></textarea>
            </div>
            
            <button type="submit" class="save-trajectory-btn">记录轨迹</button>
          </form>
        </div>
        
        <!-- 轨迹图表 -->
        <div class="trajectory-chart">
          <h3>成长趋势</h3>
          <div class="chart-container">
            <canvas id="trajectoryChart"></canvas>
          </div>
        </div>
        
        <!-- 轨迹记录列表 -->
        <div class="trajectory-list">
          <h3>最近记录</h3>
          <div class="trajectory-items">
            <div 
              v-for="trajectory in trajectories" 
              :key="trajectory.id" 
              class="trajectory-item"
              @click="viewTrajectoryDetail(trajectory)"
            >
              <div class="trajectory-header">
                <div class="trajectory-date">{{ formatDate(trajectory.recordDate) }}</div>
                <div class="trajectory-mood" v-if="trajectory.moodState">
                  {{ getMoodEmoji(trajectory.moodState) }} {{ trajectory.moodState }}
                </div>
              </div>
              <div class="trajectory-scores">
                <div class="score-row">
                  <span class="score-item">沟通: {{ trajectory.communicationScore || 0 }}</span>
                  <span class="score-item">信任: {{ trajectory.trustScore || 0 }}</span>
                </div>
                <div class="score-row">
                  <span class="score-item">支持: {{ trajectory.supportScore || 0 }}</span>
                  <span class="score-item">亲密: {{ trajectory.intimacyScore || 0 }}</span>
                </div>
                <div class="score-row overall">
                  <span class="score-item overall">综合评分: {{ trajectory.overallScore || 0 }}</span>
                </div>
              </div>
              <div class="trajectory-notes" v-if="trajectory.relationshipNotes">
                <p>{{ trajectory.relationshipNotes }}</p>
              </div>
              <div class="trajectory-actions">
                <button @click.stop="editTrajectory(trajectory)" class="edit-trajectory-btn">
                  编辑
                </button>
                <button @click.stop="deleteTrajectory(trajectory)" class="delete-trajectory-btn">
                  删除
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 目标管理模块 -->
    <div v-if="activeTab === 'goals'" class="tab-content">
      <div class="goals-section">
        <div class="section-header">
          <h2>关系目标</h2>
          <button @click="showGoalForm = true" class="add-btn">
            ➕ 添加目标
          </button>
        </div>
        
        <!-- 目标表单 -->
        <div v-if="showGoalForm" class="goal-form-overlay">
          <div class="goal-form">
            <div class="form-header">
              <h3>{{ editingGoal ? '编辑目标' : '添加目标' }}</h3>
              <button @click="closeGoalForm" class="close-btn">×</button>
            </div>
            
            <form @submit.prevent="saveGoal">
              <div class="form-group">
                <label>目标类型</label>
                <select v-model="goalForm.goalType" required>
                  <option value="">选择类型</option>
                  <option v-for="type in goalTypes" :key="type.value" :value="type.value">
                    {{ type.label }}
                  </option>
                </select>
              </div>
              
              <div class="form-group">
                <label>目标标题</label>
                <input v-model="goalForm.title" type="text" required placeholder="目标标题" />
              </div>
              
              <div class="form-group">
                <label>目标描述</label>
                <textarea v-model="goalForm.description" rows="3" placeholder="目标描述"></textarea>
              </div>
              
              <div class="form-group">
                <label>目标完成日期</label>
                <input v-model="goalForm.targetDate" type="date" required />
              </div>
              
              <div class="form-group">
                <label>优先级</label>
                <select v-model="goalForm.priority">
                  <option value="1">高</option>
                  <option value="2">中</option>
                  <option value="3">低</option>
                </select>
              </div>
              
              <div class="form-group">
                <label>完成奖励</label>
                <input v-model="goalForm.reward" type="text" placeholder="完成奖励（可选）" />
              </div>
              
              <div class="form-actions">
                <button type="button" @click="closeGoalForm" class="cancel-btn">取消</button>
                <button type="submit" class="save-btn">保存</button>
              </div>
            </form>
          </div>
        </div>

        <!-- 目标列表 -->
        <div class="goals-grid">
          <div 
            v-for="goal in goals" 
            :key="goal.id" 
            class="goal-card"
            :class="goal.status"
          >
            <div class="goal-header">
              <h3>{{ goal.title }}</h3>
              <span class="goal-type">{{ getGoalTypeLabel(goal.goalType) }}</span>
            </div>
            
            <p class="goal-description">{{ goal.description }}</p>
            
            <div class="goal-progress">
              <div class="progress-bar">
                <div class="progress-fill" :style="{ width: goal.progress + '%' }"></div>
              </div>
              <span class="progress-text">{{ goal.progress }}%</span>
            </div>
            
            <div class="goal-meta">
              <span class="target-date">目标日期: {{ formatDate(goal.targetDate) }}</span>
              <span class="priority" :class="'priority-' + goal.priority">
                {{ getPriorityLabel(goal.priority) }}
              </span>
            </div>
            
            <div class="goal-actions">
              <button @click="editGoal(goal)" class="edit-btn">编辑</button>
              <button @click="updateGoalProgress(goal)" class="progress-btn">更新进度</button>
              <button @click="completeGoal(goal)" class="complete-btn" v-if="goal.status === 0">
                完成目标
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计分析模块 -->
    <div v-if="activeTab === 'analytics'" class="tab-content">
      <div class="analytics-section">
        <h2>成长分析</h2>
        
        <!-- 统计卡片 -->
        <div class="stats-grid">
          <div class="stat-card">
            <div class="stat-icon">🎯</div>
            <div class="stat-content">
              <h3>{{ stats.milestone?.totalCount || 0 }}</h3>
              <p>里程碑总数</p>
            </div>
          </div>
          
          <div class="stat-card">
            <div class="stat-icon">📈</div>
            <div class="stat-content">
              <h3>{{ stats.trajectory?.avgOverallScore || 0 }}</h3>
              <p>平均综合评分</p>
            </div>
          </div>
          
          <div class="stat-card">
            <div class="stat-icon">🎯</div>
            <div class="stat-content">
              <h3>{{ stats.goal?.totalCount || 0 }}</h3>
              <p>目标总数</p>
            </div>
          </div>
          
          <div class="stat-card">
            <div class="stat-icon">✅</div>
            <div class="stat-content">
              <h3>{{ stats.goal?.completionRate || 0 }}%</h3>
              <p>目标完成率</p>
            </div>
          </div>
        </div>
        
        <!-- 成长趋势图 -->
        <div class="trend-chart">
          <h3>成长趋势分析</h3>
          <div class="chart-container">
            <canvas id="trendChart"></canvas>
          </div>
        </div>
        
        <!-- 成就系统 -->
        <div class="achievements-section">
          <h3>🏆 成就徽章</h3>
          <div class="achievements-grid">
            <div 
              v-for="achievement in achievements" 
              :key="achievement.id"
              class="achievement-card"
              :class="{ unlocked: achievement.unlocked }"
            >
              <div class="achievement-icon">{{ achievement.icon }}</div>
              <div class="achievement-content">
                <h4>{{ achievement.title }}</h4>
                <p>{{ achievement.description }}</p>
                <div class="achievement-progress">
                  <div class="progress-bar">
                    <div class="progress-fill" :style="{ width: achievement.progress + '%' }"></div>
                  </div>
                  <span>{{ achievement.progress }}%</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, computed } from 'vue'
import { Chart } from 'chart.js'

// 类型定义
interface Milestone {
  id: string
  milestoneType: string
  title: string
  description: string
  milestoneDate: string
  location?: string
  emotionScore?: number
  photos?: string[]
  tags?: string[]
  isPublic: number
}

interface Trajectory {
  id: string
  recordDate: string
  communicationScore?: number
  trustScore?: number
  supportScore?: number
  intimacyScore?: number
  overallScore?: number
  moodState?: string
  relationshipNotes?: string
  improvementGoals?: string
}

interface Goal {
  id: string
  goalType: string
  title: string
  description: string
  targetDate: string
  priority: number
  status: number
  progress: number
  milestones?: string[]
  reward?: string
}

interface Stats {
  milestone?: {
    totalCount: number
    typeStats: Record<string, number>
  }
  trajectory?: {
    totalCount: number
    avgOverallScore: number
    maxScore: number
    minScore: number
  }
  goal?: {
    totalCount: number
    completedCount: number
    inProgressCount: number
    completionRate: number
  }
}

interface Achievement {
  id: string
  title: string
  description: string
  icon: string
  unlocked: boolean
  progress: number
  maxProgress: number
  reward?: string
}

// 响应式数据
const activeTab = ref('milestones')
const activeGuide = ref('')
const showMilestoneForm = ref(false)
const showGoalForm = ref(false)
const editingMilestone = ref<Milestone | null>(null)
const editingGoal = ref<Goal | null>(null)

const milestones = ref<Milestone[]>([])
const trajectories = ref<Trajectory[]>([])
const goals = ref<Goal[]>([])
const stats = ref<Stats>({})
const achievements = ref<Achievement[]>([])

// 搜索和筛选
const milestoneSearch = ref('')
const milestoneTypeFilter = ref('')
const goalSearch = ref('')
const goalTypeFilter = ref('')

// 表单数据
const milestoneForm = ref({
  milestoneType: '',
  title: '',
  description: '',
  milestoneDate: '',
  location: '',
  emotionScore: undefined as number | undefined,
  tagInput: ''
})

const trajectoryForm = ref({
  communicationScore: undefined as number | undefined,
  trustScore: undefined as number | undefined,
  supportScore: undefined as number | undefined,
  intimacyScore: undefined as number | undefined,
  moodState: '',
  relationshipNotes: '',
  improvementGoals: ''
})

const goalForm = ref({
  goalType: '',
  title: '',
  description: '',
  targetDate: '',
  priority: 2,
  reward: ''
})

// 配置数据
const tabs = [
  { key: 'milestones', label: '里程碑' },
  { key: 'trajectory', label: '成长轨迹' },
  { key: 'goals', label: '目标管理' },
  { key: 'analytics', label: '成长分析' }
]

const milestoneTypes = [
  { value: 'first_meet', label: '初次见面' },
  { value: 'first_date', label: '第一次约会' },
  { value: 'confession', label: '表白' },
  { value: 'engagement', label: '订婚' },
  { value: 'wedding', label: '结婚' },
  { value: 'anniversary', label: '纪念日' },
  { value: 'travel', label: '旅行' },
  { value: 'gift', label: '礼物' },
  { value: 'conflict_resolution', label: '冲突解决' },
  { value: 'breakthrough', label: '关系突破' }
]

const moodStates = [
  { value: 'happy', label: '开心' },
  { value: 'excited', label: '兴奋' },
  { value: 'content', label: '满足' },
  { value: 'calm', label: '平静' },
  { value: 'neutral', label: '一般' },
  { value: 'worried', label: '担心' },
  { value: 'sad', label: '难过' },
  { value: 'angry', label: '生气' },
  { value: 'frustrated', label: '沮丧' }
]

const goalTypes = [
  { value: 'communication', label: '沟通改善' },
  { value: 'trust', label: '信任建立' },
  { value: 'intimacy', label: '亲密提升' },
  { value: 'conflict', label: '冲突解决' },
  { value: 'support', label: '相互支持' },
  { value: 'quality_time', label: '共处时光' },
  { value: 'understanding', label: '相互理解' },
  { value: 'appreciation', label: '表达感激' }
]

const quickGuides = [
  {
    key: 'milestone',
    label: '里程碑指南',
    title: '如何记录关系里程碑',
    steps: [
      '点击"添加里程碑"按钮',
      '选择里程碑类型（如初次见面、表白等）',
      '填写标题、描述和日期',
      '添加地点和情感评分',
      '保存里程碑记录'
    ]
  },
  {
    key: 'trajectory',
    label: '成长轨迹指南',
    title: '如何记录成长轨迹',
    steps: [
      '切换到"成长轨迹"标签页',
      '填写各项评分（沟通、信任、支持、亲密）',
      '选择当前情绪状态',
      '记录关系笔记和改进目标',
      '保存轨迹记录'
    ]
  },
  {
    key: 'goal',
    label: '目标管理指南',
    title: '如何设置关系目标',
    steps: [
      '点击"添加目标"按钮',
      '选择目标类型和优先级',
      '设定目标完成日期',
      '定期更新目标进度',
      '完成目标获得成就感'
    ]
  }
]

// 生命周期
onMounted(() => {
  loadMockData()
  loadStats()
  nextTick(() => {
    initCharts()
  })
})

// 图表相关
let trajectoryChart: Chart | null = null
let trendChart: Chart | null = null

const initCharts = () => {
  // 初始化成长轨迹图表
  const trajectoryCtx = document.querySelector('#trajectoryChart') as HTMLCanvasElement
  if (trajectoryCtx) {
    trajectoryChart = new Chart(trajectoryCtx, {
      type: 'line',
      data: {
        labels: trajectories.value.map(t => formatDate(t.recordDate)),
        datasets: [{
          label: '综合评分',
          data: trajectories.value.map(t => t.overallScore || 0),
          borderColor: '#3498db',
          backgroundColor: 'rgba(52, 152, 219, 0.1)',
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          title: {
            display: true,
            text: '成长轨迹趋势'
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            max: 100
          }
        }
      }
    })
  }

  // 初始化成长趋势图表
  const trendCtx = document.querySelector('#trendChart') as HTMLCanvasElement
  if (trendCtx) {
    trendChart = new Chart(trendCtx, {
      type: 'radar',
      data: {
        labels: ['沟通质量', '信任程度', '相互支持', '亲密度'],
        datasets: [{
          label: '当前评分',
          data: [
            trajectories.value[0]?.communicationScore || 0,
            trajectories.value[0]?.trustScore || 0,
            trajectories.value[0]?.supportScore || 0,
            trajectories.value[0]?.intimacyScore || 0
          ],
          backgroundColor: 'rgba(52, 152, 219, 0.2)',
          borderColor: '#3498db',
          pointBackgroundColor: '#3498db'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          title: {
            display: true,
            text: '关系维度雷达图'
          }
        },
        scales: {
          r: {
            beginAtZero: true,
            max: 100
          }
        }
      }
    })
  }
}

// 方法
const loadMockData = () => {
  // 模拟里程碑数据
  milestones.value = [
    {
      id: '1',
      milestoneType: 'first_meet',
      title: '初次相遇',
      description: '在朋友的聚会上第一次见面，感觉很有缘分',
      milestoneDate: '2024-01-15',
      location: '朋友家',
      emotionScore: 8,
      tags: ['缘分', '朋友聚会'],
      isPublic: 1
    },
    {
      id: '2',
      milestoneType: 'first_date',
      title: '第一次约会',
      description: '一起看电影，聊了很多，感觉很投缘',
      milestoneDate: '2024-01-20',
      location: '电影院',
      emotionScore: 9,
      tags: ['约会', '电影', '聊天'],
      isPublic: 1
    },
    {
      id: '3',
      milestoneType: 'confession',
      title: '表白成功',
      description: '在公园里表白，对方答应了，非常开心',
      milestoneDate: '2024-02-14',
      location: '公园',
      emotionScore: 10,
      tags: ['表白', '情人节', '公园'],
      isPublic: 1
    }
  ]

  // 模拟成长轨迹数据
  trajectories.value = [
    {
      id: '1',
      recordDate: '2024-12-20',
      communicationScore: 85,
      trustScore: 90,
      supportScore: 88,
      intimacyScore: 92,
      overallScore: 89,
      moodState: 'happy',
      relationshipNotes: '今天沟通很顺畅，相互理解更深了',
      improvementGoals: '继续保持开放和诚实的沟通'
    },
    {
      id: '2',
      recordDate: '2024-12-18',
      communicationScore: 80,
      trustScore: 85,
      supportScore: 82,
      intimacyScore: 88,
      overallScore: 84,
      moodState: 'content',
      relationshipNotes: '关系稳定，相互支持',
      improvementGoals: '增加一些浪漫的小惊喜'
    }
  ]

  // 模拟目标数据
  goals.value = [
    {
      id: '1',
      goalType: 'communication',
      title: '改善沟通方式',
      description: '学习更有效的沟通技巧，减少误解',
      targetDate: '2025-01-31',
      priority: 1,
      status: 0,
      progress: 60
    },
    {
      id: '2',
      goalType: 'trust',
      title: '建立更深层信任',
      description: '通过行动证明彼此的信任',
      targetDate: '2025-02-28',
      priority: 2,
      status: 0,
      progress: 40
    }
  ]

  // 模拟成就数据
  achievements.value = [
    {
      id: '1',
      title: '初次记录',
      description: '完成第一次成长轨迹记录',
      icon: '📝',
      unlocked: true,
      progress: 100,
      maxProgress: 1
    },
    {
      id: '2',
      title: '里程碑收集者',
      description: '创建5个里程碑',
      icon: '🏆',
      unlocked: false,
      progress: 3,
      maxProgress: 5
    },
    {
      id: '3',
      title: '目标达成者',
      description: '完成3个关系目标',
      icon: '🎯',
      unlocked: false,
      progress: 0,
      maxProgress: 3
    },
    {
      id: '4',
      title: '持续记录',
      description: '连续记录7天成长轨迹',
      icon: '📊',
      unlocked: false,
      progress: 2,
      maxProgress: 7
    }
  ]
}

const loadStats = () => {
  // 模拟统计数据
  stats.value = {
    milestone: {
      totalCount: milestones.value.length,
      typeStats: {}
    },
    trajectory: {
      totalCount: trajectories.value.length,
      avgOverallScore: Math.round(
        trajectories.value.reduce((sum, t) => sum + (t.overallScore || 0), 0) / trajectories.value.length
      ),
      maxScore: Math.max(...trajectories.value.map(t => t.overallScore || 0)),
      minScore: Math.min(...trajectories.value.map(t => t.overallScore || 0))
    },
    goal: {
      totalCount: goals.value.length,
      completedCount: goals.value.filter(g => g.status === 1).length,
      inProgressCount: goals.value.filter(g => g.status === 0).length,
      completionRate: Math.round(
        (goals.value.filter(g => g.status === 1).length / goals.value.length) * 100
      )
    }
  }
}

const selectMilestone = (milestone: Milestone) => {
  editingMilestone.value = milestone
  milestoneForm.value = {
    milestoneType: milestone.milestoneType,
    title: milestone.title,
    description: milestone.description || '',
    milestoneDate: milestone.milestoneDate,
    location: milestone.location || '',
    emotionScore: milestone.emotionScore,
    tagInput: milestone.tags ? milestone.tags.join(', ') : ''
  }
  showMilestoneForm.value = true
}

const closeMilestoneForm = () => {
  showMilestoneForm.value = false
  editingMilestone.value = null
  milestoneForm.value = {
    milestoneType: '',
    title: '',
    description: '',
    milestoneDate: '',
    location: '',
    emotionScore: undefined,
    tagInput: ''
  }
}

const closeGoalForm = () => {
  showGoalForm.value = false
  editingGoal.value = null
  goalForm.value = {
    goalType: '',
    title: '',
    description: '',
    targetDate: '',
    priority: 2,
    reward: ''
  }
}

const saveMilestone = () => {
  const tags = milestoneForm.value.tagInput
    .split(',')
    .map(tag => tag.trim())
    .filter(tag => tag.length > 0)

  if (editingMilestone.value) {
    // 更新里程碑
    const index = milestones.value.findIndex(m => m.id === editingMilestone.value?.id)
    if (index !== -1) {
      milestones.value[index] = {
        ...editingMilestone.value,
        ...milestoneForm.value,
        tags
      }
    }
  } else {
    // 创建新里程碑
    const newMilestone: Milestone = {
      id: Date.now().toString(),
      ...milestoneForm.value,
      tags,
      isPublic: 1
    }
    milestones.value.unshift(newMilestone)
  }

  closeMilestoneForm()
  loadStats()
}

const saveGoal = () => {
  if (editingGoal.value) {
    // 更新目标
    const index = goals.value.findIndex(g => g.id === editingGoal.value?.id)
    if (index !== -1) {
      goals.value[index] = {
        ...editingGoal.value,
        ...goalForm.value,
        priority: Number(goalForm.value.priority)
      }
    }
  } else {
    // 创建新目标
    const newGoal: Goal = {
      id: Date.now().toString(),
      ...goalForm.value,
      priority: Number(goalForm.value.priority),
      status: 0,
      progress: 0
    }
    goals.value.unshift(newGoal)
  }

  closeGoalForm()
  loadStats()
}

const saveTrajectory = () => {
  const newTrajectory: Trajectory = {
    id: Date.now().toString(),
    recordDate: new Date().toISOString().split('T')[0],
    ...trajectoryForm.value,
    overallScore: calculateOverallScore(trajectoryForm.value)
  }

  trajectories.value.unshift(newTrajectory)
  
  // 重置表单
  trajectoryForm.value = {
    communicationScore: undefined,
    trustScore: undefined,
    supportScore: undefined,
    intimacyScore: undefined,
    moodState: '',
    relationshipNotes: '',
    improvementGoals: ''
  }

  loadStats()
}

const calculateOverallScore = (form: any) => {
  const scores = [
    form.communicationScore,
    form.trustScore,
    form.supportScore,
    form.intimacyScore
  ].filter(score => score !== undefined && score !== null)

  return scores.length > 0 
    ? Math.round(scores.reduce((sum, score) => sum + score, 0) / scores.length)
    : 0
}

const updateGoalProgress = (goal: Goal) => {
  const newProgress = Math.min(100, goal.progress + 20)
  goal.progress = newProgress
  
  if (newProgress >= 100) {
    goal.status = 1 // 已完成
  }
  
  loadStats()
}

const editGoal = (goal: Goal) => {
  editingGoal.value = goal
  goalForm.value = {
    goalType: goal.goalType,
    title: goal.title,
    description: goal.description || '',
    targetDate: goal.targetDate,
    priority: goal.priority,
    reward: goal.reward || ''
  }
  showGoalForm.value = true
}

const completeGoal = (goal: Goal) => {
  goal.status = 1
  goal.progress = 100
  loadStats()
}

// 工具方法
const getMilestoneIcon = (type: string) => {
  const icons: Record<string, string> = {
    first_meet: '👋',
    first_date: '🎬',
    confession: '💝',
    engagement: '💍',
    wedding: '👰',
    anniversary: '🎉',
    travel: '✈️',
    gift: '🎁',
    conflict_resolution: '🤝',
    breakthrough: '🚀'
  }
  return icons[type] || '📌'
}

const getMoodEmoji = (mood: string) => {
  const emojis: Record<string, string> = {
    happy: '😊',
    excited: '🤩',
    content: '😌',
    calm: '😐',
    neutral: '😐',
    worried: '😟',
    sad: '😢',
    angry: '😠',
    frustrated: '😤'
  }
  return emojis[mood] || '😐'
}

const getGoalTypeLabel = (type: string) => {
  const labels: Record<string, string> = {
    communication: '沟通改善',
    trust: '信任建立',
    intimacy: '亲密提升',
    conflict: '冲突解决',
    support: '相互支持'
  }
  return labels[type] || type
}

const getPriorityLabel = (priority: number) => {
  const labels = ['', '高', '中', '低']
  return labels[priority] || '中'
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const getActiveGuide = () => {
  return quickGuides.find(guide => guide.key === activeGuide.value) || quickGuides[0]
}

// 数据导出功能
const exportData = () => {
  const data = {
    milestones: milestones.value,
    trajectories: trajectories.value,
    goals: goals.value,
    achievements: achievements.value,
    stats: stats.value,
    exportDate: new Date().toISOString()
  }
  
  const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `growth-archive-${new Date().toISOString().split('T')[0]}.json`
  a.click()
  URL.revokeObjectURL(url)
}

const generateReport = () => {
  const report = {
    title: '成长档案报告',
    date: new Date().toLocaleDateString('zh-CN'),
    summary: {
      totalMilestones: milestones.value.length,
      totalTrajectories: trajectories.value.length,
      totalGoals: goals.value.length,
      completedGoals: goals.value.filter(g => g.status === 1).length,
      avgScore: stats.value.trajectory?.avgOverallScore || 0
    },
    recommendations: generateRecommendations()
  }
  
  const blob = new Blob([JSON.stringify(report, null, 2)], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `growth-report-${new Date().toISOString().split('T')[0]}.json`
  a.click()
  URL.revokeObjectURL(url)
}

const generateRecommendations = () => {
  const recommendations = []
  
  if (milestones.value.length < 3) {
    recommendations.push('建议多记录一些关系中的重要时刻，这些里程碑将成为你们关系发展的见证')
  }
  
  if (stats.value.trajectory?.avgOverallScore < 70) {
    recommendations.push('关系评分较低，建议多关注沟通和相互理解，可以尝试一些关系改善活动')
  }
  
  if (goals.value.filter(g => g.status === 1).length === 0) {
    recommendations.push('还没有完成的目标，建议设定一些具体可行的短期目标，逐步提升关系质量')
  }
  
  return recommendations
}

// 计算属性
const filteredMilestones = computed(() => {
  let filtered = milestones.value
  
  if (milestoneSearch.value) {
    filtered = filtered.filter(m => 
      m.title.toLowerCase().includes(milestoneSearch.value.toLowerCase()) ||
      m.description.toLowerCase().includes(milestoneSearch.value.toLowerCase())
    )
  }
  
  if (milestoneTypeFilter.value) {
    filtered = filtered.filter(m => m.milestoneType === milestoneTypeFilter.value)
  }
  
  return filtered
})

const filteredGoals = computed(() => {
  let filtered = goals.value
  
  if (goalSearch.value) {
    filtered = filtered.filter(g => 
      g.title.toLowerCase().includes(goalSearch.value.toLowerCase()) ||
      g.description.toLowerCase().includes(goalSearch.value.toLowerCase())
    )
  }
  
  if (goalTypeFilter.value) {
    filtered = filtered.filter(g => g.goalType === goalTypeFilter.value)
  }
  
  return filtered
})
</script>

<style scoped lang="scss">
.growth-archive {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
  color: white;

  .page-header {
    text-align: center;
    margin-bottom: 30px;

    h1 {
      font-size: 2.5rem;
      margin-bottom: 10px;
      text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
    }

    .subtitle {
      font-size: 1.2rem;
      opacity: 0.9;
      margin-bottom: 25px;
    }

    .quick-start-guide {
      max-width: 800px;
      margin: 0 auto;

      .guide-tabs {
        display: flex;
        justify-content: center;
        gap: 15px;
        margin-bottom: 20px;

        .guide-tab {
          padding: 10px 20px;
          background: rgba(255, 255, 255, 0.2);
          border: 2px solid transparent;
          border-radius: 25px;
          color: white;
          cursor: pointer;
          transition: all 0.3s ease;
          backdrop-filter: blur(10px);

          &:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: translateY(-2px);
          }

          &.active {
            background: rgba(255, 255, 255, 0.4);
            border-color: rgba(255, 255, 255, 0.6);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
          }
        }
      }

      .guide-content {
        background: rgba(255, 255, 255, 0.95);
        border-radius: 20px;
        padding: 25px;
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
        color: #2c3e50;
        animation: slideDown 0.3s ease;

        .guide-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;

          h3 {
            margin: 0;
            color: #34495e;
            font-size: 1.3rem;
          }

          .close-guide-btn {
            background: none;
            border: none;
            font-size: 1.5rem;
            cursor: pointer;
            color: #7f8c8d;
            padding: 5px;
            border-radius: 50%;
            transition: all 0.3s ease;

            &:hover {
              background: #ecf0f1;
              color: #34495e;
            }
          }
        }

        .guide-steps {
          .guide-step {
            display: flex;
            align-items: flex-start;
            margin-bottom: 15px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 10px;
            transition: all 0.3s ease;

            &:hover {
              background: #e9ecef;
              transform: translateX(5px);
            }

            .step-number {
              background: #3498db;
              color: white;
              width: 25px;
              height: 25px;
              border-radius: 50%;
              display: flex;
              align-items: center;
              justify-content: center;
              font-weight: 600;
              margin-right: 15px;
              flex-shrink: 0;
            }

            p {
              margin: 0;
              color: #34495e;
              line-height: 1.5;
            }
          }
        }
      }
    }
  }

  @keyframes slideDown {
    from {
      opacity: 0;
      transform: translateY(-20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  .nav-tabs {
    display: flex;
    justify-content: center;
    margin-bottom: 30px;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 25px;
    padding: 5px;
    backdrop-filter: blur(10px);

    .nav-tab {
      padding: 12px 24px;
      border: none;
      background: transparent;
      color: white;
      border-radius: 20px;
      cursor: pointer;
      transition: all 0.3s ease;
      font-size: 1rem;

      &:hover {
        background: rgba(255, 255, 255, 0.2);
      }

      &.active {
        background: rgba(255, 255, 255, 0.3);
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
      }
    }
  }

  .tab-content {
    background: rgba(255, 255, 255, 0.95);
    border-radius: 20px;
    padding: 30px;
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
    color: #2c3e50;

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 25px;

      h2 {
        margin: 0;
        color: #34495e;
      }

      .header-actions {
        display: flex;
        align-items: center;
        gap: 20px;

        .search-filter {
          display: flex;
          gap: 10px;

          .search-input {
            padding: 8px 15px;
            border: 2px solid #e9ecef;
            border-radius: 20px;
            font-size: 0.9rem;
            width: 200px;
            transition: border-color 0.3s ease;

            &:focus {
              outline: none;
              border-color: #3498db;
            }
          }

          .filter-select {
            padding: 8px 15px;
            border: 2px solid #e9ecef;
            border-radius: 20px;
            font-size: 0.9rem;
            background: white;
            cursor: pointer;
            transition: border-color 0.3s ease;

            &:focus {
              outline: none;
              border-color: #3498db;
            }
          }
        }

        .add-btn {
          padding: 10px 20px;
          background: #3498db;
          color: white;
          border: none;
          border-radius: 25px;
          cursor: pointer;
          font-size: 0.9rem;
          transition: all 0.3s ease;

          &:hover {
            background: #2980b9;
            transform: translateY(-2px);
          }
        }
      }
    }
  }

  // 里程碑样式
  .milestones-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;

    .milestone-card {
      background: white;
      border-radius: 15px;
      padding: 20px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
      cursor: pointer;
      transition: all 0.3s ease;
      border: 2px solid transparent;

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
        border-color: #3498db;
      }

      .milestone-icon {
        font-size: 2rem;
        margin-bottom: 15px;
        text-align: center;
      }

      .milestone-content {
        h3 {
          margin: 0 0 10px 0;
          color: #2c3e50;
          font-size: 1.2rem;
        }

        p {
          margin: 0 0 15px 0;
          color: #7f8c8d;
          line-height: 1.5;
        }

        .milestone-meta {
          display: flex;
          flex-wrap: wrap;
          gap: 10px;
          margin-bottom: 15px;

          span {
            font-size: 0.8rem;
            padding: 4px 8px;
            background: #ecf0f1;
            border-radius: 12px;
            color: #34495e;
          }

          .emotion-score {
            background: #e74c3c;
            color: white;
          }
        }

        .milestone-tags {
          display: flex;
          flex-wrap: wrap;
          gap: 5px;

          .tag {
            font-size: 0.7rem;
            padding: 3px 8px;
            background: #3498db;
            color: white;
            border-radius: 10px;
          }
        }
      }
    }
  }

  // 里程碑表单样式
  .milestone-form-overlay,
  .goal-form-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;

    .milestone-form,
    .goal-form {
      background: white;
      border-radius: 20px;
      padding: 30px;
      width: 90%;
      max-width: 500px;
      max-height: 90vh;
      overflow-y: auto;

      .form-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        h3 {
          margin: 0;
          color: #2c3e50;
        }

        .close-btn {
          background: none;
          border: none;
          font-size: 1.5rem;
          cursor: pointer;
          color: #7f8c8d;
        }
      }

      .form-group {
        margin-bottom: 20px;

        label {
          display: block;
          margin-bottom: 8px;
          color: #34495e;
          font-weight: 600;
        }

        input, select, textarea {
          width: 100%;
          padding: 12px;
          border: 2px solid #e9ecef;
          border-radius: 10px;
          font-size: 1rem;
          transition: border-color 0.3s ease;

          &:focus {
            outline: none;
            border-color: #3498db;
          }
        }
      }

      .form-actions {
        display: flex;
        gap: 15px;
        justify-content: flex-end;

        button {
          padding: 12px 24px;
          border: none;
          border-radius: 10px;
          cursor: pointer;
          font-size: 1rem;
          transition: all 0.3s ease;

          &.cancel-btn {
            background: #95a5a6;
            color: white;

            &:hover {
              background: #7f8c8d;
            }
          }

          &.save-btn {
            background: #3498db;
            color: white;

            &:hover {
              background: #2980b9;
            }
          }
        }
      }
    }
  }

  // 成长轨迹样式
  .trajectory-section {
    .trajectory-form {
      background: #f8f9fa;
      border-radius: 15px;
      padding: 25px;
      margin-bottom: 30px;

      h3 {
        margin: 0 0 20px 0;
        color: #2c3e50;
      }

      .score-inputs {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 20px;
        margin-bottom: 20px;

        .score-group {
          label {
            display: block;
            margin-bottom: 8px;
            color: #34495e;
            font-weight: 600;
          }

          input {
            width: 100%;
            padding: 10px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            font-size: 1rem;
          }
        }
      }

      .save-trajectory-btn {
        width: 100%;
        padding: 15px;
        background: #27ae60;
        color: white;
        border: none;
        border-radius: 10px;
        font-size: 1.1rem;
        cursor: pointer;
        transition: all 0.3s ease;

        &:hover {
          background: #229954;
          transform: translateY(-2px);
        }
      }
    }

    .trajectory-chart {
      min-height: 300px;
      margin-bottom: 30px;
    }

    .trajectory-list {
      h3 {
        margin: 0 0 20px 0;
        color: #2c3e50;
      }

      .trajectory-items {
        .trajectory-item {
          background: white;
          border-radius: 15px;
          padding: 20px;
          margin-bottom: 15px;
          box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
          cursor: pointer;
          transition: all 0.3s ease;

          &:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
          }

          .trajectory-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;

            .trajectory-date {
              font-weight: 600;
              color: #2c3e50;
              font-size: 1.1rem;
            }

            .trajectory-mood {
              font-size: 0.9rem;
              padding: 5px 10px;
              background: #ecf0f1;
              border-radius: 15px;
              color: #34495e;
            }
          }

          .trajectory-scores {
            margin-bottom: 15px;

            .score-row {
              display: flex;
              gap: 20px;
              margin-bottom: 8px;

              &.overall {
                border-top: 1px solid #ecf0f1;
                padding-top: 8px;
                margin-top: 8px;

                .score-item.overall {
                  font-weight: 600;
                  color: #3498db;
                }
              }

              .score-item {
                font-size: 0.9rem;
                color: #7f8c8d;
              }
            }
          }

          .trajectory-notes {
            margin-bottom: 15px;

            p {
              margin: 0;
              color: #34495e;
              font-style: italic;
              line-height: 1.5;
            }
          }

          .trajectory-actions {
            display: flex;
            gap: 10px;

            button {
              padding: 6px 12px;
              border: none;
              border-radius: 6px;
              cursor: pointer;
              font-size: 0.8rem;
              transition: all 0.3s ease;

              &.edit-trajectory-btn {
                background: #9b59b6;
                color: white;

                &:hover {
                  background: #8e44ad;
                }
              }

              &.delete-trajectory-btn {
                background: #e74c3c;
                color: white;

                &:hover {
                  background: #c0392b;
                }
              }
            }
          }
        }
      }
    }
  }

  // 目标样式
  .goals-section {
    .goal-card {
      background: white;
      border-radius: 15px;
      padding: 20px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
      margin-bottom: 20px;
      transition: all 0.3s ease;
      border: 2px solid transparent;

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
      }

      &.0 { // 进行中
        border-left: 4px solid #3498db;
      }

      &.1 { // 已完成
        border-left: 4px solid #27ae60;
        opacity: 0.8;
      }

      &.2 { // 已延期
        border-left: 4px solid #f39c12;
      }

      &.3 { // 已放弃
        border-left: 4px solid #e74c3c;
        opacity: 0.6;
      }

      .goal-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 15px;

        h3 {
          margin: 0;
          color: #2c3e50;
          font-size: 1.2rem;
        }

        .goal-type {
          font-size: 0.8rem;
          padding: 4px 8px;
          background: #ecf0f1;
          border-radius: 12px;
          color: #34495e;
        }
      }

      .goal-description {
        margin: 0 0 20px 0;
        color: #7f8c8d;
        line-height: 1.5;
      }

      .goal-progress {
        margin-bottom: 20px;

        .progress-bar {
          width: 100%;
          height: 8px;
          background: #ecf0f1;
          border-radius: 4px;
          overflow: hidden;
          margin-bottom: 8px;

          .progress-fill {
            height: 100%;
            background: linear-gradient(90deg, #3498db, #27ae60);
            transition: width 0.3s ease;
          }
        }

        .progress-text {
          font-size: 0.9rem;
          color: #34495e;
          font-weight: 600;
        }
      }

      .goal-meta {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        .target-date {
          font-size: 0.8rem;
          color: #7f8c8d;
        }

        .priority {
          font-size: 0.8rem;
          padding: 4px 8px;
          border-radius: 12px;
          color: white;

          &.priority-1 {
            background: #e74c3c;
          }

          &.priority-2 {
            background: #f39c12;
          }

          &.priority-3 {
            background: #95a5a6;
          }
        }
      }

      .goal-actions {
        display: flex;
        gap: 10px;

        button {
          padding: 8px 16px;
          border: none;
          border-radius: 8px;
          cursor: pointer;
          font-size: 0.9rem;
          transition: all 0.3s ease;

          &.edit-btn {
            background: #9b59b6;
            color: white;

            &:hover {
              background: #8e44ad;
            }
          }

          &.progress-btn {
            background: #3498db;
            color: white;

            &:hover {
              background: #2980b9;
            }
          }

          &.complete-btn {
            background: #27ae60;
            color: white;

            &:hover {
              background: #229954;
            }
          }
        }
      }
    }
  }

  // 统计分析样式
  .analytics-section {
    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 25px;

      h2 {
        margin: 0;
        color: #34495e;
      }

      .header-actions {
        display: flex;
        gap: 15px;

        .export-btn, .report-btn {
          padding: 10px 20px;
          border: none;
          border-radius: 20px;
          cursor: pointer;
          font-size: 0.9rem;
          transition: all 0.3s ease;
          display: flex;
          align-items: center;
          gap: 8px;

          &:hover {
            transform: translateY(-2px);
          }
        }

        .export-btn {
          background: #27ae60;
          color: white;

          &:hover {
            background: #229954;
          }
        }

        .report-btn {
          background: #9b59b6;
          color: white;

          &:hover {
            background: #8e44ad;
          }
        }
      }
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 20px;
      margin-bottom: 30px;

      .stat-card {
        background: white;
        border-radius: 15px;
        padding: 20px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        text-align: center;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-5px);
          box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
        }

        .stat-icon {
          font-size: 2.5rem;
          margin-bottom: 15px;
        }

        .stat-content {
          h3 {
            margin: 0 0 10px 0;
            color: #2c3e50;
            font-size: 2rem;
            font-weight: 700;
          }

          p {
            margin: 0;
            color: #7f8c8d;
            font-size: 0.9rem;
          }
        }
      }
    }

    .trend-chart {
      background: white;
      border-radius: 15px;
      padding: 25px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
      margin-bottom: 30px;

      h3 {
        margin: 0 0 20px 0;
        color: #2c3e50;
      }

      .chart-container {
        min-height: 300px;
        position: relative;
      }
    }

    .achievements-section {
      h3 {
        margin: 0 0 20px 0;
        color: #2c3e50;
      }

      .achievements-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 20px;

        .achievement-card {
          background: white;
          border-radius: 15px;
          padding: 20px;
          box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
          transition: all 0.3s ease;
          border: 2px solid transparent;

          &:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
          }

          &.unlocked {
            border-color: #f39c12;
            background: linear-gradient(135deg, #fff9e6 0%, #ffffff 100%);

            .achievement-icon {
              animation: achievementUnlock 0.6s ease;
            }
          }

          .achievement-icon {
            font-size: 3rem;
            text-align: center;
            margin-bottom: 15px;
          }

          .achievement-content {
            h4 {
              margin: 0 0 10px 0;
              color: #2c3e50;
              font-size: 1.1rem;
              text-align: center;
            }

            p {
              margin: 0 0 15px 0;
              color: #7f8c8d;
              text-align: center;
              line-height: 1.5;
            }

            .achievement-progress {
              display: flex;
              align-items: center;
              gap: 10px;

              .progress-bar {
                flex: 1;
                height: 8px;
                background: #ecf0f1;
                border-radius: 4px;
                overflow: hidden;

                .progress-fill {
                  height: 100%;
                  background: linear-gradient(90deg, #3498db, #27ae60);
                  transition: width 0.3s ease;
                }
              }

              span {
                font-size: 0.8rem;
                color: #34495e;
                font-weight: 600;
                min-width: 40px;
              }
            }
          }
        }
      }
    }
  }

  @keyframes achievementUnlock {
    0% {
      transform: scale(1);
    }
    50% {
      transform: scale(1.2);
    }
    100% {
      transform: scale(1);
    }
  }
  }

  // 响应式设计
  @media (max-width: 768px) {
    .growth-archive {
      padding: 15px;

      .page-header h1 {
        font-size: 2rem;
      }

      .nav-tabs {
        flex-wrap: wrap;
        gap: 10px;

        .nav-tab {
          padding: 10px 20px;
          font-size: 0.9rem;
        }
      }

      .tab-content {
        padding: 20px;
      }

      .milestones-grid {
        grid-template-columns: 1fr;
      }

      .score-inputs {
        grid-template-columns: 1fr;
      }

      .stats-grid {
        grid-template-columns: repeat(2, 1fr);
      }
    }
  }

</style>