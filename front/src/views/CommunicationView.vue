<template>
  <div class="communication-view">
    <h2>沟通练习</h2>
    <p>提升沟通技巧，改善关系表达</p>
    
    <div class="practice-scenarios">
      <div class="scenario-card">
        <h3>场景一：伴侣抱怨工作压力</h3>
        <p>她说："今天工作特别累，老板又给我加了很多任务..."</p>
        <div class="response-options">
          <button class="response-btn">"我也很累，别抱怨了"</button>
          <button class="response-btn correct">"听起来你今天真的很辛苦，要不要我帮你按摩一下？"</button>
          <button class="response-btn">"那你辞职吧"</button>
        </div>
      </div>
      
      <div class="scenario-card">
        <h3>场景二：伴侣表达不满</h3>
        <p>她说："你最近都不关心我，总是忙自己的事情..."</p>
        <div class="response-options">
          <button class="response-btn">"我哪里不关心你了？"</button>
          <button class="response-btn correct">"对不起，我最近确实忽略你了，我们聊聊吧"</button>
          <button class="response-btn">"你太敏感了"</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 沟通练习页面逻辑
</script>

<style scoped>
.communication-view {
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

.practice-scenarios {
  max-width: 600px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.scenario-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 20px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  text-align: left;
}

.scenario-card h3 {
  color: white;
  margin-bottom: 10px;
}

.scenario-card p {
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 20px;
  font-style: italic;
}

.response-options {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.response-btn {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
  padding: 12px 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: left;
}

.response-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.response-btn.correct {
  border-color: #4ecdc4;
  background: rgba(78, 205, 196, 0.1);
}

.response-btn.correct:hover {
  background: rgba(78, 205, 196, 0.2);
}
</style> 