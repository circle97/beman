<template>
  <div id="app">
    <!-- 路由出口，用于渲染匹配的路由组件 -->
    <router-view />
  </div>
</template>

<script setup lang="ts">
// 根组件无需额外逻辑，主要用于路由挂载
</script>

<style scoped>
/* 全局基础样式 */
#app {
  max-width: 1280px;
  margin: 0 auto;
  padding: 2rem;
  font-family: Inter, system-ui, Avenir, Helvetica, Arial, sans-serif;
}
</style>