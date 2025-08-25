<template>
  <div class="comment-list">
    <!-- 评论输入框 -->
    <div class="comment-input-section">
      <div class="comment-input-header">
        <h3>发表评论</h3>
        <div class="input-options">
          <label class="checkbox-label">
            <input type="checkbox" v-model="newComment.isAnonymous">
            <span>匿名发表</span>
          </label>
        </div>
      </div>
      
      <div class="comment-input-wrapper">
        <textarea
          v-model="newComment.content"
          placeholder="写下你的想法..."
          class="comment-textarea"
          :disabled="isSubmitting"
        ></textarea>
        
        <div class="comment-input-footer">
          <div class="input-tips">
            <span v-if="newComment.content.length < 10" class="tip-text">
              至少输入10个字符
            </span>
            <span v-else class="tip-text success">
              {{ newComment.content.length }}/500
            </span>
          </div>
          
          <button
            class="btn-primary"
            @click="submitComment"
            :disabled="!canSubmit || isSubmitting"
          >
            {{ isSubmitting ? '发送中...' : '发送评论' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 评论列表 -->
    <div class="comments-section">
      <div class="comments-header">
        <h3>评论 ({{ total }})</h3>
        <div class="sort-options">
          <button
            v-for="option in sortOptions"
            :key="option.value"
            class="sort-btn"
            :class="{ active: currentSort === option.value }"
            @click="changeSort(option.value)"
          >
            {{ option.label }}
          </button>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading">
        <div class="loading-spinner"></div>
        <p>加载评论中...</p>
      </div>

      <!-- 空状态 -->
      <div v-else-if="comments.length === 0" class="empty-state">
        <svg viewBox="0 0 24 24" fill="currentColor" width="48" height="48">
          <path d="M20 2H4c-1.1 0-1.99.9-1.99 2L2 22l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-2 12H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z"/>
        </svg>
        <p>暂无评论，快来抢沙发吧！</p>
      </div>

      <!-- 评论列表 -->
      <div v-else class="comments-container">
        <div
          v-for="comment in comments"
          :key="comment.id"
          class="comment-item"
        >
          <!-- 主评论 -->
          <div class="comment-main">
            <div class="comment-avatar">
              <img v-if="comment.authorAvatar" :src="comment.authorAvatar" :alt="comment.authorName">
              <div v-else class="avatar-placeholder">
                {{ comment.authorName?.charAt(0) || '匿' }}
              </div>
            </div>
            
            <div class="comment-content">
              <div class="comment-header">
                <span class="author-name">{{ comment.authorName }}</span>
                <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
              </div>
              
              <div class="comment-text">{{ comment.content }}</div>
              
              <div class="comment-actions">
                <button
                  class="action-btn"
                  @click="toggleLike(comment)"
                  :class="{ liked: comment.isLiked }"
                >
                  <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
                    <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                  </svg>
                  {{ comment.likeCount }}
                </button>
                
                <button class="action-btn" @click="showReplyInput(comment)">
                  <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
                    <path d="M10 9V5a3 3 0 0 1 6 0v4.27l2.45-2.45a1 1 0 0 1 1.41 1.41L16.73 12H19a1 1 0 0 1 0 2h-7a1 1 0 0 1-1-1V9z"/>
                  </svg>
                  回复
                </button>
                
                <button
                  v-if="canDeleteComment(comment)"
                  class="action-btn delete"
                  @click="deleteComment(comment.id)"
                >
                  <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
                    <path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/>
                  </svg>
                  删除
                </button>
              </div>
            </div>
          </div>

          <!-- 回复输入框 -->
          <div v-if="replyingTo === comment.id" class="reply-input">
            <div class="reply-input-wrapper">
              <textarea
                v-model="replyContent"
                placeholder="回复 @{{ comment.authorName }}..."
                class="reply-textarea"
                :disabled="isSubmitting"
              ></textarea>
              
              <div class="reply-input-footer">
                <button
                  class="btn-secondary"
                  @click="cancelReply"
                >
                  取消
                </button>
                <button
                  class="btn-primary"
                  @click="submitReply(comment)"
                  :disabled="!replyContent.trim() || isSubmitting"
                >
                  {{ isSubmitting ? '发送中...' : '发送回复' }}
                </button>
              </div>
            </div>
          </div>

          <!-- 子评论 -->
          <div v-if="comment.children && comment.children.length > 0" class="comment-replies">
            <div
              v-for="reply in comment.children"
              :key="reply.id"
              class="reply-item"
            >
              <div class="reply-avatar">
                <img v-if="reply.authorAvatar" :src="reply.authorAvatar" :alt="reply.authorName">
                <div v-else class="avatar-placeholder">
                  {{ reply.authorName?.charAt(0) || '匿' }}
                </div>
              </div>
              
              <div class="reply-content">
                <div class="reply-header">
                  <span class="author-name">{{ reply.authorName }}</span>
                  <span v-if="reply.replyUserName" class="reply-to">
                    回复 @{{ reply.replyUserName }}
                  </span>
                  <span class="reply-time">{{ formatTime(reply.createTime) }}</span>
                </div>
                
                <div class="reply-text">{{ reply.content }}</div>
                
                <div class="reply-actions">
                  <button
                    class="action-btn"
                    @click="toggleLike(reply)"
                    :class="{ liked: reply.isLiked }"
                  >
                    <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
                      <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                    </svg>
                    {{ reply.likeCount }}
                  </button>
                  
                  <button class="action-btn" @click="showReplyInput(comment, reply)">
                    <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
                      <path d="M10 9V5a3 3 0 0 1 6 0v4.27l2.45-2.45a1 1 0 0 1 1.41 1.41L16.73 12H19a1 1 0 0 1 0 2h-7a1 1 0 0 1-1-1V9z"/>
                    </svg>
                    回复
                  </button>
                  
                  <button
                    v-if="canDeleteComment(reply)"
                    class="action-btn delete"
                    @click="deleteComment(reply.id)"
                  >
                    <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
                      <path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/>
                    </svg>
                    删除
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- 查看更多回复 -->
          <div v-if="comment.childrenCount > 3" class="more-replies">
            <button class="more-replies-btn" @click="loadMoreReplies(comment)">
              查看全部 {{ comment.childrenCount }} 条回复
            </button>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="total > size" class="pagination">
        <button
          class="page-btn"
          :disabled="current <= 1"
          @click="changePage(current - 1)"
        >
          上一页
        </button>
        
        <span class="page-info">{{ current }} / {{ pages }}</span>
        
        <button
          class="page-btn"
          :disabled="current >= pages"
          @click="changePage(current + 1)"
        >
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { 
  createComment, 
  getCommentPage, 
  likeComment as likeCommentApi, 
  unlikeComment as unlikeCommentApi,
  deleteComment as deleteCommentApi,
  type Comment,
  type CommentCreateDTO
} from '../api/comment'
import { useUserStore } from '../stores/user'

// Props
interface Props {
  postId: number
}

const props = defineProps<Props>()

// 用户状态
const userStore = useUserStore()

// 响应式数据
const loading = ref(false)
const comments = ref<Comment[]>([])
const total = ref(0)
const current = ref(1)
const size = ref(20)
const pages = ref(0)
const currentSort = ref(1)
const isSubmitting = ref(false)
const replyingTo = ref<number | null>(null)
const replyContent = ref('')

// 新评论数据
const newComment = ref<CommentCreateDTO>({
  postId: props.postId,
  content: '',
  isAnonymous: false
})

// 排序选项
const sortOptions = [
  { label: '最新', value: 2 },
  { label: '最早', value: 1 },
  { label: '最热', value: 3 }
]

// 计算属性
const canSubmit = computed(() => {
  return newComment.value.content.trim().length >= 10 && 
         newComment.value.content.trim().length <= 500
})

// 方法
const loadComments = async () => {
  loading.value = true
  try {
    const result = await getCommentPage({
      postId: props.postId,
      page: current.value,
      size: size.value,
      sortType: currentSort.value
    })
    
    comments.value = result.records
    total.value = result.total
    pages.value = result.pages
  } catch (error) {
    console.error('加载评论失败:', error)
  } finally {
    loading.value = false
  }
}

const submitComment = async () => {
  if (!canSubmit.value) return
  
  isSubmitting.value = true
  try {
    await createComment(newComment.value)
    
    // 重置表单
    newComment.value.content = ''
    newComment.value.isAnonymous = false
    
    // 重新加载评论
    await loadComments()
  } catch (error) {
    console.error('发布评论失败:', error)
  } finally {
    isSubmitting.value = false
  }
}

const showReplyInput = (comment: Comment, replyTo?: Comment) => {
  replyingTo.value = comment.id
  replyContent.value = ''
  
  if (replyTo) {
    newComment.value.replyUserId = replyTo.authorId
    newComment.value.parentId = comment.id
  } else {
    newComment.value.replyUserId = undefined
    newComment.value.parentId = comment.id
  }
}

const cancelReply = () => {
  replyingTo.value = null
  replyContent.value = ''
  newComment.value.replyUserId = undefined
  newComment.value.parentId = undefined
}

const submitReply = async (comment: Comment) => {
  if (!replyContent.value.trim()) return
  
  isSubmitting.value = true
  try {
    const replyData: CommentCreateDTO = {
      postId: props.postId,
      content: replyContent.value,
      parentId: comment.id,
      replyUserId: newComment.value.replyUserId,
      isAnonymous: newComment.value.isAnonymous
    }
    
    await createComment(replyData)
    
    // 重置回复表单
    cancelReply()
    
    // 重新加载评论
    await loadComments()
  } catch (error) {
    console.error('发布回复失败:', error)
  } finally {
    isSubmitting.value = false
  }
}

const toggleLike = async (comment: Comment) => {
  try {
    if (comment.isLiked) {
      await unlikeCommentApi(comment.id)
      comment.likeCount--
      comment.isLiked = false
    } else {
      await likeCommentApi(comment.id)
      comment.likeCount++
      comment.isLiked = true
    }
  } catch (error) {
    console.error('操作失败:', error)
  }
}

const deleteComment = async (commentId: number) => {
  if (!confirm('确定要删除这条评论吗？')) return
  
  try {
    await deleteCommentApi(commentId)
    await loadComments()
  } catch (error) {
    console.error('删除评论失败:', error)
  }
}

const canDeleteComment = (comment: Comment) => {
  return userStore.isLoggedIn && 
         (userStore.user?.id === comment.authorId || userStore.isAdmin)
}

const changeSort = (sortType: number) => {
  currentSort.value = sortType
  current.value = 1
  loadComments()
}

const changePage = (page: number) => {
  current.value = page
  loadComments()
}

const loadMoreReplies = (comment: Comment) => {
  // TODO: 实现加载更多回复的逻辑
  console.log('加载更多回复:', comment.id)
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

// 监听器
watch(() => props.postId, () => {
  newComment.value.postId = props.postId
  current.value = 1
  loadComments()
})

// 生命周期
onMounted(() => {
  loadComments()
})
</script>

<style lang="scss" scoped>
@use 'sass:color';
@import '../styles/variables.scss';

.comment-list {
  margin-top: $spacing-xl;
}

.comment-input-section {
  background: $bg-card;
  border-radius: $radius-card;
  padding: $spacing-lg;
  margin-bottom: $spacing-lg;
  border: 1px solid $border-card;
}

.comment-input-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $spacing-md;
  
  h3 {
    margin: 0;
    color: $text-primary;
    font-size: $font-size-lg;
  }
}

.input-options {
  display: flex;
  gap: $spacing-md;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  cursor: pointer;
  font-size: $font-size-sm;
  color: $text-secondary;
  
  input[type="checkbox"] {
    width: auto;
    margin: 0;
  }
}

.comment-input-wrapper {
  .comment-textarea {
    width: 100%;
    min-height: 100px;
    padding: $spacing-md;
    border: 1px solid $border-input;
    border-radius: $radius-input;
    background: $bg-input;
    color: $text-primary;
    font-size: $font-size-base;
    resize: vertical;
    
    &:focus {
      outline: none;
      border-color: $color-primary;
    }
    
    &:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }
  }
}

.comment-input-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: $spacing-sm;
}

.input-tips {
  .tip-text {
    font-size: $font-size-sm;
    color: $text-secondary;
    
    &.success {
      color: $color-success;
    }
  }
}

.comments-section {
  background: $bg-card;
  border-radius: $radius-card;
  padding: $spacing-lg;
  border: 1px solid $border-card;
}

.comments-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $spacing-lg;
  
  h3 {
    margin: 0;
    color: $text-primary;
    font-size: $font-size-lg;
  }
}

.sort-options {
  display: flex;
  gap: $spacing-xs;
}

.sort-btn {
  padding: $spacing-xs $spacing-sm;
  border: 1px solid $border-button;
  background: $bg-button;
  color: $text-secondary;
  border-radius: $radius-button;
  cursor: pointer;
  font-size: $font-size-sm;
  transition: all 0.2s;
  
  &:hover {
    background: $bg-button-hover;
    border-color: $color-primary;
  }
  
  &.active {
    background: $color-primary;
    color: white;
    border-color: $color-primary;
  }
}

.loading {
  text-align: center;
  padding: $spacing-xl;
  color: $text-secondary;
  
  .loading-spinner {
    width: 32px;
    height: 32px;
    border: 3px solid $border-spinner;
    border-top: 3px solid $color-primary;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin: 0 auto $spacing-sm;
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
  
  p {
    margin: 0;
    font-size: $font-size-base;
  }
}

.comments-container {
  .comment-item {
    margin-bottom: $spacing-lg;
    padding-bottom: $spacing-lg;
    border-bottom: 1px solid $border-divider;
    
    &:last-child {
      border-bottom: none;
      margin-bottom: 0;
      padding-bottom: 0;
    }
  }
}

.comment-main {
  display: flex;
  gap: $spacing-md;
}

.comment-avatar {
  flex-shrink: 0;
  
  img {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    object-fit: cover;
  }
  
  .avatar-placeholder {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background: $color-secondary;
    color: white;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: $font-size-lg;
    font-weight: bold;
  }
}

.comment-content {
  flex: 1;
  min-width: 0;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  margin-bottom: $spacing-xs;
  
  .author-name {
    font-weight: 600;
    color: $text-primary;
    font-size: $font-size-base;
  }
  
  .comment-time {
    font-size: $font-size-sm;
    color: $text-secondary;
  }
}

.comment-text {
  color: $text-primary;
  line-height: 1.6;
  margin-bottom: $spacing-sm;
  word-wrap: break-word;
}

.comment-actions {
  display: flex;
  gap: $spacing-sm;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: $spacing-xs;
  padding: $spacing-xs $spacing-sm;
  border: none;
  background: none;
  color: $text-secondary;
  cursor: pointer;
  font-size: $font-size-sm;
  border-radius: $radius-button;
  transition: all 0.2s;
  
  &:hover {
    background: $bg-button-hover;
    color: $text-primary;
  }
  
  &.liked {
    color: $color-primary;
  }
  
  &.delete {
    color: $color-danger;
    
    &:hover {
      background: color.adjust($color-danger, $alpha: -0.9);
    }
  }
  
  svg {
    width: 16px;
    height: 16px;
  }
}

.reply-input {
  margin-top: $spacing-md;
  margin-left: 56px;
}

.reply-input-wrapper {
  .reply-textarea {
    width: 100%;
    min-height: 80px;
    padding: $spacing-sm;
    border: 1px solid $border-input;
    border-radius: $radius-input;
    background: $bg-input;
    color: $text-primary;
    font-size: $font-size-sm;
    resize: vertical;
    
    &:focus {
      outline: none;
      border-color: $color-primary;
    }
    
    &:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }
  }
}

.reply-input-footer {
  display: flex;
  justify-content: flex-end;
  gap: $spacing-sm;
  margin-top: $spacing-sm;
}

.comment-replies {
  margin-top: $spacing-md;
  margin-left: 56px;
}

.reply-item {
  display: flex;
  gap: $spacing-sm;
  margin-bottom: $spacing-md;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.reply-avatar {
  flex-shrink: 0;
  
  img {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    object-fit: cover;
  }
  
  .avatar-placeholder {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    background: $color-secondary;
    color: white;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: $font-size-base;
    font-weight: bold;
  }
}

.reply-content {
  flex: 1;
  min-width: 0;
}

.reply-header {
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  margin-bottom: $spacing-xs;
  
  .author-name {
    font-weight: 600;
    color: $text-primary;
    font-size: $font-size-sm;
  }
  
  .reply-to {
    font-size: $font-size-sm;
    color: $color-primary;
  }
  
  .reply-time {
    font-size: $font-size-xs;
    color: $text-secondary;
  }
}

.reply-text {
  color: $text-primary;
  line-height: 1.5;
  margin-bottom: $spacing-xs;
  font-size: $font-size-sm;
  word-wrap: break-word;
}

.reply-actions {
  display: flex;
  gap: $spacing-xs;
}

.more-replies {
  margin-top: $spacing-sm;
  margin-left: 56px;
}

.more-replies-btn {
  background: none;
  border: none;
  color: $color-primary;
  cursor: pointer;
  font-size: $font-size-sm;
  padding: $spacing-xs;
  border-radius: $radius-button;
  
  &:hover {
    background: color.adjust($color-primary, $alpha: -0.9);
  }
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: $spacing-md;
  margin-top: $spacing-lg;
  padding-top: $spacing-lg;
  border-top: 1px solid $border-divider;
}

.page-btn {
  padding: $spacing-sm $spacing-md;
  border: 1px solid $border-button;
  background: $bg-button;
  color: $text-primary;
  border-radius: $radius-button;
  cursor: pointer;
  transition: all 0.2s;
  
  &:hover:not(:disabled) {
    background: $bg-button-hover;
    border-color: $color-primary;
  }
  
  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
}

.page-info {
  color: $text-secondary;
  font-size: $font-size-sm;
}

// 响应式设计
@media (max-width: $breakpoint-md) {
  .comment-input-section,
  .comments-section {
    padding: $spacing-md;
  }
  
  .comment-main {
    gap: $spacing-sm;
  }
  
  .comment-avatar img,
  .comment-avatar .avatar-placeholder {
    width: 32px;
    height: 32px;
  }
  
  .reply-input,
  .comment-replies {
    margin-left: 40px;
  }
  
  .reply-avatar img,
  .reply-avatar .avatar-placeholder {
    width: 28px;
    height: 28px;
  }
}
</style>
