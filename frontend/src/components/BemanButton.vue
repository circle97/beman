<template>
  <button
    :class="buttonClasses"
    :disabled="disabled"
    @click="handleClick"
  >
    <slot />
  </button>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  type?: 'primary' | 'secondary'
  size?: 'small' | 'medium' | 'large'
  disabled?: boolean
  loading?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  type: 'primary',
  size: 'medium',
  disabled: false,
  loading: false
})

const emit = defineEmits<{
  click: [event: MouseEvent]
}>()

const buttonClasses = computed(() => [
  'beman-button',
  `beman-button--${props.type}`,
  `beman-button--${props.size}`,
  {
    'beman-button--disabled': props.disabled,
    'beman-button--loading': props.loading
  }
])

const handleClick = (event: MouseEvent) => {
  if (!props.disabled && !props.loading) {
    emit('click', event)
  }
}
</script>

<style lang="scss" scoped>
@import '../styles/variables.scss';

.beman-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: none;
  border-radius: $radius-button;
  font-family: inherit;
  font-weight: $font-weight-medium;
  cursor: pointer;
  transition: $transition-button;
  text-decoration: none;
  outline: none;
  
  &:focus {
    outline: 2px solid $color-secondary;
    outline-offset: 2px;
  }
  
  &:disabled,
  &.beman-button--disabled {
    opacity: 0.5;
    cursor: not-allowed;
    pointer-events: none;
  }
  
  // 主按钮样式
  &.beman-button--primary {
    background: $bg-button-primary;
    color: $text-button-primary;
    box-shadow: $shadow-button-primary;
    
    &:hover:not(:disabled) {
      background: $bg-button-primary-hover;
      box-shadow: $shadow-button-primary-hover;
    }
    
    &:active:not(:disabled) {
      background: $color-primary-hover;
    }
  }
  
  // 次按钮样式
  &.beman-button--secondary {
    background: $bg-button-secondary;
    color: $text-button-secondary;
    border: 1px solid $border-button-secondary;
    
    &:hover:not(:disabled) {
      background: $bg-button-secondary-hover;
    }
    
    &:active:not(:disabled) {
      background: $bg-button-secondary;
      border-color: $color-secondary-hover;
    }
  }
  
  // 尺寸样式
  &.beman-button--small {
    padding: $spacing-xs $spacing-sm;
    font-size: $font-size-sm;
    min-height: 32px;
  }
  
  &.beman-button--medium {
    padding: $spacing-sm $spacing-md;
    font-size: $font-size-md;
    min-height: 40px;
  }
  
  &.beman-button--large {
    padding: $spacing-md $spacing-lg;
    font-size: $font-size-lg;
    min-height: 48px;
  }
  
  // 加载状态
  &.beman-button--loading {
    position: relative;
    color: transparent;
    
    &::after {
      content: '';
      position: absolute;
      width: 16px;
      height: 16px;
      border: 2px solid transparent;
      border-top: 2px solid currentColor;
      border-radius: 50%;
      animation: spin 1s linear infinite;
    }
  }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style> 