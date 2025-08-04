import { createRouter, createWebHistory } from 'vue-router'
import FinanceView from '../views/FinanceView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'finance',
      component: FinanceView
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