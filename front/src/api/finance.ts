import request from '@/utils/request'; // 修复后的导入格式

export interface BillRequest {
  description: string;
  amount: number;
  type: 'expense' | 'income';
  date: string;
  category: string;
  isShared: boolean;
}

export interface FinanceSummary {
  totalIncome: number;
  totalExpense: number;
  sharedExpense: number;
  personalExpense: number;
  savingRate: number;
  categories: Array<{name: string, amount: number, percentage: number}>;
}

export const getFinanceSummary = (period: string) => {
  return request<FinanceSummary>({
    url: '/finance/bills/summary',
    method: 'get',
    params: { period }
  });
};

export const addBill = (data: BillRequest) => {
  return request({
    url: '/finance/bills',
    method: 'post',
    data
  });
};

export const getBills = (period: string) => {
  return request<Array<any>>({
    url: '/finance/bills',
    method: 'get',
    params: { period }
  });
};