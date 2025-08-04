<template>
  <div class="community-view">
    <h2>男性互助社区</h2>
    <p>分享经验，互相支持，共同成长</p>
    
    <div class="posts-list">
      <div class="post-card">
        <h3>如何化解冷战？</h3>
        <p>最近和女朋友冷战了，想请教大家有什么好的化解方法？</p>
        <div class="post-meta">
          <span>匿名用户</span>
          <span>求助</span>
        </div>
      </div>
      
      <div class="post-card">
        <h3>我的挽回经验分享</h3>
        <p>经过3个月的努力，终于和女朋友和好了，分享一下我的经验...</p>
        <div class="post-meta">
          <span>匿名用户</span>
          <span>经验分享</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 社区页面逻辑
</script>

<style scoped>
.community-view {
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

.posts-list {
  max-width: 600px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.post-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 20px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  text-align: left;
}

.post-card h3 {
  color: white;
  margin-bottom: 10px;
}

.post-card p {
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 15px;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
}
</style> 