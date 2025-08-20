import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/community',
    name: 'Community',
    component: () => import('../views/Community.vue'),
    meta: { title: '社区' }
  },
  {
    path: '/tools',
    name: 'Tools',
    component: () => import('../views/Tools.vue'),
    meta: { title: '关系管理' }
  },
  {
    path: '/growth',
    name: 'Growth',
    component: () => import('../views/Growth.vue'),
    meta: { title: '成长档案' }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/Profile.vue'),
    meta: { title: '个人中心' }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { title: '注册' }
  },
  // 工具子页面
  {
    path: '/tools/finance',
    name: 'Finance',
    component: () => import('../views/tools/Finance.vue'),
    meta: { title: '财务透明化' }
  },
  {
    path: '/tools/schedule',
    name: 'Schedule',
    component: () => import('../views/tools/Schedule.vue'),
    meta: { title: '日程管家' }
  },
  {
    path: '/tools/emotion',
    name: 'Emotion',
    component: () => import('../views/tools/Emotion.vue'),
    meta: { title: '情绪解码器' }
  },
  {
    path: '/tools/communication',
    name: 'Communication',
    component: () => import('../views/tools/Communication.vue'),
    meta: { title: '沟通沙盒' }
  },
  // 测试页面
  {
    path: '/test-audit',
    name: 'TestAudit',
    component: () => import('../views/TestAudit.vue'),
    meta: { title: '内容审核测试' }
  },
  {
    path: '/test-dashboard',
    name: 'TestDashboard',
    component: () => import('../views/TestDashboard.vue'),
    meta: { title: '共同账户看板测试' }
  },
  {
    path: '/test-reminder',
    name: 'TestReminder',
    component: () => import('../views/TestReminder.vue'),
    meta: { title: '提醒管理测试' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - BeMan` : 'BeMan'
  
  // 检查登录状态（除了登录、注册页面）
  const publicPages = ['/login', '/register']
  const authRequired = !publicPages.includes(to.path)
  const token = localStorage.getItem('beman-token')
  
  if (authRequired && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router 