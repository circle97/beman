<template>
  <div class="finance-container">
    <div class="card">
      <h1 class="title">{{ $t('finance.summaryTitle') }}</h1>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-if="error" class="error">{{ error }}</div>
      <FinanceSummary v-else :data="summaryData" />
      <BillForm @bill-added="fetchSummary" />
      <BillList :bills="bills" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useFinanceStore } from '@/stores/finance';
import FinanceSummary from '@/components/finance/FinanceSummary.vue';
import BillForm from '@/components/finance/BillForm.vue';
import BillList from '@/components/finance/BillList.vue';

const financeStore = useFinanceStore();
const summaryData = ref<any>(null);
const bills = ref<any[]>([]);
const loading = ref(true);
const error = ref('');

const fetchSummary = async () => {
  try {
    loading.value = true;
    summaryData.value = await financeStore.getSummary('month');
    bills.value = await financeStore.getBills('month');
    error.value = '';
  } catch (err) {
    error.value = '数据加载失败，请重试';
    console.error('加载财务数据失败:', err);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchSummary();
});
</script>

<style scoped>
.finance-container {
  padding: 20px;
}

.title {
  color: var(--text-primary);
  margin-bottom: 20px;
}

.loading {
  color: var(--text-secondary);
  text-align: center;
  padding: 40px;
}

.error {
  color: #ef4444;
  text-align: center;
  padding: 40px;
}
</style>