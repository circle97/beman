<template>
  <div class="post-detail-page">
    <!-- 返回按钮 -->
    <div class="back-button">
      <button class="btn-back" @click="goBack">
        <svg viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
          <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/>
        </svg>
        返回社区
      </button>
    </div>

    <!-- 帖子详情 -->
    <div v-if="post" class="post-detail">
      <BemanCard class="post-card">
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
            <!-- 用户关注按钮 -->
            <UserFollowButton 
              :target-user-id="post.authorId"
              :target-user-name="post.authorName"
              class="follow-btn"
            />
            
            <!-- 举报按钮 -->
            <ReportButton 
              :content-type="1"
              :content-id="post.id"
              :content-title="post.title"
              class="report-btn"
            />
            
            <button class="action-btn" @click="likePost">
              <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
                <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
              </svg>
              {{ post.likeCount }}
            </button>
          </div>
        </div>
        
        <div class="post-content">
          <h1 class="post-title">{{ post.title }}</h1>
          <p class="post-text">{{ post.content }}</p>
          <div v-if="post.tags && post.tags.length > 0" class="post-tags">
            <span 
              v-for="tag in post.tags" 
              :key="tag"
              class="tag"
            >
              #{{ tag }}
            </span>
          </div>
        </div>
      </BemanCard>
    </div>

    <!-- 评论区域 -->
    <div class="comments-section">
      <h2 class="section-title">评论 ({{ commentCount }})</h2>
      
      <!-- 发表评论 -->
      <BemanCard class="comment-form-card">
        <div class="comment-form">
          <textarea 
            v-model="newComment"
            placeholder="写下你的评论..."
            rows="3"
            class="comment-input"
          ></textarea>
          <div class="comment-form-footer">
            <label class="checkbox-label">
              <input v-model="isAnonymous" type="checkbox" />
              <span>匿名评论</span>
            </label>
            <button 
              class="btn-primary" 
              @click="submitComment"
              :disabled="!newComment.trim()"
            >
              发表评论
            </button>
          </div>
        </div>
      </BemanCard>

      <!-- 评论列表 -->
      <CommentList 
        :post-id="postId"
        @comment-added="onCommentAdded"
        @comment-deleted="onCommentDeleted"
      />
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>

    <!-- 错误状态 -->
    <div v-if="error" class="error-state">
      <svg viewBox="0 0 24 24" fill="currentColor" width="64" height="64">
        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
      </svg>
      <h3>加载失败</h3>
      <p>{{ error }}</p>
      <button class="btn-primary" @click="loadPost">重试</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BemanCard from '../components/BemanCard.vue'
import CommentList from '../components/CommentList.vue'
import UserFollowButton from '../components/UserFollowButton.vue'
import ReportButton from '../components/ReportButton.vue'
import { getPostById, likePost as likePostApi, type Post } from '../api/post'
import { createComment, type CommentCreateDTO } from '../api/comment'

// 路由相关
const route = useRoute()
const router = useRouter()
const postId = computed(() => Number(route.params.id))

// 响应式数据
const loading = ref(false)
const error = ref('')
const post = ref<Post | null>(null)
const newComment = ref('')
const isAnonymous = ref(false)
const commentCount = ref(0)

// 方法
const loadPost = async () => {
  if (!postId.value) return
  
  loading.value = true
  error.value = ''
  
  try {
    const postData = await getPostById(postId.value)
    post.value = postData
    commentCount.value = postData.commentCount || 0
  } catch (err: any) {
    error.value = err.message || '加载帖子失败'
    console.error('加载帖子失败:', err)
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.back()
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

const likePost = async () => {
  if (!post.value) return
  
  try {
    await likePostApi(post.value.id)
    post.value.likeCount++
  } catch (error) {
    console.error('点赞失败:', error)
  }
}

const submitComment = async () => {
  if (!post.value || !newComment.value.trim()) return
  
  try {
    const commentData: CommentCreateDTO = {
      postId: post.value.id,
      content: newComment.value.trim(),
      isAnonymous: isAnonymous.value
    }
    
    await createComment(commentData)
    
    // 重置表单
    newComment.value = ''
    isAnonymous.value = false
    
    // 更新评论数量
    commentCount.value++
    
    // 通知评论列表组件刷新
    // CommentList 组件会自动刷新
  } catch (error) {
    console.error('发表评论失败:', error)
  }
}

const onCommentAdded = () => {
  commentCount.value++
}

const onCommentDeleted = () => {
  commentCount.value = Math.max(0, commentCount.value - 1)
}

onMounted(() => {
  loadPost()
})
</script>

<style lang="scss" scoped>
@import '../styles/variables.scss';

.post-detail-page {
  padding: $spacing-md;
  max-width: 800px;
  margin: 0 auto;
}

.back-button {
  margin-bottom: $spacing-md;
}

.btn-back {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  padding: $spacing-sm $spacing-md;
  background: none;
  border: 1px solid $border-card;
  color: $text-secondary;
  cursor: pointer;
  border-radius: $radius-button;
  transition: all 0.2s;
  
  &:hover {
    background: rgba($color-secondary, 0.1);
    border-color: $color-secondary;
    color: $color-secondary;
  }
}

.post-detail {
  margin-bottom: $spacing-xl;
}

.post-card {
  .post-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: $spacing-lg;
  }
  
  .post-author {
    display: flex;
    align-items: center;
    gap: $spacing-md;
  }
  
  .author-avatar {
    width: 48px;
    height: 48px;
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
      font-size: $font-size-lg;
    }
  }
  
  .author-info {
    .author-name {
      font-weight: $font-weight-semibold;
      color: $text-primary;
      font-size: $font-size-lg;
      margin-bottom: $spacing-xs;
    }
    
    .post-time {
      font-size: $font-size-sm;
      color: $text-secondary;
    }
  }
  
  .post-actions {
    display: flex;
    align-items: center;
    gap: $spacing-sm;
    
    .follow-btn,
    .report-btn {
      margin-right: $spacing-sm;
    }
  }
  
  .action-btn {
    display: flex;
    align-items: center;
    gap: $spacing-xs;
    padding: $spacing-sm $spacing-md;
    background: none;
    border: 1px solid $border-card;
    color: $text-secondary;
    cursor: pointer;
    border-radius: $radius-button;
    transition: all 0.2s;
    
    &:hover {
      background: rgba($color-secondary, 0.1);
      border-color: $color-secondary;
      color: $color-secondary;
    }
  }
  
  .post-content {
    .post-title {
      font-size: $font-size-xxl;
      font-weight: $font-weight-bold;
      color: $text-primary;
      margin-bottom: $spacing-lg;
      line-height: 1.3;
    }
    
    .post-text {
      color: $text-secondary;
      line-height: 1.8;
      font-size: $font-size-lg;
      margin-bottom: $spacing-lg;
    }
    
    .post-tags {
      display: flex;
      flex-wrap: wrap;
      gap: $spacing-sm;
      
      .tag {
        padding: $spacing-xs $spacing-md;
        background: rgba($color-secondary, 0.1);
        color: $color-secondary;
        border-radius: 16px;
        font-size: $font-size-sm;
        font-weight: $font-weight-medium;
      }
    }
  }
}

.comments-section {
  .section-title {
    font-size: $font-size-xl;
    font-weight: $font-weight-semibold;
    color: $text-primary;
    margin-bottom: $spacing-lg;
  }
}

.comment-form-card {
  margin-bottom: $spacing-lg;
}

.comment-form {
  .comment-input {
    width: 100%;
    padding: $spacing-md;
    border: 1px solid $border-card;
    border-radius: $radius-button;
    background: $bg-app;
    color: $text-primary;
    font-size: $font-size-md;
    resize: vertical;
    min-height: 80px;
    
    &:focus {
      outline: none;
      border-color: $color-secondary;
    }
    
    &::placeholder {
      color: $text-secondary;
    }
  }
  
  .comment-form-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: $spacing-md;
  }
  
  .checkbox-label {
    display: flex;
    align-items: center;
    gap: $spacing-sm;
    cursor: pointer;
    color: $text-secondary;
    
    input[type="checkbox"] {
      width: auto;
      margin: 0;
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

.error-state {
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

// 响应式设计
@media (max-width: $breakpoint-md) {
  .post-detail-page {
    padding: $spacing-sm;
  }
  
  .post-card {
    .post-header {
      flex-direction: column;
      align-items: flex-start;
      gap: $spacing-md;
    }
    
    .post-actions {
      width: 100%;
      justify-content: space-between;
    }
  }
  
  .comment-form-footer {
    flex-direction: column;
    gap: $spacing-md;
    align-items: stretch;
  }
}
</style>
