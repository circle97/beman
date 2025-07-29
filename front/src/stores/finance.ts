import { defineStore } from 'pinia';
import * as financeApi from '@/api/finance';

export const useFinanceStore = defineStore('finance', {
  state: () => ({
    summary: null,
    bills: [],
    loading: false
  }),
  actions: {
    async getSummary(period: string) {
      this.loading = true;
      try {
        const data = await financeApi.getFinanceSummary(period);
        this.summary = data;
        return data;
      } finally {
        this.loading = false;
      }
    },
    async getBills(period: string) {
      this.loading = true;
      try {
        const data = await financeApi.getBills(period);
        this.bills = data;
        return data;
      } finally {
        this.loading = false;
      }
    },
    async addBill(billData: financeApi.BillRequest) {
      this.loading = true;
      try {
        await financeApi.addBill(billData);
        // 添加成功后刷新数据
        await Promise.all([
          this.getSummary('month'),
          this.getBills('month')
        ]);
      } finally {
        this.loading = false;
      }
    }
  }
});