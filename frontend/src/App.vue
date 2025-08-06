<script setup lang="ts">
import { computed, h } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from './stores/user'

// 图标组件（这里用简单的SVG，实际项目中可以使用图标库）
const HomeIcon = () => h('svg', { viewBox: '0 0 24 24', fill: 'currentColor' }, [
  h('path', { d: 'M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z' })
])

const CommunityIcon = () => h('svg', { viewBox: '0 0 24 24', fill: 'currentColor' }, [
  h('path', { d: 'M16 4c0-1.11.89-2 2-2s2 .89 2 2-.89 2-2 2-2-.89-2-2zm4 18v-6h2.5l-2.54-7.63A1.5 1.5 0 0 0 18.54 8H17c-.8 0-1.54.37-2.01 1l-1.7 2.26V15h-1.5v6H20zM12.5 11.5c.83 0 1.5-.67 1.5-1.5s-.67-1.5-1.5-1.5S11 9.17 11 10s.67 1.5 1.5 1.5zM5.5 6c1.11 0 2-.89 2-2s-.89-2-2-2-2 .89-2 2 .89 2 2 2zm2 16v-7H9V9c0-1.1-.9-2-2-2H4c-1.1 0-2 .9-2 2v6h1.5v7h4z' })
])

const ToolsIcon = () => h('svg', { viewBox: '0 0 24 24', fill: 'currentColor' }, [
  h('path', { d: 'M22.7 19l-9.1-9.1c.9-2.3.4-5-1.5-6.9-2-2-5-2.4-7.4-1.3L9 6 6 9 1.6 4.7C.4 7.1.9 10.1 2.9 12.1c1.9 1.9 4.6 2.4 6.9 1.5l9.1 9.1c.4.4 1 .4 1.4 0l2.3-2.3c.5-.4.5-1.1.1-1.4z' })
])

const GrowthIcon = () => h('svg', { viewBox: '0 0 24 24', fill: 'currentColor' }, [
  h('path', { d: 'M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z' })
])

const ProfileIcon = () => h('svg', { viewBox: '0 0 24 24', fill: 'currentColor' }, [
  h('path', { d: 'M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z' })
])

const route = useRoute()
const userStore = useUserStore()

// 导航项配置
const navItems = [
  { path: '/home', text: '首页', icon: HomeIcon },
  { path: '/community', text: '社区', icon: CommunityIcon },
  { path: '/tools', text: '工具', icon: ToolsIcon },
  { path: '/growth', text: '成长', icon: GrowthIcon },
  { path: '/profile', text: '我的', icon: ProfileIcon }
]

// 是否显示底部导航
const showNav = computed(() => {
  const hideNavPages = ['/login', '/register']
  return !hideNavPages.includes(route.path)
})
</script>

<template>
  <div id="app">
    <!-- 主内容区域 -->
    <main class="app-main">
      <router-view />
    </main>
    
    <!-- 底部导航栏 -->
    <nav class="app-nav" v-if="showNav">
      <router-link 
        v-for="item in navItems" 
        :key="item.path"
        :to="item.path"
        class="nav-item"
        :class="{ 'nav-item--active': $route.path === item.path }"
      >
        <div class="nav-item__icon">
          <component :is="item.icon" />
        </div>
        <span class="nav-item__text">{{ item.text }}</span>
      </router-link>
    </nav>
  </div>
</template>

<style lang="scss" scoped>
@import './styles/variables.scss';

#app {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-main {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 80px; // 为底部导航留出空间
}

.app-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 80px;
  background: $bg-section;
  border-top: 1px solid $border-card;
  display: flex;
  align-items: center;
  justify-content: space-around;
  padding: 0 $spacing-md;
  z-index: 1000;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 100%;
  color: $text-secondary;
  text-decoration: none;
  transition: color 0.2s;
  
  &:hover {
    color: $color-secondary;
  }
  
  &--active {
    color: $color-secondary;
  }
  
  &__icon {
    width: 24px;
    height: 24px;
    margin-bottom: $spacing-xs;
    
    svg {
      width: 100%;
      height: 100%;
    }
  }
  
  &__text {
    font-size: $font-size-xs;
    font-weight: $font-weight-medium;
  }
}

// 响应式设计
@media (max-width: $breakpoint-sm) {
  .app-nav {
    height: 70px;
  }
  
  .app-main {
    padding-bottom: 70px;
  }
  
  .nav-item__text {
    font-size: 10px;
  }
}
</style>
