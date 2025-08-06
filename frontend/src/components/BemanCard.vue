<template>
  <div :class="cardClasses" @click="handleClick">
    <div class="beman-card__header" v-if="$slots.header">
      <slot name="header" />
    </div>
    <div class="beman-card__content">
      <slot />
    </div>
    <div class="beman-card__footer" v-if="$slots.footer">
      <slot name="footer" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  hoverable?: boolean
  clickable?: boolean
  padding?: 'none' | 'small' | 'medium' | 'large'
}

const props = withDefaults(defineProps<Props>(), {
  hoverable: true,
  clickable: false,
  padding: 'medium'
})

const emit = defineEmits<{
  click: [event: MouseEvent]
}>()

const cardClasses = computed(() => [
  'beman-card',
  `beman-card--padding-${props.padding}`,
  {
    'beman-card--hoverable': props.hoverable,
    'beman-card--clickable': props.clickable
  }
])

const handleClick = (event: MouseEvent) => {
  if (props.clickable) {
    emit('click', event)
  }
}
</script>

<style lang="scss" scoped>
@import '../styles/variables.scss';

.beman-card {
  background: $bg-card;
  border: 1px solid $border-card;
  border-radius: $radius-card;
  box-shadow: $shadow-card;
  transition: $transition-card;
  overflow: hidden;
  
  // 悬停效果
  &.beman-card--hoverable:hover {
    background: $bg-card-hover;
    box-shadow: $shadow-card-hover;
  }
  
  // 可点击效果
  &.beman-card--clickable {
    cursor: pointer;
    
    &:hover {
      transform: translateY(-2px);
    }
    
    &:active {
      transform: translateY(0);
    }
  }
  
  // 内边距
  &.beman-card--padding-none {
    .beman-card__content {
      padding: 0;
    }
  }
  
  &.beman-card--padding-small {
    .beman-card__content {
      padding: $spacing-sm;
    }
  }
  
  &.beman-card--padding-medium {
    .beman-card__content {
      padding: $spacing-md;
    }
  }
  
  &.beman-card--padding-large {
    .beman-card__content {
      padding: $spacing-lg;
    }
  }
  
  // 卡片头部
  &__header {
    padding: $spacing-md;
    border-bottom: 1px solid $border-card;
    background: rgba($bg-section, 0.5);
  }
  
  // 卡片内容
  &__content {
    color: $text-primary;
  }
  
  // 卡片底部
  &__footer {
    padding: $spacing-md;
    border-top: 1px solid $border-card;
    background: rgba($bg-section, 0.5);
  }
}
</style> 