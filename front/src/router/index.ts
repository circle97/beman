import { createRouter, createWebHistory } from 'vue-router'
import FinanceView from '../views/FinanceView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/finance'
    },
    {
      path: '/finance',
      name: 'finance',
      component: FinanceView
    },
    {
      path: '/community',
      name: 'community',
      component: () => import('../views/CommunityView.vue')
    },
    {
      path: '/schedule',
      name: 'schedule',
      component: () => import('../views/ScheduleView.vue')
    },
    {
      path: '/emotion',
      name: 'emotion',
      component: () => import('../views/EmotionView.vue')
    },
    {
      path: '/communication',
      name: 'communication',
      component: () => import('../views/CommunicationView.vue')
    },
    // 添加404路由
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('../views/NotFoundView.vue')
    }
  ]
})

export default router