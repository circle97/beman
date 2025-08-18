<template>
  <div class="community-page">
    <!-- 顶部导航 -->
    <div class="community-header">
      <div class="header-content">
        <h1 class="page-title">男性互助社区</h1>
        <div class="header-actions">
          <button class="btn-secondary" @click="showFilterModal = true">
            <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
              <path d="M10 18h4v-2h-4v2zM3 6v2h18V6H3zm3 7h12v-2H6v2z"/>
            </svg>
            筛选
          </button>
          <button class="btn-primary" @click="showPostModal = true">
            <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
              <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
            </svg>
            发帖
          </button>
        </div>
      </div>
    </div>

    <!-- 标签筛选 -->
    <div class="tag-filter" v-if="selectedTags.length > 0">
      <div class="tag-list">
        <span 
          v-for="tag in selectedTags" 
          :key="tag"
          class="tag-item"
          @click="removeTag(tag)"
        >
          {{ tag }}
          <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
            <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
          </svg>
        </span>
      </div>
    </div>

    <!-- 帖子列表 -->
    <div class="post-list">
      <div v-if="loading" class="loading">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>
      
      <div v-else-if="posts.length === 0" class="empty-state">
        <svg viewBox="0 0 24 24" fill="currentColor" width="64" height="64">
          <path d="M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm-5 14H4v-4h11v4zm0-5H4V9h11v4zm5 5h-4V9h4v9z"/>
        </svg>
        <h3>暂无帖子</h3>
        <p>快来分享你的经验和想法吧</p>
        <button class="btn-primary" @click="showPostModal = true">发布第一篇帖子</button>
      </div>
      
      <BemanCard 
        v-else
        v-for="post in posts" 
        :key="post.id"
        class="post-card"
        clickable
        @click="viewPost(post)"
      >
        <div class="post-header">
          <div class="post-author">
            <div class="author-avatar">
              <img v-if="post.authorAvatar" :src="post.authorAvatar" :alt="post.authorName">
              <div v-else class="avatar-placeholder">
                {{ post.authorName?.charAt(0) || '匿' }}
              </div>
            </div>
            <div class="author-info">
              <div class="author-name">{{ post.authorName || '匿名用户' }}</div>
              <div class="post-time">{{ formatTime(post.createTime) }}</div>
            </div>
          </div>
          <div class="post-actions">
            <button class="action-btn" @click.stop="likePost(post)">
              <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
                <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
              </svg>
              {{ post.likeCount }}
            </button>
            <button class="action-btn" @click.stop="viewPost(post)">
              <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
                <path d="M21.99 4c0-1.1-.89-2-2-2H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h14l4 4-.01-18zM18 14H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z"/>
              </svg>
              {{ post.commentCount }}
            </button>
          </div>
        </div>
        
        <div class="post-content">
          <h3 class="post-title">{{ post.title }}</h3>
          <p class="post-text">{{ post.content }}</p>
          <div v-if="post.tags && post.tags.length > 0" class="post-tags">
            <span 
              v-for="tag in post.tags" 
              :key="tag"
              class="tag"
              @click.stop="selectTag(tag)"
            >
              #{{ tag }}
            </span>
          </div>
        </div>
      </BemanCard>
    </div>

    <!-- 发帖模态框 -->
    <div v-if="showPostModal" class="modal-overlay" @click="showPostModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>发布帖子</h3>
          <button class="close-btn" @click="showPostModal = false">
            <svg viewBox="0 0 24 24" fill="currentColor" width="24" height="24">
              <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
            </svg>
          </button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>标题</label>
            <input v-model="newPost.title" type="text" placeholder="请输入标题" />
          </div>
          <div class="form-group">
            <label>内容</label>
            <textarea 
              v-model="newPost.content" 
              placeholder="分享你的经验和想法..." 
              rows="6"
              @input="onContentInput"
            ></textarea>
            
            <!-- 内容审核状态显示 -->
            <div v-if="auditResult" class="audit-status">
              <div v-if="auditResult.isExtreme" class="audit-warning">
                <div class="warning-header">
                  <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
                    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                  </svg>
                  <span>检测到极端内容</span>
                </div>
                <div class="warning-content">
                  <p class="suggestion">{{ auditResult.suggestion }}</p>
                  <div class="risk-level">
                    风险等级: 
                    <span :class="getRiskLevelClass(auditResult.riskLevel)">
                      {{ getRiskLevelText(auditResult.riskLevel) }}
                    </span>
                  </div>
                </div>
              </div>
              <div v-else-if="auditResult.riskLevel === 1" class="audit-notice">
                <div class="notice-header">
                  <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
                    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                  </svg>
                  <span>内容需要关注</span>
                </div>
                <p class="suggestion">{{ auditResult.suggestion }}</p>
              </div>
              <div v-else class="audit-success">
                <div class="success-header">
                  <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
                    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                  </svg>
                  <span>内容审核通过</span>
                </div>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label>标签</label>
            <div class="tag-input">
              <span 
                v-for="tag in newPost.tags" 
                :key="tag"
                class="tag-item"
                @click="removeNewPostTag(tag)"
              >
                {{ tag }}
                <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
                  <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 19 17.59 13.41 12z"/>
                </svg>
              </span>
              <input 
                v-model="tagInput" 
                type="text" 
                placeholder="输入标签后按回车"
                @keyup.enter="addTag"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="checkbox-label">
              <input v-model="newPost.isAnonymous" type="checkbox" />
              <span>匿名发布</span>
            </label>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-secondary" @click="showPostModal = false">取消</button>
          <button class="btn-primary" @click="submitPost" :disabled="!newPost.title || !newPost.content">
            发布
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import BemanCard from '../components/BemanCard.vue'
import { createPost, getPostPage, likePost as likePostApi, getHotTags, type Post, type PostCreateDTO } from '../api/post'
import { auditAPI } from '../api/audit'

// 响应式数据
const loading = ref(false)
const posts = ref<Post[]>([])
const selectedTags = ref<string[]>([])
const showPostModal = ref(false)
const showFilterModal = ref(false)
const tagInput = ref('')
const hotTags = ref<string[]>([])

// 新帖子数据
const newPost = ref<PostCreateDTO>({
  title: '',
  content: '',
  tags: [],
  isAnonymous: false
})

// 内容审核相关
const auditResult = ref<any>(null)
const isAuditing = ref(false)



// 方法
const loadPosts = async () => {
  loading.value = true
  try {
    const pageResult = await getPostPage({
      page: 1,
      size: 20,
      tags: selectedTags.value.length > 0 ? selectedTags.value : undefined
    })
    posts.value = pageResult.records
  } catch (error) {
    console.error('加载帖子失败:', error)
  } finally {
    loading.value = false
  }
}

const loadHotTags = async () => {
  try {
    const tags = await getHotTags()
    hotTags.value = tags
  } catch (error) {
    console.error('加载热门标签失败:', error)
  }
}

const formatTime = (timeStr: string) => {
  const date = new Date(timeStr)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  return date.toLocaleDateString()
}

const viewPost = (post: any) => {
  console.log('查看帖子:', post)
  // TODO: 跳转到帖子详情页
}

const likePost = async (post: Post) => {
  try {
    await likePostApi(post.id)
    post.likeCount++
  } catch (error) {
    console.error('点赞失败:', error)
  }
}

const selectTag = (tag: string) => {
  if (!selectedTags.value.includes(tag)) {
    selectedTags.value.push(tag)
  }
}

const removeTag = (tag: string) => {
  const index = selectedTags.value.indexOf(tag)
  if (index > -1) {
    selectedTags.value.splice(index, 1)
  }
}

const addTag = () => {
  const tag = tagInput.value.trim()
  if (tag && !newPost.value.tags.includes(tag)) {
    newPost.value.tags.push(tag)
    tagInput.value = ''
  }
}

const removeNewPostTag = (tag: string) => {
  const index = newPost.value.tags.indexOf(tag)
  if (index > -1) {
    newPost.value.tags.splice(index, 1)
  }
}

// 内容审核相关方法
const onContentInput = async () => {
  const content = newPost.value.content.trim()
  if (content.length < 10) {
    auditResult.value = null
    return
  }
  
  // 防抖处理，避免频繁调用API
  if (isAuditing.value) return
  
  isAuditing.value = true
  try {
    const result = await auditAPI.checkExtremeContent(content)
    if (result.success) {
      auditResult.value = result.data
    }
  } catch (error) {
    console.error('内容审核失败:', error)
  } finally {
    isAuditing.value = false
  }
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

const submitPost = async () => {
  // 检查内容是否通过审核
  if (auditResult.value && auditResult.value.isExtreme) {
    alert('内容包含极端信息，请修改后重新发布')
    return
  }
  
  try {
    await createPost(newPost.value)
    showPostModal.value = false
    // 重置表单和审核结果
    newPost.value = {
      title: '',
      content: '',
      tags: [],
      isAnonymous: false
    }
    auditResult.value = null
    // 重新加载帖子列表
    await loadPosts()
  } catch (error) {
    console.error('发布失败:', error)
  }
}

onMounted(() => {
  loadPosts()
  loadHotTags()
})
</script>

<style lang="scss" scoped>
@use 'sass:color';
@import '../styles/variables.scss';

// 内容审核样式
.audit-status {
  margin-top: $spacing-sm;
  padding: $spacing-sm;
  border-radius: $border-radius-sm;
  font-size: $font-size-sm;
  
  .audit-warning {
    background: rgba($color-warning, 0.1);
    border: 1px solid rgba($color-warning, 0.3);
    color: $color-warning;
    
    .warning-header {
      display: flex;
      align-items: center;
      gap: $spacing-xs;
      font-weight: $font-weight-medium;
      margin-bottom: $spacing-xs;
      
      svg {
        color: $color-warning;
      }
    }
    
    .warning-content {
      .suggestion {
        margin: $spacing-xs 0;
        font-style: italic;
      }
      
      .risk-level {
        font-size: $font-size-xs;
        opacity: 0.8;
        
        .risk-high {
          color: $color-danger;
          font-weight: $font-weight-medium;
        }
        
        .risk-medium {
          color: $color-warning;
          font-weight: $font-weight-medium;
        }
        
        .risk-low {
          color: $color-success;
          font-weight: $font-weight-medium;
        }
      }
    }
  }
  
  .audit-notice {
    background: rgba($color-info, 0.1);
    border: 1px solid rgba($color-info, 0.3);
    color: $color-info;
    
    .notice-header {
      display: flex;
      align-items: center;
      gap: $spacing-xs;
      font-weight: $font-weight-medium;
      margin-bottom: $spacing-xs;
      
      svg {
        color: $color-info;
      }
    }
    
    .suggestion {
      margin: $spacing-xs 0;
      font-style: italic;
    }
  }
  
  .audit-success {
    background: rgba($color-success, 0.1);
    border: 1px solid rgba($color-success, 0.3);
    color: $color-success;
    
    .success-header {
      display: flex;
      align-items: center;
      gap: $spacing-xs;
      font-weight: $font-weight-medium;
      
      svg {
        color: $color-success;
      }
    }
  }
}

.community-page {
  padding: $spacing-md;
  max-width: 1200px;
  margin: 0 auto;
}

.community-header {
  margin-bottom: $spacing-lg;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  font-size: $font-size-xxl;
  font-weight: $font-weight-bold;
  color: $text-primary;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: $spacing-sm;
}

.tag-filter {
  margin-bottom: $spacing-md;
  padding: $spacing-sm;
  background: $bg-section;
  border-radius: $radius-card;
  border: 1px solid $border-card;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-xs;
}

.tag-item {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  padding: $spacing-xs $spacing-sm;
  background: $color-secondary;
  color: white;
  border-radius: 16px;
  font-size: $font-size-sm;
  cursor: pointer;
  transition: all 0.2s;
  
  &:hover {
    background: color.adjust($color-secondary, $lightness: -10%);
  }
  
  svg {
    width: 12px;
    height: 12px;
  }
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
}

.post-card {
  .post-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: $spacing-md;
  }
  
  .post-author {
    display: flex;
    align-items: center;
    gap: $spacing-sm;
  }
  
  .author-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    overflow: hidden;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    
    .avatar-placeholder {
      width: 100%;
      height: 100%;
      background: $color-secondary;
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: $font-weight-bold;
    }
  }
  
  .author-info {
    .author-name {
      font-weight: $font-weight-medium;
      color: $text-primary;
      margin-bottom: $spacing-xs;
    }
    
    .post-time {
      font-size: $font-size-sm;
      color: $text-secondary;
    }
  }
  
  .post-actions {
    display: flex;
    gap: $spacing-sm;
  }
  
  .action-btn {
    display: flex;
    align-items: center;
    gap: $spacing-xs;
    padding: $spacing-xs $spacing-sm;
    background: none;
    border: none;
    color: $text-secondary;
    cursor: pointer;
    border-radius: $radius-button;
    transition: all 0.2s;
    
    &:hover {
      background: rgba($color-secondary, 0.1);
      color: $color-secondary;
    }
  }
  
  .post-content {
    .post-title {
      font-size: $font-size-lg;
      font-weight: $font-weight-semibold;
      color: $text-primary;
      margin-bottom: $spacing-sm;
    }
    
    .post-text {
      color: $text-secondary;
      line-height: 1.6;
      margin-bottom: $spacing-md;
    }
    
    .post-tags {
      display: flex;
      flex-wrap: wrap;
      gap: $spacing-xs;
      
      .tag {
        padding: $spacing-xs $spacing-sm;
        background: rgba($color-secondary, 0.1);
        color: $color-secondary;
        border-radius: 12px;
        font-size: $font-size-sm;
        cursor: pointer;
        transition: all 0.2s;
        
        &:hover {
          background: rgba($color-secondary, 0.2);
        }
      }
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

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
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
  
  p {
    margin-bottom: $spacing-lg;
  }
}

.modal-overlay {
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

.modal-content {
  background: $bg-section;
  border-radius: $radius-card;
  border: 1px solid $border-card;
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-lg;
  border-bottom: 1px solid $border-card;
  
  h3 {
    margin: 0;
    color: $text-primary;
  }
  
  .close-btn {
    background: none;
    border: none;
    color: $text-secondary;
    cursor: pointer;
    padding: $spacing-xs;
    border-radius: $radius-button;
    
    &:hover {
      background: rgba($color-secondary, 0.1);
      color: $color-secondary;
    }
  }
}

.modal-body {
  padding: $spacing-lg;
}

.form-group {
  margin-bottom: $spacing-md;
  
  label {
    display: block;
    margin-bottom: $spacing-xs;
    color: $text-primary;
    font-weight: $font-weight-medium;
  }
  
  input, textarea {
    width: 100%;
    padding: $spacing-sm;
    border: 1px solid $border-card;
    border-radius: $radius-button;
    background: $bg-app;
    color: $text-primary;
    font-size: $font-size-md;
    
    &:focus {
      outline: none;
      border-color: $color-secondary;
    }
    
    &::placeholder {
      color: $text-secondary;
    }
  }
  
  textarea {
    resize: vertical;
    min-height: 120px;
  }
}

.tag-input {
  display: flex;
  flex-wrap: wrap;
  gap: $spacing-xs;
  padding: $spacing-sm;
  border: 1px solid $border-card;
  border-radius: $radius-button;
  background: $bg-app;
  
  .tag-item {
    display: flex;
    align-items: center;
    gap: $spacing-xs;
    padding: $spacing-xs $spacing-sm;
    background: $color-secondary;
    color: white;
    border-radius: 12px;
    font-size: $font-size-sm;
    cursor: pointer;
    
    svg {
      width: 12px;
      height: 12px;
    }
  }
  
  input {
    flex: 1;
    min-width: 120px;
    border: none;
    background: none;
    padding: $spacing-xs;
    color: $text-primary;
    
    &:focus {
      outline: none;
    }
  }
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  cursor: pointer;
  
  input[type="checkbox"] {
    width: auto;
    margin: 0;
  }
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: $spacing-sm;
  padding: $spacing-lg;
  border-top: 1px solid $border-card;
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .community-page {
    padding: $spacing-sm;
  }
  
  .header-content {
    flex-direction: column;
    gap: $spacing-md;
    align-items: stretch;
  }
  
  .header-actions {
    justify-content: center;
  }
  
  .modal-content {
    width: 95%;
    margin: $spacing-sm;
  }
}
</style>
