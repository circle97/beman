<template>
  <div class="user-follow-button">
    <button
      v-if="!isFollowing"
      class="btn-follow"
      @click="handleFollow"
      :disabled="isLoading"
    >
      <svg v-if="!isLoading" viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
        <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
      </svg>
      <span v-if="!isLoading">关注</span>
      <span v-else>关注中...</span>
    </button>
    
    <button
      v-else
      class="btn-following"
      @click="handleUnfollow"
      :disabled="isLoading"
    >
      <svg v-if="!isLoading" viewBox="0 0 24 24" fill="currentColor" width="16" height="16">
        <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/>
      </svg>
      <span v-if="!isLoading">已关注</span>
      <span v-else>取消中...</span>
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { followUser, unfollowUser, checkIsFollowing } from '../api/userFollow'

interface Props {
  userId: number
  size?: 'small' | 'medium' | 'large'
}

const props = withDefaults(defineProps<Props>(), {
  size: 'medium'
})

const emit = defineEmits<{
  (e: 'follow-change', isFollowing: boolean): void
}>()

const isFollowing = ref(false)
const isLoading = ref(false)

// 检查关注状态
const checkFollowStatus = async () => {
  try {
    isFollowing.value = await checkIsFollowing(props.userId)
  } catch (error) {
    console.error('检查关注状态失败:', error)
  }
}

// 关注用户
const handleFollow = async () => {
  if (isLoading.value) return
  
  isLoading.value = true
  try {
    await followUser({
      followingId: props.userId,
      followType: 1
    })
    isFollowing.value = true
    emit('follow-change', true)
  } catch (error) {
    console.error('关注失败:', error)
  } finally {
    isLoading.value = false
  }
}

// 取消关注
const handleUnfollow = async () => {
  if (isLoading.value) return
  
  if (!confirm('确定要取消关注吗？')) return
  
  isLoading.value = true
  try {
    await unfollowUser(props.userId)
    isFollowing.value = false
    emit('follow-change', false)
  } catch (error) {
    console.error('取消关注失败:', error)
  } finally {
    isLoading.value = false
  }
}

// 组件挂载时检查关注状态
onMounted(() => {
  checkFollowStatus()
})
</script>

<style lang="scss" scoped>
@import '../styles/variables.scss';

.user-follow-button {
  display: inline-block;
}

.btn-follow,
.btn-following {
  display: inline-flex;
  align-items: center;
  gap: $spacing-xs;
  padding: $spacing-xs $spacing-sm;
  border: 1px solid;
  border-radius: $radius-button;
  font-size: $font-size-sm;
  cursor: pointer;
  transition: all 0.2s;
  
  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
  
  svg {
    width: 16px;
    height: 16px;
  }
}

.btn-follow {
  background: $color-primary;
  color: white;
  border-color: $color-primary;
  
  &:hover:not(:disabled) {
    background: darken($color-primary, 10%);
    border-color: darken($color-primary, 10%);
  }
}

.btn-following {
  background: $bg-button;
  color: $text-secondary;
  border-color: $border-button;
  
  &:hover:not(:disabled) {
    background: $bg-button-hover;
    color: $text-primary;
    border-color: $color-primary;
  }
}

// 尺寸变体
.btn-follow,
.btn-following {
  &.small {
    padding: $spacing-xs;
    font-size: $font-size-xs;
    
    svg {
      width: 14px;
      height: 14px;
    }
  }
  
  &.large {
    padding: $spacing-sm $spacing-md;
    font-size: $font-size-base;
    
    svg {
      width: 18px;
      height: 18px;
    }
  }
}
</style>
