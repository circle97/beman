<template>
  <div class="emotion-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">情绪解码器</h1>
      <p class="page-subtitle">分析情感状态，评估关系健康度，获取个性化建议</p>
    </div>

    <div class="emotion-content">
      <!-- 输入区域 -->
      <div class="input-section">
        <BemanCard padding="large">
          <template #header>
            <h2>情感分析</h2>
          </template>
          
          <div class="input-area">
            <div class="input-group">
              <label for="emotion-text" class="input-label">请输入您要分析的文本</label>
              <textarea
                id="emotion-text"
                v-model="inputText"
                class="emotion-textarea"
                placeholder="请描述您的情感状态、关系问题或需要分析的对话内容..."
                rows="6"
              ></textarea>
            </div>
            
            <div class="analysis-options">
              <div class="option-group">
                <label class="option-label">分析类型</label>
                <div class="radio-group">
                  <label class="radio-item">
                    <input v-model="analysisType" type="radio" value="emotion" />
                    <span>情感状态分析</span>
                  </label>
                  <label class="radio-item">
                    <input v-model="analysisType" type="radio" value="relationship" />
                    <span>关系健康度评估</span>
                  </label>
                  <label class="radio-item">
                    <input v-model="analysisType" type="radio" value="communication" />
                    <span>沟通质量分析</span>
                  </label>
                </div>
              </div>
            </div>

            <BemanButton
              type="primary"
              size="large"
              :loading="analyzing"
              :disabled="!inputText.trim() || analyzing"
              @click="analyzeEmotion"
              class="analyze-btn"
            >
              {{ analyzing ? '分析中...' : '开始分析' }}
            </BemanButton>
          </div>
        </BemanCard>
      </div>

      <!-- 分析结果 -->
      <div v-if="analysisResult" class="result-section">
        <!-- 情感状态分析 -->
        <div v-if="analysisType === 'emotion'" class="result-card">
          <BemanCard padding="large">
            <template #header>
              <h3>情感状态分析结果</h3>
            </template>
            
            <div class="emotion-result">
              <div class="emotion-summary">
                <div class="emotion-type">
                  <span class="type-label">主要情感：</span>
                  <span class="type-value" :class="getEmotionClass(analysisResult.emotionType)">
                    {{ getEmotionText(analysisResult.emotionType) }}
                  </span>
                </div>
                <div class="emotion-intensity">
                  <span class="intensity-label">情感强度：</span>
                  <div class="intensity-bar">
                    <div 
                      class="intensity-fill" 
                      :style="{ width: analysisResult.intensity + '%' }"
                    ></div>
                  </div>
                  <span class="intensity-value">{{ analysisResult.intensity }}%</span>
                </div>
              </div>
              
              <div class="emotion-details">
                <h4>情感关键词</h4>
                <div class="keyword-tags">
                  <span 
                    v-for="keyword in analysisResult.keywords" 
                    :key="keyword"
                    class="keyword-tag"
                  >
                    {{ keyword }}
                  </span>
                </div>
              </div>

              <div class="emotion-suggestions">
                <h4>改善建议</h4>
                <ul class="suggestion-list">
                  <li v-for="suggestion in analysisResult.suggestions" :key="suggestion">
                    {{ suggestion }}
                  </li>
                </ul>
              </div>
            </div>
          </BemanCard>
        </div>

        <!-- 关系健康度评估 -->
        <div v-if="analysisType === 'relationship'" class="result-card">
          <BemanCard padding="large">
            <template #header>
              <h3>关系健康度评估结果</h3>
            </template>
            
            <div class="relationship-result">
              <div class="health-score">
                <div class="score-circle" :class="getHealthClass(analysisResult.healthScore)">
                  <span class="score-value">{{ analysisResult.healthScore }}</span>
                  <span class="score-label">分</span>
                </div>
                <div class="score-info">
                  <h4>{{ getHealthLevel(analysisResult.healthScore) }}</h4>
                  <p>{{ getHealthDescription(analysisResult.healthScore) }}</p>
                </div>
              </div>

              <div class="dimension-analysis">
                <h4>各维度分析</h4>
                <div class="dimension-item" v-for="dim in analysisResult.dimensions" :key="dim.name">
                  <div class="dimension-header">
                    <span class="dimension-name">{{ dim.name }}</span>
                    <span class="dimension-score">{{ dim.score }}分</span>
                  </div>
                  <div class="dimension-bar">
                    <div 
                      class="dimension-fill" 
                      :style="{ width: dim.score + '%' }"
                      :class="getDimensionClass(dim.score)"
                    ></div>
                  </div>
                  <p class="dimension-comment">{{ dim.comment }}</p>
                </div>
              </div>

              <div class="improvement-suggestions">
                <h4>改善建议</h4>
                <div class="suggestion-grid">
                  <div 
                    v-for="suggestion in analysisResult.improvements" 
                    :key="suggestion.area"
                    class="suggestion-item"
                  >
                    <h5>{{ suggestion.area }}</h5>
                    <p>{{ suggestion.suggestion }}</p>
                  </div>
                </div>
              </div>
            </div>
          </BemanCard>
        </div>

        <!-- 沟通质量分析 -->
        <div v-if="analysisType === 'communication'" class="result-card">
          <BemanCard padding="large">
            <template #header>
              <h3>沟通质量分析结果</h3>
            </template>
            
            <div class="communication-result">
              <div class="communication-score">
                <div class="score-display">
                  <span class="score-number">{{ analysisResult.communicationScore }}</span>
                  <span class="score-unit">/100</span>
                </div>
                <div class="score-description">
                  <h4>{{ getCommunicationLevel(analysisResult.communicationScore) }}</h4>
                  <p>{{ getCommunicationDescription(analysisResult.communicationScore) }}</p>
                </div>
              </div>

              <div class="communication-aspects">
                <h4>沟通要素分析</h4>
                <div class="aspect-item" v-for="aspect in analysisResult.aspects" :key="aspect.name">
                  <div class="aspect-info">
                    <span class="aspect-name">{{ aspect.name }}</span>
                    <span class="aspect-score">{{ aspect.score }}分</span>
                  </div>
                  <div class="aspect-bar">
                    <div 
                      class="aspect-fill" 
                      :style="{ width: aspect.score + '%' }"
                    ></div>
                  </div>
                  <p class="aspect-analysis">{{ aspect.analysis }}</p>
                </div>
              </div>

              <div class="communication-tips">
                <h4>沟通技巧建议</h4>
                <div class="tips-list">
                  <div 
                    v-for="tip in analysisResult.tips" 
                    :key="tip.title"
                    class="tip-item"
                  >
                    <h5>{{ tip.title }}</h5>
                    <p>{{ tip.content }}</p>
                  </div>
                </div>
              </div>
            </div>
          </BemanCard>
        </div>
      </div>

      <!-- 历史记录 -->
      <div v-if="analysisHistory.length > 0" class="history-section">
        <BemanCard padding="large">
          <template #header>
            <h3>分析历史</h3>
          </template>
          
          <div class="history-list">
            <div 
              v-for="record in analysisHistory" 
              :key="record.id"
              class="history-item"
              @click="loadHistory(record)"
            >
              <div class="history-content">
                <p class="history-text">{{ record.text.substring(0, 50) }}...</p>
                <div class="history-meta">
                  <span class="history-type">{{ getTypeText(record.type) }}</span>
                  <span class="history-time">{{ formatTime(record.time) }}</span>
                </div>
              </div>
              <div class="history-action">
                <button class="btn-secondary" @click.stop="deleteHistory(record.id)">
                  删除
                </button>
              </div>
            </div>
          </div>
        </BemanCard>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import BemanCard from '../../components/BemanCard.vue'
import BemanButton from '../../components/BemanButton.vue'

// 响应式数据
const inputText = ref('')
const analysisType = ref('emotion')
const analyzing = ref(false)
const analysisResult = ref<any>(null)
const analysisHistory = ref<any[]>([])

// 分析情感
const analyzeEmotion = async () => {
  if (!inputText.value.trim()) return
  
  analyzing.value = true
  
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    // 根据分析类型生成不同的结果
    if (analysisType.value === 'emotion') {
      analysisResult.value = generateEmotionResult()
    } else if (analysisType.value === 'relationship') {
      analysisResult.value = generateRelationshipResult()
    } else if (analysisType.value === 'communication') {
      analysisResult.value = generateCommunicationResult()
    }
    
    // 保存到历史记录
    saveToHistory()
    
  } catch (error) {
    alert('分析失败，请重试')
  } finally {
    analyzing.value = false
  }
}

// 生成情感分析结果
const generateEmotionResult = () => {
  const emotions = ['positive', 'negative', 'neutral', 'mixed']
  const emotion = emotions[Math.floor(Math.random() * emotions.length)]
  const intensity = Math.floor(Math.random() * 40) + 60
  
  return {
    emotionType: emotion,
    intensity: intensity,
    keywords: ['理解', '支持', '关心', '信任'],
    suggestions: [
      '保持积极的心态，多关注美好的事物',
      '与信任的人分享你的感受',
      '尝试一些放松的活动来缓解情绪'
    ]
  }
}

// 生成关系健康度结果
const generateRelationshipResult = () => {
  const score = Math.floor(Math.random() * 30) + 70
  
  return {
    healthScore: score,
    dimensions: [
      { name: '沟通质量', score: Math.floor(Math.random() * 30) + 70, comment: '沟通较为顺畅，但仍有提升空间' },
      { name: '信任程度', score: Math.floor(Math.random() * 30) + 70, comment: '彼此信任，关系稳定' },
      { name: '相互支持', score: Math.floor(Math.random() * 30) + 70, comment: '能够相互支持，共同成长' }
    ],
    improvements: [
      { area: '沟通技巧', suggestion: '学习更有效的表达方式，避免误解' },
      { area: '情感表达', suggestion: '勇敢表达真实感受，增进理解' },
      { area: '冲突解决', suggestion: '遇到分歧时保持冷静，寻找共同点' }
    ]
  }
}

// 生成沟通质量结果
const generateCommunicationResult = () => {
  const score = Math.floor(Math.random() * 30) + 70
  
  return {
    communicationScore: score,
    aspects: [
      { name: '倾听能力', score: Math.floor(Math.random() * 30) + 70, analysis: '能够认真倾听对方，理解对方观点' },
      { name: '表达清晰度', score: Math.floor(Math.random() * 30) + 70, analysis: '表达较为清晰，但可以更加具体' },
      { name: '情感共鸣', score: Math.floor(Math.random() * 30) + 70, analysis: '能够理解对方情感，产生共鸣' }
    ],
    tips: [
      { title: '积极倾听', content: '保持眼神接触，不打断对方，重复关键信息' },
      { title: '清晰表达', content: '使用"我"语句，具体描述感受和需求' },
      { title: '情感确认', content: '确认对方的感受，表达理解和关心' }
    ]
  }
}

// 保存到历史记录
const saveToHistory = () => {
  const record = {
    id: Date.now(),
    text: inputText.value,
    type: analysisType.value,
    time: new Date().toISOString(),
    result: analysisResult.value
  }
  
  analysisHistory.value.unshift(record)
  
  // 限制历史记录数量
  if (analysisHistory.value.length > 10) {
    analysisHistory.value = analysisHistory.value.slice(0, 10)
  }
  
  // 保存到本地存储
  localStorage.setItem('emotion-analysis-history', JSON.stringify(analysisHistory.value))
}

// 加载历史记录
const loadHistory = (record: any) => {
  inputText.value = record.text
  analysisType.value = record.type
  analysisResult.value = record.result
}

// 删除历史记录
const deleteHistory = (id: number) => {
  analysisHistory.value = analysisHistory.value.filter(item => item.id !== id)
  localStorage.setItem('emotion-analysis-history', JSON.stringify(analysisHistory.value))
}

// 工具方法
const getEmotionClass = (type: string) => {
  const classes = {
    positive: 'emotion-positive',
    negative: 'emotion-negative',
    neutral: 'emotion-neutral',
    mixed: 'emotion-mixed'
  }
  return classes[type as keyof typeof classes] || 'emotion-neutral'
}

const getEmotionText = (type: string) => {
  const texts = {
    positive: '积极正面',
    negative: '消极负面',
    neutral: '中性平静',
    mixed: '复杂混合'
  }
  return texts[type as keyof typeof texts] || '未知'
}

const getHealthClass = (score: number) => {
  if (score >= 80) return 'health-excellent'
  if (score >= 70) return 'health-good'
  if (score >= 60) return 'health-fair'
  if (score >= 50) return 'health-poor'
  return 'health-critical'
}

const getHealthLevel = (score: number) => {
  if (score >= 80) return '优秀'
  if (score >= 70) return '良好'
  if (score >= 60) return '一般'
  if (score >= 50) return '较差'
  return '危险'
}

const getHealthDescription = (score: number) => {
  if (score >= 80) return '你们的关系非常健康，继续保持！'
  if (score >= 70) return '关系良好，有提升空间'
  if (score >= 60) return '关系一般，需要关注和改进'
  if (score >= 50) return '关系较差，建议寻求专业帮助'
  return '关系危险，需要立即干预'
}

const getDimensionClass = (score: number) => {
  if (score >= 80) return 'dimension-excellent'
  if (score >= 70) return 'dimension-good'
  if (score >= 60) return 'dimension-fair'
  return 'dimension-poor'
}

const getCommunicationLevel = (score: number) => {
  if (score >= 80) return '优秀'
  if (score >= 70) return '良好'
  if (score >= 60) return '一般'
  return '需要改进'
}

const getCommunicationDescription = (score: number) => {
  if (score >= 80) return '沟通质量很高，继续保持！'
  if (score >= 70) return '沟通良好，可以进一步提升'
  if (score >= 60) return '沟通一般，有改进空间'
  return '沟通需要改进，建议学习相关技巧'
}

const getTypeText = (type: string) => {
  const texts = {
    emotion: '情感分析',
    relationship: '关系评估',
    communication: '沟通分析'
  }
  return texts[type as keyof typeof texts] || '未知'
}

const formatTime = (time: string) => {
  return new Date(time).toLocaleString('zh-CN')
}

// 生命周期
onMounted(() => {
  // 加载历史记录
  const history = localStorage.getItem('emotion-analysis-history')
  if (history) {
    try {
      analysisHistory.value = JSON.parse(history)
    } catch (error) {
      console.error('加载历史记录失败:', error)
    }
  }
})
</script>

<style lang="scss" scoped>
@import '../../styles/variables.scss';

.emotion-page {
  min-height: 100vh;
  background: $bg-app;
  padding: $spacing-lg;
}

.page-header {
  text-align: center;
  margin-bottom: $spacing-xl;

  .page-title {
    font-size: 2.5rem;
    font-weight: $font-weight-bold;
    color: $text-primary;
    margin: 0 0 $spacing-sm 0;
  }

  .page-subtitle {
    font-size: 1.1rem;
    color: $text-secondary;
    margin: 0;
  }
}

.emotion-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: $spacing-xl;
}

.input-section {
  .input-area {
    .input-group {
      margin-bottom: $spacing-lg;

      .input-label {
        display: block;
        font-weight: $font-weight-medium;
        color: $text-primary;
        margin-bottom: $spacing-sm;
      }

      .emotion-textarea {
        width: 100%;
        padding: $spacing-md;
        border: 2px solid $border-color;
        border-radius: $border-radius;
        font-size: 1rem;
        font-family: inherit;
        resize: vertical;
        transition: border-color 0.3s ease;

        &:focus {
          outline: none;
          border-color: $primary-color;
        }
      }
    }

    .analysis-options {
      margin-bottom: $spacing-lg;

      .option-group {
        .option-label {
          display: block;
          font-weight: $font-weight-medium;
          color: $text-primary;
          margin-bottom: $spacing-sm;
        }

        .radio-group {
          display: flex;
          gap: $spacing-lg;

          .radio-item {
            display: flex;
            align-items: center;
            cursor: pointer;

            input[type="radio"] {
              margin-right: $spacing-xs;
            }

            span {
              color: $text-primary;
            }
          }
        }
      }

      .analyze-btn {
        width: 100%;
        margin-top: $spacing-lg;
      }
    }
  }
}

.result-section {
  .result-card {
    margin-bottom: $spacing-xl;
  }
}

// 情感分析结果样式
.emotion-result {
  .emotion-summary {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: $spacing-lg;
    margin-bottom: $spacing-xl;

    @media (max-width: 768px) {
      grid-template-columns: 1fr;
    }

    .emotion-type {
      display: flex;
      align-items: center;
      gap: $spacing-sm;

      .type-label {
        font-weight: $font-weight-medium;
        color: $text-primary;
      }

      .type-value {
        padding: $spacing-xs $spacing-sm;
        border-radius: 20px;
        font-size: 0.9rem;
        font-weight: $font-weight-medium;

        &.emotion-positive {
          background: $success-color;
          color: white;
        }

        &.emotion-negative {
          background: $error-color;
          color: white;
        }

        &.emotion-neutral {
          background: $text-secondary;
          color: white;
        }

        &.emotion-mixed {
          background: $warning-color;
          color: white;
        }
      }
    }

    .emotion-intensity {
      display: flex;
      align-items: center;
      gap: $spacing-sm;

      .intensity-label {
        font-weight: $font-weight-medium;
        color: $text-primary;
      }

      .intensity-bar {
        flex: 1;
        height: 8px;
        background: $border-color;
        border-radius: 4px;
        overflow: hidden;

        .intensity-fill {
          height: 100%;
          background: $primary-color;
          transition: width 0.3s ease;
        }
      }

      .intensity-value {
        font-weight: $font-weight-medium;
        color: $primary-color;
        min-width: 40px;
      }
    }
  }

  .emotion-details {
    margin-bottom: $spacing-xl;

    h4 {
      color: $text-primary;
      margin: 0 0 $spacing-md 0;
      font-size: 1.1rem;
      font-weight: $font-weight-semibold;
    }

    .keyword-tags {
      display: flex;
      flex-wrap: wrap;
      gap: $spacing-sm;

      .keyword-tag {
        padding: $spacing-xs $spacing-sm;
        background: $bg-secondary;
        color: $text-primary;
        border-radius: 20px;
        font-size: 0.9rem;
        font-weight: $font-weight-medium;
      }
    }
  }

  .emotion-suggestions {
    h4 {
      color: $text-primary;
      margin: 0 0 $spacing-md 0;
      font-size: 1.1rem;
      font-weight: $font-weight-semibold;
    }

    .suggestion-list {
      list-style: none;
      padding: 0;

      li {
        padding: $spacing-sm 0;
        border-bottom: 1px solid $border-color;
        color: $text-secondary;
        line-height: 1.6;

        &:last-child {
          border-bottom: none;
        }
      }
    }
  }
}

// 关系健康度结果样式
.relationship-result {
  .health-score {
    display: flex;
    align-items: center;
    gap: $spacing-xl;
    margin-bottom: $spacing-xl;

    @media (max-width: 768px) {
      flex-direction: column;
      text-align: center;
    }

    .score-circle {
      width: 120px;
      height: 120px;
      border-radius: 50%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: white;
      font-weight: $font-weight-bold;

      &.health-excellent {
        background: $success-color;
      }

      &.health-good {
        background: #28a745;
      }

      &.health-fair {
        background: $warning-color;
      }

      &.health-poor {
        background: #fd7e14;
      }

      &.health-critical {
        background: $error-color;
      }

      .score-value {
        font-size: 2rem;
        line-height: 1;
      }

      .score-label {
        font-size: 0.9rem;
        margin-top: $spacing-xs;
      }
    }

    .score-info {
      h4 {
        margin: 0 0 $spacing-sm 0;
        color: $text-primary;
        font-size: 1.3rem;
      }

      p {
        margin: 0;
        color: $text-secondary;
        line-height: 1.6;
      }
    }
  }

  .dimension-analysis {
    margin-bottom: $spacing-xl;

    h4 {
      color: $text-primary;
      margin: 0 0 $spacing-lg 0;
      font-size: 1.1rem;
      font-weight: $font-weight-semibold;
    }

    .dimension-item {
      margin-bottom: $spacing-lg;

      .dimension-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: $spacing-sm;

        .dimension-name {
          font-weight: $font-weight-medium;
          color: $text-primary;
        }

        .dimension-score {
          font-weight: $font-weight-bold;
          color: $primary-color;
        }
      }

      .dimension-bar {
        height: 8px;
        background: $border-color;
        border-radius: 4px;
        overflow: hidden;
        margin-bottom: $spacing-sm;

        .dimension-fill {
          height: 100%;
          transition: width 0.3s ease;

          &.dimension-excellent {
            background: $success-color;
          }

          &.dimension-good {
            background: #28a745;
          }

          &.dimension-fair {
            background: $warning-color;
          }

          &.dimension-poor {
            background: $error-color;
          }
        }
      }

      .dimension-comment {
        margin: 0;
        color: $text-secondary;
        font-size: 0.9rem;
      }
    }
  }

  .improvement-suggestions {
    h4 {
      color: $text-primary;
      margin: 0 0 $spacing-lg 0;
      font-size: 1.1rem;
      font-weight: $font-weight-semibold;
    }

    .suggestion-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: $spacing-lg;

      .suggestion-item {
        background: $bg-secondary;
        padding: $spacing-lg;
        border-radius: $border-radius;

        h5 {
          margin: 0 0 $spacing-sm 0;
          color: $text-primary;
          font-size: 1rem;
        }

        p {
          margin: 0;
          color: $text-secondary;
          line-height: 1.6;
        }
      }
    }
  }
}

// 沟通质量结果样式
.communication-result {
  .communication-score {
    display: flex;
    align-items: center;
    gap: $spacing-xl;
    margin-bottom: $spacing-xl;

    @media (max-width: 768px) {
      flex-direction: column;
      text-align: center;
    }

    .score-display {
      .score-number {
        font-size: 3rem;
        font-weight: $font-weight-bold;
        color: $primary-color;
        line-height: 1;
      }

      .score-unit {
        font-size: 1.5rem;
        color: $text-secondary;
        margin-left: $spacing-xs;
      }
    }

    .score-description {
      h4 {
        margin: 0 0 $spacing-sm 0;
        color: $text-primary;
        font-size: 1.3rem;
      }

      p {
        margin: 0;
        color: $text-secondary;
        line-height: 1.6;
      }
    }
  }

  .communication-aspects {
    margin-bottom: $spacing-xl;

    h4 {
      color: $text-primary;
      margin: 0 0 $spacing-lg 0;
      font-size: 1.1rem;
      font-weight: $font-weight-semibold;
    }

    .aspect-item {
      margin-bottom: $spacing-lg;

      .aspect-info {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: $spacing-sm;

        .aspect-name {
          font-weight: $font-weight-medium;
          color: $text-primary;
        }

        .aspect-score {
          font-weight: $font-weight-bold;
          color: $primary-color;
        }
      }

      .aspect-bar {
        height: 8px;
        background: $border-color;
        border-radius: 4px;
        overflow: hidden;
        margin-bottom: $spacing-sm;

        .aspect-fill {
          height: 100%;
          background: $primary-color;
          transition: width 0.3s ease;
        }
      }

      .aspect-analysis {
        margin: 0;
        color: $text-secondary;
        font-size: 0.9rem;
      }
    }
  }

  .communication-tips {
    h4 {
      color: $text-primary;
      margin: 0 0 $spacing-lg 0;
      font-size: 1.1rem;
      font-weight: $font-weight-semibold;
    }

    .tips-list {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: $spacing-lg;

      .tip-item {
        background: $bg-secondary;
        padding: $spacing-lg;
        border-radius: $border-radius;

        h5 {
          margin: 0 0 $spacing-sm 0;
          color: $text-primary;
          font-size: 1rem;
        }

        p {
          margin: 0;
          color: $text-secondary;
          line-height: 1.6;
        }
      }
    }
  }
}

// 历史记录样式
.history-section {
  .history-list {
    .history-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: $spacing-md;
      border: 1px solid $border-color;
      border-radius: $border-radius;
      margin-bottom: $spacing-sm;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        background: $bg-hover;
        border-color: $primary-color;
      }

      .history-content {
        flex: 1;

        .history-text {
          margin: 0 0 $spacing-xs 0;
          color: $text-primary;
          font-weight: $font-weight-medium;
        }

        .history-meta {
          display: flex;
          gap: $spacing-md;
          font-size: 0.8rem;
          color: $text-secondary;

          .history-type {
            background: $bg-secondary;
            padding: $spacing-xs $spacing-sm;
            border-radius: 12px;
          }

          .history-time {
            color: $text-secondary;
          }
        }
      }

      .history-action {
        .btn-secondary {
          padding: $spacing-xs $spacing-sm;
          font-size: 0.8rem;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .emotion-page {
    padding: $spacing-md;
  }

  .page-header {
    .page-title {
      font-size: 2rem;
    }
  }

  .emotion-content {
    gap: $spacing-lg;
  }

  .analysis-options {
    .option-group {
      .radio-group {
        flex-direction: column;
        gap: $spacing-sm;
      }
    }
  }

  .suggestion-grid,
  .tips-list {
    grid-template-columns: 1fr;
  }
}
</style>