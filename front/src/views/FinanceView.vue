<template>
  <div class="finance-container">
    <div class="card" :style="cardStyles">
      <h1 class="title">{{ $t('finance.summaryTitle') }}</h1>
      <FinanceSummary :data="summaryData" />
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

const cardStyles = {
  background: 'var(--card-gradient)',
  border: '1px solid var(--card-border)',
  borderRadius: '8px',
  boxShadow: '0 4px 6px rgba(15, 23, 42, 0.5)'
};

const fetchSummary = async () => {
  summaryData.value = await financeStore.getSummary();
  bills.value = await financeStore.getBills('month');
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
</style>