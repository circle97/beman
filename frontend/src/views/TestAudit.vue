<template>
  <div class="test-audit-page">
    <h1>内容审核功能测试</h1>
    
    <div class="test-section">
      <h2>测试内容输入</h2>
      <div class="form-group">
        <label>输入测试内容：</label>
        <textarea 
          v-model="testContent" 
          placeholder="输入一些测试内容，比如包含'分手'、'离婚'等关键词"
          rows="4"
          @input="onContentChange"
        ></textarea>
      </div>
      
      <div class="test-buttons">
        <button @click="testExtremeContent" class="btn-test">测试极端内容</button>
        <button @click="testNormalContent" class="btn-test">测试正常内容</button>
        <button @click="testSensitiveContent" class="btn-test">测试敏感内容</button>
        <button @click="clearContent" class="btn-clear">清空内容</button>
      </div>
    </div>
    
    <div class="test-section">
      <h2>审核结果</h2>
      <div v-if="auditResult" class="audit-result">
        <div class="result-item">
          <strong>是否为极端内容：</strong>
          <span :class="auditResult.isExtreme ? 'extreme' : 'normal'">
            {{ auditResult.isExtreme ? '是' : '否' }}
          </span>
        </div>
        <div class="result-item">
          <strong>风险等级：</strong>
          <span :class="getRiskLevelClass(auditResult.riskLevel)">
            {{ getRiskLevelText(auditResult.riskLevel) }}
          </span>
        </div>
        <div v-if="auditResult.suggestion" class="result-item">
          <strong>建议：</strong>
          <p class="suggestion">{{ auditResult.suggestion }}</p>
        </div>
        <div class="result-item">
          <strong>审核状态：</strong>
          <span :class="getStatusClass(auditResult.auditStatus)">
            {{ getStatusText(auditResult.auditStatus) }}
          </span>
        </div>
      </div>
      <div v-else class="no-result">
        请输入内容进行测试
      </div>
    </div>
    
    <div class="test-section">
      <h2>API测试</h2>
      <div class="api-tests">
        <button @click="testAuditAPI" class="btn-api">测试审核API</button>
        <button @click="testRiskAPI" class="btn-api">测试风险等级API</button>
        <button @click="testSuggestionAPI" class="btn-api">测试建议API</button>
      </div>
      <div v-if="apiResult" class="api-result">
        <pre>{{ JSON.stringify(apiResult, null, 2) }}</pre>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { auditAPI } from '../api/audit'

const testContent = ref('')
const auditResult = ref<any>(null)
const apiResult = ref<any>(null)

const onContentChange = async () => {
  if (testContent.value.trim().length < 5) {
    auditResult.value = null
    return
  }
  
  try {
    const result = await auditAPI.checkExtremeContent(testContent.value)
    if (result.success) {
      auditResult.value = result.data
    }
  } catch (error) {
    console.error('内容审核失败:', error)
  }
}

const testExtremeContent = () => {
  testContent.value = '我真的受不了了，我要和她分手，永远不再见面！'
}

const testNormalContent = () => {
  testContent.value = '今天和女朋友一起看了电影，感觉很开心，关系越来越好了。'
}

const testSensitiveContent = () => {
  testContent.value = '最近和女朋友吵架了，感觉很生气，也很失望，不知道该怎么办。'
}

const clearContent = () => {
  testContent.value = ''
  auditResult.value = null
  apiResult.value = null
}

const getRiskLevelClass = (riskLevel: number) => {
  switch (riskLevel) {
    case 0: return 'risk-low'
    case 1: return 'risk-medium'
    case 2: return 'risk-high'
    default: return 'risk-unknown'
  }
}

const getRiskLevelText = (riskLevel: number) => {
  switch (riskLevel) {
    case 0: return '低风险'
    case 1: return '中风险'
    case 2: return '高风险'
    default: return '未知'
  }
}

const getStatusClass = (status: number) => {
  switch (status) {
    case 0: return 'status-pending'
    case 1: return 'status-approved'
    case 2: return 'status-rejected'
    default: return 'status-unknown'
  }
}

const getStatusText = (status: number) => {
  switch (status) {
    case 0: return '待审核'
    case 1: return '通过'
    case 2: return '拒绝'
    default: return '未知'
  }
}

const testAuditAPI = async () => {
  if (!testContent.value.trim()) {
    alert('请先输入测试内容')
    return
  }
  
  try {
    const result = await auditAPI.auditContent(testContent.value)
    apiResult.value = result
  } catch (error) {
    console.error('API测试失败:', error)
    apiResult.value = { error: error.message }
  }
}

const testRiskAPI = async () => {
  if (!testContent.value.trim()) {
    alert('请先输入测试内容')
    return
  }
  
  try {
    const result = await auditAPI.getRiskLevel(testContent.value)
    apiResult.value = result
  } catch (error) {
    console.error('API测试失败:', error)
    apiResult.value = { error: error.message }
  }
}

const testSuggestionAPI = async () => {
  if (!testContent.value.trim()) {
    alert('请先输入测试内容')
    return
  }
  
  try {
    const result = await auditAPI.getSuggestion(testContent.value)
    apiResult.value = result
  } catch (error) {
    console.error('API测试失败:', error)
    apiResult.value = { error: error.message }
  }
}
</script>

<style lang="scss" scoped>
.test-audit-page {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  
  h1 {
    text-align: center;
    color: #333;
    margin-bottom: 30px;
  }
  
  .test-section {
    margin-bottom: 30px;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 8px;
    background: #f9f9f9;
    
    h2 {
      margin-top: 0;
      color: #555;
      border-bottom: 2px solid #007bff;
      padding-bottom: 10px;
    }
    
    .form-group {
      margin-bottom: 15px;
      
      label {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
        color: #333;
      }
      
      textarea {
        width: 100%;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
        font-size: 14px;
        resize: vertical;
      }
    }
    
    .test-buttons, .api-tests {
      display: flex;
      gap: 10px;
      flex-wrap: wrap;
      margin-bottom: 15px;
      
      button {
        padding: 8px 16px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 14px;
        transition: all 0.3s;
        
        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 2px 8px rgba(0,0,0,0.2);
        }
      }
      
      .btn-test {
        background: #007bff;
        color: white;
        
        &:hover {
          background: #0056b3;
        }
      }
      
      .btn-clear {
        background: #6c757d;
        color: white;
        
        &:hover {
          background: #545b62;
        }
      }
      
      .btn-api {
        background: #28a745;
        color: white;
        
        &:hover {
          background: #1e7e34;
        }
      }
    }
  }
  
  .audit-result {
    background: white;
    padding: 15px;
    border-radius: 4px;
    border: 1px solid #ddd;
    
    .result-item {
      margin-bottom: 10px;
      
      strong {
        color: #333;
        margin-right: 10px;
      }
      
      .extreme {
        color: #dc3545;
        font-weight: bold;
      }
      
      .normal {
        color: #28a745;
        font-weight: bold;
      }
      
      .risk-high {
        color: #dc3545;
        font-weight: bold;
      }
      
      .risk-medium {
        color: #ffc107;
        font-weight: bold;
      }
      
      .risk-low {
        color: #28a745;
        font-weight: bold;
      }
      
      .status-approved {
        color: #28a745;
        font-weight: bold;
      }
      
      .status-rejected {
        color: #dc3545;
        font-weight: bold;
      }
      
      .status-pending {
        color: #ffc107;
        font-weight: bold;
      }
      
      .suggestion {
        margin: 5px 0 0 20px;
        font-style: italic;
        color: #666;
        background: #f8f9fa;
        padding: 10px;
        border-radius: 4px;
        border-left: 3px solid #007bff;
      }
    }
  }
  
  .no-result {
    text-align: center;
    color: #666;
    font-style: italic;
    padding: 20px;
  }
  
  .api-result {
    background: white;
    padding: 15px;
    border-radius: 4px;
    border: 1px solid #ddd;
    
    pre {
      background: #f8f9fa;
      padding: 10px;
      border-radius: 4px;
      overflow-x: auto;
      font-size: 12px;
      color: #333;
    }
  }
}
</style>
