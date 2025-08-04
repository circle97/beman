<template>
  <div class="emotion-view">
    <h2>情绪解码器</h2>
    <p>理解伴侣话语背后的真实需求</p>
    
    <div class="emotion-analyzer">
      <div class="input-section">
        <textarea 
          v-model="partnerText" 
          placeholder="输入伴侣说的话..."
          rows="4"
          class="text-input"
        ></textarea>
        <button @click="analyzeEmotion" class="analyze-btn">分析情绪</button>
      </div>
      
      <div v-if="analysisResult" class="result-section">
        <div class="result-card">
          <h3>分析结果</h3>
          <div class="result-item">
            <span class="label">情绪状态：</span>
            <span class="value">{{ analysisResult.emotion }}</span>
          </div>
          <div class="result-item">
            <span class="label">真实需求：</span>
            <span class="value">{{ analysisResult.need }}</span>
          </div>
          <div class="result-item">
            <span class="label">建议回应：</span>
            <span class="value">{{ analysisResult.suggestedResponse }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const partnerText = ref('')
const analysisResult = ref<any>(null)

const analyzeEmotion = () => {
  if (!partnerText.value.trim()) return
  
  // 模拟情绪分析结果
  const lowerText = partnerText.value.toLowerCase()
  
  if (lowerText.includes('累') || lowerText.includes('疲惫')) {
    analysisResult.value = {
      emotion: '疲惫',
      need: '关心',
      suggestedResponse: '辛苦了，我给你揉揉肩吧？'
    }
  } else if (lowerText.includes('不关心') || lowerText.includes('忽略')) {
    analysisResult.value = {
      emotion: '失望',
      need: '关注',
      suggestedResponse: '对不起，我最近确实忽略你了，我们聊聊吧'
    }
  } else {
    analysisResult.value = {
      emotion: '中性',
      need: '陪伴',
      suggestedResponse: '我在听，继续说'
    }
  }
}
</script>

<style scoped>
.emotion-view {
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

.emotion-analyzer {
  max-width: 600px;
  margin: 0 auto;
}

.input-section {
  margin-bottom: 30px;
}

.text-input {
  width: 100%;
  padding: 15px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.1);
  color: white;
  font-size: 16px;
  resize: vertical;
  margin-bottom: 15px;
}

.text-input::placeholder {
  color: rgba(255, 255, 255, 0.6);
}

.analyze-btn {
  background: linear-gradient(45deg, #ff6b6b, #4ecdc4);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 25px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.analyze-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
}

.result-section {
  margin-top: 30px;
}

.result-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 20px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  text-align: left;
}

.result-card h3 {
  color: white;
  margin-bottom: 20px;
  text-align: center;
}

.result-item {
  display: flex;
  margin-bottom: 15px;
  align-items: flex-start;
}

.label {
  color: rgba(255, 255, 255, 0.8);
  font-weight: 500;
  min-width: 80px;
  margin-right: 10px;
}

.value {
  color: white;
  flex: 1;
}
</style> 