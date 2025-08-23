<template>
    <div class="communication-sandbox">
      <!-- 页面头部 -->
      <div class="page-header">
        <h1>沟通沙盒</h1>
        <p class="subtitle">练习沟通技巧，提升关系质量</p>
        
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
          
          <div v-if="activeGuide" class="guide-content active">
            <div class="guide-header">
              <h3>{{ getActiveGuide().title }}</h3>
              <button @click="activeGuide = ''" class="close-guide-btn">×</button>
            </div>
            <div class="guide-steps">
              <ol>
                <li v-for="(step, index) in getActiveGuide().steps" :key="index">
                  {{ step }}
                </li>
              </ol>
            </div>
            <div class="guide-tips">
              <h4>💡 小贴士</h4>
              <p>{{ getActiveGuide().tip }}</p>
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
      
      <!-- 搜索功能 -->
      <div class="search-section">
        <div class="search-box">
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="搜索场景、技巧或内容..."
            class="search-input"
            @input="performSearch"
          />
          <button class="search-btn">
            🔍
          </button>
        </div>
        
        <div v-if="searchResults.length > 0" class="search-results">
          <h3>搜索结果</h3>
          <div class="results-grid">
            <div 
              v-for="result in searchResults" 
              :key="result.id"
              class="result-item"
              @click="navigateToResult(result)"
            >
              <div class="result-icon">{{ getResultIcon(result.type) }}</div>
              <div class="result-content">
                <h4>{{ result.title }}</h4>
                <p>{{ result.description }}</p>
                <span class="result-type">{{ getResultTypeName(result.type) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
  
      <!-- 场景模拟模块 -->
      <div v-if="activeTab === 'scenarios'" class="tab-content">
        <div class="scenario-section">
          <div class="section-header">
            <h2>沟通场景模拟</h2>
            <div class="filters">
              <select v-model="selectedCategory" @change="loadScenarios">
                <option value="">所有类别</option>
                <option v-for="cat in categories" :key="cat" :value="cat">
                  {{ getCategoryDisplayName(cat) }}
                </option>
              </select>
              <select v-model="selectedDifficulty" @change="loadScenarios">
                <option value="">所有难度</option>
                <option value="easy">简单</option>
                <option value="medium">中等</option>
                <option value="hard">困难</option>
              </select>
            </div>
          </div>
  
          <div class="scenarios-grid">
            <div 
              v-for="scenario in scenarios" 
              :key="scenario.id"
              :class="['scenario-card', { selected: selectedScenario?.id === scenario.id }]"
              @click="selectScenario(scenario)"
            >
              <div class="scenario-header">
                <h3>{{ scenario.title }}</h3>
                <span :class="['difficulty-badge', scenario.difficulty]">
                  {{ getDifficultyDisplayName(scenario.difficulty) }}
                </span>
              </div>
              <p class="scenario-description">{{ scenario.description }}</p>
              <div class="scenario-tags">
                <span v-for="tag in scenario.tags" :key="tag" class="tag">{{ tag }}</span>
              </div>
            </div>
          </div>
  
          <!-- 场景详情和对话练习 -->
          <div v-if="selectedScenario" class="scenario-detail">
            <div class="detail-header">
              <h3>{{ selectedScenario.title }}</h3>
              <button @click="selectedScenario = null" class="close-btn">×</button>
            </div>
            <div class="scenario-context">
              <h4>场景背景</h4>
              <p>{{ selectedScenario.context }}</p>
            </div>
            
            <div class="dialogue-practice">
              <h4>对话练习</h4>
              <div class="input-section">
                <textarea 
                  v-model="userInput" 
                  placeholder="输入你想说的话..."
                  rows="3"
                  class="user-input"
                ></textarea>
                <button 
                  @click="generateSuggestions" 
                  :disabled="!userInput.trim() || loading"
                  class="generate-btn"
                >
                  <span v-if="loading">生成中...</span>
                  <span v-else>生成建议</span>
                </button>
              </div>
              
              <div v-if="suggestions" class="suggestions-section">
                <h5>AI建议</h5>
                <div class="suggestion-content">
                  <div class="analysis">
                    <h6>输入分析</h6>
                    <p>{{ suggestions.user_input_analysis?.analysis || '暂无分析' }}</p>
                  </div>
                  <div class="suggestions">
                    <h6>改进建议</h6>
                    <ul>
                      <li v-for="(suggestion, index) in suggestions.suggestions?.improvements" :key="index">
                        {{ suggestion }}
                      </li>
                    </ul>
                  </div>
                  <div class="next-steps">
                    <h6>下一步行动</h6>
                    <ul>
                      <li v-for="(step, index) in suggestions.next_steps" :key="index">
                        {{ step }}
                      </li>
                    </ul>
                  </div>
                </div>
                
                <!-- 用户反馈 -->
                <div class="feedback-section">
                  <h6>这个建议对你有帮助吗？</h6>
                  <div class="feedback-buttons">
                    <button 
                      @click="submitFeedback('helpful')" 
                      :class="['feedback-btn', 'helpful', { active: userFeedback === 'helpful' }]"
                    >
                      👍 有帮助
                    </button>
                    <button 
                      @click="submitFeedback('neutral')" 
                      :class="['feedback-btn', 'neutral', { active: userFeedback === 'neutral' }]"
                    >
                      😐 一般
                    </button>
                    <button 
                      @click="submitFeedback('unhelpful')" 
                      :class="['feedback-btn', 'unhelpful', { active: userFeedback === 'unhelpful' }]"
                    >
                      👎 没帮助
                    </button>
                  </div>
                  
                  <div v-if="userFeedback" class="feedback-comment">
                    <textarea 
                      v-model="feedbackComment" 
                      placeholder="请分享你的具体感受或建议..."
                      rows="2"
                      class="feedback-textarea"
                    ></textarea>
                    <button @click="saveFeedback" class="save-feedback-btn">保存反馈</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
  
      <!-- 技巧练习模块 -->
      <div v-if="activeTab === 'skills'" class="tab-content">
        <div class="skills-section">
          <h2>沟通技巧练习</h2>
          <div class="skills-grid">
            <div 
              v-for="skill in availableSkills" 
              :key="skill"
              :class="['skill-card', { active: selectedSkill === skill }]"
              @click="selectSkill(skill)"
            >
              <h3>{{ getSkillDisplayName(skill) }}</h3>
              <p>{{ getSkillDescription(skill) }}</p>
            </div>
          </div>
  
          <div v-if="selectedSkill && skillPractice" class="skill-practice">
            <div class="practice-header">
              <h3>{{ getSkillDisplayName(selectedSkill) }}练习</h3>
              <button @click="selectedSkill = null" class="close-btn">×</button>
            </div>
            
            <div class="practice-content">
              <div class="tips-section">
                <h4>核心要点</h4>
                <ul>
                  <li v-for="(tip, index) in skillPractice.tips" :key="index">{{ tip }}</li>
                </ul>
              </div>
              
              <div class="exercises-section">
                <h4>练习方法</h4>
                <ul>
                  <li v-for="(exercise, index) in skillPractice.practice_exercises" :key="index">
                    {{ exercise }}
                  </li>
                </ul>
              </div>
              
              <div class="goal-section">
                <h4>今日目标</h4>
                <p>{{ skillPractice.daily_goal }}</p>
                <div class="progress-tracking">
                  <h5>进度追踪</h5>
                  <p>{{ skillPractice.progress_tracking }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
  
      <!-- 冲突解决指导模块 -->
      <div v-if="activeTab === 'conflict'" class="tab-content">
        <div class="conflict-section">
          <h2>冲突解决指导</h2>
          <div class="conflict-types">
            <button 
              v-for="type in conflictTypes" 
              :key="type.key"
              :class="['conflict-type-btn', { active: selectedConflictType === type.key }]"
              @click="selectConflictType(type.key)"
            >
              {{ type.label }}
            </button>
          </div>
  
          <div v-if="conflictGuide" class="conflict-guide">
            <div class="guide-content">
              <div v-if="conflictGuide.warning_signs" class="guide-section">
                <h4>⚠️ 警告信号</h4>
                <ul>
                  <li v-for="(sign, index) in conflictGuide.warning_signs" :key="index">
                    {{ sign }}
                  </li>
                </ul>
              </div>
              
              <div v-if="conflictGuide.immediate_actions" class="guide-section">
                <h4>🚨 立即行动</h4>
                <ul>
                  <li v-for="(action, index) in conflictGuide.immediate_actions" :key="index">
                    {{ action }}
                  </li>
                </ul>
              </div>
              
              <div v-if="conflictGuide.step_by_step_process" class="guide-section">
                <h4>📋 解决步骤</h4>
                <ol>
                  <li v-for="(step, index) in conflictGuide.step_by_step_process" :key="index">
                    {{ step }}
                  </li>
                </ol>
              </div>
              
              <div v-if="conflictGuide.communication_tools" class="guide-section">
                <h4>🛠️ 沟通工具</h4>
                <ul>
                  <li v-for="(tool, index) in conflictGuide.communication_tools" :key="index">
                    {{ tool }}
                  </li>
                </ul>
              </div>
              
              <div v-if="conflictGuide.prevention_tips" class="guide-section">
                <h4>💡 预防建议</h4>
                <ul>
                  <li v-for="(tip, index) in conflictGuide.prevention_tips" :key="index">
                    {{ tip }}
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
  
      <!-- 对话模板生成模块 -->
      <div v-if="activeTab === 'templates'" class="tab-content">
        <div class="templates-section">
          <h2>对话模板生成</h2>
          <div class="template-form">
            <div class="form-group">
              <label>情况描述</label>
              <textarea 
                v-model="templateSituation" 
                placeholder="描述你想要沟通的情况..."
                rows="3"
              ></textarea>
            </div>
            
            <div class="form-group">
              <label>情感状态</label>
              <select v-model="templateEmotion">
                <option value="">选择情感状态</option>
                <option value="happy">开心</option>
                <option value="sad">难过</option>
                <option value="angry">生气</option>
                <option value="worried">担心</option>
                <option value="excited">兴奋</option>
                <option value="calm">平静</option>
              </select>
            </div>
            
            <button 
              @click="generateTemplate" 
              :disabled="!templateSituation.trim() || !templateEmotion || loading"
              class="generate-template-btn"
            >
              <span v-if="loading">生成中...</span>
              <span v-else>生成模板</span>
            </button>
          </div>

          <div v-if="dialogueTemplate" class="template-result">
            <h3>对话模板</h3>
            <div class="template-content">
              <div v-for="(template, key) in dialogueTemplate.templates" :key="key" class="template-item">
                <h4>{{ getTemplateTypeDisplayName(key) }}</h4>
                <p>{{ template }}</p>
              </div>
              
              <div class="usage-tips">
                <h4>使用建议</h4>
                <ul>
                  <li v-for="(tip, index) in dialogueTemplate.usage_tips" :key="index">
                    {{ tip }}
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 进度追踪模块 -->
      <div v-if="activeTab === 'progress'" class="tab-content">
        <div class="progress-section">
          <h2>学习进度追踪</h2>
          
          <div class="progress-overview">
            <div class="progress-card">
              <h3>本周练习</h3>
              <div class="progress-circle">
                <span class="progress-number">{{ weeklyProgress }}%</span>
                <span class="progress-label">完成度</span>
              </div>
              <p>本周已练习 {{ weeklyPracticeCount }} 次</p>
            </div>
            
            <div class="progress-card">
              <h3>技能掌握</h3>
              <div class="skills-progress">
                <div v-for="skill in skillsProgress" :key="skill.name" class="skill-progress-item">
                  <span class="skill-name">{{ getSkillDisplayName(skill.name) }}</span>
                  <div class="progress-bar">
                    <div class="progress-fill" :style="{ width: skill.progress + '%' }"></div>
                  </div>
                  <span class="progress-text">{{ skill.progress }}%</span>
                </div>
              </div>
            </div>
            
            <div class="progress-card">
              <h3>学习统计</h3>
              <div class="stats-grid">
                <div class="stat-item">
                  <span class="stat-number">{{ totalScenarios }}</span>
                  <span class="stat-label">练习场景</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number">{{ totalPracticeTime }}</span>
                  <span class="stat-label">练习时长(分钟)</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number">{{ improvementScore }}</span>
                  <span class="stat-label">改进评分</span>
                </div>
              </div>
            </div>
          </div>
          
          <div class="recent-activities">
            <h3>最近活动</h3>
            <div class="activity-list">
              <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
                <div class="activity-icon" :class="activity.type">
                  {{ getActivityIcon(activity.type) }}
                </div>
                <div class="activity-content">
                  <h4>{{ activity.title }}</h4>
                  <p>{{ activity.description }}</p>
                  <span class="activity-time">{{ activity.time }}</span>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 成就系统 -->
          <div class="achievements-section">
            <h3>🏆 成就徽章</h3>
            <div class="achievements-grid">
              <div 
                v-for="achievement in achievements" 
                :key="achievement.id"
                :class="['achievement-item', { unlocked: achievement.unlocked }]"
              >
                <div class="achievement-icon">
                  {{ achievement.icon }}
                </div>
                <div class="achievement-info">
                  <h4>{{ achievement.title }}</h4>
                  <p>{{ achievement.description }}</p>
                  <span class="achievement-progress">
                    {{ achievement.progress }}/{{ achievement.target }}
                  </span>
                </div>
                <div v-if="achievement.unlocked" class="achievement-badge">
                  ✓
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
  
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-overlay">
        <div class="loading-spinner"></div>
        <p>处理中...</p>
      </div>
  
      <!-- 错误提示 -->
      <div v-if="error" class="error-message">
        <p>{{ error }}</p>
        <button @click="error = ''" class="close-error">×</button>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted } from 'vue'
  import { communicationAPI, type Scenario, type DialogueSuggestionResponse, type SkillPracticeResponse, type ConflictGuideResponse, type DialogueTemplateResponse } from '@/api/communication'
  
  // 类型定义
  interface QuickGuide {
    key: string
    label: string
    title: string
    steps: string[]
    tip: string
  }

  interface Achievement {
    id: string
    title: string
    description: string
    icon: string
    progress: number
    target: number
    unlocked: boolean
  }

  interface SearchResult {
    id: string
    type: string
    title: string
    description: string
  }

  // 响应式数据
  const activeTab = ref('scenarios')
  const loading = ref(false)
  const error = ref('')
  
  // 场景相关
  const scenarios = ref<Scenario[]>([])
  const categories = ref<string[]>([])
  const selectedCategory = ref('')
  const selectedDifficulty = ref('')
  const selectedScenario = ref<Scenario | null>(null)
  const userInput = ref('')
  const suggestions = ref<DialogueSuggestionResponse | null>(null)
  
  // 技巧练习相关
  const availableSkills = ref<string[]>([])
  const selectedSkill = ref('')
  const skillPractice = ref<SkillPracticeResponse | null>(null)
  
  // 冲突解决相关
  const selectedConflictType = ref('')
  const conflictGuide = ref<ConflictGuideResponse | null>(null)
  
  // 模板生成相关
  const templateSituation = ref('')
  const templateEmotion = ref('')
  const dialogueTemplate = ref<DialogueTemplateResponse | null>(null)
  
  // 用户反馈相关
  const userFeedback = ref('')
  const feedbackComment = ref('')
  
  // 进度追踪相关
  const weeklyProgress = ref(0)
  const weeklyPracticeCount = ref(0)
  const skillsProgress = ref<{ name: string; progress: number }[]>([])
  const totalScenarios = ref(0)
  const totalPracticeTime = ref(0)
  const improvementScore = ref(0)
  const recentActivities = ref<{ id: string; type: string; title: string; description: string; time: string }[]>([])
  
  // 标签页配置
  const tabs = [
    { key: 'scenarios', label: '场景模拟' },
    { key: 'skills', label: '技巧练习' },
    { key: 'conflict', label: '冲突解决' },
    { key: 'templates', label: '对话模板' },
    { key: 'progress', label: '进度追踪' }
  ]
  
  // 冲突类型
  const conflictTypes = [
    { key: 'escalation', label: '冲突升级' },
    { key: 'resolution', label: '冲突解决' }
  ]
  
  // 快速开始指南
  const activeGuide = ref('')
  const quickGuides = ref<QuickGuide[]>([
    { key: 'scenario', label: '从场景模拟开始', title: '如何开始使用沟通沙盒', steps: ['点击导航栏中的"场景模拟"标签', '选择一个你感兴趣的场景', '阅读场景背景并尝试模拟对话'], tip: '场景模拟是练习沟通技巧的基础，通过模拟不同情境下的对话，你可以更好地理解沟通的要点。' },
    { key: 'skill', label: '从技巧练习开始', title: '如何提升沟通技巧', steps: ['点击导航栏中的"技巧练习"标签', '选择一个你想要提升的技巧', '跟随练习内容进行实践'], tip: '技巧练习模块提供了具体的沟通技巧，通过反复练习，你可以更好地掌握这些技巧。' },
    { key: 'conflict', label: '从冲突解决开始', title: '如何处理冲突', steps: ['点击导航栏中的"冲突解决"标签', '了解冲突的类型', '学习如何预防和解决冲突'], tip: '冲突解决模块帮助你理解冲突的本质，并提供实用的解决方案。' },
    { key: 'template', label: '从对话模板开始', title: '如何生成对话模板', steps: ['点击导航栏中的"对话模板"标签', '描述你想要沟通的情况', '选择情感状态并生成模板'], tip: '对话模板可以作为你沟通的参考，帮助你更好地表达自己的想法和感受。' },
    { key: 'progress', label: '从进度追踪开始', title: '如何追踪学习进度', steps: ['点击导航栏中的"进度追踪"标签', '查看本周练习完成度', '了解技能掌握情况'], tip: '进度追踪模块让你清楚地知道自己的学习进度，帮助你更好地规划学习计划。' }
  ])
  
  // 计算属性
  const getCategoryDisplayName = (category: string): string => {
    const names: Record<string, string> = {
      'relationship_conflict': '关系冲突',
      'emotional_support': '情感支持',
      'communication_improvement': '沟通改善'
    }
    return names[category] || category
  }
  
  const getDifficultyDisplayName = (difficulty: string): string => {
    const names: Record<string, string> = {
      'easy': '简单',
      'medium': '中等',
      'hard': '困难'
    }
    return names[difficulty] || difficulty
  }
  
  const getSkillDisplayName = (skill: string): string => {
    const names: Record<string, string> = {
      'active_listening': '积极倾听',
      'nonviolent_communication': '非暴力沟通',
      'conflict_resolution': '冲突解决',
      'emotional_expression': '情感表达'
    }
    return names[skill] || skill
  }
  
  const getSkillDescription = (skill: string): string => {
    const descriptions: Record<string, string> = {
      'active_listening': '学会真正倾听对方，理解对方的感受和需求',
      'nonviolent_communication': '使用非暴力沟通方式，避免指责和攻击',
      'conflict_resolution': '掌握解决冲突的技巧，寻找双赢的解决方案',
      'emotional_expression': '学会健康地表达自己的情感和需求'
    }
    return descriptions[skill] || ''
  }
  
  const getTemplateTypeDisplayName = (type: string): string => {
    const names: Record<string, string> = {
      'opening_statements': '开场白',
      'feeling_expressions': '情感表达',
      'understanding_questions': '理解性问题',
      'resolution_suggestions': '解决建议'
    }
    return names[type] || type
  }
  
  const getActivityIcon = (type: string): string => {
    if (type === 'practice') return '📚'
    if (type === 'template') return '💬'
    if (type === 'conflict') return '⚔️'
    if (type === 'skill') return '🧠'
    return '🔄'
  }
  
  const getActiveGuide = (): QuickGuide | null => {
    return quickGuides.value.find((guide: QuickGuide) => guide.key === activeGuide.value) || null
  }
  
  // 方法
  const loadScenarios = async (): Promise<void> => {
    try {
      loading.value = true
      const response = await communicationAPI.getScenarios(selectedCategory.value, selectedDifficulty.value)
      scenarios.value = response.scenarios
    } catch (err: any) {
      error.value = err.message || '加载场景失败'
    } finally {
      loading.value = false
    }
  }
  
  const loadCategories = async (): Promise<void> => {
    try {
      categories.value = await communicationAPI.getScenarioCategories()
    } catch (err: any) {
      console.error('加载类别失败:', err)
    }
  }
  
  const loadSkills = async (): Promise<void> => {
    try {
      availableSkills.value = await communicationAPI.getAvailableSkills()
    } catch (err: any) {
      console.error('加载技巧失败:', err)
    }
  }
  
  const selectScenario = (scenario: Scenario): void => {
    selectedScenario.value = scenario
    userInput.value = ''
    suggestions.value = null
  }
  
  // 生命周期
  onMounted(async () => {
    // 加载模拟数据
    loadMockData()
    
    // 尝试加载真实数据（如果有后端服务）
    try {
      await Promise.all<void>([
        loadScenarios(),
        loadCategories(),
        loadSkills()
      ])
    } catch (err) {
      console.log('使用模拟数据')
    }
  })
  
  // 模拟数据
  const loadMockData = (): void => {
    // 模拟场景数据
    scenarios.value = [
      {
        id: '1',
        title: '与伴侣的日常沟通',
        description: '学习如何与伴侣进行有效的日常沟通，增进感情',
        context: '你和伴侣因为工作忙碌，很少有时间深入交流。今天你想主动开启一个关于未来规划的对话。',
        difficulty: 'easy',
        tags: ['日常沟通', '感情增进', '未来规划']
      },
      {
        id: '2',
        title: '处理家庭矛盾',
        description: '面对家庭内部矛盾时的沟通技巧和解决方案',
        context: '你的父母和你的伴侣在育儿观念上存在分歧，你需要在中间协调，避免矛盾升级。',
        difficulty: 'medium',
        tags: ['家庭矛盾', '育儿观念', '协调沟通']
      },
      {
        id: '3',
        title: '职场人际关系',
        description: '在职场中建立和维护良好的人际关系',
        context: '你的同事经常在背后说你的闲话，影响工作氛围。你需要找到合适的方式处理这种情况。',
        difficulty: 'hard',
        tags: ['职场关系', '人际冲突', '专业处理']
      }
    ]
    
    // 模拟类别数据
    categories.value = ['relationship_conflict', 'emotional_support', 'communication_improvement']
    
    // 模拟技巧数据
    availableSkills.value = ['active_listening', 'nonviolent_communication', 'conflict_resolution', 'emotional_expression']
    
    // 模拟进度数据
    weeklyProgress.value = 75
    weeklyPracticeCount.value = 3
    skillsProgress.value = [
      { name: 'active_listening', progress: 80 },
      { name: 'nonviolent_communication', progress: 65 },
      { name: 'conflict_resolution', progress: 45 },
      { name: 'emotional_expression', progress: 70 }
    ]
    totalScenarios.value = 12
    totalPracticeTime.value = 180
    improvementScore.value = 8.5
    
    // 模拟最近活动数据
    recentActivities.value = [
      {
        id: '1',
        type: 'practice',
        title: '完成了"与伴侣的日常沟通"练习',
        description: '学习了如何开启深度对话，获得了积极的反馈',
        time: '2小时前'
      },
      {
        id: '2',
        type: 'template',
        title: '生成了情感表达模板',
        description: '为"担心"情感状态创建了沟通模板',
        time: '昨天'
      },
      {
        id: '3',
        type: 'skill',
        title: '练习了积极倾听技巧',
        description: '完成了15分钟的专注倾听练习',
        time: '3天前'
      },
      {
        id: '4',
        type: 'conflict',
        title: '学习了冲突解决步骤',
        description: '掌握了处理家庭矛盾的方法',
        time: '1周前'
      }
    ]
  }
  
  // 改进的生成建议方法
  const generateSuggestions = async (): Promise<void> => {
    if (!selectedScenario.value || !userInput.value.trim()) return
    
    try {
      loading.value = true
      
      // 模拟API调用延迟
      await new Promise<void>((resolve: () => void) => setTimeout(resolve, 1500))
      
      // 模拟建议数据
      suggestions.value = {
        scenario: selectedScenario.value,
        user_input_analysis: {
          analysis: `你的表达"${userInput.value}"体现了你的关心和主动性。语言温和，态度积极，这是一个很好的开始。`
        },
        suggestions: {
          improvements: [
            '可以更具体地表达你的感受和需求',
            '给对方更多表达的机会，避免单方面主导对话',
            '使用"我"语句，减少指责性语言'
          ]
        },
        next_steps: [
          '保持耐心，给对方时间思考和回应',
          '注意观察对方的情绪变化',
          '如果对方有负面情绪，先表示理解再继续'
        ]
      }
    } catch (err: any) {
      error.value = err.message || '生成建议失败'
    } finally {
      loading.value = false
    }
  }
  
  // 改进的技巧练习方法
  const selectSkill = async (skill: string): Promise<void> => {
    selectedSkill.value = skill
    try {
      loading.value = true
      
      // 模拟API调用延迟
      await new Promise<void>((resolve: () => void) => setTimeout(resolve, 1000))
      
      // 模拟练习数据
      skillPractice.value = {
        skill_type: skill,
        tips: [
          '保持开放的心态，不要急于下结论',
          '关注对方的非语言信号',
          '定期反思和总结沟通效果'
        ],
        practice_exercises: [
          '每天花10分钟专注倾听他人说话',
          '练习复述对方的话，确保理解准确',
          '记录沟通中的成功和失败案例'
        ],
        daily_goal: '今天至少进行一次深度倾听练习，记录感受和收获',
        progress_tracking: '本周已练习3次，目标完成度75%'
      }
    } catch (err: any) {
      error.value = err.message || '获取练习内容失败'
    } finally {
      loading.value = false
    }
  }
  
  // 改进的冲突解决指导方法
  const selectConflictType = async (type: string): Promise<void> => {
    selectedConflictType.value = type
    try {
      loading.value = true
      
      // 模拟API调用延迟
      await new Promise<void>((resolve: () => void) => setTimeout(resolve, 1000))
      
      // 模拟指导数据
      conflictGuide.value = {
        warning_signs: [
          '对方开始提高音量或语速加快',
          '出现人身攻击或指责性语言',
          '对方开始回避或拒绝继续对话'
        ],
        immediate_actions: [
          '立即停止当前话题，转移注意力',
          '深呼吸，保持冷静和理性',
          '建议暂停对话，稍后再继续'
        ],
        step_by_step_process: [
          '确认冲突的具体内容和双方立场',
          '寻找共同点和共同目标',
          '提出多个解决方案供选择',
          '达成共识并制定行动计划'
        ],
        communication_tools: [
          '使用"我"语句表达感受',
          '积极倾听和复述确认',
          '寻找双赢的解决方案'
        ],
        prevention_tips: [
          '定期进行关系维护对话',
          '建立健康的沟通习惯',
          '学会识别和表达情绪'
        ]
      }
    } catch (err: any) {
      error.value = err.message || '获取冲突解决指导失败'
    } finally {
      loading.value = false
    }
  }
  
  // 改进的模板生成方法
  const generateTemplate = async (): Promise<void> => {
    if (!templateSituation.value.trim() || !templateEmotion.value) return
    
    try {
      loading.value = true
      
      // 模拟API调用延迟
      await new Promise<void>((resolve: () => void) => setTimeout(resolve, 2000))
      
      // 模拟模板数据
      dialogueTemplate.value = {
        situation: templateSituation.value,
        emotion: templateEmotion.value,
        templates: {
          opening_statements: `我注意到最近我们之间可能有一些误解，我想和你聊聊，听听你的想法。`,
          feeling_expressions: `我感觉${templateEmotion.value === 'worried' ? '有些担心' : templateEmotion.value === 'angry' ? '有些生气' : '有些困惑'}，因为...`,
          understanding_questions: `你能告诉我你是怎么想的吗？我想更好地理解你的感受。`,
          resolution_suggestions: `也许我们可以一起找到解决方案，你觉得怎么样？`
        },
        usage_tips: [
          '根据实际情况调整语言表达',
          '保持真诚和开放的态度',
          '给对方足够的回应时间',
          '注意语气和肢体语言'
        ]
      }
    } catch (err: any) {
      error.value = err.message || '生成模板失败'
    } finally {
      loading.value = false
    }
  }
  
  // 反馈相关方法
  const submitFeedback = (feedback: string): void => {
    userFeedback.value = feedback
    if (feedback === 'unhelpful') {
      feedbackComment.value = ''
    }
  }
  
  const saveFeedback = (): void => {
    // 这里可以保存反馈到后端或本地存储
    console.log('保存反馈:', {
      feedback: userFeedback.value,
      comment: feedbackComment.value,
      scenario: selectedScenario.value?.title,
      timestamp: new Date().toISOString()
    })
    
    // 显示成功消息
    error.value = '反馈已保存，感谢你的建议！'
    setTimeout(() => {
      error.value = ''
    }, 3000)
    
    // 重置反馈
    userFeedback.value = ''
    feedbackComment.value = ''
  }
  
  // 搜索相关方法
  const performSearch = (): void => {
    if (!searchQuery.value.trim()) {
      searchResults.value = []
      return
    }
    
    const query = searchQuery.value.toLowerCase()
    const results: SearchResult[] = []
    
    // 搜索场景
    scenarios.value.forEach((scenario: Scenario) => {
      if (scenario.title.toLowerCase().includes(query) || 
          scenario.description.toLowerCase().includes(query) ||
          scenario.tags.some((tag: string) => tag.toLowerCase().includes(query))) {
        results.push({
          id: scenario.id,
          type: 'scenario',
          title: scenario.title,
          description: scenario.description
        })
      }
    })
    
    // 搜索技巧
    availableSkills.value.forEach((skill: string) => {
      const skillName = getSkillDisplayName(skill)
      const skillDesc = getSkillDescription(skill)
      if (skillName.toLowerCase().includes(query) || skillDesc.toLowerCase().includes(query)) {
        results.push({
          id: skill,
          type: 'skill',
          title: skillName,
          description: skillDesc
        })
      }
    })
    
    searchResults.value = results.slice(0, 6) // 限制结果数量
  }
  
  const navigateToResult = (result: SearchResult): void => {
    if (result.type === 'scenario') {
      activeTab.value = 'scenarios'
      const scenario = scenarios.value.find((s: Scenario) => s.id === result.id)
      if (scenario) {
        selectScenario(scenario)
      }
    } else if (result.type === 'skill') {
      activeTab.value = 'skills'
      selectSkill(result.id)
    }
    
    // 清空搜索
    searchQuery.value = ''
    searchResults.value = []
  }
  
  const getResultIcon = (type: string): string => {
    if (type === 'scenario') return '🎭'
    if (type === 'skill') return '🧠'
    return '🔍'
  }
  
  const getResultTypeName = (type: string): string => {
    if (type === 'scenario') return '场景'
    if (type === 'skill') return '技巧'
    return '内容'
  }

  // 成就数据
  const achievements = ref<Achievement[]>([
    {
      id: '1',
      title: '沟通新手',
      description: '完成第一个沟通场景练习',
      icon: '🎯',
      progress: 1,
      target: 1,
      unlocked: true
    },
    {
      id: '2',
      title: '技巧达人',
      description: '掌握3个沟通技巧',
      icon: '🧠',
      progress: 2,
      target: 3,
      unlocked: false
    },
    {
      id: '3',
      title: '场景探索者',
      description: '完成5个不同场景的练习',
      icon: '🗺️',
      progress: 3,
      target: 5,
      unlocked: false
    },
    {
      id: '4',
      title: '倾听大师',
      description: '积极倾听技巧达到80%',
      icon: '👂',
      progress: 80,
      target: 80,
      unlocked: true
    },
    {
      id: '5',
      title: '冲突调解员',
      description: '完成3次冲突解决指导',
      icon: '⚖️',
      progress: 1,
      target: 3,
      unlocked: false
    },
    {
      id: '6',
      title: '模板创作者',
      description: '生成10个对话模板',
      icon: '📝',
      progress: 2,
      target: 10,
      unlocked: false
    }
  ])
  
  // 搜索相关
  const searchQuery = ref('')
  const searchResults = ref<SearchResult[]>([])
  </script>
  
  <style scoped lang="scss">
  .communication-sandbox {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
    min-height: 100vh;
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  }
  
  .page-header {
    text-align: center;
    margin-bottom: 30px;
    
    h1 {
      font-size: 2.5rem;
      color: #2c3e50;
      margin-bottom: 10px;
      font-weight: 700;
    }
    
    .subtitle {
      font-size: 1.1rem;
      color: #7f8c8d;
      margin: 0;
    }
  }
  
  .nav-tabs {
    display: flex;
    justify-content: center;
    margin-bottom: 30px;
    background: white;
    border-radius: 15px;
    padding: 10px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    
    .nav-tab {
      padding: 12px 24px;
      margin: 0 5px;
      border: none;
      background: transparent;
      border-radius: 10px;
      cursor: pointer;
      transition: all 0.3s ease;
      font-size: 1rem;
      color: #7f8c8d;
      
      &:hover {
        background: #ecf0f1;
        color: #34495e;
      }
      
      &.active {
        background: #3498db;
        color: white;
        transform: translateY(-2px);
        box-shadow: 0 4px 15px rgba(52, 152, 219, 0.3);
      }
    }
  }

  .quick-start-guide {
    background: #f8f9fa;
    border-radius: 15px;
    padding: 25px;
    margin-top: 20px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

    .guide-tabs {
      display: flex;
      gap: 10px;
      margin-bottom: 20px;
      flex-wrap: wrap;

      .guide-tab {
        padding: 10px 20px;
        border: 2px solid #e9ecef;
        background: white;
        border-radius: 10px;
        cursor: pointer;
        transition: all 0.3s ease;
        font-size: 0.9rem;
        color: #7f8c8d;

        &:hover {
          border-color: #3498db;
          color: #34495e;
        }

        &.active {
          background: #3498db;
          border-color: #3498db;
          color: white;
          transform: translateY(-2px);
          box-shadow: 0 4px 15px rgba(52, 152, 219, 0.3);
        }
      }
    }

    .guide-content {
      display: none; /* Hidden by default */
      background: white;
      border-radius: 15px;
      padding: 20px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);

      .guide-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;

        h3 {
          color: #2c3e50;
          margin: 0;
          font-size: 1.2rem;
        }

        .close-guide-btn {
          background: none;
          border: none;
          font-size: 1.5rem;
          color: #7f8c8d;
          cursor: pointer;
          padding: 5px;

          &:hover {
            color: #e74c3c;
          }
        }
      }

      .guide-steps {
        margin-bottom: 15px;

        ol {
          padding-left: 20px;
          color: #6c757d;
          line-height: 1.6;

          li {
            margin-bottom: 8px;
          }
        }
      }

      .guide-tips {
        background: #f8f9fa;
        padding: 15px;
        border-radius: 10px;
        border-left: 4px solid #3498db;

        h4 {
          color: #34495e;
          margin-bottom: 10px;
          font-size: 1rem;
        }

        p {
          color: #6c757d;
          line-height: 1.5;
          margin: 0;
        }
      }
    }

    .guide-content.active {
      display: block; /* Show when active */
    }
  }
  
  .tab-content {
    background: white;
    border-radius: 20px;
    padding: 30px;
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
    min-height: 600px;
  }
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 25px;
    
    h2 {
      color: #2c3e50;
      margin: 0;
      font-size: 1.8rem;
    }
    
    .filters {
      display: flex;
      gap: 15px;
      
      select {
        padding: 8px 15px;
        border: 2px solid #ecf0f1;
        border-radius: 8px;
        background: white;
        color: #2c3e50;
        font-size: 0.9rem;
        
        &:focus {
          outline: none;
          border-color: #3498db;
        }
      }
    }
  }
  
  .scenarios-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;
    margin-bottom: 30px;
  }
  
  .scenario-card {
    background: #f8f9fa;
    border: 2px solid #e9ecef;
    border-radius: 15px;
    padding: 20px;
    cursor: pointer;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
      border-color: #3498db;
    }
    
    &.selected {
      border-color: #3498db;
      background: #ebf3fd;
      box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
    }
    
    .scenario-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 15px;
      
      h3 {
        margin: 0;
        color: #2c3e50;
        font-size: 1.2rem;
        flex: 1;
      }
      
      .difficulty-badge {
        padding: 4px 12px;
        border-radius: 20px;
        font-size: 0.8rem;
        font-weight: 600;
        
        &.easy {
          background: #d4edda;
          color: #155724;
        }
        
        &.medium {
          background: #fff3cd;
          color: #856404;
        }
        
        &.hard {
          background: #f8d7da;
          color: #721c24;
        }
      }
    }
    
    .scenario-description {
      color: #6c757d;
      margin-bottom: 15px;
      line-height: 1.5;
    }
    
    .scenario-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      
      .tag {
        background: #e9ecef;
        color: #495057;
        padding: 4px 10px;
        border-radius: 15px;
        font-size: 0.8rem;
      }
    }
  }
  
  .scenario-detail {
    background: #f8f9fa;
    border-radius: 15px;
    padding: 25px;
    margin-top: 20px;
    
    .detail-header {
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
        color: #7f8c8d;
        cursor: pointer;
        padding: 5px;
        
        &:hover {
          color: #e74c3c;
        }
      }
    }
    
    .scenario-context {
      margin-bottom: 25px;
      
      h4 {
        color: #34495e;
        margin-bottom: 10px;
      }
      
      p {
        color: #6c757d;
        line-height: 1.6;
      }
    }
  }
  
  .dialogue-practice {
    h4 {
      color: #34495e;
      margin-bottom: 15px;
    }
    
    .input-section {
      display: flex;
      gap: 15px;
      margin-bottom: 20px;
      
      .user-input {
        flex: 1;
        padding: 15px;
        border: 2px solid #e9ecef;
        border-radius: 10px;
        resize: vertical;
        font-family: inherit;
        font-size: 1rem;
        
        &:focus {
          outline: none;
          border-color: #3498db;
        }
      }
      
      .generate-btn {
        padding: 15px 25px;
        background: #3498db;
        color: white;
        border: none;
        border-radius: 10px;
        cursor: pointer;
        font-size: 1rem;
        font-weight: 600;
        transition: all 0.3s ease;
        
        &:hover:not(:disabled) {
          background: #2980b9;
          transform: translateY(-2px);
          box-shadow: 0 4px 15px rgba(52, 152, 219, 0.3);
        }
        
        &:disabled {
          background: #bdc3c7;
          cursor: not-allowed;
          transform: none;
        }
      }
    }
  }
  
  .suggestions-section {
    background: #f8f9fa;
    border-radius: 15px;
    padding: 20px;
    margin-top: 20px;
    
    h5 {
      color: #2c3e50;
      margin-bottom: 15px;
      font-size: 1.2rem;
    }
    
    .suggestion-content {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 20px;
      
      .analysis, .suggestions, .next-steps {
        background: white;
        padding: 15px;
        border-radius: 10px;
        border-left: 4px solid #3498db;
        
        h6 {
          color: #34495e;
          margin-bottom: 10px;
          font-size: 1rem;
        }
        
        p, ul {
          color: #6c757d;
          line-height: 1.6;
          margin: 0;
        }
        
        ul {
          padding-left: 20px;
          
          li {
            margin-bottom: 8px;
          }
        }
      }
    }
  }
  
  .skills-section {
    h2 {
      color: #2c3e50;
      margin-bottom: 25px;
      font-size: 1.8rem;
    }
  }
  
  .skills-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 20px;
    margin-bottom: 30px;
  }
  
  .skill-card {
    background: #f8f9fa;
    border: 2px solid #e9ecef;
    border-radius: 15px;
    padding: 20px;
    cursor: pointer;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
      border-color: #3498db;
    }
    
    &.active {
      border-color: #3498db;
      background: #ebf3fd;
      box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
    }
    
    h3 {
      color: #2c3e50;
      margin-bottom: 10px;
      font-size: 1.2rem;
    }
    
    p {
      color: #6c757d;
      line-height: 1.5;
      margin: 0;
    }
  }
  
  .skill-practice {
    background: #f8f9fa;
    border-radius: 15px;
    padding: 25px;
    margin-top: 20px;
    
    .practice-header {
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
        color: #7f8c8d;
        cursor: pointer;
        padding: 5px;
        
        &:hover {
          color: #e74c3c;
        }
      }
    }
    
    .practice-content {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 20px;
      
      .tips-section, .exercises-section, .goal-section {
        background: white;
        padding: 20px;
        border-radius: 10px;
        border-left: 4px solid #e74c3c;
        
        h4 {
          color: #34495e;
          margin-bottom: 15px;
          font-size: 1.1rem;
        }
        
        h5 {
          color: #2c3e50;
          margin-bottom: 10px;
          font-size: 1rem;
        }
        
        p, ul {
          color: #6c757d;
          line-height: 1.6;
          margin: 0;
        }
        
        ul {
          padding-left: 20px;
          
          li {
            margin-bottom: 8px;
          }
        }
      }
      
      .goal-section {
        border-left-color: #27ae60;
      }
      
      .exercises-section {
        border-left-color: #f39c12;
      }
    }
  }
  
  .conflict-section {
    h2 {
      color: #2c3e50;
      margin-bottom: 25px;
      font-size: 1.8rem;
    }
  }
  
  .conflict-types {
    display: flex;
    gap: 15px;
    margin-bottom: 25px;
    
    .conflict-type-btn {
      padding: 12px 24px;
      border: 2px solid #ecf0f1;
      background: white;
      border-radius: 10px;
      cursor: pointer;
      transition: all 0.3s ease;
      font-size: 1rem;
      color: #7f8c8d;
      
      &:hover {
        border-color: #3498db;
        color: #34495e;
      }
      
      &.active {
        background: #3498db;
        border-color: #3498db;
        color: white;
        transform: translateY(-2px);
        box-shadow: 0 4px 15px rgba(52, 152, 219, 0.3);
      }
    }
  }
  
  .conflict-guide {
    background: #f8f9fa;
    border-radius: 15px;
    padding: 25px;
    
    .guide-content {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 20px;
      
      .guide-section {
        background: white;
        padding: 20px;
        border-radius: 10px;
        border-left: 4px solid #e74c3c;
        
        h4 {
          color: #34495e;
          margin-bottom: 15px;
          font-size: 1.1rem;
        }
        
        ul, ol {
          color: #6c757d;
          line-height: 1.6;
          margin: 0;
          padding-left: 20px;
          
          li {
            margin-bottom: 8px;
          }
        }
        
        &:nth-child(2) {
          border-left-color: #f39c12;
        }
        
        &:nth-child(3) {
          border-left-color: #3498db;
        }
        
        &:nth-child(4) {
          border-left-color: #9b59b6;
        }
        
        &:nth-child(5) {
          border-left-color: #27ae60;
        }
      }
    }
  }
  
  .templates-section {
    h2 {
      color: #2c3e50;
      margin-bottom: 25px;
      font-size: 1.8rem;
    }
  }
  
  .template-form {
    background: #f8f9fa;
    border-radius: 15px;
    padding: 25px;
    margin-bottom: 25px;
    
    .form-group {
      margin-bottom: 20px;
      
      label {
        display: block;
        color: #2c3e50;
        margin-bottom: 8px;
        font-weight: 600;
      }
      
      textarea, select {
        width: 100%;
        padding: 12px 15px;
        border: 2px solid #e9ecef;
        border-radius: 10px;
        background: white;
        color: #2c3e50;
        font-size: 1rem;
        font-family: inherit;
        
        &:focus {
          outline: none;
          border-color: #3498db;
        }
      }
      
      textarea {
        resize: vertical;
        min-height: 80px;
      }
    }
    
    .generate-template-btn {
      width: 100%;
      padding: 15px;
      background: #27ae60;
      color: white;
      border: none;
      border-radius: 10px;
      cursor: pointer;
      font-size: 1rem;
      font-weight: 600;
      transition: all 0.3s ease;
      
      &:hover:not(:disabled) {
        background: #229954;
        transform: translateY(-2px);
        box-shadow: 0 4px 15px rgba(39, 174, 96, 0.3);
      }
      
      &:disabled {
        background: #bdc3c7;
        cursor: not-allowed;
        transform: none;
      }
    }
  }
  
  .template-result {
    background: #f8f9fa;
    border-radius: 15px;
    padding: 25px;
    
    h3 {
      color: #2c3e50;
      margin-bottom: 20px;
      font-size: 1.5rem;
    }
    
    .template-content {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 20px;
      
      .template-item {
        background: white;
        padding: 20px;
        border-radius: 10px;
        border-left: 4px solid #3498db;
        
        h4 {
          color: #34495e;
          margin-bottom: 10px;
          font-size: 1.1rem;
        }
        
        p {
          color: #6c757d;
          line-height: 1.6;
          margin: 0;
        }
      }
      
      .usage-tips {
        background: white;
        padding: 20px;
        border-radius: 10px;
        border-left: 4px solid #f39c12;
        
        h4 {
          color: #34495e;
          margin-bottom: 15px;
          font-size: 1.1rem;
        }
        
        ul {
          color: #6c757d;
          line-height: 1.6;
          margin: 0;
          padding-left: 20px;
          
          li {
            margin-bottom: 8px;
          }
        }
      }
    }
  }
  
  .progress-section {
    h2 {
      color: #2c3e50;
      margin-bottom: 25px;
      font-size: 1.8rem;
    }
  }

  .progress-overview {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 20px;
    margin-bottom: 30px;
  }

  .progress-card {
    background: #f8f9fa;
    border: 2px solid #e9ecef;
    border-radius: 15px;
    padding: 25px;
    text-align: center;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);

    h3 {
      color: #2c3e50;
      margin-bottom: 15px;
      font-size: 1.2rem;
    }

    .progress-circle {
      width: 120px;
      height: 120px;
      border-radius: 50%;
      background: #e9ecef;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      margin: 0 auto 15px;
      position: relative;

      .progress-number {
        font-size: 2.5rem;
        font-weight: 700;
        color: #3498db;
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
      }

      .progress-label {
        font-size: 0.9rem;
        color: #7f8c8d;
      }
    }

    p {
      color: #6c757d;
      font-size: 0.9rem;
      margin-bottom: 15px;
    }
  }

  .skills-progress {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }

  .skill-progress-item {
    display: flex;
    align-items: center;
    gap: 15px;

    .skill-name {
      font-weight: 600;
      color: #2c3e50;
      font-size: 1rem;
    }

    .progress-bar {
      flex: 1;
      height: 8px;
      background: #e9ecef;
      border-radius: 4px;
      overflow: hidden;
    }

    .progress-fill {
      height: 100%;
      background: #3498db;
      border-radius: 4px;
      transition: width 0.3s ease-in-out;
    }

    .progress-text {
      font-size: 0.8rem;
      color: #7f8c8d;
      margin-left: 10px;
    }
  }

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
    gap: 15px;
    margin-top: 20px;
  }

  .stat-item {
    text-align: center;
    padding: 15px;
    background: #f8f9fa;
    border-radius: 10px;
    border: 1px solid #e9ecef;

    .stat-number {
      font-size: 1.8rem;
      font-weight: 700;
      color: #3498db;
      display: block;
      margin-bottom: 5px;
    }

    .stat-label {
      font-size: 0.9rem;
      color: #7f8c8d;
    }
  }

  .recent-activities {
    h3 {
      color: #2c3e50;
      margin-bottom: 20px;
      font-size: 1.2rem;
    }
  }

  .activity-list {
    display: flex;
    flex-direction: column;
    gap: 15px;
  }

  .activity-item {
    display: flex;
    align-items: center;
    gap: 15px;
    background: #f8f9fa;
    border: 1px solid #e9ecef;
    border-radius: 10px;
    padding: 15px;

    .activity-icon {
      font-size: 1.8rem;
      color: #3498db;
    }

    .activity-content {
      flex: 1;

      h4 {
        color: #2c3e50;
        margin-bottom: 5px;
        font-size: 1rem;
      }

      p {
        color: #6c757d;
        font-size: 0.9rem;
        margin-bottom: 5px;
      }

      .activity-time {
        font-size: 0.8rem;
        color: #7f8c8d;
      }
    }
  }
  
  .achievements-section {
    margin-top: 30px;
    padding-top: 25px;
    border-top: 1px solid #e9ecef;

    h3 {
      color: #2c3e50;
      margin-bottom: 20px;
      font-size: 1.5rem;
    }
  }

  .achievements-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 20px;
  }

  .achievement-item {
    background: #f8f9fa;
    border: 2px solid #e9ecef;
    border-radius: 15px;
    padding: 20px;
    display: flex;
    align-items: center;
    gap: 15px;
    position: relative;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
      border-color: #3498db;
    }

    &.unlocked {
      border-color: #27ae60;
      background: #e8f5e9;
      box-shadow: 0 0 0 3px rgba(39, 174, 96, 0.1);
    }

    .achievement-icon {
      font-size: 2.5rem;
      color: #3498db;
      flex-shrink: 0;
    }

    .achievement-info {
      flex: 1;

      h4 {
        color: #2c3e50;
        margin-bottom: 8px;
        font-size: 1.1rem;
      }

      p {
        color: #6c757d;
        font-size: 0.9rem;
        line-height: 1.5;
        margin-bottom: 10px;
      }

      .achievement-progress {
        font-size: 0.9rem;
        color: #7f8c8d;
      }
    }

    .achievement-badge {
      position: absolute;
      top: 10px;
      right: 10px;
      background: #27ae60;
      color: white;
      width: 30px;
      height: 30px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 1.2rem;
      font-weight: 700;
      border: 2px solid white;
    }
  }
  
  .loading-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    z-index: 1000;
    
    .loading-spinner {
      width: 50px;
      height: 50px;
      border: 4px solid #f3f3f3;
      border-top: 4px solid #3498db;
      border-radius: 50%;
      animation: spin 1s linear infinite;
      margin-bottom: 20px;
    }
    
    p {
      color: white;
      font-size: 1.1rem;
      margin: 0;
    }
  }
  
  .error-message {
    position: fixed;
    top: 20px;
    right: 20px;
    background: #e74c3c;
    color: white;
    padding: 15px 20px;
    border-radius: 10px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
    z-index: 1000;
    display: flex;
    align-items: center;
    gap: 15px;
    
    p {
      margin: 0;
      font-weight: 600;
    }
    
    .close-error {
      background: none;
      border: none;
      color: white;
      font-size: 1.2rem;
      cursor: pointer;
      padding: 0;
      width: 20px;
      height: 20px;
      display: flex;
      align-items: center;
      justify-content: center;
      
      &:hover {
        background: rgba(255, 255, 255, 0.2);
        border-radius: 50%;
      }
    }
  }
  
  .feedback-section {
    margin-top: 25px;
    padding-top: 20px;
    border-top: 1px solid #e9ecef;
    
    h6 {
      color: #34495e;
      margin-bottom: 15px;
      font-size: 1rem;
    }
    
    .feedback-buttons {
      display: flex;
      gap: 10px;
      margin-bottom: 15px;
      
      .feedback-btn {
        padding: 8px 16px;
        border: 2px solid #e9ecef;
        background: white;
        border-radius: 20px;
        cursor: pointer;
        transition: all 0.3s ease;
        font-size: 0.9rem;
        
        &:hover {
          border-color: #3498db;
          transform: translateY(-2px);
        }
        
        &.active {
          border-color: #3498db;
          background: #3498db;
          color: white;
        }
        
        &.helpful.active {
          background: #27ae60;
          border-color: #27ae60;
        }
        
        &.unhelpful.active {
          background: #e74c3c;
          border-color: #e74c3c;
        }
      }
    }
    
    .feedback-comment {
      .feedback-textarea {
        width: 100%;
        padding: 12px;
        border: 2px solid #e9ecef;
        border-radius: 8px;
        resize: vertical;
        font-family: inherit;
        font-size: 0.9rem;
        margin-bottom: 10px;
        
        &:focus {
          outline: none;
          border-color: #3498db;
        }
      }
      
      .save-feedback-btn {
        padding: 8px 16px;
        background: #27ae60;
        color: white;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        font-size: 0.9rem;
        transition: all 0.3s ease;
        
        &:hover {
          background: #229954;
          transform: translateY(-2px);
        }
      }
    }
  }
  
  @keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
  
  // 响应式设计
  @media (max-width: 768px) {
    .communication-sandbox {
      padding: 15px;
    }
    
    .page-header h1 {
      font-size: 2rem;
    }
    
    .nav-tabs {
      flex-wrap: wrap;
      gap: 10px;
      
      .nav-tab {
        flex: 1;
        min-width: 120px;
        margin: 0;
      }
    }
    
    .tab-content {
      padding: 20px;
    }
    
    .section-header {
      flex-direction: column;
      gap: 15px;
      align-items: flex-start;
      
      .filters {
        width: 100%;
        flex-direction: column;
        gap: 10px;
      }
    }
    
    .scenarios-grid {
      grid-template-columns: 1fr;
    }
    
    .skills-grid {
      grid-template-columns: 1fr;
    }
    
    .conflict-types {
      flex-direction: column;
      
      .conflict-type-btn {
        width: 100%;
      }
    }
    
    .conflict-guide .guide-content {
      grid-template-columns: 1fr;
    }
    
    .template-content {
      grid-template-columns: 1fr;
    }
    
    .suggestion-content {
      grid-template-columns: 1fr;
    }
    
    .practice-content {
      grid-template-columns: 1fr;
    }

    .progress-overview {
      grid-template-columns: 1fr;
    }

    .stats-grid {
      grid-template-columns: 1fr;
    }

    .activity-list {
      flex-direction: column;
    }

    .achievements-grid {
      grid-template-columns: 1fr;
    }
  }
  
  @media (max-width: 480px) {
    .page-header h1 {
      font-size: 1.8rem;
    }
    
    .tab-content {
      padding: 15px;
    }
    
    .scenario-card, .skill-card {
      padding: 15px;
    }
  }

  .search-section {
    margin-bottom: 30px;
    
    .search-box {
      display: flex;
      max-width: 600px;
      margin: 0 auto;
      background: white;
      border-radius: 25px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      overflow: hidden;
      
      .search-input {
        flex: 1;
        padding: 15px 20px;
        border: none;
        outline: none;
        font-size: 1rem;
        color: #2c3e50;
        
        &::placeholder {
          color: #bdc3c7;
        }
      }
      
      .search-btn {
        padding: 15px 20px;
        background: #3498db;
        border: none;
        color: white;
        cursor: pointer;
        font-size: 1.2rem;
        transition: background 0.3s ease;
        
        &:hover {
          background: #2980b9;
        }
      }
    }
    
    .search-results {
      margin-top: 20px;
      background: white;
      border-radius: 15px;
      padding: 20px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      
      h3 {
        color: #2c3e50;
        margin-bottom: 15px;
        font-size: 1.2rem;
      }
      
      .results-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
        gap: 15px;
      }
      
      .result-item {
        display: flex;
        align-items: center;
        gap: 15px;
        padding: 15px;
        background: #f8f9fa;
        border: 1px solid #e9ecef;
        border-radius: 10px;
        cursor: pointer;
        transition: all 0.3s ease;
        
        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
          border-color: #3498db;
        }
        
        .result-icon {
          font-size: 2rem;
          color: #3498db;
          flex-shrink: 0;
        }
        
        .result-content {
          flex: 1;
          
          h4 {
            color: #2c3e50;
            margin-bottom: 5px;
            font-size: 1rem;
          }
          
          p {
            color: #6c757d;
            font-size: 0.9rem;
            line-height: 1.4;
            margin-bottom: 8px;
          }
          
          .result-type {
            font-size: 0.8rem;
            color: #7f8c8d;
            background: #e9ecef;
            padding: 2px 8px;
            border-radius: 10px;
          }
        }
      }
    }
  }
  
  .tab-content {
    background: white;
    border-radius: 20px;
    padding: 30px;
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
    min-height: 600px;
  }
</style>